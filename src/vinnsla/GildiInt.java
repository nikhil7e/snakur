package vinnsla;

/*****************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Enum klasi sem geymir ýmsar fastar int breytur. Notar constructor
 *            og getValue aðferð til að útfæra gildi fyrir hvert enum
 *
 *
 *****************************************************************************/

public enum GildiInt {

    // stefnur fyrir snáka
    UPP(90),
    NIDUR(270),
    VINSTRI(180),
    HAEGRI(360),

    // Adal fastar
    //  fyrir stærð senu
    SCENEWIDTH(800),
    SCENEHEIGHT(500),

    // SnakurController fastar
    // hversu löng hver frame skal vera
    TIMI(10),

    // SnakurPane fastar
    // ath. sum hnit ery notuð á fleiri en 1 stað
    // x hnit snáks í eins leikmanna útgáfu
    SNAKUR1XHNIT(300),
    // y hnit snáka í eins og tveggja leikmanna útgáfu
    SNAKUR1YHNIT(200),
    // hnit snáks í  leikmanna útgáfu
    // x, y hnit snáks í tveggja leikmanna útgáfu
    SNAKUR2XYHNIT(45),
    // x hnit mótherjasnáks í Tank útgáfu
    MOTHERJATANKXHNIT(510),
    // y hnit mótherjasnáks í Tank útgáfu
    MOTHERJATANKYHNIT(340),
    // lægsta fjarlægð hlutur þarf að vera frá öðrum tilteknum hlut
    LENGDFRA(75),

    // Snakur fastar
    // breidd og hæð snáks
    WIDTH(45),
    HEIGHT(20),
    // hversu oft byssuskot skal skoppa af vegg áður en hann hverfur
    HAMARKSKOPP(2),

    // Faeda fastar
    // stærsta x gildi fæðu
    FAEDAMAXX(550),
    // stærsta y gildi fæðu
    FAEDAMAXY(350),
    // radíus fæðu hrings
    FAEDARADIUS(15),
    // hliðrum mat alltaf smá til, til að koma í veg fyrir veggja collision
    FAEDAOFFSET(35),

    // EinnStillingarController fastar
    // útgáfa 1
    NRMODE1(1),

    // TveirStillingarController fastar
    // útgáfa 2
    NRMODE2(2),
    // default fjöldi matar
    GRUNNMATUR(5),

    // TankStillingarController fastar
    // útgáfa 3
    NRMODE3(3),
    // default fjöldi lífa
    GRUNNLIF(1),
    ;


    // gildi enums
    private final int valueInt;


    /**
     * Gefur enuminu gildi
     *
     * @param value gildið
     */
    GildiInt(int value) {
        this.valueInt = value;
    }

    public int getValue() {
        return valueInt;
    }

}
