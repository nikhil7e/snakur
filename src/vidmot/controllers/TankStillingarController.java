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
 *  Lýsing  : Sér um stillingagluggann fyrir Tank útgáfu. Tekur við
 *            gögnum frá notanda og sendar í Adal klasann.
 *
 *
 *****************************************************************************/
public class TankStillingarController implements Initializable {

    // java tilviksbreytur
    private Adal adal;
    // eru veggir eitraðir?
    private boolean eitrad;
    // eitrað en á íslensku fyrir label
    private SimpleStringProperty eitradIsl;
    // fjöldi lífa leikmanna
    private SimpleIntegerProperty fjoldiLifa;
    // heildartexti labels
    private String heild;
    // inniheldur texta
    private ResourceBundle resourceBundle;

    // javafx breytur
    @FXML
    private Label fxValLabel;
    @FXML
    private TextField fxFjoldiLifa;
    @FXML
    private Button fxAfram;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        eitradIsl = new SimpleStringProperty(
                this.resourceBundle.getString("nei"));
        fjoldiLifa = new SimpleIntegerProperty(GildiInt.GRUNNLIF.getValue());

        // breyta label ef fjöldi lífa breytist
        fxFjoldiLifa.textProperty().addListener((obs, oldText, newText) -> {
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
        return !fxFjoldiLifa.getText().equals("") &&
                !fxFjoldiLifa.getText().equals("0") &&
                !fxFjoldiLifa.getText().contains("-");
    }

    /**
     * Ef tala er lögleg þá uppfæra magnMats, annars default-a
     * í 1
     */
    public void loglegTala() {
        try {
            if (!isPosInt()) {
                fjoldiLifa.setValue(1);
            } else {
                fjoldiLifa.setValue(Integer.valueOf(fxFjoldiLifa.getText()));
            }
        }
        // ef notandi hefur ekki skrifað int þá default-a í 5
        catch (NumberFormatException e) {
            fjoldiLifa.setValue(1);
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
    public void tilBaka2Handler(ActionEvent actionEvent) throws IOException {
        adal.showIntro();
    }

    /**
     * Sýnir leikborð
     *
     * @param actionEvent ekki notað
     */
    public void aframHandler(ActionEvent actionEvent) throws IOException {
        adal.synaLeik(GildiInt.NRMODE3.getValue(), String.valueOf(
                fjoldiLifa.getValue().intValue()), eitrad);
    }

    /**
     * Uppfærir heild string
     */
    public void updateHeild() {
        heild = this.resourceBundle.getString("fjoldilifa") + ": "
                + fjoldiLifa.getValue() + ". " + this.resourceBundle
                .getString("veggireitradir") + ": " +
                eitradIsl.getValue() + ".";
    }

    public void setAdal(Adal adal) {
        this.adal = adal;
    }

}
