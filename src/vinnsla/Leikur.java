package vinnsla;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/******************************************************************************
 *  Nafn    : Nikhil Kumar
 *  T-póstur: nik10@hi.is
 *
 *  Lýsing  : Klasi sem sér um vinnsluna. Sér um stigatöfluna, stigin og að
 *            búa til simpleStringProperty.
 *
 *
 *****************************************************************************/

public class Leikur {

    // java breytur
    private final ObservableList<Integer> stigaTafla;
    private int stig;
    private int totalStig;
    private SimpleStringProperty simpleStringProperty;


    public Leikur() {
        stig = 0;
        totalStig = 0;
        stigaTafla = FXCollections.observableArrayList();
        simpleStringProperty = new SimpleStringProperty(Integer
                .toString(stig));
    }

    /**
     * Hækkar stig um einn og setur upp property.
     */
    public void vinningur() {
        stig++;
        totalStigCheck();
    }

    /**
     * Bætir núverandi stig við í töfluna.
     */
    public void leikLokid() {
        stigaTafla.add(stig);
    }

    /**
     * Skoðar hvort 2 leikmanna útgáfa er í gangi og uppfærir property
     */
    public void totalStigCheck() {
        if (totalStig == 0) {
            simpleStringProperty.setValue(String.valueOf(stig));
        } else {
            simpleStringProperty.setValue(stig + " / " + totalStig);
        }
    }

    public ObservableList<Integer> getStigaTafla() {
        return stigaTafla;
    }

    public void setStig(int stig) {
        this.stig = stig;
        totalStigCheck();
    }

    public int getStig() {
        return stig;
    }

    public void setTotalStig(int x) {
        totalStig = x;
        simpleStringProperty = new SimpleStringProperty(stig + " / " + totalStig);
    }

    public SimpleStringProperty stigProperty() {
        return simpleStringProperty;
    }

}
