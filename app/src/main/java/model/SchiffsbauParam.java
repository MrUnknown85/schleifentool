/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author nico
 */
public class SchiffsbauParam {

    public static RequestBody gotoPlaniWithSpeed(ProdParameter param) {
        String requestParams = "sid=" + param.getSid();
        requestParams += "&planetindex=" + param.plani.getPlanetIdPair().getPlaniId();
        requestParams += "&gotouniverse=" + param.plani.getUni();
        requestParams += "&speedperc" + param.prozent;
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }

    public static RequestBody buildShip(ProdParameter param) {
        String requestParams = "sid=" + param.getSid();
        requestParams += "&action=build";
        String pack = param.pack ? "r" : "";
        switch (param.shipType) {
            case SONDE:
                requestParams += "&s" + pack + "0=" + param.menge;
                break;
            case SNATCHER:
                requestParams += "&s" + pack + "1=" + param.menge;
                break;
            case RETTUNGSSCHIFF:
                requestParams += "&s" + pack + "2=" + param.menge;
                break;
            case HANDELSSCHIFF:
                requestParams += "&s" + pack + "3=" + param.menge;
                break;
            case HANDELSRIESE:
                requestParams += "&s" + pack + "4=" + param.menge;
                break;
            case AGLIDER:
                requestParams += "&s" + pack + "5=" + param.menge;
                break;
            case NOULON:
                requestParams += "&s" + pack + "6=" + param.menge;
                break;
            case BOMBER:
                requestParams += "&s" + pack + "7=" + param.menge;
                break;
            case KOLO:
                requestParams += "&s" + pack + "8=" + param.menge;
                break;
            case TRUGAR:
                requestParams += "&s" + pack + "9=" + param.menge;
                break;
            case VIOLO:
                requestParams += "&s" + pack + "10=" + param.menge;
                break;
            case NARUBU:
                requestParams += "&s" + pack + "11=" + param.menge;
                break;
            case NEOMAR:
                requestParams += "&s" + pack + "12=" + param.menge;
                break;
            case BLOODHOUND:
                requestParams += "&s" + pack + "13=" + param.menge;
                break;
            case KEMZEN:
                requestParams += "&s" + pack + "14=" + param.menge;
                break;
            case ZEMAR:
                requestParams += "&s" + pack + "15=" + param.menge;
                break;
            case FINUR:
                requestParams += "&s" + pack + "16=" + param.menge;
                break;
            case LUXOR:
                requestParams += "&s" + pack + "17=" + param.menge;
                break;
            case GRANDOR:
                requestParams += "&s" + pack + "18=" + param.menge;
                break;
            case INVA:
                requestParams += "&s" + pack + "19=" + param.menge;
                break;
            case FUSIONATOR:
                requestParams += "&s" + pack + "20=" + param.menge;
                break;
        }
        requestParams += "&sid=" + param.getSid();
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }

    public static RequestBody schleifenAuflösen(String sid, String item) {
        String requestParams = "sid=" + sid;
        requestParams += "&action=delete";
        requestParams += "&item=" + item;
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }

    public static RequestBody mehrereSchleifenAuflösen(String sid, List<SchiffsbauschleifenPaket> items) {
        String requestParams = "sid=" + sid;
        requestParams = items.stream().map((item) -> "&checked[]=" + item.id).reduce(requestParams, String::concat);
        requestParams += "&multaction=del";
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }

    public static RequestBody sortSchleifen(List<String> ids) {
        String requestParams = "sort=" + String.join("%26", ids.stream().map(id -> "shipslist%5B%5D%3D" + id).collect(Collectors.toList()));
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }
}
