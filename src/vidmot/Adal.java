package vidmot;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import vidmot.controllers.*;
import vinnsla.GildiInt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Aðal klasinn sem sér um að ræsa og loka forritinu. Klasinn sér
 *            einnig um að taka við upplýsingum frá stillingargluggum
 *            og sýna viðeigandi leik. Nú er samskiptaleið milli Aðal og
 *            SnakurController svo hægt verði að flytja upplýsingar um hvaða
 *            senu skal sýna og stillingar sem hafa áhrif á leik
 *
 *
 *****************************************************************************/

public class Adal extends Application {

    // java fastar
    // hakkatafla fyrir leikmann 1
    private final HashMap<KeyCode, Integer> map1 = new HashMap<>();
    // auka hakkatafla fyrir 2 leikmenn
    private final HashMap<KeyCode, Integer> map2 = new HashMap<>();
    private static final ButtonType bType = new ButtonType("Í lagi",
            ButtonBar.ButtonData.OK_DONE);
    private static final ButtonType hType = new ButtonType("Hætta við",
            ButtonBar.ButtonData.CANCEL_CLOSE);

    // java tilviksbreytur
    private SnakurController snakurController;
    private Stage stage;
    private ResourceBundle resourceBundle;


    /**
     * Setur upp senur og glugga og ræsir forrtið. Sýnir inngangsgluggann
     *
     * @param primaryStage aðal senan
     * @throws Exception villumelding
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        resourceBundle = ResourceBundle.getBundle("vidmot.texti");
        showIntro();
    }

    /**
     * Sýnir inngangsgluggann og setur upp samskiptaleið
     *
     * @throws IOException ef skrá finnst ekki
     */
    public void showIntro() throws IOException {
        FXMLLoader loaderIntro = new FXMLLoader(getClass()
                .getResource("fxml/Inngangur.fxml"),
                resourceBundle);
        Parent root1 = loaderIntro.load();
        InngangurController c1 = loaderIntro.getController();
        c1.setAdal(this);
        Scene intro = new Scene(root1, GildiInt.SCENEWIDTH.getValue(),
                GildiInt.SCENEHEIGHT.getValue());
        nyrGluggi(root1, intro);
    }

    /**
     * Sýnir viðeigandi stillingasenu og stillir controller
     *
     * @param filename fxml skrá fyrir stillingar viðmótið
     * @param nrMode   hvaða stillingu skal sýna.
     *                 1: 1 leikmaður, 2: 2 leikmenn, 3: tank
     */
    public void showSettings(String filename, int nrMode) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource(filename));
            Parent root = fxmlLoader.load();
            // stillir samskiptaleið fyrir viðeigandi controller
            // ef eins leikmanna útgáfa
            if (nrMode == 1) {
                EinnStillingarController c1 = fxmlLoader.getController();
                c1.setAdal(this);
            }
            // ef tveggja leikmanna útgáfa
            else if (nrMode == 2) {
                TveirStillingarController c1 = fxmlLoader.getController();
                c1.setAdal(this);
            }
            // ef Tank útgáfa
            else {
                TankStillingarController c1 = fxmlLoader.getController();
                c1.setAdal(this);
            }
            nyrGluggi(root, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Tekur við upplýsingum úr viðeigandi stillangar controller
     * og sýnir tilheyrandi útgáfu leiksins. Setur upp event filter
     * og sér um hvaða lyklum er ýtt á til að færa snák / skjóta
     *
     * @param nrMode útgáfa leiks. 1: 1 leikmaður, 2: 2 leikmenn,
     *               3: Tank
     * @param uppl   upplýsingar um valdar stillingar
     * @throws IOException ef ekki tekst að loada root
     */
    public void synaLeik(int nrMode, String uppl, boolean eitrad)
            throws IOException {
        FXMLLoader loaderGame = new FXMLLoader(getClass()
                .getResource("fxml/Snake.fxml"));
        Parent root2 = loaderGame.load();
        Scene s = new Scene(root2, GildiInt.SCENEWIDTH.getValue(),
                GildiInt.SCENEHEIGHT.getValue());
        nyrGluggi(root2, s);

        // setja upp samskiptaleið
        snakurController = loaderGame.getController();
        snakurController.setAdal(this);
        snakurController.setEitrad(eitrad);
        fillaMapEinn();

        // ef einn leikmaður
        if (nrMode == 1) {
            snakurController.setStig(uppl);
            einnLeikmadurFilter(s);
        }
        // ef 2 leikmanna útgáfa eða Tank
        else {
            if (nrMode == 2 || nrMode == 3) {
                fillaMapTveir();
                s.addEventFilter(KeyEvent.ANY,
                        event -> {
                            tveirLeikmennFilterVidbot(event);
                            // ef tank þá bæta við skot functionality
                            if (nrMode == 3) {
                                tankFilterVidbot(event);
                            }
                            event.consume();
                        });
            }
            // setja réttar stillingar
            if (nrMode == 2) {
                snakurController.setMagn(uppl);
            }
            // setja réttar stillingar
            else {
                snakurController.setLif(uppl);
                snakurController.setErTank(true);
            }
        }
    }

    /**
     * setjum upp beina aðganginn frá örvatökkunum og í hornið
     **/
    private void fillaMapEinn() {
        // setjum upp beina aðganginn frá örvatökkunum og í hornið
        map1.put(KeyCode.UP, GildiInt.UPP.getValue());
        map1.put(KeyCode.DOWN, GildiInt.NIDUR.getValue());
        map1.put(KeyCode.RIGHT, GildiInt.HAEGRI.getValue());
        map1.put(KeyCode.LEFT, GildiInt.VINSTRI.getValue());
    }

    /**
     * Setjum upp beina aðganginn frá örvatökkunum og í hornið
     * en nú fyrir leikmann 2
     **/
    private void fillaMapTveir() {
        map2.put(KeyCode.W, GildiInt.UPP.getValue());
        map2.put(KeyCode.S, GildiInt.NIDUR.getValue());
        map2.put(KeyCode.D, GildiInt.HAEGRI.getValue());
        map2.put(KeyCode.A, GildiInt.VINSTRI.getValue());
    }

    /**
     * Event filter fyrir einn leikmann (örvalyklar) breytir stefnu snáks
     * eftir því hvaða lykil er ýtt á
     *
     * @param s senan
     */
    private void einnLeikmadurFilter(Scene s) {
        //KeyEvents eru sendar á Scene
        s.addEventFilter(KeyEvent.ANY,
                event -> {
                    if (map1.containsKey(event.getCode()) &&
                            snakurController.getT()
                                    .getStatus() == Animation.Status.RUNNING) {
                        // flettum upp horninu fyrir KeyCode í map
                        snakurController.setStefna1(map1.
                                get(event.getCode()));
                    }
                    event.consume();
                });
    }

    /**
     * Sér um að setja rétta stefnu á 2 leikmönnunum (WASD og örvalyklar)
     * eftir því hvaða lykil er ýtt á
     *
     * @param event lykill sem ýtt er á
     */
    private void tveirLeikmennFilterVidbot(KeyEvent event) {
        if (map1.containsKey(event.getCode()) && snakurController.getT()
                .getStatus() == Animation.Status.RUNNING) {
            // flettum upp horninu fyrir KeyCode í map fyrir leikmann 1
            snakurController.setStefna1(map1.
                    get(event.getCode()));
        } else if (map2.containsKey(event.getCode()) &&
                snakurController.getT()
                        .getStatus() == Animation.Status.RUNNING) {
            // flettum upp horninu fyrir KeyCode í map fyrir leikmann 2
            snakurController.setStefna2(map2.
                    get(event.getCode()));
        }
    }

    /**
     * Sér um að skjóta ef ýtt er á space bar eða ctrl
     *
     * @param event lykill sem ýtt er á
     */
    private void tankFilterVidbot(KeyEvent event) {
        // má ekki skjóta ef leikur er pásaður
        if (!snakurController.erPasad()) {
            if (event.getCode() == KeyCode.SPACE) {
                snakurController.skjota2();
            }
            if (event.getCode() == KeyCode.CONTROL) {
                snakurController.skjota1();
            }
        }
    }

    /**
     * Setur upp handler fyrir að loka glugga
     *
     * @param stage glugginn
     */
    private void lokaGluggaHandler(Stage stage) {
        stage.setOnCloseRequest(event -> {
            boolean snakurContNotNull = false;
            if (snakurController != null) {
                // pása tímalínu
                snakurController.getT().pause();
                snakurContNotNull = true;
            }
            Alert a = stofnaAlert();
            // Bíða eftir svarinu
            Optional<ButtonType> svar = a.showAndWait();
            if (svar.isPresent() && svar.get() == bType) {
                Platform.exit();
                System.exit(0);
            } else {
                event.consume();
            }
            if (snakurContNotNull) {
                snakurController.getT().play();
            }
        });
    }

    /**
     * Birtir Alert glugga til að láta vita að hætta sé á ferð.
     *
     * @return Alert glugginn
     */
    public Alert stofnaAlert() {
        Alert a = new Alert(Alert.AlertType.NONE, resourceBundle.getString("spurning"),
                bType, hType);
        a.setTitle(resourceBundle.getString("nafnforrits"));
        a.setHeaderText(resourceBundle.getString("tilkynning"));
        return a;
    }

    /**
     * Birtir nýja senu sem er í root í glugganum s með titlinum t
     *
     * @param root  senan (viðmótstréð)
     * @param scene senan
     */
    private void nyrGluggi(Parent root, Scene scene) {
        stage.setTitle(resourceBundle.getString("nafnforrits"));
        if (scene == null) {
            scene = new Scene(root, GildiInt.SCENEWIDTH.getValue(),
                    GildiInt.SCENEHEIGHT.getValue());
        }
        stage.setScene(scene);
        lokaGluggaHandler(stage);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
