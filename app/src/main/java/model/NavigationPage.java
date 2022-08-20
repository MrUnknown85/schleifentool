/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nico
 */
public class NavigationPage extends BotschutzResponse {

    public PlanetIdPair selectedPlani;
    public List<MyPlani> planiList = new ArrayList<>();
    public List<MyPlani> gates = new ArrayList<>();

}
