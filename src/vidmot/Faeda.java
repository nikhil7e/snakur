package vidmot;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import vinnsla.GildiInt;

import java.util.Random;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Klasi sem sér um fæðuna. Býr hana til á random staðsetningu,
 *            innan leiksvæðisins.
 *
 *
 *****************************************************************************/

public class Faeda extends Circle {

    public Faeda() {
        // notum super til að kalla á constructor úr yfirklasa
        super(new Random().nextInt(GildiInt.FAEDAMAXX.getValue()) +
                GildiInt.FAEDAOFFSET.getValue(),
                new Random().nextInt(GildiInt.FAEDAMAXY.getValue()) +
                        GildiInt.FAEDAOFFSET.getValue(),
                GildiInt.FAEDARADIUS.getValue());
        // Setjum ImagePattern á fæðuna (hamborgari)
        ImagePattern imagePattern = new ImagePattern(new Image(getClass()
                .getResourceAsStream("Myndir/burger.png")));
        setFill(imagePattern);
    }

}
