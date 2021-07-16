package vidmot.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import vidmot.Adal;
import vidmot.SnakurPane;
import vinnsla.GildiDouble;
import vinnsla.GildiInt;
import vinnsla.Leikur;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Controller sem heldur utan um ýmsa viðmótshluti og sér um að
 *            senda sum gögn yfir í aðra klasa. Sér einnig um að stilla
 *            timeline fyrir færslu leikmanna/byssuskota og um að ljúka leik
 *
 *
 *****************************************************************************/

public class SnakurController implements Initializable {

    // java static breytur
    // hakkatafla fyrir hraða hvers erfiðleikastigs
    private static HashMap<String, Double> stigValues;

    // java tilviksbreytur
    private Adal adal;
    private Leikur leikur;
    private Timeline t;
    // erfiðleikastig
    private String stig;
    // fjöldi matar
    private String magn;
    // fjöldi lífa
    private int lif;
    // er leik lokið
    private boolean lokid;
    // eru veggir eitraðir
    private boolean eitrad;
    // er þetta eins leikmanna útgáfa
    private boolean erEinnLeikmadur;
    // er þetta Tank útgáfa
    private boolean erTank;
    // inniheldur texta
    private ResourceBundle resourceBundle;

    // javafx breytur
    @FXML
    private SnakurPane snakurBord;
    @FXML
    private ListView<Integer> fxTafla;
    @FXML
    public Label fxNunaStig;
    @FXML
    private ImageView fxMynd;
    @FXML
    private Label fxLokidLabel;
    @FXML
    private VBox fxVbox2;


    /**
     * Frumstillir hluti og gerir leikinn tilbúna
     *
     * @param url            ekki notað
     * @param resourceBundle inniheldur textann
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leikur = new Leikur();
        fxMynd.toBack();
        fxTafla.setItems(leikur.getStigaTafla());
        fxNunaStig.textProperty().bind(leikur.stigProperty());
        this.resourceBundle = ResourceBundle.getBundle("vidmot.texti");
    }

    /**
     * Stillir tímalínu eftir útgáfu leiks. Tímalínan færir
     * snák hluti áfram eftir 10ms og skoðar hver hafi unnið.
     *
     * @param time tími per frame
     */
    public void stillaTimeLine(int time) {
        KeyFrame k = new KeyFrame(Duration.millis(time),
                e -> {
                    if (erEinnLeikmadur) {
                        einnTimeline();
                    } else {
                        tveirTimeline();
                        if (erTank) {
                            tankTimeline();
                        }
                    }
                });
        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    /**
     * Færir alla snáka á borði áfram og skoðar hvort leikmaður hafi tapað
     */
    public void einnTimeline() {
        snakurBord.afram1();
        if (snakurBord.getSnakur().getLeiklokid()) {
            leikurLokid(null);
        }
        snakurBord.aframEitursnakar();
    }

    /**
     * Færir alla snáka á borði áfram og skoðar hvort leikmaður hafi tapað
     */
    public void tveirTimeline() {
        snakurBord.afram2();
        if (snakurBord.getSnakur().getLeiklokid()) {
            leikurLokid(resourceBundle
                    .getString("eitursnakur"));
        }
        if (snakurBord.getMotherjaSnakur().getLeiklokid()) {
            leikurLokid(resourceBundle
                    .getString("snakur"));
        }
    }

    /**
     * Færir alla snáka á borði áfram og skoðar hvort leikmaður hafi tapað
     */
    public void tankTimeline() {
        snakurBord.aframByssuSkot1();
        snakurBord.aframByssuSkot2();
        // í Tank hraðast allir lítillega stöðugt
        hradarTank();
    }

    /**
     * Lætur 1 leikmanna snák ferðast aðeins hraðar
     */
    public void hradar1() {
        snakurBord.getSnakur().setHradi(snakurBord.getSnakur()
                .getHradi() + stigValues.get(stig));
    }

    /**
     * Lætur 2 leikmanna snáka ferðast aðeins hraðar
     */
    public void hradar2() {
        snakurBord.getSnakur().setHradi(snakurBord.getSnakur()
                .getHradi() + GildiDouble.SNAKUR2HRADAR.getValue());
        // mótherjasnákur lengist ekki svo við hægum á honum smá
        snakurBord.getMotherjaSnakur().setHradi(snakurBord.getSnakur()
                .getHradi() + GildiDouble.MOTHERJA2HRADAR.getValue());
    }

    /**
     * Lætur Tank snáka ferðast örlítið hraðar
     */
    public void hradarTank() {
        snakurBord.getSnakur().setHradi
                (snakurBord.getSnakur().getHradi() + GildiDouble.TANKHRADAR.getValue());
        snakurBord.getMotherjaSnakur().setHradi
                (snakurBord.getMotherjaSnakur().getHradi() + GildiDouble.TANKHRADAR.getValue());
    }

    /**
     * Skýtur byssuskoti leikmanns 1
     */
    public void skjota1() {
        snakurBord.skjota1();
    }

    /**
     * Skýtur byssuskoti leikmanns 2
     */
    public void skjota2() {
        snakurBord.skjota2();
    }

    /**
     * Stöðvar tímalínuna og sýnir label sem segir hver hafi unnið
     *
     * @param snakur snákurinn sem vann
     */
    public void leikurLokid(String snakur) {
        t.stop();
        lokid = true;
        if (erEinnLeikmadur) {
            // basic label
            fxLokidLabel.setText(resourceBundle.getString("leiklokid"));
            fxLokidLabel.setTranslateX(10);
        } else {
            // sýna basic + nafn snáks sem vann
            fxLokidLabel.setText(resourceBundle.getString("leiklokid")
                    + " " + snakur + " " + resourceBundle
                    .getString("vinnur"));
            // hliðrum labelnum
            tveirHlidrun(snakur);
        }
        labelToFront(fxLokidLabel);
    }

    /**
     * Minnkar leturstærð og hliðrar texta
     *
     * @param snakur snákurinn sem vann
     */
    public void tveirHlidrun(String snakur) {
        fxLokidLabel.setFont(new Font(35));
        if (snakur.equals(resourceBundle.getString("eitursnakur"))) {
            fxLokidLabel.setTranslateX(-90);
        } else {
            fxLokidLabel.setTranslateX(-70);
        }
        fxLokidLabel.setTranslateY(40);
    }

    /**
     * Handler fyrir spila aftur hnapp slekkur sem kveikir á viðeigandi
     * viðmótshlutum og kallar á reset sem gerir það sama en
     * fyrir leikborðið
     *
     * @param actionEvent ekki notað
     */
    public void afturHandler(ActionEvent actionEvent) {
        leikur.leikLokid();
        snakurBord.reset();
        leikur.setStig(0);
        fxLokidLabel.setText("");
        snakurBord.getChildren().add(fxLokidLabel);
        lokid = false;
        t.play();
    }

    /**
     * Pásar leikinn ef hann er running, spilar annars
     */
    public void bida() {
        if (t.getStatus() == Animation.Status.RUNNING) {
            t.pause();
            fxLokidLabel.setFont(new Font(55));
            fxLokidLabel.setText(resourceBundle.getString("pasa"));
            fxLokidLabel.setTranslateX(80);
            labelToFront(fxLokidLabel);
        } else {
            t.play();
            fxLokidLabel.setText("");
            fxLokidLabel.setTranslateX(-80);
        }
    }

    /**
     * Færir label fremst á skjá
     *
     * @param label label sem færa á
     */
    public void labelToFront(Label label) {
        int ind = snakurBord.getChildren().indexOf(label);
        snakurBord.getChildren().get(ind).toFront();
    }

    /**
     * Gerir lekborðið tilbúna fyrir Tank og stillir controller
     */
    public void tankFixes() {
        // sleppum öllu tengt stigum en geymum heim hnapp
        fxVbox2.getChildren().remove(0, fxVbox2.getChildren()
                .size() - 1);
        // heldur heim takka föstum þar sem hann var áður
        fxVbox2.getChildren().get(0).setTranslateY(360);
        // gerum setController nú svo líf flytjist rétt
        snakurBord.setController(this);
    }

    /**
     * Gerir lekborðið tilbúna fyrir 2 leikmenn og stillir controller
     */
    public void tveirLeikmennFixes(String uppl) {
        // gerum setController nú svo gögn flytjist rétt
        snakurBord.setController(this);
        // stillum heildarstig
        leikur.setTotalStig(Integer.parseInt(uppl));
        fxNunaStig.textProperty().bind(leikur.stigProperty());
        fxNunaStig.setTranslateX(-15);
    }

    /**
     * Gerir lekborðið tilbúna fyrir 1 leikmann og stillir controller
     */
    public void einnLeikmadurFixes() {
        erEinnLeikmadur = true;
        // gerum setController nú svo gögn flytjist rétt
        snakurBord.setController(this);
        fillStig();
    }

    /**
     * Býr til og fyllir í hakkatöflu fyrir stig og tilsvarandi hraða
     */
    public void fillStig() {
        stigValues = new HashMap<>();
        stigValues.put(resourceBundle.getString("lett"), 0.1);
        stigValues.put(resourceBundle.getString("midlungs"), 0.2);
        stigValues.put(resourceBundle.getString("erfitt"), 0.3);
    }

    /**
     * Sýnir inngangsglugga aftur
     *
     * @param actionEvent ekki notað
     * @throws IOException ef skrá finnst ekki
     */
    public void heimHandler(ActionEvent actionEvent) throws IOException {
        adal.showIntro();
    }

    /**
     * Setur upp handler fyrir að loka glugga
     *
     * @param actionEvent ekki notað
     */
    public void haettaHandler(ActionEvent actionEvent) {
        t.pause();
        Alert a = adal.stofnaAlert();
        // Bíða eftir svarinu
        Optional<ButtonType> svar = a.showAndWait();
        if (svar.isPresent() && svar.get().getButtonData()
                == ButtonBar.ButtonData.OK_DONE) {
            Platform.exit();
            System.exit(0);
        } else {
            if (fxLokidLabel.getText().equals(""))
                t.play();
        }
    }

    /**
     * Skoðar hvort leikur sé pásaður
     *
     * @return true ef leikur er pásaður, false annars
     */
    public boolean erPasad() {
        return t.getStatus() != Animation.Status.RUNNING;
    }

    public boolean getNotLokid() {
        return !lokid;
    }

    public void setStefna1(int x) {
        snakurBord.setStefna1(x);
    }

    public void setStefna2(int x) {
        snakurBord.setStefna2(x);
    }

    public Leikur getLeikur() {
        return leikur;
    }

    public Timeline getT() {
        return t;
    }

    public void setStig(String uppl) {
        stig = uppl;
        einnLeikmadurFixes();
    }

    public String getStig() {
        return stig;
    }

    public void setMagn(String uppl) {
        magn = uppl;
        tveirLeikmennFixes(uppl);
    }

    public String getMagn() {
        return magn;
    }

    public void setEitrad(boolean x) {
        eitrad = x;
        // gerum stillaTimeline nú svo stillingar flytjist rétt
        stillaTimeLine(GildiInt.TIMI.getValue());
    }

    public boolean getEitrad() {
        return eitrad;
    }

    public void setLif(String uppl) {
        lif = Integer.parseInt(uppl);
    }

    public void setAdal(Adal adal) {
        this.adal = adal;
    }

    public boolean getErEinnLeikmadur() {
        return erEinnLeikmadur;
    }

    public void setErTank(boolean x) {
        erTank = x;
        tankFixes();
    }

    public boolean getErTank() {
        return erTank;
    }

    public int getLif() {
        return lif;
    }

}
