/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gebäude;

import interfaces.NetworkInterface;
import model.Constants;
import model.ConstructionPage;
import model.GebäudeBaukosten;
import model.MyPlani;
import model.NavigationPage;
import model.PlanetIdPair;
import model.PlanetenPage;
import model.PlaniState;
import model.Ress;
import model.Schiffstyp;
import ui.MainPanel;
import util.Log;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author nico
 */
public class GebäudebauTool {

    private final String sid;
    private final NetworkInterface networkInterface;
    private final GebäudeUserInterface userInterface;
    public HashMap<Integer, PlanetenPage> pages = new HashMap<>();
    private NavigationPage navPage = new NavigationPage();
    public HashMap<PlanetIdPair, PlaniState> planiListStates = new HashMap();
    String gebäudeName = "Hauptquartier";
    private ArrayList<MyPlani> selectedPlanis = new ArrayList<>();
    HashMap<Integer, GebäudeBaukosten> map = new HashMap<>();

    public GebäudebauTool(String sid, GebäudeUserInterface userInterface, NetworkInterface networkInterface) {
        this.sid = sid;
        this.networkInterface = networkInterface;
        this.userInterface = userInterface;
        readKosten();
        loadPlaniList();
    }

    final public void loadPlaniList() {
        Log.d("SchiffsbauTool loadPlaniList");
        networkInterface.getPlaniList(sid, (page) -> {
            if (page == null) {
                Log.err(new IllegalArgumentException("Planiliste ist null"));
                return;
            }
            navPage = page;
            onNavigationPage();
        });

    }

    final void readKosten() {
        map.clear();

        BufferedReader br = null;
        try {
            File file = new File(getClass().getResource("/" + gebäudeName + ".txt").toURI());
            br = new BufferedReader(new FileReader(file));
            String st;
            String regex = "Baukosten für Stufe ([\\d.]+): ([\\d.]+) Eisen - ([\\d.]+) Titan - ([\\d.]+) Tage, (\\d{2}):(\\d{2}):(\\d{2})";
            Pattern p = Pattern.compile(regex);
            while ((st = br.readLine()) != null) {
                // System.out.println(st);
                Matcher m = p.matcher(st);
                if (m.find()) {
                    int stufe = Integer.valueOf(m.group(1).replace(".", ""));
                    GebäudeBaukosten k = new GebäudeBaukosten();
                    k.eisen = Long.valueOf(m.group(2).replace(".", ""));
                    k.titan = Long.valueOf(m.group(3).replace(".", ""));
                    k.tage = Integer.valueOf(m.group(4).replace(".", ""));
                    k.stunden = Integer.valueOf(m.group(5));
                    k.minuten = Integer.valueOf(m.group(6));
                    k.sekunden = Integer.valueOf(m.group(7));
                    map.put(stufe, k);
                    long durationSek = k.sekunden + k.minuten * 60 + k.stunden * 60 * 60 + k.tage * 60 * 60 * 24;
                }

            }
        } catch (IOException | URISyntaxException ex) {
            Log.err(ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<MyPlani> getPlaniList() {
        return navPage.planiList;
    }

    private void onNavigationPage() {
        PlanetIdPair selectedPlaniBefore = navPage.selectedPlani;
        boolean selectedNotMainUni = navPage.gates.stream().map(gate -> gate.getUni()).anyMatch(m -> m == selectedPlaniBefore.getUni());
        final List<PlanetIdPair> planis = new ArrayList<>();

        if (selectedNotMainUni) {
            List<PlanetIdPair> pairs = navPage.gates.stream().map(gate -> gate.getPlanetIdPair()).collect(Collectors.toList());
            planis.addAll(pairs);
            planis.add(navPage.planiList.get(0).getPlanetIdPair());
            for (Iterator<PlanetIdPair> i = planis.iterator(); i.hasNext();) {
                if (i.next().getUni() == selectedPlaniBefore.getUni()) {
                    i.remove();
                }
            }
            planis.add(selectedPlaniBefore);
        } else {
            List<PlanetIdPair> pairs = navPage.gates.stream().map(gate -> gate.getPlanetIdPair()).collect(Collectors.toList());
            planis.addAll(pairs);
            planis.add(navPage.selectedPlani);
        }

        loadOtherData(planis, selectedPlaniBefore);
    }

    private void loadOtherData(List<PlanetIdPair> planis, PlanetIdPair selectedPlaniBefore) {
        PlanetIdPair plani = planis.get(0);
        int uni = plani.getUni();
        int id = plani.getPlaniId();
        pages.put(uni, new PlanetenPage());

        networkInterface.getRess(sid, id, uni, ressPage -> {
            pages.get(uni).aktuelleRessMenge = ressPage.aktuelleRessMenge;
            networkInterface.getKonstruktionsübersicht(sid, id, uni, (PlanetenPage akt) -> {
                pages.get(uni).konstruktionsListe = akt.konstruktionsListe;
                networkInterface.getSpeichergebäude(sid, id, uni, (PlanetenPage sp) -> {
                    pages.get(uni).bunker = sp.bunker;
                    pages.get(uni).nahrungssilo = sp.nahrungssilo;
                    pages.get(uni).eisenspeicher = sp.eisenspeicher;
                    pages.get(uni).titanspeicher = sp.titanspeicher;
                    pages.get(uni).wasserspeicher = sp.wasserspeicher;
                    pages.get(uni).wasserstoffspeicher = sp.wasserstoffspeicher;
                    networkInterface.getMinengebäude(sid, id, uni, (PlanetenPage mip) -> {
                        pages.get(uni).farm = mip.farm;
                        pages.get(uni).eisenmine = mip.eisenmine;
                        pages.get(uni).titanmine = mip.titanmine;
                        pages.get(uni).bohrturm = mip.bohrturm;
                        pages.get(uni).chemiefabrik = mip.chemiefabrik;
                        pages.get(uni).recyclingcenter = mip.recyclingcenter;

                        networkInterface.getZivilgebäude(sid, id, uni, (PlanetenPage zp) -> {
                            pages.get(uni).hauptquartier = zp.hauptquartier;
                            pages.get(uni).biozelle = zp.biozelle;
                            pages.get(uni).universitaet = zp.universitaet;
                            pages.get(uni).vergnuegungszentrum = zp.vergnuegungszentrum;

                            networkInterface.getMilitärgebäude(sid, plani.getPlaniId(), plani.getUni(), (PlanetenPage mp) -> {
                                pages.get(uni).schiffsfabrik = mp.schiffsfabrik;
                                pages.get(uni).verteidigungsstation = mp.verteidigungsstation;
                                pages.get(uni).fkz = mp.fkz;
                                pages.get(uni).schildgenerator = mp.schildgenerator;

                                networkInterface.getWeltwundergebäude(sid, id, uni, (PlanetenPage wp) -> {
                                    pages.get(uni).weltraumhafen = wp.weltraumhafen;
                                    pages.get(uni).palast = wp.palast;
                                    pages.get(uni).kernforschungszentrum = wp.kernforschungszentrum;
                                    pages.get(uni).statue = wp.statue;
                                    this.planiListStates = wp.planiListStates;

                                    planis.remove(0);
                                    if (planis.isEmpty()) {
                                        setInitialSelectedPlanis();
                                        userInterface.notifyDataChanged();
                                    } else {
                                        loadOtherData(planis, selectedPlaniBefore);
                                    }
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    public void setInitialSelectedPlanis() {
        selectedPlanis.clear();
        navPage.planiList.stream().filter((t) -> {
            PlanetIdPair pair = t.getPlanetIdPair();
            PlaniState state = (planiListStates.containsKey(pair)) ? planiListStates.get(pair) : null;
            if (state == null) {
                return true;
            } else {
                return !state.currentBuildings.equals("1") && !state.currentBuildings.equals("2");
            }
        }).forEach((t) -> {
            if (hasRess(t)) {
                selectedPlanis.add(t);
            }
        });
    }

    public void gebäudeSelected(String gebäudeName) {
        this.gebäudeName = gebäudeName;
        readKosten();
        loadCurrentBuildings();
    }

    private void loadCurrentBuildings() {
        switch (gebäudeName) {
            case "Farm":
            case "Eisenmine":
            case "Titanmine":
            case "Bohrturm":
            case "Chemiefabrik":
            case "Recyclingcenter":
                networkInterface.getMinengebäude(sid, null, null, (PlanetenPage t) -> {
                    setInitialSelectedPlanis();
                    userInterface.notifyDataChanged();
                });
                break;
            case "Bunker":
            case "Nahrungssilo":
            case "Eisenspeicher":
            case "Titanspeicher":
            case "Wasserspeicher":
            case "Wasserstoffspeicher":
                networkInterface.getSpeichergebäude(sid, null, null, (PlanetenPage t) -> {
                    setInitialSelectedPlanis();
                    userInterface.notifyDataChanged();
                });
                break;
            case "Schiffsfabrik":
            case "Verteidigungsstation":
            case "Flottenkontrollzentrum":
            case "Schildgenerator":
                networkInterface.getMilitärgebäude(sid, null, null, (PlanetenPage t) -> {
                    setInitialSelectedPlanis();
                    userInterface.notifyDataChanged();
                });
                break;
            case "Hauptquartier":
            case "Biozelle":
            case "Universität":
            case "Vergnügungszentrum":
                networkInterface.getZivilgebäude(sid, null, null, (PlanetenPage t) -> {
                    setInitialSelectedPlanis();
                    userInterface.notifyDataChanged();
                });
                break;
            case "Intergalaktischer Weltraumhafen":
            case "Palast":
            case "Kernforschungszentrum":
            case "Statue des Imperators":
                networkInterface.getWeltwundergebäude(sid, null, null, (PlanetenPage t) -> {
                    setInitialSelectedPlanis();
                    userInterface.notifyDataChanged();
                });
                break;

            default:
                throw new AssertionError();

        }
    }

    boolean isPlaniSelected(MyPlani plani) {
        return selectedPlanis.contains(plani);
    }

    void setPlaniSelected(MyPlani plani, boolean selected) {
        if (selected) {
            if (hasRess(plani) && !selectedPlanis.contains(plani)) {
                selectedPlanis.add(plani);
            }
        } else {
            selectedPlanis.remove(plani);
        }
    }

    String getKosten(MyPlani plani) {
        int nextStufe = getStufe(plani) + 1;
        GebäudeBaukosten kosten = map.get(nextStufe);
        String koords = plani.getKoords().getString();
        int uni = plani.getUni();
        PlanetenPage page = pages.get(uni);
        Ress ress = page.aktuelleRessMenge.get(koords);
        if (kosten != null) {
            String eisenColor = (ress.getEisen() > kosten.eisen) ? "#00AA00" : "#BB0000";
            String titanColor = (ress.getTitan() > kosten.titan) ? "#00AA00" : "#BB0000";
            String str = "<html><font color='#ffffff'>Kosten: </font>";
            str += "<font color='" + eisenColor + "'>" + String.format("%,.0f", (double) kosten.eisen) + " Eisen</font>";
            str += "<font color='#ffffff'> - </font>";
            str += "<font color='" + titanColor + "'>" + String.format("%,.0f", (double) kosten.eisen) + " Titan</font>";
            str += "</html>";
            return str;
        } else {
            return "Ausbau nur bis Stufe 500";
        }
    }

    boolean hasRess(MyPlani plani) {
        int nextStufe = getStufe(plani) + 1;
        GebäudeBaukosten kosten = map.get(nextStufe);
        String koords = plani.getKoords().getString();
        int uni = plani.getUni();
        PlanetenPage page = pages.get(uni);
        Ress ress = page.aktuelleRessMenge.get(koords);
        if (kosten != null) {
            return (ress.getEisen() > kosten.eisen) && (ress.getTitan() > kosten.titan);
        } else {
            return false;
        }
    }

    int getStufe(MyPlani plani) {
        String koords = plani.getKoords().getString();
        int uni = plani.getUni();
        PlanetenPage page = pages.get(uni);
        switch (gebäudeName) {
            case "Hauptquartier":
                return page.hauptquartier.getOrDefault(koords, 0);
            case "Biozelle":
                return page.biozelle.getOrDefault(koords, 0);
            case "Bunker":
                return page.bunker.getOrDefault(koords, 0);
            case "Farm":
                return page.farm.getOrDefault(koords, 0);
            case "Eisenmine":
                return page.eisenmine.getOrDefault(koords, 0);
            case "Titanmine":
                return page.titanmine.getOrDefault(koords, 0);
            case "Bohrturm":
                return page.bohrturm.getOrDefault(koords, 0);
            case "Chemiefabrik":
                return page.chemiefabrik.getOrDefault(koords, 0);
            case "Recyclingcenter":
                return page.recyclingcenter.getOrDefault(koords, 0);
            case "Nahrungssilo":
                return page.nahrungssilo.getOrDefault(koords, 0);
            case "Eisenspeicher":
                return page.eisenspeicher.getOrDefault(koords, 0);
            case "Titanspeicher":
                return page.titanspeicher.getOrDefault(koords, 0);
            case "Wasserspeicher":
                return page.wasserspeicher.getOrDefault(koords, 0);
            case "Wasserstoffspeicher":
                return page.wasserstoffspeicher.getOrDefault(koords, 0);
            case "Universität":
                return page.universitaet.getOrDefault(koords, 0);
            case "Vergnügungszentrum":
                return page.vergnuegungszentrum.getOrDefault(koords, 0);
            case "Schiffsfabrik":
                return page.schiffsfabrik.getOrDefault(koords, 0);
            case "Verteidigungsstation":
                return page.verteidigungsstation.getOrDefault(koords, 0);
            case "Flottenkontrollzentrum":
                return page.fkz.getOrDefault(koords, 0);
            case "Schildgenerator":
                return page.schildgenerator.getOrDefault(koords, 0);
            case "Intergalaktischer Weltraumhafen":
                return page.weltraumhafen.getOrDefault(koords, 0);
            case "Palast":
                return page.palast.getOrDefault(koords, 0);
            case "Kernforschungszentrum":
                return page.kernforschungszentrum.getOrDefault(koords, 0);
            case "Statue des Imperators":
                return page.statue.getOrDefault(koords, 0);
            default:
                throw new AssertionError();
        }
    }

    String getAktuellerBau(MyPlani plani) {
        String koords = plani.getKoords().getString();
        int uni = plani.getUni();
        PlanetenPage page = pages.get(uni);
        if (page.konstruktionsListe.containsKey(koords)) {
            return page.konstruktionsListe.get(koords).gebäude + " Stufe " + page.konstruktionsListe.get(koords).stufe;
        } else {
            return "";
        }

    }

    String getFertigstellung(MyPlani plani) {
        String koords = plani.getKoords().getString();
        int uni = plani.getUni();
        PlanetenPage page = pages.get(uni);
        if (page.konstruktionsListe.containsKey(koords)) {
            String tag = page.konstruktionsListe.get(koords).fertigstellungTag;
            String monat = page.konstruktionsListe.get(koords).fertigstellungMonat;
            String jahr = page.konstruktionsListe.get(koords).fertigstellungJahr;
            String zeit = page.konstruktionsListe.get(koords).fertigstellungZeit;
            return tag + "." + monat + "." + jahr + " " + zeit;
        } else {
            return "";
        }
    }

    String getBackgroundColor(MyPlani plani) {
        PlanetIdPair pair = plani.getPlanetIdPair();
        if (planiListStates.containsKey(pair)) {
            PlaniState state = planiListStates.get(pair);
            if (state.backgroundColor == null) {
                return "#000000";
            } else {
                return state.backgroundColor;
            }
        } else {
            return "#000000";
        }
    }

    void startBuild() {
        networkInterface.getConstuctionPage(sid, null, null, null, null, null, null, ((ConstructionPage p) -> {
            planiListStates = p.planiListStates;
            userInterface.notifyDataChanged();
            int building = getBuildNum();
            String buildKey = p.constructionKey;
            buildNext(new ArrayList<>(selectedPlanis), building, buildKey);
        }));
    }

    private void buildNext(List<MyPlani> planis, int building, String buildKey) {
        MyPlani nextPlani = planis.get(0);

        boolean wasNotBuilding = planiListStates.get(nextPlani.getPlanetIdPair()).currentBuildings.equals("0");
        networkInterface.getConstuctionPage(sid, nextPlani, building, buildKey, null, null, null, ((ConstructionPage p2) -> {
            planiListStates = p2.planiListStates;

            boolean isNowBuilding = planiListStates.get(nextPlani.getPlanetIdPair()).currentBuildings.equals("1");
            if (wasNotBuilding && isNowBuilding) {
                String koords = nextPlani.getKoords().getString();
                int uni = nextPlani.getUni();
                PlanetenPage page = pages.get(uni);
                page.currentBuilding.put(koords, gebäudeName);
            }
            userInterface.notifyDataChanged();
            selectedPlanis.remove(nextPlani);
            planis.remove(0);
            if (!planis.isEmpty()) {
                buildNext(planis, building, p2.constructionKey);
            }
        }));
    }

    private int getBuildNum() {
        switch (gebäudeName) {
            case "Hauptquartier":
                return 0;
            case "Biozelle":
                return 1;
            case "Bunker":
                return 2;
            case "Farm":
                return 3;
            case "Eisenmine":
                return 4;
            case "Titanmine":
                return 5;
            case "Bohrturm":
                return 6;
            case "Chemiefabrik":
                return 7;
            case "Recyclingcenter":
                return 8;
            case "Nahrungssilo":
                return 9;
            case "Eisenspeicher":
                return 10;
            case "Titanspeicher":
                return 11;
            case "Wasserspeicher":
                return 12;
            case "Wasserstoffspeicher":
                return 13;
            case "Universität":
                return 14;
            case "Vergnügungszentrum":
                return 15;
            case "Schiffsfabrik":
                return 16;
            case "Verteidigungsstation":
                return 17;
            case "Flottenkontrollzentrum":
                return 18;
            case "Schildgenerator":
                return 19;
            case "Intergalaktischer Weltraumhafen":
                return 20;
            case "Palast":
                return 21;
            case "Kernforschungszentrum":
                return 22;
            case "Statue des Imperators":
                return 23;
            default:
                throw new AssertionError();
        }
    }

    void clearSelection() {
        selectedPlanis.clear();
        userInterface.notifyDataChanged();
    }

    void selectAll() {
        selectedPlanis.addAll(navPage.planiList);
        userInterface.notifyDataChanged();
    }

}
