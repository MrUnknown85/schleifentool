/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import java.util.Objects;

/**
 *
 * @author Nico
 */
public class Koords implements Comparable<Koords> {

    private final String displayKoords;
    private final String sortKoords;
    private final String sortKoordsWithoutUni;
    private final int uni;
    private final int gala;
    private final int sys;
    private final int nr;

    public Koords(int uni, int gala, int sys, int nr) {
        this.uni = uni;
        this.gala = gala;
        this.sys = sys;
        this.nr = nr;
        this.displayKoords = gala + ":" + sys + ":" + nr;
        this.sortKoords = String.format("%1d:%2d:%3d:%2d", this.uni, gala, sys, nr);
        this.sortKoordsWithoutUni = String.format("%2d:%3d:%2d", gala, sys, nr);
    }

    public Koords(String displayKoords, int uni) {
        this.displayKoords = displayKoords;
        this.uni = uni;
        String[] koords = displayKoords.split(":");
        this.gala = Integer.parseInt(koords[0]);
        this.sys = Integer.parseInt(koords[1]);
        this.nr = Integer.parseInt(koords[2]);
        this.sortKoords = String.format("%1d:%2d:%3d:%2d", this.uni, gala, sys, nr);
        this.sortKoordsWithoutUni = String.format("%2d:%3d:%2d", gala, sys, nr);
    }

    public Koords(String uniKoords) {
        String[] koords = uniKoords.split(":");
        this.displayKoords = koords[1] + ":" + koords[2] + ":" + koords[3];
        this.uni = Integer.parseInt(koords[0]);
        this.gala = Integer.parseInt(koords[1]);
        this.sys = Integer.parseInt(koords[2]);
        this.nr = Integer.parseInt(koords[3]);
        this.sortKoords = String.format("%1d:%2d:%3d:%2d", this.uni, gala, sys, nr);
        this.sortKoordsWithoutUni = String.format("%2d:%3d:%2d", gala, sys, nr);
    }

    @Override
    public int compareTo(Koords o) {
        return sortKoords.compareTo(o.sortKoords);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Koords) {
            Koords koo = (Koords) obj;
            return this.getUniqueKoords().equals(koo.getUniqueKoords());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.sortKoords);
        return hash;
    }

    @Override
    public String toString() {
        return "U" + uni + ": " + displayKoords;
    }

    public String getUniKoords() {
        return uni + ":" + displayKoords;
    }

    public String getUniqueKoords() {
        return uni + ":" + gala + ":" + sys + ":" + nr;
    }

    public String getString() {
        return displayKoords;
    }

    public int getUni() {
        return uni;
    }

    public int getGala() {
        return gala;
    }

    public int getSys() {
        return sys;
    }

    public int getNr() {
        return nr;
    }

    public String getSortKoords() {
        return sortKoords;
    }

    public String getSortKoordsWithoutUni() {
        return sortKoordsWithoutUni;
    }

    public Koords planiBelow() {
        return new Koords(uni, gala, sys, nr + 1);
    }

}
