/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import model.ConstructionPage;
import model.MyPlani;
import util.Callback;
import model.PlanetenPage;
import model.NavigationPage;
import model.ProdParameter;
import model.ProductionPage;
import model.SchiffsbauschleifenPaket;
import java.util.List;

/**
 *
 * @author nico
 */
public interface NetworkInterface {

    public void getPlaniList(String sid, Callback<NavigationPage> listener);

    public void getRess(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getSF(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getBauschleifen(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void gotoPlani(ProdParameter param, Callback<ProductionPage> callback);

    public void buildShip(ProdParameter param, Callback<ProductionPage> callback);

    public void schleifenAuflösen(String sid, List<SchiffsbauschleifenPaket> items, Callback<ProductionPage> onResult);

    public void gotoPlaniSort(String sid, MyPlani plani, Callback<ProductionPage> onResult);

    public void sortSchleifen(String sid, List<String> ids, Callback<ProductionPage> callback);

    public void getAktuell(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getKonstruktionsübersicht(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getMinengebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getSpeichergebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getMilitärgebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getZivilgebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getWeltwundergebäude(String sid, Integer planiIndex, Integer uni, Callback<PlanetenPage> callback);

    public void getConstuctionPage(String sid, MyPlani planiToLoad, Integer building, String buildKey, Integer switchMode, Integer cancel, Integer askOK, Callback<ConstructionPage> callback);
}
