/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

import model.KonstruktionsÜbersichtListItem;
import model.MyPlani;
import model.PlanetIdPair;
import model.PlanetenPage;
import model.Ress;
import model.Schleife;
import util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

/**
 *
 * @author nico
 */
public class PlanetenPageAdapter extends BotschutzAdapter<PlanetenPage> {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final String SELECTED_PLANI_ID_REGEX = "selectPlanet\\((\\d+), (\\d), forceScroll\\);[\\W\\w]+resource_header2\"[\\W\\w]+?\"right\">(\\d{1,2}:\\d{1,3}:\\d{1,2})";
    Pattern aktuellRegex = Pattern.compile("<a href=\"planeten\\.phtml\\?show=10&sid=[A-Za-z0-9]{26}\"><b>Aktuell<\\/b><\\/a>");
    Pattern rohstoffÜbersichtRegex = Pattern.compile("<a href=\"planeten\\.phtml\\?show=20&sid=[A-Za-z0-9]{26}\"><b>Rohstoffübersicht<b><\\/a>");
    Pattern schiffeRegex = Pattern.compile("<a href=\"planeten\\.phtml\\?show=30&sid=[A-Za-z0-9]{26}\"><b>Schiffe und Verteidigung<b><\\/a>");
    Pattern zivilRegex = Pattern.compile("<a href=\"planeten\\.phtml\\?show=40&sid=[A-Za-z0-9]{26}\"><b>Zivilgebäude<b><\\/a>");
    Pattern wunderRegex = Pattern.compile("<a href=\"planeten\\.phtml\\?show=50&sid=[A-Za-z0-9]{26}\"><b>Weltwunder<b><\\/a>");

    Pattern aktuÜbersicht = Pattern.compile("href=\"planeten\\.phtml\\?show=10&showsub=1&sid=[A-Za-z0-9]{26}\"><b>Übersicht<\\/b><\\/a>");
    Pattern aktuKonstruktion = Pattern.compile("href=\"planeten\\.phtml\\?show=10&showsub=2&sid=[A-Za-z0-9]{26}\"><b>Konstruktion<b><\\/a>");
    Pattern aktuSchiffschleifen = Pattern.compile("href=\"planeten\\.phtml\\?show=10&showsub=3&sid=[A-Za-z0-9]{26}\"><b>Schiffsbauschleifen<b><\\/a>");
    Pattern aktuDeffschleifen = Pattern.compile("href=\"planeten\\.phtml\\?show=10&showsub=4&sid=[A-Za-z0-9]{26}\"><b>Verteidigungsbauschleifen<\\/a>");

    Pattern aktuRohstoff = Pattern.compile("href=\"planeten\\.phtml\\?show=20&showsub=1&sid=[A-Za-z0-9]{26}\"><b>aktuelle<br>Rohstoffmengen<\\/b><\\/a>");
    Pattern minenRohstoff = Pattern.compile("href=\"planeten\\.phtml\\?show=20&showsub=2&sid=[A-Za-z0-9]{26}\"><b>Minenausbau<b><\\/a>");
    Pattern speicherRohstoff = Pattern.compile("href=\"planeten\\.phtml\\?show=20&showsub=3&sid=[A-Za-z0-9]{26}\"><b>Speicherausbau<b><\\/a>");
    Pattern förderRohstoff = Pattern.compile("href=\"planeten\\.phtml\\?show=20&showsub=4&sid=[A-Za-z0-9]{26}\"><b>Fördermenge<b><\\/a>");

    Pattern zivilschiffe = Pattern.compile("href=\"planeten\\.phtml\\?show=30&showsub=1&sid=[A-Za-z0-9]{26}\"><b>Zivilschiffe<\\/b><\\/a>");
    Pattern kriegsschiffe = Pattern.compile("href=\"planeten\\.phtml\\?show=30&showsub=2&sid=[A-Za-z0-9]{26}\"><b>Kriegsschiffe<b><\\/a>");
    Pattern spezialschiffe = Pattern.compile("href=\"planeten\\.phtml\\?show=30&showsub=3&sid=[A-Za-z0-9]{26}\"><b>Spezialschiffe<b><\\/a>");
    Pattern verteidigung = Pattern.compile("href=\"planeten\\.phtml\\?show=30&showsub=4&sid=[A-Za-z0-9]{26}\"><b>Verteidigungs-<br>anlagen<b><\\/a>");
    Pattern gebäude = Pattern.compile("href=\"planeten\\.phtml\\?show=30&showsub=5&sid=[A-Za-z0-9]{26}\"><b>Gebäude<b><\\/a>");

    Pattern konstruktion = Pattern.compile("<tr class=\"(?:highlight|blackrow)\"><td>(?:<a href=\"JavaScript:gotoPlanet\\(\\d+, '\\/construction\\.phtml'\\)\"><b>)?(\\d{1,2}:\\d{1,3}:\\d{1,2})(?:<\\/b><\\/a>)<\\/td><td>(\\d+)-(\\d{2})-(\\d{2}) (\\d{2}:\\d{2}:\\d{2})<\\/td><td>(?:<a href=\"javascript:popUp\\('buildings\\.phtml\\?sid=[a-zA-Z0-9]{26}&building=\\d+', \\d+\\)\">)([\\W\\w]+?)(?:<\\/a>) ([\\d.]+)<\\/td><\\/tr>");

    Pattern contentAktuell
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b><a href=\"JavaScript:gotoPlanet\\((\\d+), '\\/intro\\.phtml'\\)\"><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/a><\\/b> <a href[\\W\\w]+?\">(verstecken|anzeigen)<\\/a><\\/td><td align=\"right\">([\\d.]+)<\\/td><td><\\/td><td align=\"left\">([\\W\\w]*?)<\\/td><td align=\"left\">([\\W\\w]*?)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td colspan=\"3\">([\\W\\w]*?)<\\/td><td align=\"left\">([\\W\\w]*?)<\\/td><td align=\"left\">([\\W\\w]*?)<\\/td><\\/tr>");
    Pattern contentRohstoffÜbersicht
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>[\\W\\w]*?<\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><td align=\"right\" class=\"(red|green)\">([\\d.]*)<\\/td><\\/tr>");
    Pattern contentMinenOrSpeicherAusbau
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><\\/tr>");
    Pattern contentFörderMenge
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]* \\/ h)<\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]* \\/ h)<\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]* \\/ h)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>[\\W\\w]*?<\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]*) \\/ h<\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]*) \\/ h<\\/td><td align=\"right\" class=\"(red|green)\">([-\\d.]*) \\/ h<\\/td><\\/tr>");
    Pattern contentFörderGesamtUndSteuer
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>Gesamt<\\/b><\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><\\/tr><tr class=\"(?:light|dark)\"><td> <\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><td align=\"right\">([-\\d.]* \\/ h)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>Steuereinnahmen<\\/td><td><\\/td><td><\\/td><td align=\"right\">([-\\d.]*) Cash \\/ h<\\/td><\\/tr>");
    Pattern contentZivilschiffe
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\"> <\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\"> <\\/td><\\/tr>");
    Pattern contentKampfschiffe = Pattern.compile(
            "<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\"> <\\/td><\\/tr><tr class=\"(?:dark|light)\"><td> <\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\"> <\\/td><\\/tr>"
    );
    Pattern contentSpezialschiffe
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\">(?: |\\s+)<\\/td><td align=\"right\"><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)(?: |\\s+)*<\\/td><\\/tr>");
    Pattern contentVerteidigung
            = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td><\\/td><td align=\"right\"><\\/td><td align=\"right\"><\\/td><td align=\"right\">([\\d.]+) \\(([\\d.]+)\\)<\\/td><\\/tr>"); //1=light/dark, 2=displaykoords, 3=sf, 4=fkz, 5=name, 6=verteid, 7=schild
    Pattern contentGebäude = Pattern.compile("<tr class=\"(dark|light)\"><td><b>(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/b><\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><\\/tr><tr class=\"(?:dark|light)\"><td>([\\W\\w]*?)<\\/td><td align=\"right\">([\\d.]+)<\\/td><td align=\"right\">([\\d.]+)<\\/td><\\/tr>");

    @Override
    public PlanetenPage provideInstance() {
        return new PlanetenPage();
    }

    @Override
    public void convertType(String convertF, PlanetenPage convertTo) {
        String schiffsbauschleifen = "<b>Schiffsbauschleifen<b>";
        String convertFrom = convertF.replaceAll("    ", "");
        Matcher sel = Pattern.compile(SELECTED_PLANI_ID_REGEX).matcher(convertFrom);
        if (sel.find()) {
            int id = Integer.parseInt(sel.group(1));
            int uni = Integer.parseInt(sel.group(2));
            String koords = sel.group(3);
            convertTo.selectedPlani = new MyPlani(new PlanetIdPair(id, uni), koords);
        }

        convertTo.aktuell = aktuellRegex.matcher(convertFrom).find();
        convertTo.rohstoffe = rohstoffÜbersichtRegex.matcher(convertFrom).find();
        convertTo.schiffe = schiffeRegex.matcher(convertFrom).find();
        convertTo.zivilgebäude = zivilRegex.matcher(convertFrom).find();
        convertTo.weltwunder = wunderRegex.matcher(convertFrom).find();

        convertTo.übersicht = aktuÜbersicht.matcher(convertFrom).find();
        convertTo.konstruktion = aktuKonstruktion.matcher(convertFrom).find();
        convertTo.schiffsbauschleifen = aktuSchiffschleifen.matcher(convertFrom).find();
        convertTo.verteidigungsbauschleifen = aktuDeffschleifen.matcher(convertFrom).find();

        convertTo.aktuelleRohstoffMengen = aktuRohstoff.matcher(convertFrom).find();
        convertTo.minenAusbau = minenRohstoff.matcher(convertFrom).find();
        convertTo.speicherAusbau = speicherRohstoff.matcher(convertFrom).find();
        convertTo.foerdermenge = förderRohstoff.matcher(convertFrom).find();

        convertTo.zivil = zivilschiffe.matcher(convertFrom).find();
        convertTo.krieg = kriegsschiffe.matcher(convertFrom).find();
        convertTo.spezial = spezialschiffe.matcher(convertFrom).find();
        convertTo.verteidigung = verteidigung.matcher(convertFrom).find();
        convertTo.gebäude = gebäude.matcher(convertFrom).find();

        if (convertFrom.contains(schiffsbauschleifen)) {
            String schleifenRegex = "<td><a href=\"JavaScript:gotoPlanet\\(\\d+, '\\/produktion\\.phtml'\\)\"><b>(\\d+:\\d+:\\d+)<\\/b><\\/a><\\/td><td>([\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{2}:[\\d]{2}:[\\d]{2})<\\/td><td>([\\W\\w]+?)<\\/td><\\/tr>";
            Matcher m = Pattern.compile(schleifenRegex).matcher(convertFrom);
            while (m.find()) {
                String koords = m.group(1);
                Date d = null;
                try {
                    d = df.parse(m.group(2));
                } catch (ParseException ex) {
                    Log.err(ex);
                }
                if (d != null) {
                    String inhalt = m.group(3).replaceAll("<a href[\\W\\w]+?>", "").replaceAll("<\\/a>", "");
                    convertTo.bauschleifen.put(koords, new Schleife(d, inhalt));
                }
            }
        }

        if (convertTo.aktuell) {
            Matcher aktuellMatcher = contentAktuell.matcher(convertFrom);
            while (aktuellMatcher.find()) {
                //String background = aktuellMatcher.group(1);
                //String planetId = aktuellMatcher.group(2);
                String displayKoords = aktuellMatcher.group(3);
                //Wert ist 'verstecken' oder 'anzeigen'
                convertTo.showHidePlanet.put(displayKoords, aktuellMatcher.group(4));
                convertTo.points.put(displayKoords, Long.valueOf(aktuellMatcher.group(5).replace(".", "")));
                convertTo.currentBuilding.put(displayKoords, Jsoup.parse(aktuellMatcher.group(6)).text());
                convertTo.currentShip.put(displayKoords, Jsoup.parse(aktuellMatcher.group(7)).text());
                convertTo.planetName.put(displayKoords, aktuellMatcher.group(8));
                convertTo.currentResearch.put(displayKoords, aktuellMatcher.group(9));
                convertTo.currentDeffBuild.put(displayKoords, Jsoup.parse(aktuellMatcher.group(10)).text());
            }
        }

        if (convertTo.konstruktion) {
            Matcher m = konstruktion.matcher(convertFrom);
            while (m.find()) {
                KonstruktionsÜbersichtListItem it = new KonstruktionsÜbersichtListItem();
                it.displayKoords = m.group(1);
                it.fertigstellungJahr = m.group(2);
                it.fertigstellungMonat = m.group(3);
                it.fertigstellungTag = m.group(4);
                it.fertigstellungZeit = m.group(5);
                it.gebäude = m.group(6);
                it.stufe = Integer.parseInt(m.group(7));
                convertTo.konstruktionsListe.put(it.displayKoords, it);
            }
        }

        Matcher spezialschiffeMatcher = contentSpezialschiffe.matcher(convertFrom);
        while (spezialschiffeMatcher.find()) {
            //String background = spezialschiffeMatcher.group(1);
            String displayKoords = spezialschiffeMatcher.group(2);
            convertTo.sondeGebaut.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(3).replace(".", "")));
            convertTo.sondeSchleife.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(4).replace(".", "")));
            convertTo.snatcherGebaut.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(5).replace(".", "")));
            convertTo.snatcherSchleife.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(6).replace(".", "")));
            convertTo.invasGebaut.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(7).replace(".", "")));
            convertTo.invasSchleife.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(8).replace(".", "")));
            //String planiName = spezialschiffeMatcher.group(9);
            convertTo.fusionatorGebaut.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(10).replace(".", "")));
            convertTo.fusionatorSchleife.put(displayKoords, Long.valueOf(spezialschiffeMatcher.group(11).replace(".", "")));
        }

        if (convertTo.gebäude) {
            Matcher buildingMatcher = contentGebäude.matcher(convertFrom);
            while (buildingMatcher.find()) {
                //String background = buildingMatcher.group(1);
                String displayKoords = buildingMatcher.group(2);
                convertTo.schiffsfabrik.put(displayKoords, Integer.valueOf(buildingMatcher.group(3).replace(".", "")));
                convertTo.fkz.put(displayKoords, Integer.valueOf(buildingMatcher.group(4).replace(".", "")));
                //String planiName = buildingMatcher.group(5);
                convertTo.verteidigungsstation.put(displayKoords, Integer.valueOf(buildingMatcher.group(6).replace(".", "")));
                convertTo.schildgenerator.put(displayKoords, Integer.valueOf(buildingMatcher.group(7).replace(".", "")));
            }
        }
        if (convertTo.minenAusbau) {
            Matcher minenMatcher = contentMinenOrSpeicherAusbau.matcher(convertFrom);
            while (minenMatcher.find()) {
                //String background = minenMatcher.group(1);
                String displayKoords = minenMatcher.group(2);
                convertTo.eisenmine.put(displayKoords, Integer.valueOf(minenMatcher.group(3).replace(".", "")));
                convertTo.bohrturm.put(displayKoords, Integer.valueOf(minenMatcher.group(4).replace(".", "")));
                convertTo.farm.put(displayKoords, Integer.valueOf(minenMatcher.group(5).replace(".", "")));
                //String planiName = minenMatcher.group(6);
                convertTo.titanmine.put(displayKoords, Integer.valueOf(minenMatcher.group(7).replace(".", "")));
                convertTo.chemiefabrik.put(displayKoords, Integer.valueOf(minenMatcher.group(8).replace(".", "")));
                convertTo.recyclingcenter.put(displayKoords, Integer.valueOf(minenMatcher.group(9).replace(".", "")));
            }
        }

        if (convertTo.speicherAusbau) {
            Matcher speicherMatcher = contentMinenOrSpeicherAusbau.matcher(convertFrom);
            while (speicherMatcher.find()) {
                //String background = speicherMatcher.group(1);
                String displayKoords = speicherMatcher.group(2);
                convertTo.eisenspeicher.put(displayKoords, Integer.valueOf(speicherMatcher.group(3).replace(".", "")));
                convertTo.wasserspeicher.put(displayKoords, Integer.valueOf(speicherMatcher.group(4).replace(".", "")));
                convertTo.nahrungssilo.put(displayKoords, Integer.valueOf(speicherMatcher.group(5).replace(".", "")));
                //String planiName = speicherMatcher.group(6);
                convertTo.titanspeicher.put(displayKoords, Integer.valueOf(speicherMatcher.group(7).replace(".", "")));
                convertTo.wasserstoffspeicher.put(displayKoords, Integer.valueOf(speicherMatcher.group(8).replace(".", "")));
                convertTo.bunker.put(displayKoords, Integer.valueOf(speicherMatcher.group(9).replace(".", "")));
            }
        }

        if (convertTo.zivilgebäude) {
            Matcher zivilMatcher = contentGebäude.matcher(convertFrom);
            while (zivilMatcher.find()) {
                //String background = zivilMatcher.group(1);
                String displayKoords = zivilMatcher.group(2);
                convertTo.hauptquartier.put(displayKoords, Integer.valueOf(zivilMatcher.group(3).replace(".", "")));
                convertTo.universitaet.put(displayKoords, Integer.valueOf(zivilMatcher.group(4).replace(".", "")));
                //String planiName = zivilMatcher.group(5);
                convertTo.biozelle.put(displayKoords, Integer.valueOf(zivilMatcher.group(6).replace(".", "")));
                convertTo.vergnuegungszentrum.put(displayKoords, Integer.valueOf(zivilMatcher.group(7).replace(".", "")));
            }
        }

        if (convertTo.aktuelleRohstoffMengen) {
            Matcher ressMatcher = contentRohstoffÜbersicht.matcher(convertFrom);
            while (ressMatcher.find()) {
                // String background = ressMatcher.group(1);
                String displayKoords = ressMatcher.group(2);
                Ress ress = new Ress();
                // boolean eisenSave = ressMatcher.group(3) == "green"; //red
                ress.setEisen(Long.valueOf(ressMatcher.group(4).replace(".", "")));
                //boolean wasserSave = ressMatcher.group(5) == "green"; //red
                ress.setWasser(Long.valueOf(ressMatcher.group(6).replace(".", "")));
                //boolean einwohnerSave = ressMatcher.group(7) == "green"; //red
                ress.setEinwohner(Long.valueOf(ressMatcher.group(8).replace(".", "")));
                // boolean titanSave = ressMatcher.group(9) == "green"; //red
                ress.setTitan(Long.valueOf(ressMatcher.group(10).replace(".", "")));
                //boolean wasserstoffSave = ressMatcher.group(11) == "green"; //red
                ress.setWasserstoff(Long.valueOf(ressMatcher.group(12).replace(".", "")));
                //boolean nahrungSave = ressMatcher.group(13) == "green"; //red
                ress.setNahrung(Long.valueOf(ressMatcher.group(14).replace(".", "")));
                convertTo.aktuelleRessMenge.put(displayKoords, ress);
            }
        }

    }
}
