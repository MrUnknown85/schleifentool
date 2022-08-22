/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Objects;

/**
 *
 * @author Nico
 */
public class MyPlani implements Comparable<MyPlani> {

    private final PlanetIdPair idPair;
    private final Koords koords;

    public MyPlani(PlanetIdPair planiIdPair, String koordinaten) {
        idPair = planiIdPair;
        this.koords = new Koords(koordinaten, planiIdPair.getUni());
    }

    public Koords getKoords() {
        return koords;
    }

    public PlanetIdPair getPlanetIdPair() {
        return idPair;
    }

    public int getUni() {
        return koords.getUni();
    }

    @Override
    public String toString() {
        return "U" + koords.getUni() + " " + koords.getString();
    }

    public String getGala() {
        return String.valueOf(koords.getGala());
    }

    public String getSysKey() {
        return "U" + koords.getUni() + ":" + koords.getGala() + ":" + koords.getSys();
    }

    public String getSys() {
        return String.valueOf(koords.getSys());
    }

    public String getNr() {
        return String.valueOf(koords.getNr());
    }

    @Override
    public int compareTo(MyPlani o) {
        return koords.compareTo(o.koords);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.koords);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MyPlani && ((MyPlani) obj).koords.equals(koords);
    }

}
