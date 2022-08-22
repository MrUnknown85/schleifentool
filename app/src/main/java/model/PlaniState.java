/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nico
 */
public class PlaniState {

    private PlanetIdPair idPair;

    public String backgroundColor = null;
    public String currentBuildings = null;//vals[0] 1=BAU 2=AUFTRAG
    public String hunger = null;//vals[1]
    public Boolean attack = false;
    public String research = null;//vals[3
    public String shipProd = null;//vals[4]
    public String state5 = null;   //vals[5]  > 1 => images/happy2.png  ODER == 1 => images/happy.png
    public String all = null;
    public boolean selected = false;

    public PlaniState(PlanetIdPair idPair) {
        this.idPair = idPair;
    }

    public PlanetIdPair getIdPair() {
        return idPair;
    }

}
