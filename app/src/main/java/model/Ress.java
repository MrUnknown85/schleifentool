/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

/**
 *
 * @author Nico
 */
public class Ress {

    private long eisen = 0;
    private long titan = 0;
    private long wasser = 0;
    private long wasserstoff = 0;
    private long nahrung = 0;
    private long einwohner = 0;
    private long einwohnerProzent = -1;

    public static Ress copy(Ress copyThis) {
        Ress copy = new Ress();
        copy.eisen = copyThis.eisen;
        copy.titan = copyThis.titan;
        copy.wasser = copyThis.wasser;
        copy.wasserstoff = copyThis.wasserstoff;
        copy.nahrung = copyThis.nahrung;
        copy.einwohner = copyThis.einwohner;
        copy.einwohnerProzent = copyThis.einwohnerProzent;
        return copy;
    }

    public Ress() {
    }

    public Ress(long eisen, long titan, long waNa, long einwohner) {
        this.eisen = eisen;
        this.titan = titan;
        this.wasser = waNa;
        this.wasserstoff = waNa / 10;
        this.nahrung = waNa;
        this.einwohner = einwohner;
    }

    public long getEisen() {
        return eisen;
    }

    public String getFe() {
        return String.format("%,.0f", (double) eisen);
    }

    public void setEisen(long eisen) {
        this.eisen = eisen;
    }

    public long getTitan() {
        return titan;
    }

    public String getTi() {
        return String.format("%,.0f", (double) titan);
    }

    public void setTitan(long titan) {
        this.titan = titan;
    }

    public long getWasser() {
        return wasser;
    }

    public String getWa() {
        return String.format("%,.0f", (double) wasser);
    }

    public void setWasser(long wasser) {
        this.wasser = wasser;
    }

    public long getWasserstoff() {
        return wasserstoff;
    }

    public String getH2() {
        return String.format("%,.0f", (double) wasserstoff);
    }

    public void setWasserstoff(long wasserstoff) {
        this.wasserstoff = wasserstoff;
    }

    public long getNahrung() {
        return nahrung;
    }

    public String getNa() {
        return String.format("%,.0f", (double) nahrung);
    }

    public void setNahrung(long nahrung) {
        this.nahrung = nahrung;
    }

    public long getEWTotal() {
        return einwohner;
    }
    public long getEWAvailableForBuild() {
        return einwohner - 100;
    }

    public void setEinwohner(long einwohner) {
        this.einwohner = einwohner;
    }

    public long getEinwohnerProzent() {
        return einwohnerProzent;
    }

    public void setEinwohnerProzent(long einwohnerProzent) {
        this.einwohnerProzent = einwohnerProzent;
    }

    public double getHrMengeForRess() {
        long summe = 0;
        if (eisen > 0) {
            summe += eisen;
        }
        if (titan > 0) {
            summe += titan;
        }
        if (wasser > 0) {
            summe += wasser;
        }
        if (wasserstoff > 0) {
            summe += wasserstoff;
        }
        if (nahrung > 0) {
            summe += nahrung;
        }
        return summe / 100000.0;
    }

    @Override
    public String toString() {

        return String.format("%,.0f", (double) eisen) + " Fe, "
                + String.format("%,.0f", (double) titan) + " Ti, "
                + String.format("%,.0f", (double) wasser) + " Wa, "
                + String.format("%,.0f", (double) wasserstoff) + " H2, "
                + String.format("%,.0f", (double) nahrung) + " Na, "
                + String.format("%,.0f", (double) einwohner) + " EW";
    }

    public String printBauRess() {
        return this.toString() + " " + String.format("%,.0f", (double) einwohner) + " EW (" + einwohnerProzent + "%)";
    }

    public void setPaketeRabatt(int maxPakete, double rabatt) {
        eisen = (long) (eisen * maxPakete * rabatt);
        titan = (long) (titan * maxPakete * rabatt);
        wasser = (long) (wasser * maxPakete * rabatt);
        wasserstoff = (long) (wasserstoff * maxPakete * rabatt);
        nahrung = (long) (nahrung * maxPakete * rabatt);
        einwohner = (long) (einwohner * maxPakete * rabatt);

    }

    public Ress multiplizierenMit(long longValueFromString) {
        Ress ress = new Ress();
        ress.eisen = eisen * longValueFromString;
        ress.titan = titan * longValueFromString;
        ress.wasser = wasser * longValueFromString;
        ress.wasserstoff = wasserstoff * longValueFromString;
        ress.nahrung = nahrung * longValueFromString;
        ress.einwohner = einwohner * longValueFromString;
        return ress;
    }

    public void rabattProzentAbziehen(double rabattProzent) {
        eisen -= (eisen * rabattProzent);
        titan -= (titan * rabattProzent);
        wasser -= (wasser * rabattProzent);
        wasserstoff -= (wasserstoff * rabattProzent);
        nahrung -= (nahrung * rabattProzent);
        einwohner -= (einwohner * rabattProzent);
    }

    public void add(Ress ress) {
        eisen += ress.eisen;
        titan += ress.titan;
        wasser += ress.wasser;
        wasserstoff += ress.wasserstoff;
        nahrung += ress.nahrung;
        einwohner += ress.einwohner;
    }

    public void subtr(Ress ress) {
        eisen -= ress.eisen;
        titan -= ress.titan;
        wasser -= ress.wasser;
        wasserstoff -= ress.wasserstoff;
        nahrung -= ress.nahrung;
        einwohner -= ress.einwohner;
    }

    public boolean allGreaterZero() {
        return eisen >= 0 && titan >= 0 && wasser >= 0 && wasserstoff >= 0 && nahrung >= 0;
    }

}
