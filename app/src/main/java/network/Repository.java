/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.network;

import ELschleifentool.interfaces.NetworkInterface;
import ELschleifentool.model.ConstructionPage;
import ELschleifentool.util.Callback;
import ELschleifentool.model.ProductionPage;
import ELschleifentool.model.SchiffsbauschleifenPaket;
import ELschleifentool.model.ProdParameter;
import ELschleifentool.model.MyPlani;
import ELschleifentool.model.NavigationPage;
import ELschleifentool.model.PlanetenPage;
import ELschleifentool.model.SchiffsbauParam;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author Nico
 */
public class Repository implements NetworkInterface {

    private final NetworkRequest network;

    public Repository() {
        network = NetworkRequest.getNetwork();
    }

    @Override
    public void getPlaniList(String sid, Callback<NavigationPage> callback) {
        network.executeRequest(network.getAPI().callNavipage(sid), callback);
    }

    @Override
    public void getRess(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("20", "1", sid, planiIndex, uni), callback);
    }

    @Override
    public void getSF(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("30", "5", sid, planiIndex, uni), callback);
    }

    @Override
    public void getBauschleifen(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("10", "3", sid, planiIndex, uni), callback);
    }

    @Override
    public void gotoPlani(ProdParameter param, Callback<ProductionPage> callback) {
        network.executeRequest(network.getAPI().gotoPlani(param.getSid(), param.plani.getPlanetIdPair().getPlaniId(), param.plani.getUni()), callback);
    }

    @Override
    public void buildShip(ProdParameter param, Callback<ProductionPage> callback) {
        RequestBody p = SchiffsbauParam.buildShip(param);
        network.executeRequest(network.getAPI().callProduction(param.plani.getPlanetIdPair().getPlaniId(), param.plani.getUni(), param.prozent, p), callback);
    }

    @Override
    public void schleifenAuflösen(String sid, List<SchiffsbauschleifenPaket> items, Callback<ProductionPage> onResult) {
        RequestBody p = SchiffsbauParam.mehrereSchleifenAuflösen(sid, items);
        network.executeRequest(network.getAPI().callProduction(null, null, null, p), onResult);
    }

    @Override
    public void gotoPlaniSort(String sid, MyPlani plani, Callback<ProductionPage> onResult) {
        network.executeRequest(network.getAPI().gotoPlani(sid, plani.getPlanetIdPair().getPlaniId(), plani.getUni()), onResult);
    }

    @Override
    public void sortSchleifen(String sid, List<String> ids, Callback<ProductionPage> callback) {
        RequestBody p = SchiffsbauParam.sortSchleifen(ids);
        network.executeRequest(network.getAPI().sortProductionSchleifen(sid, 1, p), callback);
    }

    @Override
    public void getAktuell(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("10", "1", sid, planiIndex, uni), callback);
    }

    @Override
    public void getKonstruktionsübersicht(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("10", "2", sid, planiIndex, uni), callback);
    }

    @Override
    public void getMinengebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("20", "2", sid, planiIndex, uni), callback);
    }

    @Override
    public void getSpeichergebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("20", "3", sid, planiIndex, uni), callback);
    }

    @Override
    public void getMilitärgebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("30", "5", sid, planiIndex, uni), callback);
    }

    @Override
    public void getZivilgebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("40", null, sid, planiIndex, uni), callback);
    }

    @Override
    public void getWeltwundergebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback) {
        network.executeRequest(network.getAPI().getPlanetenPage("50", null, sid, planiIndex, uni), callback);
    }

    @Override
    public void getConstuctionPage(String sid, MyPlani plani, Integer building, String buildKey, Integer switchMode, Integer cancel,
            Integer askOk, Callback<ConstructionPage> callback) {
        Integer planiId = (plani == null) ? null : plani.getPlanetIdPair().getPlaniId();
        Integer uni = (plani == null) ? null : plani.getUni();
        network.executeRequest(network.getAPI().construction(
                sid,
                planiId,
                uni,
                building,
                buildKey,
                switchMode,
                cancel,
                askOk), callback);
    }

    public void gebäudeBaukosten(String building, String sid, int stufe, Callback<RechnerResponse> callback) {
        String requestParams = "sid=" + sid;
        requestParams += "&typ=5";
        requestParams += "&" + getParam(building) + "=" + stufe;
        requestParams += "&sid=" + sid;
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
        network.executeRequest(network.getAPI().calc(sid, body), callback);
    }

    private String getParam(String name) {
        switch (name) {
            case "Hauptquartier":
                return "b0";
            case "Biozelle":
                return "b1";
            case "Bunker":
                return "b2";
            case "Farm":
                return "b3";
            case "Eisenmine":
                return "b4";
            case "Titanmine":
                return "b5";
            case "Bohrturm":
                return "b6";
            case "Chemiefabrik":
                return "b7";
            case "Recyclingcenter":
                return "b8";
            case "Nahrungssilo":
                return "b9";
            case "Eisenspeicher":
                return "b10";
            case "Titanspeicher":
                return "b11";
            case "Wasserspeicher":
                return "b12";
            case "Wasserstoffspeicher":
                return "b13";
            case "Universität":
                return "b14";
            case "Vergnügungszentrum":
                return "b15";
            case "Schiffsfabrik":
                return "b16";
            case "Verteidigungsstation":
                return "b17";
            case "Flottenkontrollzentrum":
                return "b18";
            case "Schildgenerator":
                return "b19";
            case "Intergalaktischer Weltraumhafen":
                return "b20";
            case "Palast":
                return "b21";
            case "Kernforschungszentrum":
                return "b22";
            case "Statue des Imperators":
                return "b23";
            default:
                throw new RuntimeException("fehler");
        }
    }
}
