/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Nico
 */
public enum Schiffstyp {
    SONDE, SNATCHER, RETTUNGSSCHIFF, HANDELSSCHIFF, HANDELSRIESE, AGLIDER, NOULON, BOMBER, KOLO, TRUGAR, VIOLO, NARUBU, NEOMAR, BLOODHOUND, KEMZEN, ZEMAR, FINUR, LUXOR, GRANDOR, INVA, FUSIONATOR;

    public boolean kostetGeld(Rasse rasse) {
        if (rasse == Rasse.SIEDLER) {
            return false;
        }
        if (rasse == Rasse.HÄNDLER) {
            return this == FUSIONATOR || isKampfschiff();
        }

        return isKampfschiff();
    }

    public boolean canBuild(Rasse rasse) {
        return (rasse == Rasse.HÄNDLER || this != FUSIONATOR);
    }

    private boolean isKampfschiff() {
        return this == AGLIDER
                || this == NOULON
                || this == BOMBER
                || this == KOLO
                || this == TRUGAR
                || this == VIOLO
                || this == NARUBU
                || this == NEOMAR
                || this == BLOODHOUND
                || this == KEMZEN
                || this == ZEMAR
                || this == FINUR
                || this == LUXOR
                || this == GRANDOR
                || this == INVA;
    }
}
