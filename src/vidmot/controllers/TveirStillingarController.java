package vidmot.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import vidmot.Adal;
import vinnsla.GildiInt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*****************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  :  Sér um stillingagluggann fyrir 2 leikmanna útgáfu. Tekur við
 *             gögnum frá notanda og sendar í Adal klasann.
 *
 *
 *****************************************************************************/
public class TveirStillingarController implements Initializable {

    // java tilviksbreytur
    private Adal adal;
    // eru veggir eitraðir
    private boolean eitrad;
    // eitrað en á íslensku fyrir label
    private SimpleStringProperty eitradIsl;
    // erfiðleikastig en á íslensku fyrir label
    private SimpleIntegerProperty magnMats;
    // heildartexti labels
    private String heild;
    // inniheldur texta
    private ResourceBundle resourceBundle;

    // javafx breytur
    @FXML
    private Label fxValLabel;
    @FXML
    private TextField fxMagnMats;
    @FXML
    private Button fxAfram;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        eitrad = false;
        magnMats = new SimpleIntegerProperty(GildiInt.GRUNNMATUR.getValue());
        eitradIsl = new SimpleStringProperty(
                this.resourceBundle.getString("nei"));

        // breyta label ef magn mats breytist
        fxMagnMats.textProperty().addListener((v, oldValue, newValue) -> {
            // skoðar hvort notandi hafi sett inn löglega tölu
            loglegTala();
            updateHeild();
            fxValLabel.setText(heild);
        });

        // breyta label ef veggir eru eitraðir
        eitradIsl.addListener((v, oldValue, newValue) -> {
            updateHeild();
            fxValLabel.setText(heild);
        });

        // taka fókus af textfield í byrjun
        Platform.runLater(() -> fxAfram.requestFocus());
    }

    /**
     * Skoðar hvort leikmaður hafi skrifað int tölu > 0
     *
     * @return true ef svo er, false annars
     */
    public boolean isPosInt() {
        return !fxMagnMats.getText().equals("") &&
                !fxMagnMats.getText().equals("0") &&
                !fxMagnMats.getText().contains("-");
    }

    /**
     * Ef tala er lögleg þá uppfæra magnMats, annars default-a
     * í 5
     */
    public void loglegTala() {
        try {
            if (!isPosInt()) {
                magnMats.setValue(5);
            } else {
                magnMats.setValue(Integer.valueOf((fxMagnMats.getText())));
            }
        }
        // ef notandi hefur ekki skrifað int þá default-a í 5
        catch (NumberFormatException e) {
            magnMats.setValue(5);
        }
    }

    /**
     * Stillir hvort veggir séu eitraðir eða ekki
     *
     * @param actionEvent ekki notað
     */
    public void veggirEitradirHandler(ActionEvent actionEvent) {
        if (!eitrad) {
            eitrad = true;
            eitradIsl.setValue(resourceBundle.getString("ja")) ;
        } else {
            eitrad = false;
            eitradIsl.setValue(resourceBundle.getString("nei"));
        }
    }

    /**
     * Fer aftur í inngangsglugga
     *
     * @param actionEvent ekki notað
     */
    public void tilBaka2Handler(ActionEvent actionEvent) throws IOException {
        adal.showIntro();
    }

    /**
     * Sýnir leikborð
     *
     * @param actionEvent ekki notað
     */
    public void aframHandler(ActionEvent actionEvent) throws IOException {
        adal.synaLeik(GildiInt.NRMODE2.getValue(),
                String.valueOf(magnMats.getValue().intValue()), eitrad);
    }

    /**
     * Uppfærir heild string og value í stingproperty
     */
    public void updateHeild() {
        heild = resourceBundle.getString("magnmats") + ": " +
                magnMats.getValue()  + ". " +
                resourceBundle.getString("veggireitradir") +
                ": " + eitradIsl.getValue() + ".";
    }

    public void setAdal(Adal adal) {
        this.adal = adal;
    }

}
