package vidmot.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import vidmot.Adal;

import java.net.URL;
import java.util.ResourceBundle;

/*****************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Einfaldur controller fyrir inngangsgluggann. Segir Adal klasa
 *            hvaða útgáfu leikmaður hefur valið
 *
 *
 *****************************************************************************/

public class InngangurController implements Initializable {

    private Adal adal;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void einnHandler(ActionEvent actionEvent) {
        adal.showSettings("fxml/EinnStillingar.fxml", 1);
    }

    public void tveirHandler(ActionEvent actionEvent) {
        adal.showSettings("fxml/TveirStillingar.fxml", 2);
    }

    public void tankHandler(ActionEvent actionEvent) {
        adal.showSettings("fxml/TankStillingar.fxml", 3);
    }

    public void setAdal(Adal adal) {
        this.adal = adal;
    }

}
