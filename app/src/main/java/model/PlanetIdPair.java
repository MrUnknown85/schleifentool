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
public class PlanetIdPair {

    private final int planiId;
    private final int uni;

    public PlanetIdPair(int planiId, int uni) {
        this.planiId = planiId;
        this.uni = uni;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PlanetIdPair && ((PlanetIdPair) obj).uni == uni && ((PlanetIdPair) obj).planiId == planiId);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.planiId;
        hash = 83 * hash + this.uni;
        return hash;
    }

    public int getPlaniId() {
        return planiId;
    }

    public int getUni() {
        return uni;
    }

}
