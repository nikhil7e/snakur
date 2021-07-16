package vidmot;

import edu.princeton.cs.algs4.StdRandom;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import vidmot.controllers.SnakurController;
import vinnsla.GildiInt;

import java.util.*;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Sérhæfður klasi sem erfir frá Pane og er leikborð leiksins. Sér
 *            t.d. um að birta hluti á leikborðið og er millivegur milli
 *            SnakurController og Snakur.
 *
 *
 *****************************************************************************/

public class SnakurPane extends Pane {

    // java fastar
    // fastur listi sem inniheldur allar 4 stefnurnar
    private static final List<Integer> stefnuListi
            = Arrays.asList(GildiInt.UPP.getValue(),
            GildiInt.VINSTRI.getValue(),
            GildiInt.NIDUR.getValue(),
            GildiInt.HAEGRI.getValue());

    // java tilviksbreytur
    private Snakur snakur;
    private Snakur motherjaSnakur;
    private SnakurController snakurController;
    // hakkatafla fyrir líkur þess að eitursnákar birtist
    private static HashMap<String, Integer> likur;
    // obs listi fyrir mat
    private ObservableList<Faeda> matur;
    // obs listi fyrir eitursnáka
    private ObservableList<Snakur> eiturSnakar;
    // obs listi fyrir byssuskot leikmanns 1
    private ObservableList<Snakur> bSkot1;
    // obs listi fyrir byssuskotin leikmanns 2
    private ObservableList<Snakur> bSkot2;
    // erfiðleikastig
    private String eStig;
    // fjöldi lífa leikmanna
    private int lif;
    // hversu oft l.maður 1 hefur verið skotinn
    private int damage1;
    // hversu oft l.maður 2 hefur verið skotinn
    private int damage2;
    // hvort veggir eru eitraðir
    private boolean eitrad;
    // inniheldur texta
    private ResourceBundle resourceBundle;


    /**
     * SnakurPane er búinn til fyrr en SnakurController, svo
     * ég kallaði á uppsetning1() hér. Ef útgáfan er
     * ekki 1 leikmanna er kallað á önnur uppsetning föll
     * sem overwrite-a viðeigandi gögn
     */
    public SnakurPane() {
        resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        likur = new HashMap<>();
        likur.put(resourceBundle.getString("lett"), 50);
        likur.put(resourceBundle.getString("midlungs"), 70);
        likur.put(resourceBundle.getString("erfitt"), 90);
        this.resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        uppsetning1();
    }

    /**
     * Setur upp leikborðið fyrir 1 leikmanna útgáfu
     */
    public void uppsetning1() {
        snakurSetUp(snakur, GildiInt.SNAKUR1XHNIT.getValue(), GildiInt
                .SNAKUR1YHNIT.getValue(), GildiInt.UPP.getValue());
        matur = FXCollections.observableArrayList();
        eiturSnakar = FXCollections.observableArrayList();
        eitursnakarRandomStefna();
        eldaMat();
    }

    /**
     * Setur upp leikborðið fyrir 2 leikmanna útgáfu og Tank
     */
    public void uppsetningTveirOgTank() {
        // eyða núverandi mat
        getChildren().remove(matur.get(0));
        // tökum út gamla snákinn
        getChildren().remove(snakur);
        if (snakurController.getErTank()) {
            tankUppsetning();
        } else {
            tveirUppsetning();
        }
        snakurToFront(motherjaSnakur);
    }

    /**
     * Býr til snák og mótherjasnák, og frumstillir byssuskots
     * fylki þeirra
     */
    public void tankUppsetning() {
        snakurSetUp(snakur, GildiInt.SNAKUR2XYHNIT.getValue(),
                GildiInt.SNAKUR2XYHNIT.getValue(),
                GildiInt.HAEGRI.getValue());
        snakurSetUp(motherjaSnakur, GildiInt.MOTHERJATANKXHNIT.getValue(),
                GildiInt.MOTHERJATANKYHNIT.getValue(),
                GildiInt.VINSTRI.getValue());
        bSkot1 = FXCollections.observableArrayList();
        bSkot2 = FXCollections.observableArrayList();
    }

    /**
     * Býr til snák og mótherjasnák, frumstillir matar
     * fylki og eldar mat tvisvar
     */
    public void tveirUppsetning() {
        snakurSetUp(snakur, GildiInt.SNAKUR2XYHNIT.getValue(),
                GildiInt.SNAKUR1YHNIT.getValue(),
                GildiInt.HAEGRI.getValue());
        snakurSetUp(motherjaSnakur, GildiInt.MOTHERJATANKXHNIT.getValue(),
                GildiInt.SNAKUR1YHNIT.getValue(),
                GildiInt.VINSTRI.getValue());
        matur = FXCollections.observableArrayList();
        // höfum tvær fæður á borði á hverjum tíma
        eldaMat();
        eldaMat();
    }

    /**
     * Býr til nýjan snák með gefnum hnitum og stefnu, bætir
     * við á leikborðið og setur viðeigandi mynd að auki
     *
     * @param s      segir til um hvort hluturinn er snákur
     *               eða mótherjasnákur
     * @param x      x hnit
     * @param y      y hnit
     * @param stefna stefnan
     */
    private void snakurSetUp(Snakur s, int x, int y, int stefna) {
        Snakur nyr = new Snakur(x, y);
        if (s == snakur) {
            snakur = nyr;
            myndASnak(nyr, resourceBundle.getString("green"));
        } else {
            motherjaSnakur = nyr;
            myndASnak(nyr, resourceBundle.getString("red"));
        }
        getChildren().add(nyr);
        eitradCheck(nyr);
        nyr.setStefna(stefna);
    }

    /**
     * Skoðar hvort veggir séu eitraðir og segir snák hlutnum
     * ef svo er
     *
     * @param s snákurinn
     */
    public void eitradCheck(Snakur s) {
        if (eitrad) {
            s.setErEitrad(true);
        }
    }

    /**
     * Setur viðeigandi mynd á snák
     *
     * @param s     snákurinn
     * @param color green ef grænn snákur, red annars
     */
    public void myndASnak(Snakur s, String color) {
        ImagePattern imagePattern = new ImagePattern(new Image(
                getClass().getResourceAsStream("Myndir/" + color +
                        "Snake.jpg")));
        s.setFill(imagePattern);
    }

    public void snakurToFront(Snakur s) {
        int index = getChildren().indexOf(s);
        getChildren().get(index).toFront();
    }

    /**
     * Færir snák áfram með því að kalla á aðferð í Snakur.
     * Skoðar hvort snákur sé að borða og hvort rekist hafi á vegg.
     */
    public void afram1() {
        snakur.afram();
        borda();
    }

    /**
     * Færir báða snáka áfram með því að kalla á aðferð í Snakur.
     * Skoðar hvort einhver hafi rekist á vegg og ef ekki tank útgáfa
     * hvort snákur sé að borða
     */
    public void afram2() {
        snakur.afram();
        motherjaSnakur.afram();
        if (snakurController.getErTank()) {
            tankRekistA();
        } else {
            motherjiRekistA();
            borda();
        }
    }

    /**
     * Færir eitursnáka áfram með því að kalla á aðferð í Snakur.
     * Skoðar hvort snákur sé að rekast á eitursnák.
     */
    public void aframEitursnakar() {
        for (Snakur value : eiturSnakar) {
            value.afram();
        }
        rekistA();
    }

    /**
     * Býr til byssuskot sem meiðir aðeins mótherja
     */
    public void skjota1() {
        // gerir ekkert ef leik er lokið
        if (snakurController.getNotLokid()) {
            if (bSkot1.isEmpty()) {
                skjota(snakur);
            }
            // verður að vera lítil pása milli byssuskota
            else if (pasaMilliSkota(snakur)) {
                skjota(snakur);
            }
        }
    }

    /**
     * Býr til byssuskot sem meiðir aðeins mótherja
     */
    public void skjota2() {
        if (snakurController.getNotLokid()) {
            if (bSkot2.isEmpty()) {
                skjota(motherjaSnakur);
            }
            // verður að vera lítil pása milli byssuskota
            else if (pasaMilliSkota(motherjaSnakur)) {
                skjota(motherjaSnakur);
            }
        }
    }

    /**
     * Frames per second forritsins eru mjög há, svo til þess að
     * koma í veg fyrir að skotið er 2 með 10 ms bili þegar notandi
     * ýtir á skjóta force-a ég pásu meða því að reikna bil milli
     * skotsins og snáksins
     *
     * @param s snákurinn sem skaut
     * @return true ef hægt er að skjóta aftur, false annars
     */
    public boolean pasaMilliSkota(Snakur s) {
        ObservableList<Snakur> skotListi;
        if (s == snakur) {
            skotListi = bSkot1;
        } else {
            skotListi = bSkot2;
        }
        return Math.abs(s.getX() - skotListi.get(0).getX()) > GildiInt
                .LENGDFRA.getValue() &&
                skotListi.size() < 2 || Math.abs(s.getY() - skotListi
                .get(0).getY()) > GildiInt.LENGDFRA.getValue() &&
                skotListi.size() < 2;
    }

    /**
     * Býr til byssuskot og setur í viðeigandi obs lista
     *
     * @param s snákurinn sem skaut
     */
    public void skjota(Snakur s) {
        // stoppa leikmann frá að skjóta meðan hann er meiddur
        if (!s.getFill().equals(
                new Color(0.33, 0, 0, 1))) {
            Snakur skot = new Snakur(s);
            skot.setErByssuSkot(true);
            skot.setFill(s.getFill());
            if (s == snakur) {
                bSkot1.add(skot);
            } else {
                bSkot2.add(skot);
            }
            getChildren().add(skot);
        }
    }

    /**
     * Færir byssuskot leikmanns 1 áfram
     */
    public void aframByssuSkot1() {
        if (!bSkot1.isEmpty()) {
            aframByssuSkot(snakur);
        }
    }

    /**
     * Færir byssuskot leikmanns 1 áfram
     */
    public void aframByssuSkot2() {
        if (!bSkot2.isEmpty()) {
            aframByssuSkot(motherjaSnakur);
        }
    }

    /**
     * Færir viðeigandi byssuskot áfram. Eyðir þeim ef
     * þeir hafa rekist á mótherja eða vegg
     *
     * @param s snákurinn sem skaut
     */
    public void aframByssuSkot(Snakur s) {
        ObservableList<Snakur> bSkot;
        if (s == snakur) {
            bSkot = bSkot1;
        } else {
            bSkot = bSkot2;
        }

        // ítrum í gegnum viðeigandi byssuskot
        for (Iterator<Snakur> iterator = bSkot.iterator();
             iterator.hasNext(); ) {
            Snakur skot = iterator.next();
            skot.afram();
            // ef byssuskot hefur skotið snák eða skoppað nógu oft
            if (s == snakur && rekistA1Byssu(skot) || s == motherjaSnakur
                    && rekistA2Byssu(skot) || skot.getBskotBuid()) {
                // eyða honum
                getChildren().remove(skot);
                iterator.remove();
            }
        }
    }

    /**
     * Skoðar hvort leikmaður 2 hafi rekist á byssuskot leikmanns 1,
     * og ef svo er klárar leikinn ef leikmaður 2 hefur engin
     * líf eftir
     *
     * @param skot byssuskotið sem skoða skal
     * @return true ef byssuskotið rakst á, false annars
     */
    public boolean rekistA1Byssu(Snakur skot) {
        if (motherjaSnakur.getBoundsInParent()
                .intersects(skot.getBoundsInParent())) {
            if (++damage2 == lif) {
                snakurController.leikurLokid(
                        resourceBundle.getString("snakur"));
            }
            rekistAByssu(motherjaSnakur);
            return true;
        }
        return false;
    }

    /**
     * Skoðar hvort leikmaður 1 hafi rekist á byssuskot leikmanns 2,
     * og ef svo er klárar leikinn ef leikmaður 1 hefur engin
     * líf eftir
     *
     * @param skot byssuskotið sem skoða skal
     * @return true ef byssuskotið rakst á, false annars
     */
    public boolean rekistA2Byssu(Snakur skot) {
        if (snakur.getBoundsInParent()
                .intersects(skot.getBoundsInParent())) {
            if (++damage1 == lif) {
                snakurController.leikurLokid("Eitursnákur");
            }
            rekistAByssu(snakur);
            return true;
        }
        return false;
    }

    /**
     * Breytir lit snáks sem meiddist í hálfa sekúndu
     *
     * @param s snákurinn sem meiddist
     */
    public void rekistAByssu(Snakur s) {
        String color;
        if (s == snakur) {
            color = resourceBundle.getString("green");
        } else {
            color = resourceBundle.getString("red");
        }
        s.setFill(new Color(0.33, 0, 0, 1));
        KeyFrame k = new KeyFrame(Duration.millis(500),
                e -> myndASnak(s, color));
        Timeline t = new Timeline(k);
        t.setCycleCount(1);
        t.play();
    }

    /**
     * Skoðar fyrir hvorn eitursnák hvort hann hafi rekist á
     * snákinn, og kallar á aðferð úr SnakurController ef svo
     * er til að stöðva leikinn.
     */
    public void rekistA() {
        for (Snakur eitur : eiturSnakar) {
            if (eitur.getBoundsInParent().intersects(
                    snakur.getBoundsInParent())) {
                snakurController.leikurLokid(null);
            }
        }
    }

    /**
     * Skoðar hvort leikmaður 1 hafi rekist á leikmann 2, og
     * klárar leik ef svo er
     */
    public void motherjiRekistA() {
        tveirLeikmennRekistA(resourceBundle.getString("eitursnakur"));
    }

    /**
     * Skoðar hvort leikmaður 1 hafi rekist á leikmann 2, og
     * klárar leik ef svo er, enginn vinnur hér þó
     */
    public void tankRekistA() {
        tveirLeikmennRekistA(resourceBundle.getString("enginn"));
    }

    /**
     * Klárar leik ef mótherjasnákurinn rekst á snákinn
     *
     * @param hverVann leikmaður sem vann leikinn
     */
    public void tveirLeikmennRekistA(String hverVann) {
        if (motherjaSnakur.getBoundsInParent().intersects(
                snakur.getBoundsInParent())) {
            snakurController.leikurLokid(hverVann);
        }
    }

    /**
     * Býr til mat og setur á leikborðið.
     */
    public void eldaMat() {
        Faeda x = new Faeda();
        if (matur.isEmpty()) {
            while (maturNalaegtSnak(x)) {
                x = new Faeda();
            }
        }
        else {
            x = eldaMatAftur(x);
        }
        getChildren().add(x);
        matur.add(x);
    }

    /**
     * Reiknar hvort fæða sé of nálæg snák
     *
     * @param faeda fæðan sem skoða á
     * @return true ef of nálæg, false annars
     */
    private boolean maturNalaegtSnak(Faeda faeda) {
        return Math.abs(faeda.getCenterX() -
                snakur.getX()) < GildiInt.LENGDFRA.getValue() ||
                Math.abs(faeda
                .getCenterY() - snakur.getY()) <
                        GildiInt.LENGDFRA.getValue();
    }

    /**
     * Býr til nýja fæðu þar til hún er nógu langt frá annari
     * fæðu og snák
     *
     * @param x nýja fæðan
     */
    private Faeda eldaMatAftur(Faeda x) {
        for (Faeda faeda : matur) {
            while (maturOfNalaegt(faeda, x) ||
                    maturNalaegtSnak(x)) {
                x = new Faeda();
            }
        }
        return x;
    }

    /**
     * Reiknar ef fæða faeda er of nálæg annari fæðu x
     *
     * @param faeda maturinn sem skoða á
     * @return true ef maturinn er of nálægt hinum matnum
     * false annars
     */
    private boolean maturOfNalaegt(Faeda faeda, Faeda x) {
        return Math.abs(faeda.getCenterX() -
                x.getCenterX()) < GildiInt.LENGDFRA.getValue()
                && Math.abs(faeda.getCenterY() - x.getCenterY()) <
                GildiInt.LENGDFRA.getValue();
    }

    /**
     * Skoðar fyrir hverja fæðu hvort snákur hafi borðið
     * og bregst við ef svo er eftir útgáfu leiks
     */
    public void borda() {
        for (int i = 0; i < matur.size(); i++) {
            Faeda f = matur.get(i);
            if (f.intersects(snakur.getBoundsInParent())) {
                // hækka stig
                snakurController.getLeikur().vinningur();
                // eyða mat
                getChildren().remove(f);
                matur.remove(f);
                snakur.vaxa();
                if (snakurController.getErEinnLeikmadur()) {
                    borda1();
                    snakurController.hradar1();
                } else {
                    borda2();
                    snakurController.hradar2();
                }
            }
        }
    }

    /**
     * Býr e.t.v. til fleiri eitursnáka og eldar mat
     */
    public void borda1() {
        eldaMat();
        /* lækkandi líkur eftir því hversu margir eitursnákar eru
           á borði */
        if (!eitrad && StdRandom.uniform(101) < likur.get(eStig) -
                eiturSnakar.size() * 10) {
            fleiriEitursnakar();
        }
    }

    /**
     * Klárar leik ef leikmaður 1 hefur borðað allan matinn
     * og eldar mat
     */
    public void borda2() {
        eldaMat();
        if (snakurController.getLeikur().getStig() ==
                Integer.parseInt(snakurController.getMagn())) {
            // klára leik
            snakurController.leikurLokid(resourceBundle
                    .getString("snakur"));
        }
    }

    /**
     * Býr til nýjan eitursnák með random hnitum og stefnu
     */
    public void fleiriEitursnakar() {
        Random rand = new Random();
        Snakur x = new Snakur(rand.nextInt(570),
                rand.nextInt(370));
        // meðan hnit eitursnáks of nálægt snáki
        while (eitursnakurOfNalaegt(x)) {
            x = new Snakur(rand.nextInt(570),
                    rand.nextInt(370));
        }
        myndASnak(x, resourceBundle.getString("red"));
        // fá random stefnu
        int randomStefna = stefnuListi.get(rand.nextInt(
                stefnuListi.size()));
        x.setStefna(randomStefna);
        getChildren().add(x);
        eiturSnakar.add(x);
    }

    /**
     * Reiknar hvort að eitursnákur sé of nálægur leikmanni 1
     *
     * @param nyr nýji eitursnákurinn
     * @return true ef eitursnákurinn er of nálægur leikmanni 1,
     * false annars
     */
    public boolean eitursnakurOfNalaegt(Snakur nyr) {
        return Math.abs(nyr.getX() - snakur.getX()) <
                GildiInt.LENGDFRA.getValue() &&
                Math.abs(nyr.getY() - snakur.getY()) <
                        GildiInt.LENGDFRA.getValue();
    }

    /**
     * Breytir stefnu random eitursnáks á semi-random hátt
     */
    public void eitursnakarRandomStefna() {
        Random rand = new Random();
        KeyFrame k = new KeyFrame(Duration.millis(1000),
                e -> {
                    /* lækkandi líkur á því að eitursnákur skipti um
                       stefnu eftir því hversu margir eitursnákar
                       eru á borði */
                    if (!eiturSnakar.isEmpty() && StdRandom
                            .uniform(101) < likur.get(eStig) -
                            eiturSnakar.size() * 5) {
                        int randomStefna = stefnuListi.get(rand.nextInt(
                                stefnuListi.size()));
                        Snakur x = eiturSnakar.get(rand
                                .nextInt(eiturSnakar.size()));
                        x.setStefna(randomStefna);
                    }
                });
        Timeline t = new Timeline(k);
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }

    /**
     * Eyðir öllum börnum SnakurPane nema bakgrunnsmyndinni,
     * og setur upp SnakurPane á ný líkt og í contructornum.
     */
    public void reset() {
        getChildren().remove(1, getChildren().size());
        if (snakurController.getErEinnLeikmadur()) {
            uppsetning1();
        } else {
            uppsetningTveirOgTank();
            if (snakurController.getErTank()) {
                bSkot1.removeAll();
                bSkot2.removeAll();
                damage1 = 0;
                damage2 = 0;
            }
        }
    }

    /**
     * Pásar leikinn þegar ýtt er á leikborðið
     */
    public void pasaHandler() {
        EventHandler<MouseEvent> mus = mouseEvent -> {
            if (snakurController.getNotLokid()) {
                snakurController.bida();
            }
        };
        setOnMousePressed(mus);
    }

    /**
     * Einfaldur setter. Köllum á setUpplysingar() hér
     * því þetta er fyrsti tímapunkturinn sem snakurController
     * er ekki null
     *
     * @param snakurController snakurControllerinn
     */
    public void setController(SnakurController snakurController) {
        this.snakurController = snakurController;
        setUpplysingar();
    }

    /**
     * Skoðar hvaða útgáfa er í spilun, og setur upp
     * eiginleika sem eiga við útgáfuna. Setur einnig
     * upp pása fítusinn
     */
    public void setUpplysingar() {
        eitrad = snakurController.getEitrad();
        if (snakurController.getErEinnLeikmadur()) {
            eStig = snakurController.getStig();
        } else {
            if (snakurController.getErTank()) {
                lif = snakurController.getLif();
            }
            uppsetningTveirOgTank();
        }
        if (eitrad) {
            snakur.setErEitrad(true);
        }
        pasaHandler();
    }

    public void setStefna1(int x) {
        snakur.setStefna(x);
    }

    public void setStefna2(int x) {
        motherjaSnakur.setStefna(x);
    }

    public Snakur getSnakur() {
        return snakur;
    }

    public Snakur getMotherjaSnakur() {
        return motherjaSnakur;
    }

}
