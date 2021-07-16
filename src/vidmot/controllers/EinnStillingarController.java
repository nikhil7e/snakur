package vidmot.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import vidmot.Adal;
import vinnsla.GildiInt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*****************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Sér um stillingagluggann fyrir 1 leikmanna útgáfu. Tekur við
 *            gögnum frá notanda og sendar í Adal klasann.
 *
 *
 *****************************************************************************/

public class EinnStillingarController implements Initializable {

    // java tilviksbreytur
    private Adal adal;
    // eru veggir eitraðir
    private boolean eitrad;
    // erfiðleikastigið
    private String erfidleiki;
    // eitrað en á íslensku fyrir label
    private SimpleStringProperty eitradIsl;
    // erfiðleikastig en á íslensku fyrir label
    private SimpleStringProperty erfidIsl;
    // heildartexti labels
    private String heild;
    // inniheldur texta
    private ResourceBundle resourceBundle;

    // javafx bretytur
    @FXML
    private Label fxValLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        erfidleiki = this.resourceBundle.getString("midlungs");
        eitrad = false;
        erfidIsl = new SimpleStringProperty(
                this.resourceBundle.getString("midlungsisl"));
        eitradIsl = new SimpleStringProperty(
                this.resourceBundle.getString("nei"));
        updateHeild();

        // breyta label ef erfiðleikastig er valið / veggir eru eitraðir
        erfidIsl.addListener((v, oldValue, newValue) -> {
           updateHeild();
            fxValLabel.setText(heild);
        });

        // breyta label ef erfiðleikastig er valið / veggir eru eitraðir
        eitradIsl.addListener((v, oldValue, newValue) -> {
            updateHeild();
            fxValLabel.setText(heild);
        });
    }

    /**
     * Stillir létt erfiðleikastig
     *
     * @param actionEvent ekki notað
     */
    public void lettHandler(ActionEvent actionEvent) {
        erfidleiki = resourceBundle.getString("lett");
        erfidIsl.setValue(resourceBundle.getString("lettisl"));
    }

    /**
     * Stillir miðlungs erfiðleikastig
     *
     * @param actionEvent ekki notað
     */
    public void midlungsHandler(ActionEvent actionEvent) {
        erfidleiki = resourceBundle.getString("midlungs");
        erfidIsl.setValue(resourceBundle.getString("midlungsisl"));
    }

    /**
     * Stillir erfitt erfiðleikastig
     *
     * @param actionEvent ekki notað
     */
    public void erfittHandler(ActionEvent actionEvent) {
        erfidleiki = resourceBundle.getString("erfitt");
        erfidIsl.setValue(resourceBundle.getString("erfittisl"));
    }

    /**
     * Stillir hvort veggir séu eitraðir eða ekki
     *
     * @param actionEvent ekki notað
     */
    public void veggirEitradirHandler(ActionEvent actionEvent) {
        if (!eitrad) {
            eitrad = true;
            eitradIsl.setValue(resourceBundle.getString("ja"));
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
    public void tilBaka1Handler(ActionEvent actionEvent) throws IOException {
        adal.showIntro();
    }

    /**
     * Sýnir leikborð
     *
     * @param actionEvent ekki notað
     */
    public void aframHandler(ActionEvent actionEvent) throws IOException {
        adal.synaLeik(GildiInt.NRMODE1.getValue(), erfidleiki, eitrad);
    }

    /**
     * Uppfærir heild strenginn
     */
    public void updateHeild() {
        heild = this.resourceBundle.getString("erfidleikastig") + ": "
                + erfidIsl.getValue() + ". " + this.resourceBundle
                .getString("classic") + ": " + eitradIsl.getValue() + ".";
    }

    public void setAdal(Adal adal) {
        this.adal = adal;
    }

}
