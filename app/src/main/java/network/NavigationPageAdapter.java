/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.network;

import ELschleifentool.model.NavigationPage;
import ELschleifentool.model.PlanetIdPair;
import ELschleifentool.model.MyPlani;
import ELschleifentool.model.PlaniState;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nico
 */
public class NavigationPageAdapter extends BotschutzAdapter<NavigationPage> {

    private static final String FIND_PLANI_LIST_REGEX = "buildPlanetList\\((\\d)\\+(\\d), \\[([\\W\\w]*?\\])\\][\\W\\w]*?(?:false|true)\\);<\\/script>";
    private static final String PLANI_LIST_REGEX = "\\[\"(\\d+)\",\"(\\d+)\",\"(\\d+)\",\"(\\d+)\",\"?[\\w\\W]*?\"?,\"(\\d+)\"\\]";
    private static final String SELECTED_PLANI_ID_REGEX = "selectPlanet\\((\\d+), (\\d), forceScroll";

    @Override
    public NavigationPage provideInstance() {
        return new NavigationPage();
    }

    @Override
    public void convertType(String convertFrom, NavigationPage convertTo) throws IOException {
        Matcher m = Pattern.compile(FIND_PLANI_LIST_REGEX).matcher(convertFrom);
        while (m.find()) {
            int u = Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2));
            Matcher listMatcher = Pattern.compile(PLANI_LIST_REGEX).matcher(m.group(3));
            while (listMatcher.find()) {
                int planiId = Integer.parseInt(listMatcher.group(1));
                String gala = listMatcher.group(2);
                String sys = listMatcher.group(3);
                String nr = listMatcher.group(4);
                String koords = gala + ":" + sys + ":" + nr;
                //1 Steinplanet 2 Wasserplanet 3 Wüstenplanet 4 roter Planet 5 blauer Planet 6 Planetenverbund
                //String planiType = listMatcher.group(5);
                MyPlani p = new MyPlani(new PlanetIdPair(planiId, u), koords);
                convertTo.planiList.add(p);
                if (gala.equals("26")) {
                    convertTo.gates.add(p);
                }
            }
        }

        Matcher sel = Pattern.compile(SELECTED_PLANI_ID_REGEX).matcher(convertFrom);
        if (sel.find()) {
            int id = Integer.parseInt(sel.group(1));
            int uni = Integer.parseInt(sel.group(2));
            convertTo.selectedPlani = new PlanetIdPair(id, uni);
        }
    }

    public static HashMap<PlanetIdPair, PlaniState> getPlaniListStates(String reguestResult) {
        Matcher sel = Pattern.compile("selectPlanet\\((\\d+), (\\d), forceScroll").matcher(reguestResult);
        PlanetIdPair selectedPlani = null;
        if (sel.find()) {
            int id = Integer.valueOf(sel.group(1));
            int uni = Integer.valueOf(sel.group(2));
            selectedPlani = new PlanetIdPair(id, uni);
        }

        String statesRegex = "\\['(\\d)#(\\d+)', '(#[A-F\\d]+)?', \\[(\\d), (\\d), (\\d), (\\d), (\\d), (\\d)\\]\\]";
        Matcher states = Pattern.compile(statesRegex).matcher(reguestResult);
        HashMap<PlanetIdPair, PlaniState> statesMap = new HashMap<>();
        while (states.find()) {
            PlaniState state = new PlaniState(new PlanetIdPair(Integer.valueOf(states.group(2)), Integer.valueOf(states.group(1))));
            state.all = states.group(0);
            state.backgroundColor = states.group(3);
            state.selected = selectedPlani != null && selectedPlani.equals(state.getIdPair());
            state.currentBuildings = states.group(4);
            state.hunger = states.group(5);
            state.attack = states.group(6).equals("1");
            state.research = states.group(7);
            state.shipProd = states.group(8);
            state.state5 = states.group(9);
            statesMap.put(state.getIdPair(), state);
        }
        return statesMap;
    }
}
