/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import ELschleifentool.model.Koords;
import ELschleifentool.model.Schiffstyp;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Objects;

/**
 *
 * @author Nico
 */
public class Flotte implements Comparable<Flotte> {

    public String headerTitle = "";
    public String id;
    public String hintergrund;
    public String auswählbar;
    public String name = "";

    private EnumMap<Schiffstyp, Long> schiffe = new EnumMap<>(Schiffstyp.class);

    public String besitzer = "";
    public boolean auflösbar;
    public Koords koords;
    public String freigabe;
    public String anum;
    public boolean fliegt;

    public Flotte() {
    }

    public Flotte(Flotte f) {
        this.headerTitle = f.headerTitle;
        this.id = f.id;
        this.hintergrund = f.hintergrund;
        this.auswählbar = f.auswählbar;
        this.name = f.name;
        this.schiffe = f.schiffe;
        this.besitzer = f.besitzer;
        this.auflösbar = f.auflösbar;
        this.koords = f.koords;
        this.freigabe = f.freigabe;
        this.anum = f.anum;
        this.fliegt = f.fliegt;
    }

    public Flotte(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Flotte && ((Flotte) obj).id.equals(id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setAnzahlSchiffe(Schiffstyp typ, long anzahl) {
        schiffe.put(typ, anzahl);
    }

    public String getShips() {
        String ships = "<html>";
        for (Schiffstyp typ : schiffe.keySet()) {
            long shipCount = schiffe.get(typ);
            if (shipCount > 0) {
                if (!ships.equals("<html>")) {
                    ships += "<br>";
                }
                ships += "<font color='#ffffff'>" + shipCount + "</font> <font color='#7aaae1'>" + Constants.schiffsNamen.get(typ) + " <b>X</b></font> <a href=\"url\">link text</a>";
            }
        }
        ships += "</html>";
        return ships;
    }

    public EnumMap<Schiffstyp, Long> getSchiffe() {
        return schiffe;
    }

    public String getBesitzer() {
        return besitzer;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Flotte o) {
        return Comparator.comparing(Flotte::getBesitzer)
                .thenComparing(Flotte::getName)
                .compare(this, o);
    }

    int getShipTypeCount() {
        int c = 0;
        c = schiffe.values().stream().filter((count) -> (count > 0)).map((_item) -> 1).reduce(c, Integer::sum);
        return c;
    }
}
