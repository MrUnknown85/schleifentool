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
public class ProdParameter implements Comparable<ProdParameter> {

    private final String sid;
    public MyPlani plani;
    public Schiffstyp shipType = null;
    public boolean pack = false;
    public int prozent = 100;
    public long menge = 0;

    private String status;

    public ProdParameter(String sid, MyPlani plani) {
        this.sid = sid;
        this.plani = plani;
    }

    public boolean bereit() {
        return plani != null && shipType != null && menge != 0;
    }

    @Override
    public int compareTo(ProdParameter o) {
        return plani.compareTo(o.plani);
    }

    @Override
    public String toString() {
        return status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSid() {
        return sid;
    }

}
