/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import java.util.Date;


/**
 *
 * @author Nico
 */
public class ProductionPage extends BotschutzResponse {

    public MyPlani selectedPlani;
    public String koords;
    public Ress offeneRess = new Ress();
    public Bauschleifen schleifen = new Bauschleifen();

    public int prozentSpeed;
    public Date schleifeEndDate = null;
    public boolean moveSuccess = false;
    public boolean moveError = false;

    public long getEisen() {
        long eisen = 0;
        eisen = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getEisen() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(eisen, (accumulator, _item) -> accumulator + _item);
        return eisen;
    }

    public long getTitan() {
        long titan = 0;
        titan = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getTitan() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(titan, (accumulator, _item) -> accumulator + _item);
        return titan;
    }

    public long getWasser() {
        long wasser = 0;
        wasser = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getWasser() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(wasser, (accumulator, _item) -> accumulator + _item);
        return wasser;
    }

    public long getWasserstoff() {
        long wasserstoff = 0;
        wasserstoff = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getWasserstoff() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(wasserstoff, (accumulator, _item) -> accumulator + _item);
        return wasserstoff;
    }

    public long getNahrung() {
        long nahrung = 0;
        nahrung = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getNahrung() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(nahrung, (accumulator, _item) -> accumulator + _item);
        return nahrung;
    }

    public long getEinwohner() {
        long ew = 0;
        ew = schleifen.warteschlange.stream().map((p) -> {
            Ress kosten = Constants.prozenzBauRessMap.get(p.prozent).get(p.schiffsTyp);
            long gesamtKosten = p.menge * kosten.getEWTotal() * p.paket;
            gesamtKosten -= (gesamtKosten * p.rabattProzent());
            return gesamtKosten;
        }).map((gesamtKosten) -> gesamtKosten).reduce(ew, (accumulator, _item) -> accumulator + _item);
        return ew;
    }

    public String printInhalt() {
        String str = "";
        for (SchiffsbauschleifenPaket p : schleifen.warteschlange) {
            if (p.paket == 1) {
                str += (p.menge + " " + Constants.getSchiffsName(p.schiffsTyp) + " auf " + p.prozent) + "\n";
            } else {
                str += (p.menge + "x " + p.paket + " " + Constants.getSchiffsName(p.schiffsTyp) + " auf " + p.prozent) + "\n";
            }
        }
        return str;
    }

}
