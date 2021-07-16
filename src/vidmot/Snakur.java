package vidmot;

import javafx.scene.shape.Rectangle;
import vinnsla.GildiDouble;
import vinnsla.GildiInt;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Klasi sem sér um snáka/byssukot og að færa hann í einhverja
 *            stefnu. Hefur einnig aðferðir sem færa snák og skoða hvort hann
 *            hafi rekist á vegg.
 *
 *
 *****************************************************************************/

public class Snakur extends Rectangle {

    // java tilviksbreytur
    // hversu langt snákur skal færast á hverju frame
    private double hradi;
    private double xPos;
    private double yPos;
    // stefna snáks
    private int stefna;
    // hvort leikur sé búinn
    private boolean leikLokid;
    // hvort byssuskot skuli hverfa
    private boolean bskotBuid;
    // hversu oft byssuskot skoppar af vegg
    private int nrSkopp;
    // hvort veggir séu eitraðir
    private boolean erEitrad;
    // hvort þessi hlutur sé byssuskot
    private boolean erByssuSkot;


    public Snakur(int x, int y) {
        // notum super til að nota Rectangle smið
        super(x, y, GildiInt.WIDTH.getValue(), GildiInt.HEIGHT.getValue());
        xPos = x;
        yPos = y;
        setStefna(GildiInt.UPP.getValue());
        hradi = 1;
        leikLokid = false;
    }

    /**
     * Annar constructor sem býr til byssuskotin
     *
     * @param snake snákurinn sem skaut
     */
    public Snakur(Snakur snake) {
        // hefur sömu breidd og hæð
        super(snake.xPos, snake.yPos, GildiInt.HEIGHT.getValue(),
                GildiInt.HEIGHT.getValue());
        xPos = snake.xPos;
        yPos = snake.yPos;
        setStefna(snake.stefna);
        leikLokid = false;
        hradi = GildiDouble.bSkotHradi.getValue();
        bskotBuid = false;
        nrSkopp = 0;
        erEitrad = false;
        erByssuSkot = true;
    }

    /**
     * Skoðar stefnuna og sendir snák í viðeigandi átt um bil hradi.
     * Skoðar hvort rekist hafi verið á vegg og bregst við
     */
    public void afram() {
        if (stefna == GildiInt.NIDUR.getValue()) {
            yPos += hradi;
            setY(yPos);
            setRotate(GildiInt.NIDUR.getValue());
            nedriVeggur();
        } else if (stefna == GildiInt.HAEGRI.getValue()) {
            xPos += hradi;
            setX(xPos);
            setRotate(GildiInt.HAEGRI.getValue());
            haegriVeggur();
        } else if (stefna == GildiInt.UPP.getValue()) {
            yPos -= hradi;
            setY(yPos);
            setRotate(GildiInt.UPP.getValue());
            efriVeggur();
        } else {
            xPos -= hradi;
            setX(xPos);
            setRotate(GildiInt.VINSTRI.getValue());
            vinstriVeggur();
        }
    }

    /**
     * Reiknar hvort snákur hafi rekist á neðri vegg.
     * Ef veggir eru eitraðir er leik lokið þegar snákur rekst
     * á vegg. Ef hluturinn er byssuskot eru nr skopps, rakstAVegg
     * og stefna leiðrétt á viðeigandi hátt
     */
    public void nedriVeggur() {
        if (getBoundsInParent().getMaxY() >= (int) ((SnakurPane)
                getParent()).getHeight()) {
            if (erByssuSkot) {
                stefna = GildiInt.UPP.getValue();
                byssuSkotCheck();
            } else {
                yPos = getRotateOffset();
                if (erEitrad) {
                    leikLokid = true;
                }
            }
        }
    }

    /**
     * Reiknar hvort snákur hafi rekist á efri vegg.
     * Ef veggir eru eitraðir er leik lokið þegar snákur rekst
     * á vegg. Ef hluturinn er byssuskot eru nr skopps, rakstAVegg
     * og stefna leiðrétt á viðeigandi hátt
     */
    public void haegriVeggur() {
        if (getBoundsInParent().getMaxX() >= (int) ((SnakurPane)
                getParent()).getWidth()) {
            if (erByssuSkot) {
                stefna = GildiInt.VINSTRI.getValue();
                byssuSkotCheck();
            } else {
                xPos = 0;
                if (erEitrad) {
                    leikLokid = true;
                }
            }
        }
    }

    /**
     * Reiknar hvort snákur hafi rekist á efri vegg.
     * Ef veggir eru eitraðir er leik lokið þegar snákur rekst
     * á vegg. Ef hluturinn er byssuskot eru nr skopps, rakstAVegg
     * og stefna leiðrétt á viðeigandi hátt
     */
    public void efriVeggur() {
        // ef snertir efri vegg
        if (getBoundsInParent().getMinY() <= 0) {
            if (erByssuSkot) {
                stefna = GildiInt.NIDUR.getValue();
                byssuSkotCheck();
            } else {
                yPos = ((SnakurPane) getParent()).getHeight() -
                        getWidth() + getRotateOffset();
                if (erEitrad) {
                    leikLokid = true;
                }
            }
        }
    }

    /**
     * Reiknar hvort snákur hafi rekist á vinstri vegg.
     * Ef veggir eru eitraðir er leik lokið þegar snákur rekst
     * á vegg. Ef hluturinn er byssuskot eru nr skopps, rakstAVegg
     * og stefna leiðrétt á viðeigandi hátt
     */
    public void vinstriVeggur() {
        if (getBoundsInParent().getMinX() <= 0) {
            if (erByssuSkot) {
                stefna = GildiInt.HAEGRI.getValue();
                byssuSkotCheck();
            } else {
                xPos = (int) ((SnakurPane) getParent())
                        .getWidth() - getWidth();
                if (erEitrad) {
                    leikLokid = true;
                }
            }
        }
    }

    /**
     * Hækkar nrSkopp og skoðar hvort byssuskot hafi skoppað 2 sinnum,
     * bSkotBuid er þá true
     */
    public void byssuSkotCheck() {
        nrSkopp++;
        if (nrSkopp == GildiInt.HAMARKSKOPP.getValue()) {
            bskotBuid = true;
        }
    }

    /**
     * Lengir snák um 4.5
     */
    public void vaxa() {
        setWidth(getWidth() + GildiDouble.VAXAOFFSET.getValue());
    }

    /**
     * setRotate hliðrar snákinum en ekki upprunalegu x- og y hnitin,
     * svo við þurfum að reikna hvar nýju hnitin eru
     *
     * @return rotate offsetinu
     */
    public double getRotateOffset() {
        return (getWidth() - getHeight()) * 0.5;
    }

    public void setStefna(int stefna) {
        this.stefna = stefna;
    }

    public void setHradi(double t) {
        this.hradi = t;
    }

    public double getHradi() {
        return hradi;
    }

    public boolean getLeiklokid() {
        return leikLokid;
    }

    public boolean getBskotBuid() {
        return bskotBuid;
    }

    public void setErEitrad(boolean x) {
        erEitrad = x;
    }

    public void setErByssuSkot(boolean x) {
        erByssuSkot = x;
    }

}
