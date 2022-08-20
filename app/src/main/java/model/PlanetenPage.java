/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nico
 */
public class PlanetenPage extends BotschutzResponse {

    public MyPlani selectedPlani;
     public boolean aktuell;
    public boolean übersicht;
    public boolean konstruktion;
    public boolean schiffsbauschleifen;
    public boolean verteidigungsbauschleifen;

    public boolean rohstoffe;
    public boolean aktuelleRohstoffMengen;
    public boolean minenAusbau;
    public boolean speicherAusbau;
    public boolean foerdermenge;

    public boolean schiffe;
    public boolean zivil;
    public boolean krieg;
    public boolean spezial;
    public boolean verteidigung;
    public boolean gebäude;

    public boolean zivilgebäude;
    public boolean weltwunder;

    public Map<String, Schleife> bauschleifen = new HashMap<>();
      public Map<String, KonstruktionsÜbersichtListItem> konstruktionsListe = new HashMap<>();
    
      
    //displaykoords -> speialschiffe
    public Map<String, Long> sondeGebaut = new HashMap<>();
    public Map<String, Long> sondeSchleife = new HashMap<>();
    public Map<String, Long> snatcherGebaut = new HashMap<>();
    public Map<String, Long> snatcherSchleife = new HashMap<>();
    public Map<String, Long> invasGebaut = new HashMap<>();
    public Map<String, Long> invasSchleife = new HashMap<>();
    public Map<String, Long> fusionatorGebaut = new HashMap<>();
    public Map<String, Long> fusionatorSchleife = new HashMap<>();


    public Map<String, String> showHidePlanet = new HashMap<>();
    public Map<String, Long> points = new HashMap<>();
    public Map<String, String> currentBuilding = new HashMap<>();
    public Map<String, String> currentShip = new HashMap<>();
    public Map<String, String> planetName = new HashMap<>();
    public Map<String, String> currentResearch = new HashMap<>();
    public Map<String, String> currentDeffBuild = new HashMap<>();


    public Map<String, Integer> hauptquartier = new HashMap<>();
    public Map<String, Integer> biozelle = new HashMap<>();
    public Map<String, Integer> bunker = new HashMap<>();
    public Map<String, Integer> farm = new HashMap<>();
    public Map<String, Integer> eisenmine = new HashMap<>();
    public Map<String, Integer> titanmine = new HashMap<>();
    public Map<String, Integer> bohrturm = new HashMap<>();
    public Map<String, Integer> chemiefabrik = new HashMap<>();
    public Map<String, Integer> recyclingcenter = new HashMap<>();
    public Map<String, Integer> nahrungssilo = new HashMap<>();
    public Map<String, Integer> eisenspeicher = new HashMap<>();
    public Map<String, Integer> titanspeicher = new HashMap<>();
    public Map<String, Integer> wasserspeicher = new HashMap<>();
    public Map<String, Integer> wasserstoffspeicher = new HashMap<>();
    public Map<String, Integer> universitaet = new HashMap<>();
    public Map<String, Integer> vergnuegungszentrum = new HashMap<>();
    public Map<String, Integer> schiffsfabrik = new HashMap<>();
    public Map<String, Integer> verteidigungsstation = new HashMap<>();
    public Map<String, Integer> fkz = new HashMap<>();
    public Map<String, Integer> schildgenerator = new HashMap<>();
    public Map<String, Integer> weltraumhafen = new HashMap<>();
    public Map<String, Integer> palast = new HashMap<>();
    public Map<String, Integer> kernforschungszentrum = new HashMap<>();
    public Map<String, Integer> statue = new HashMap<>();


    public Map<String, Ress> aktuelleRessMenge = new HashMap<>();
}
