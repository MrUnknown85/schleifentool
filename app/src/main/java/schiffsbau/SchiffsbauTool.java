/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.schiffsbau;

import ELschleifentool.interfaces.NetworkInterface;
import ELschleifentool.util.Log;
import ELschleifentool.model.Constants;
import ELschleifentool.model.Ress;
import ELschleifentool.model.ProductionPage;
import ELschleifentool.model.NavigationPage;
import ELschleifentool.model.Rasse;
import ELschleifentool.model.SchiffsbauschleifenPaket;
import ELschleifentool.model.PlanetIdPair;
import ELschleifentool.model.ProdParameter;
import ELschleifentool.model.Schleife;
import ELschleifentool.model.Schiffstyp;
import ELschleifentool.model.Koords;
import ELschleifentool.model.MyPlani;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Nico
 */
public class SchiffsbauTool {

    NetworkInterface networkInterface;
    SchiffsbauUserinterface userInterface;

    private final String sid;
    public List<MyPlani> listdata = new ArrayList<>();
    public List<MyPlani> viewlistdata = new ArrayList<>();

    private final Map<MyPlani, ProdParameter> prodMap = new HashMap<>();
    private final Map<Koords, Ress> ressMap = new HashMap<>();
    private final Map<Koords, Integer> sfMap = new HashMap<>();
    private final Map<Koords, Schleife> schleifenMap = new HashMap<>();
    private final Map<Koords, Integer> hqMap = new HashMap<>();
    private final Map<Koords, Integer> bioMap = new HashMap<>();

    public SchiffsbauTool(String sid, SchiffsbauUserinterface userinterface, NetworkInterface networkInterface) {
        this.sid = sid;
        this.userInterface = userinterface;
        this.networkInterface = networkInterface;
        loadPlaniList();
    }

    public void loadPlaniList() {
        Log.d("SchiffsbauTool loadPlaniList");
        networkInterface.getPlaniList(sid, (page) -> {
            onNavigationPage(page);
        });

    }

    private void onNavigationPage(NavigationPage page) {
        if (page == null) {
            Log.err(new IllegalArgumentException("Planiliste ist null"));
            return;
        }
        Log.d("SchiffsbauTool Planiliste geladen: " + page.planiList.size() + " Planis");
        PlanetIdPair selectedPlaniBefore = page.selectedPlani;

        prodMap.keySet().retainAll(page.planiList);
        page.planiList.stream().forEach(plani -> {
            if (!prodMap.containsKey(plani)) {
                prodMap.put(plani, new ProdParameter(sid, plani));
            }
        });
        listdata = page.planiList;
        viewlistdata = new ArrayList<>(listdata);
        userInterface.notifyDataChanged();

        boolean selectedNotMainUni = page.gates.stream().map(gate -> gate.getUni()).anyMatch(m -> m == selectedPlaniBefore.getUni());
        final List<PlanetIdPair> planis = new ArrayList<>();

        if (selectedNotMainUni) {
            List<PlanetIdPair> pairs = page.gates.stream().map(gate -> gate.getPlanetIdPair()).collect(Collectors.toList());
            planis.addAll(pairs);
            planis.add(page.planiList.get(0).getPlanetIdPair());
            for (Iterator<PlanetIdPair> i = planis.iterator(); i.hasNext();) {
                if (i.next().getUni() == selectedPlaniBefore.getUni()) {
                    i.remove();
                }
            }
            planis.add(selectedPlaniBefore);
        } else {
            List<PlanetIdPair> pairs = page.gates.stream().map(gate -> gate.getPlanetIdPair()).collect(Collectors.toList());
            planis.addAll(pairs);
            planis.add(page.selectedPlani);
        }

        ressMap.clear();
        sfMap.clear();
        schleifenMap.clear();
        hqMap.clear();
        bioMap.clear();

        planis.stream().map((pair) -> {
            return pair;
        }).forEach((pair) -> {
            networkInterface.getRess(sid, pair.getPlaniId(), pair.getUni(), ressPage -> {
                ressPage.aktuelleRessMenge.keySet().stream().map(displayKoords -> new Koords(displayKoords, pair.getUni())).forEach(myPlani -> {
                    ressMap.put(myPlani, ressPage.aktuelleRessMenge.get(myPlani.getString()));
                });

                networkInterface.getSF(sid, pair.getPlaniId(), pair.getUni(), sfPage -> {
                    sfPage.schiffsfabrik.keySet().stream().map(displayKoords -> new Koords(displayKoords, pair.getUni())).forEach(myPlani -> {
                        int sf = sfPage.schiffsfabrik.get(myPlani.getString());
                        if (sf == 0) {
                            MyPlani sf0 = new MyPlani(pair, myPlani.getString());
                            Log.d(sf0.getKoords().getString() + " aus Planiliste entfernt da SF = 0");
                            listdata.remove(sf0);
                            viewlistdata.remove(sf0);
                            ressMap.remove(myPlani);
                            sfMap.remove(myPlani);
                            schleifenMap.remove(myPlani);
                            hqMap.remove(myPlani);
                            bioMap.remove(myPlani);
                            userInterface.notifyDataChanged();
                        } else {
                            sfMap.put(myPlani, sf);
                        }
                    });

                    networkInterface.getBauschleifen(sid, pair.getPlaniId(), pair.getUni(), schleifenPage -> {
                        schleifenPage.bauschleifen.keySet().stream().map(displayKoords -> new Koords(displayKoords, pair.getUni())).forEach(myPlani -> {
                            schleifenMap.put(myPlani, schleifenPage.bauschleifen.get(myPlani.getString()));
                        });

                        networkInterface.getZivilgebäude(sid, pair.getPlaniId(), pair.getUni(), buildingsPage -> {
                            buildingsPage.hauptquartier.keySet().stream().map(displayKoords -> new Koords(displayKoords, pair.getUni())).forEach(myPlani -> {
                                hqMap.put(myPlani, buildingsPage.hauptquartier.get(myPlani.getString()));
                            });
                            buildingsPage.biozelle.keySet().stream().map(displayKoords -> new Koords(displayKoords, pair.getUni())).forEach(myPlani -> {
                                bioMap.put(myPlani, buildingsPage.biozelle.get(myPlani.getString()));
                            });
                            if (pair.equals(selectedPlaniBefore)) {
                                updateProductionValues();
                            }
                        });
                    });
                });
            });
        });
    }

    public String getEinwohner(MyPlani plani) {
        boolean hasHq = hqMap.containsKey(plani.getKoords());
        boolean hasBio = bioMap.containsKey(plani.getKoords());
        if (ressMap.containsKey(plani.getKoords()) && hasHq && hasBio) {
            double ew = (double) ressMap.get(plani.getKoords()).getEWTotal();
            double maxEW = bioMap.get(plani.getKoords()) * 5000 + hqMap.get(plani.getKoords()) * 1000;
            double prozentEW = maxEW > 0 ? (ew / maxEW) * 100.0 : 0;
            return String.format("%,.0f (%,.0f", ew, prozentEW) + "%)";
        } else {
            return "";
        }
    }

    public String getFüllDauer(MyPlani plani) {
        Date startDate = new Date();
        if (schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()) != null) {
            Date endDate = schleifenMap.get(plani.getKoords()).endDate;
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            long diffHours = (diffTime / (1000 * 60 * 60)) - (diffDays * 24);
            return (diffDays > 0 ? diffDays + "d " : "") + diffHours + "h";
        } else {
            return "";
        }
    }

    public String getSchiffsTyp(MyPlani plani) {
        if (!prodMap.containsKey(plani) || prodMap.get(plani) == null || prodMap.get(plani).shipType == null || prodMap.get(plani).menge == 0) {
            return "";
        } else {
            ProdParameter param = prodMap.get(plani);
            return Constants.schiffsNamen.get(param.shipType);
        }
    }

    public String getProzent(MyPlani plani) {
        if (!prodMap.containsKey(plani) || prodMap.get(plani) == null || prodMap.get(plani).shipType == null || prodMap.get(plani).menge == 0) {
            return "";
        } else {
            return String.valueOf(prodMap.get(plani).prozent);
        }
    }

    public String getMenge(MyPlani plani) {
        if (!prodMap.containsKey(plani)
                || prodMap.get(plani) == null
                || !sfMap.containsKey(plani.getKoords())
                || sfMap.get(plani.getKoords()) == null
                || prodMap.get(plani).shipType == null
                || prodMap.get(plani).menge == 0) {
            return "";
        } else {
            ProdParameter param = prodMap.get(plani);
            int maxPakete = Constants.getPaketgröße(param.shipType, sfMap.get(plani.getKoords()), param.prozent);
            int paket = userInterface.cmb_paket_selected_index() == 0 ? 1 : maxPakete;
            return (param.menge == 0) ? "" : param.menge + "x" + paket;
        }
    }

    public String getStatus(MyPlani plani) {
        if (!prodMap.containsKey(plani) || prodMap.get(plani) == null) {
            return "";
        } else {
            return prodMap.get(plani).getStatus();
        }
    }

    public String getSchleifen(MyPlani plani) {
        if (schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()) != null) {
            return schleifenMap.get(plani.getKoords()).inhalt;
        } else {
            return "";
        }
    }

    public void updateProductionValues() {
        Date startDate = new Date();
        for (int r = 0; r < viewlistdata.size(); r++) {
            MyPlani planet = viewlistdata.get(r);
            ProdParameter param = prodMap.get(planet);

            Koords koords = planet.getKoords();
            boolean mapOk = hqMap.containsKey(koords)
                    && ressMap.containsKey(koords)
                    && sfMap.containsKey(koords)
                    && bioMap.containsKey(koords);
            final int finalRow = r;
            boolean rowSelected = IntStream.of(userInterface.jtableShipBuild_selected_rows()).anyMatch((int x1) -> x1 == finalRow);
            boolean update = userInterface.rb_fill_all_planis_selected() || rowSelected;

            if (!mapOk || !update) {
                continue;
            }

            double ew = (double) ressMap.get(koords).getEWTotal();
            double maxEW = bioMap.get(koords) * 5000 + hqMap.get(koords) * 1000;
            double prozentEW = maxEW > 0 ? (ew / maxEW) * 100.0 : 0;
            int einwohnerMin = userInterface.getSchleifenEinwohnerMin();
            boolean einwohnerNichtVollgenug = userInterface.cmb_schleifen_einwohner_min_prozent_anz_selected_index() == 0 ? einwohnerMin >= prozentEW : einwohnerMin >= ew;
            boolean zuWenigEinwohnerZumBauen = userInterface.optionEinwohnerLeerSelected() && einwohnerNichtVollgenug;

            boolean schleifeNichtVollgenug;
            int schleifeMin = userInterface.getSchleifenMaxTage();
            Schleife schleife = schleifenMap.get(koords);
            if (schleife != null) {
                Date endDate = schleifenMap.get(koords).endDate;
                long startTime = startDate.getTime();
                long endTime = endDate.getTime();
                long diffTime = endTime - startTime;
                long diffDays = diffTime / (1000 * 60 * 60 * 24);
                long diffHours = (diffTime / (1000 * 60 * 60));
                schleifeNichtVollgenug = userInterface.cmb_schleifen_max_tage_stunden_selected_index() == 0 ? diffDays < schleifeMin : diffHours < schleifeMin;
            } else {
                schleifeNichtVollgenug = true;
            }
            boolean schleifenVollGenug = userInterface.cb_option_schleife_leer_selected() && !schleifeNichtVollgenug;
            if (zuWenigEinwohnerZumBauen || schleifenVollGenug) {
                param.shipType = null;
            }
            if (schleifenVollGenug || zuWenigEinwohnerZumBauen) {
                continue;
            }

            //   Log.d("Update Koords: " + koords.getString());
            param.prozent = Integer.parseInt(userInterface.cmb_prozent_selected_item());
            param.shipType = Constants.getTyp(userInterface.cmb_schiffstyp_selected_index());
            param.menge = calcMenge(param, userInterface.getSchleifenFüllMenge());
            int maxPakete = Constants.getPaketgröße(param.shipType, sfMap.get(koords), param.prozent);
            param.pack = userInterface.cmb_paket_selected_index() == 1 && maxPakete > 1;

            Ress kosten = Ress.copy(Constants.prozenzBauRessMap.get(param.prozent).get(param.shipType));
            double rabatt = Constants.getRabatt(param.shipType, sfMap.get(koords), param.prozent);

            if (param.pack) {
                kosten.setPaketeRabatt(maxPakete, rabatt);
            }

//            checkMissingRess(planet);
            boolean zuWenigEinwohner;
            long bauenMitEisen = (long) (ressMap.get(koords).getEisen() / kosten.getEisen());
            long bauenMitTitan = (long) (ressMap.get(koords).getTitan() / kosten.getTitan());

            long minimum = Math.min(bauenMitEisen, bauenMitTitan);
            if (kosten.getWasser() > 0) {
                long bauenMitWasser = (long) (ressMap.get(koords).getWasser() / kosten.getWasser());
                minimum = Math.min(bauenMitWasser, minimum);
            }
            if (kosten.getNahrung() > 0) {
                long bauenMitNahrung = (long) (ressMap.get(koords).getNahrung() / kosten.getNahrung());
                minimum = Math.min(bauenMitNahrung, minimum);
            }
            if (kosten.getWasserstoff() > 0) {
                long bauenMitWasserstoff = (long) (ressMap.get(koords).getWasserstoff() / kosten.getWasserstoff());
                minimum = Math.min(bauenMitWasserstoff, minimum);
            }
            if (kosten.getEWTotal() > 0) {
                long bauenMitEinwohner = (long) (ressMap.get(koords).getEWAvailableForBuild() / kosten.getEWTotal());
                minimum = Math.min(bauenMitEinwohner, minimum);
                zuWenigEinwohner = bauenMitEinwohner == 0;
            } else {
                zuWenigEinwohner = false;
            }

            if (param.menge == 0) {
                param.setStatus("");
            } else if (zuWenigEinwohner) {
                param.setStatus("zu wenig EW");
                param.menge = 0;
            } else if (minimum == 0) {
                param.setStatus("zu wenig Ress");
                param.menge = 0;
            } else {
                int paket = userInterface.cmb_paket_selected_index() == 0 ? 1 : maxPakete;
                param.setStatus(minimum < param.menge ? "Ress reicht nur für " + minimum + "x" + paket : "bereit");
            }

        }
        userInterface.notifyDataChanged();
    }

//    private void checkMissingRess(MyPlani plani) {
//        ProdParameter param = prodMap.get(plani);
//        Ress ressAufPlani = ressMap.get(plani.getKoords());
//        Ress kosten = Ress.copy(Constants.prozenzBauRessMap.get(param.prozent).get(param.shipType));
//        long feKosten = kosten.getEisen() * param.menge;
//        long tiKosten = kosten.getTitan() * param.menge;
//        long waKosten = kosten.getWasser() * param.menge;
//        long h2Kosten = kosten.getWasserstoff() * param.menge;
//        long naKosten = kosten.getNahrung() * param.menge;
//        long ewKosten = kosten.getEinwohner() * param.menge;
//        double feFehlt = feKosten > ressAufPlani.getEisen() ? feKosten - ressAufPlani.getEisen() : 0;
//        double tiFehlt = tiKosten > ressAufPlani.getTitan() ? tiKosten - ressAufPlani.getTitan() : 0;
//        double waFehlt = waKosten > ressAufPlani.getWasser() ? waKosten - ressAufPlani.getWasser() : 0;
//        double h2Fehlt = h2Kosten > ressAufPlani.getWasserstoff() ? h2Kosten - ressAufPlani.getWasserstoff() : 0;
//        double naFehlt = naKosten > ressAufPlani.getNahrung() ? naKosten - ressAufPlani.getNahrung() : 0;
//        double ewFehlt = ewKosten > ressAufPlani.getEinwohner() ? ewKosten - ressAufPlani.getEinwohner() : 0;
//        double ressFehlt = feFehlt + tiFehlt + waFehlt + h2Fehlt + naFehlt + ewFehlt;
//        if (ressFehlt > 0) {
//            String fehlt = "<HTML>Es fehlt<br>";
//            if (feFehlt > 0) {
//                fehlt += String.format("%,.0f Eisen<br>", feFehlt);
//            }
//            if (tiFehlt > 0) {
//                fehlt += String.format("%,.0f Titan<br>", tiFehlt);
//            }
//            if (waFehlt > 0) {
//                fehlt += String.format("%,.0f Wasser<br>", waFehlt);
//            }
//            if (h2Fehlt > 0) {
//                fehlt += String.format("%,.0f Wasserstoff<br>", h2Fehlt);
//            }
//            if (naFehlt > 0) {
//                fehlt += String.format("%,.0f Nahrung<br>", naFehlt);
//            }
//            if (ewFehlt > 0) {
//                fehlt += String.format("%,.0f Einwohner", ewFehlt);
//            }
//            fehlt += "</HTML>";
//        } else {
//        }
//    }
    private long calcMenge(ProdParameter param, String mengeStr) {
        int sf = sfMap.get(param.plani.getKoords());
        long currentEW = ressMap.get(param.plani.getKoords()).getEWAvailableForBuild();

        boolean paket = userInterface.cmb_paket_selected_index() == 1;
        int maxPakete = Constants.getPaketgröße(param.shipType, sf, param.prozent);
        Ress kosten = Ress.copy(Constants.prozenzBauRessMap.get(param.prozent).get(param.shipType));
        double rabatt = Constants.getRabatt(param.shipType, sf, param.prozent);

        if (paket) {
            kosten.setPaketeRabatt(maxPakete, rabatt);
        }

        switch (userInterface.cmb_schleifen_fill_art_selected_index()) {
            case 0:
                if (!mengeStr.equals("")) {
                    return Long.parseLong(mengeStr);
                } else {
                    return 0;
                }
            case 1: {
                long ewUntergrenze = mengeStr.equals("") ? currentEW : Long.parseLong(mengeStr);
                double ewVerbauch = currentEW > ewUntergrenze ? currentEW - ewUntergrenze : 0;

                return (long) (ewVerbauch / kosten.getEWAvailableForBuild());
            }
            case 2:
                int hq = hqMap.get(param.plani.getKoords());
                int bio = bioMap.get(param.plani.getKoords());
                double maxEW = bio * 5000 + hq * 1000;
                double ewRestProzent = mengeStr.equals("") ? 100 : Long.parseLong(mengeStr);
                double ewUntergrenze = maxEW * (ewRestProzent / 100.0);
                double ewVerbauch = currentEW > ewUntergrenze ? currentEW - ewUntergrenze : 0;
                return (long) (ewVerbauch / kosten.getEWAvailableForBuild());
            case 3:
                return 0;
//                return stundenMenge;
            default:
                return 0;
        }
    }

    public void resetValues() {
        updateProductionValues(); //TODO test
        prodMap.values().stream().forEach(prod -> {
            prod.menge = 0;
            prod.shipType = null;
            prod.prozent = Integer.parseInt(userInterface.cmb_prozent_selected_item());
            prod.setStatus("");
        });
        userInterface.notifyDataChanged();
    }

    public void fillSchleifen() {
        viewlistdata.stream().map(prodMap::get).filter(ProdParameter::bereit).forEach(param -> {
            param.setStatus("in Warteschlange");
        });
        fillLoop(viewlistdata.iterator());
    }

    private void fillLoop(Iterator<MyPlani> iter) {
        MyPlani plani = iter.next();
        ProdParameter param = prodMap.get(plani);
        if (!param.bereit()) {
            if (iter.hasNext()) {
                fillLoop(iter);
            }
            return;
        }
        Log.d("gehe zu Plani: " + param.plani.toString());
        networkInterface.gotoPlani(param, response -> {
            Koords koords = response.selectedPlani.getKoords();
            Log.d("Plani ausgewählt: " + response.selectedPlani.toString());
            final int[] sizeBefore = new int[1];
            sizeBefore[0] = response.schleifen.warteschlange.size();
            if (response.schleifen.aktuellesSchiff == null) {
                sizeBefore[0] -= 1;
            }

            Log.d("baue Schiffe auf plani: " + response.selectedPlani.toString());
            networkInterface.buildShip(param, buildResponse -> {

                int sizeAfter = buildResponse.schleifen.warteschlange.size();
                if (buildResponse.schleifen.aktuellesSchiff == null) {
                    sizeAfter -= 1;
                }

                if (buildResponse.schleifeEndDate != null) {
                    List<String> schleifen = buildResponse.schleifen.warteschlange.stream().map(p -> p.menge + (p.paket > 1 ? " " + p.paket + "x" : " ") + Constants.schiffsNamen.get(p.schiffsTyp)).collect(Collectors.toList());
                    schleifenMap.put(koords, new Schleife(buildResponse.schleifeEndDate, String.join(" - ", schleifen)));
                }
                ressMap.put(koords, buildResponse.offeneRess);

                if (sizeAfter > sizeBefore[0]) {
                    Log.d("Schiffe erfolgreich auf Plani gebaut: " + koords);
                    param.setStatus("Erfolgreich");
                } else {
                    Log.d("Schiffe nicht gebaut auf Plani: " + koords);
                    param.setStatus("Nicht gefüllt");
                }
                Log.d("");
                Log.d("");
                userInterface.notifyDataChanged();
                if (iter.hasNext()) {
                    fillLoop(iter);
                }
            });
        });
    }

    public void schleifenZusammenfassen() {
        for (int i = 0; i < viewlistdata.size(); i++) {
            MyPlani plani = viewlistdata.get(i);
            final int finalRow = i;
            boolean rowSelected = IntStream.of(userInterface.jtableShipBuild_selected_rows()).anyMatch((int x1) -> x1 == finalRow);
            boolean hasSchleife = schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()).inhalt != null && schleifenMap.get(plani.getKoords()).inhalt.contains("-");
            boolean update = (userInterface.rb_fill_all_planis_selected() || rowSelected) && hasSchleife;
            if (update) {
                prodMap.get(plani).setStatus("in Warteschlange");
            }
        }
        userInterface.notifyDataChanged();
        summarizeLoop(viewlistdata.iterator());
    }

    private void summarizeLoop(Iterator<MyPlani> iter) {
        MyPlani plani = iter.next();
        final int finalRow = viewlistdata.indexOf(plani);
        boolean ignoreCash = !userInterface.siedlerSchleifenIgnorieren();
        boolean rowSelected = IntStream.of(userInterface.jtableShipBuild_selected_rows()).anyMatch((int x1) -> x1 == finalRow);
        boolean hasSchleife = schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()).inhalt != null && schleifenMap.get(plani.getKoords()).inhalt.contains("-");
        boolean update = (userInterface.rb_fill_all_planis_selected() || rowSelected) && hasSchleife;
        if (!update) {
            if (iter.hasNext()) {
                summarizeLoop(iter);
            }
            return;
        }

        Log.d("Zusammenfassung: gehe zu plani " + plani);
        networkInterface.gotoPlaniSort(sid, plani, page -> {
            HashMap<SchiffsbauschleifenPaket.Identifier, Long> counts = new HashMap<>();

            Map<SchiffsbauschleifenPaket.Identifier, Long> numberOfItems = page.schleifen.warteschlange.stream().collect(Collectors.
                    groupingBy(e -> e.getIdentifier(ignoreCash), Collectors.counting()));

            page.schleifen.warteschlange.stream().forEach(paket -> {
                SchiffsbauschleifenPaket.Identifier identifier = paket.getIdentifier(ignoreCash);
                if (!counts.containsKey(identifier)) {
                    counts.put(identifier, paket.menge);
                } else {
                    counts.put(identifier, counts.get(identifier) + paket.menge);
                }
            });

            long anzahlZusammenfassbarePakete = numberOfItems.values().stream().filter(anzahlPakete -> anzahlPakete > 1).count();
            if (anzahlZusammenfassbarePakete == 0) {
                Log.d(plani.getKoords().getString() + "Keine Änderung");
                prodMap.get(plani).setStatus("Keine Änderung");
                userInterface.notifyDataChanged();
                if (iter.hasNext()) {
                    summarizeLoop(iter);
                }
                return;
            }

            numberOfItems.keySet().stream().filter(key -> numberOfItems.get(key) > 1).forEach((key) -> {
                List<SchiffsbauschleifenPaket> pakete = new ArrayList<>();
                page.schleifen.warteschlange.stream().forEach(paket -> {
                    boolean canBuild = paket.schiffsTyp.canBuild(Rasse.PIRAT);
                    if (canBuild && paket.getIdentifier(ignoreCash).equals(key)) {
                        pakete.add(paket);
                    }
                });
                if (!pakete.isEmpty()) {
                    Log.d("Zusammenfassung: " + pakete.size() + " Schleifen auflösen");
                    networkInterface.schleifenAuflösen(sid, pakete, afterDelete -> {
                        ProdParameter param = new ProdParameter(sid, plani);
                        param.menge = counts.get(key);
                        param.pack = key.paket > 1;
                        param.prozent = key.prozent;
                        param.shipType = key.schiffsTyp;

                        Log.d("Zusammenfassung: Baue: " + param.menge + " " + key.paket + "x" + Constants.schiffsNamen.get(param.shipType));
                        networkInterface.buildShip(param, buildResponse -> {
                            Koords koords = buildResponse.selectedPlani.getKoords();
                            Log.d("buildResponse: " + koords);
                            if (buildResponse.schleifeEndDate != null) {
                                List<String> schleifen = buildResponse.schleifen.warteschlange.stream().map(p -> p.menge + (p.paket > 1 ? " " + p.paket + "x" : " ") + Constants.schiffsNamen.get(p.schiffsTyp)).collect(Collectors.toList());
                                schleifenMap.put(koords, new Schleife(buildResponse.schleifeEndDate, String.join(" - ", schleifen)));
                            }

                            ressMap.put(koords, buildResponse.offeneRess);
                            Log.d(plani.getKoords().getString() + "Fertig");
                            prodMap.get(plani).setStatus("Fertig");
                            userInterface.notifyDataChanged();
                            if (iter.hasNext()) {
                                summarizeLoop(iter);
                            }
                        });
                    });
                } else {
                    Log.d(plani.getKoords().getString() + " paket leer");
                }
            });

        });
    }

    public void schleifenSortieren() {
        for (int i = 0; i < viewlistdata.size(); i++) {
            MyPlani plani = viewlistdata.get(i);
            final int finalRow = i;
            boolean rowSelected = IntStream.of(userInterface.jtableShipBuild_selected_rows()).anyMatch((int x1) -> x1 == finalRow);
            boolean hasSchleife = schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()).inhalt != null && schleifenMap.get(plani.getKoords()).inhalt.contains("-");
            boolean update = (userInterface.rb_fill_all_planis_selected() || rowSelected) && hasSchleife;
            if (update) {
                prodMap.get(plani).setStatus("in Warteschlange");
            }
        }
        userInterface.notifyDataChanged();
        sortLoop(viewlistdata.iterator());
    }

    private void sortLoop(Iterator<MyPlani> iter) {
        MyPlani plani = iter.next();
        final int finalRow = viewlistdata.indexOf(plani);
        boolean rowSelected = IntStream.of(userInterface.jtableShipBuild_selected_rows()).anyMatch((int x1) -> x1 == finalRow);
        boolean hasSchleife = schleifenMap.containsKey(plani.getKoords()) && schleifenMap.get(plani.getKoords()).inhalt != null && schleifenMap.get(plani.getKoords()).inhalt.contains("-");
        boolean update = (userInterface.rb_fill_all_planis_selected() || rowSelected) && hasSchleife;
        ProdParameter param = prodMap.get(plani);
        param.setStatus(update && param.shipType != null && param.menge != 0 ? "in Warteschlange" : "");
        userInterface.notifyDataChanged();

        if (!update) {
            if (iter.hasNext()) {
                sortLoop(iter);
            }
            return;
        }
        Log.d("Sortieren: gehe zu plani " + plani);
        networkInterface.gotoPlaniSort(sid, plani, page -> {
            Log.d("Sortieren: Plani ausgewählt: " + page.selectedPlani.toString());
            param.setStatus("in Bearbeitung");
            userInterface.notifyDataChanged();
            Optional<SchiffsbauschleifenPaket> opt = page.schleifen.warteschlange.stream().filter(paket -> !paket.verschiebbar).findAny();
            if (opt.isPresent()) {
                //TODO paket auflösen, und neu einstellen
                SchiffsbauschleifenPaket delete = opt.get();
                Log.d("Sortieren: Erste Schleife auflösen " + Constants.schiffsNamen.get(delete.schiffsTyp));
                networkInterface.schleifenAuflösen(page.sid, Collections.singletonList(delete), afterDelete -> {
                    Log.d("Sortieren: erste Schleife aufgelöst: " + page.selectedPlani.toString());
                    ProdParameter newParam = new ProdParameter(page.sid, param.plani);
                    newParam.menge = delete.menge;
                    newParam.pack = delete.paket > 1;
                    newParam.prozent = delete.prozent;
                    newParam.shipType = delete.schiffsTyp;
                    Log.d("Sortieren: schleife neu hinzufügen " + Constants.schiffsNamen.get(newParam.shipType));
                    networkInterface.buildShip(newParam, afterRebuild -> {
                        Log.d("Sortieren: erste Schleife neu hinzugefügt: " + page.selectedPlani.toString());
                        sort(afterRebuild, iter);
                    });
                });
            } else {
                sort(page, iter);
            }

        });
    }

    private void sort(ProductionPage page, Iterator<MyPlani> iter) {
        List<Schiffstyp> order = userInterface.getShipOrder();
        Comparator<SchiffsbauschleifenPaket> percent = Comparator.comparing(p -> p.prozent);
        Comparator<SchiffsbauschleifenPaket> type = Comparator.comparing(p -> order.indexOf(p.schiffsTyp));

        if (userInterface.sortByPercentageFirst()) {
            Collections.sort(page.schleifen.warteschlange, percent.reversed().thenComparing(type));
        } else {
            Collections.sort(page.schleifen.warteschlange, type.thenComparing(percent.reversed()));
        }

        List<SchiffsbauschleifenPaket> list = new ArrayList<>();
        list.addAll(page.schleifen.warteschlange.stream().filter(paket -> paket.verschiebbar).collect(Collectors.toList()));
        Log.d(page.selectedPlani.getKoords().toString() + " Sortierung:");
        list.forEach(paket -> Log.d(paket.id + ":" + paket.menge + " " + paket.schiffsTyp.name()));
        Log.d("Sortieren: " + page.selectedPlani.toString());
        networkInterface.sortSchleifen(page.sid, list.stream().map(paket -> paket.id).collect(Collectors.toList()), buildResponse -> {

            if (page.schleifeEndDate != null) {
                List<String> schleifen = buildResponse.schleifen.warteschlange.stream().map(p -> p.menge + (p.paket > 1 ? " " + p.paket + "x" : " ") + Constants.schiffsNamen.get(p.schiffsTyp)).collect(Collectors.toList());
                schleifenMap.put(page.selectedPlani.getKoords(), new Schleife(page.schleifeEndDate, String.join(" - ", schleifen)));
            }
            Log.d("Sortieren: " + page.selectedPlani.getKoords().toString() + " fertig");
            Log.d("");
            Log.d("");

            prodMap.get(page.selectedPlani).setStatus("Fertig");
            userInterface.notifyDataChanged();
            if (iter.hasNext()) {
                sortLoop(iter);
            }
        });
    }

}
