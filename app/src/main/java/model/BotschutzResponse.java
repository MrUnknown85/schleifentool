/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.model;

import java.util.HashMap;

/**
 *
 * @author nico
 */
public class BotschutzResponse {

    public String sid = "";
    public String captchaId = "";
    public boolean hasBotschutz = false;
    public boolean blocked = false;
    public boolean ausgeloggt = false;
    public boolean ipAbgelaufen = false;

    public HashMap<PlanetIdPair, PlaniState> planiListStates = new HashMap();
}
