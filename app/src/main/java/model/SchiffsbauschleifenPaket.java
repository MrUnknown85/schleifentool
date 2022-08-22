/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

/**
 *
 * @author nico
 */
public class SchiffsbauschleifenPaket {

    public String id;
    public int sort;
    public boolean verschiebbar;
    public long menge;
    public int paket;
    public Schiffstyp schiffsTyp;
    public int prozent;
    public String cash;

    public double rabattProzent() {
        switch (paket) {
            case 1000:
                return 0.05;
            case 100:
                return 0.01;
            default:
                return 0.0;
        }
    }

    public Identifier getIdentifier(boolean ignoreCash) {
        return new Identifier(paket, prozent, ignoreCash ? "" : cash, schiffsTyp);
    }

    public class Identifier {

        public int paket;
        public int prozent;
        public String cash;
        public Schiffstyp schiffsTyp;

        public Identifier(int paket, int prozent, String cash, Schiffstyp schiffsTyp) {
            this.paket = paket;
            this.prozent = prozent;
            this.schiffsTyp = schiffsTyp;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 17 * hash + this.paket;
            hash = 17 * hash + this.prozent;
            hash = 17 * hash + Objects.hashCode(this.schiffsTyp.name());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Identifier other = (Identifier) obj;
            if (this.paket != other.paket) {
                return false;
            }
            if (this.prozent != other.prozent) {
                return false;
            }
            if (!Objects.equals(this.schiffsTyp.name(), other.schiffsTyp.name())) {
                return false;
            }
            return true;
        }

    }
}
