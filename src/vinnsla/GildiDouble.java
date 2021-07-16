package vinnsla;

/*****************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Enum klasi sem geymir ýmsar fastar double breytur. Notar
 *            constructor og getValue aðferð til að útfæra gildi fyrir hvert
 *            enum
 *
 *
 *****************************************************************************/

public enum GildiDouble {

    // SnakurController fastar
    // hversu mikið hraði snáks í 2 leikmanna útgáfu skal aukast
    SNAKUR2HRADAR(0.2),
    // hversu mikið hraði mótherjasnáks í 2 leikmanna útgáfu skal aukast
    MOTHERJA2HRADAR(0.08),
    // hversu mikið hraði snáka í Tank útgáfu skal aukast
    TANKHRADAR(0.001),

    // Snakur fastar
    // hversu mikið snákur skal lengjast þegar hann borðar
    VAXAOFFSET(4.5),
    // hversu langt byssuskot skal færast á hverju frame
    bSkotHradi(3);


    private final double valueDouble;

    /**
     * Gefur enuminu gildi
     *
     * @param value gildið
     */
    GildiDouble(double value) {
        this.valueDouble = value;
    }

    public double getValue() {
        return valueDouble;
    }

}
