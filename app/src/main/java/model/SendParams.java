package ELschleifentool.model;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nico
 */
public class SendParams {

    public boolean waitForBot = false;
    public boolean ready = false;
    public boolean fremdPlani = false; //true wenn eine (Teil)Flotte länger als 24h auf einem fremden Planeten steht
    public String status = "";
    public List<Flotte> flotten = new ArrayList<>();
    public Koords zielkoords;
    // public Botschutz.Params sendBotschutz;
    public int mode = 3; //3 = Att
    public int rescue = -1; // -1 = nicht hinzufügen, 1 = zivis retten, 2 = zivis angreifen
    public boolean inva;
    public boolean raid;

    public SendParams() {

    }

    @Override
    public String toString() {
        String fleets = "<html>";
        for (Flotte flotte : flotten) {
            if (!fleets.equals("<html>")) {
                fleets += "<br>";
            }
            String besitzer = flotte.besitzer.isEmpty() ? "Mir" : flotte.besitzer;
            fleets += flotte.name + " von " + besitzer;
        }
        fleets += "</html>";
        return fleets;
    }

}
