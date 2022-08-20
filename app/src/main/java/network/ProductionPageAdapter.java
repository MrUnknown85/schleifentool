/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.network;

import ELschleifentool.util.Log;
import ELschleifentool.model.ProductionPage;
import ELschleifentool.model.SchiffsbauschleifenPaket;
import ELschleifentool.model.PlanetIdPair;
import ELschleifentool.model.Schiffstyp;
import ELschleifentool.model.MyPlani;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nico
 */
public class ProductionPageAdapter extends BotschutzAdapter<ProductionPage> {

    private static final String SELECTED_PLANI_ID_REGEX = "selectPlanet\\((\\d+), (\\d), forceScroll\\);[\\W\\w]+resource_header2\"[\\W\\w]+?\"right\">(\\d{1,2}:\\d{1,3}:\\d{1,2})";
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public ProductionPage provideInstance() {
        return new ProductionPage();
    }

    @Override
    public void convertType(String convertFrom, ProductionPage convertTo) throws IOException {
        try {
            Matcher sel = Pattern.compile(SELECTED_PLANI_ID_REGEX).matcher(convertFrom);
            if (sel.find()) {
                int id = Integer.parseInt(sel.group(1));
                int uni = Integer.parseInt(sel.group(2));
                String koords = sel.group(3);
                convertTo.selectedPlani = new MyPlani(new PlanetIdPair(id, uni), koords);
            }

            convertTo.moveSuccess = convertFrom.contains("Erfolgreich verschoben");
            convertTo.moveError = convertFrom.contains("Element kann nicht verschoben werden");

            String regexGesamtdauer = "<div id=\"TotalUhr\" class=\"cost\">&nbsp<\\/div>[\\W\\w]*?, ([\\d]{2}\\.[\\d]{2}\\.[\\d]{4} [\\d]{2}:[\\d]{2}:[\\d]{2})<br>";
            Matcher gesamtDauerMatcher = Pattern.compile(regexGesamtdauer).matcher(convertFrom);
            if (gesamtDauerMatcher.find()) {
                convertTo.schleifeEndDate = df.parse(gesamtDauerMatcher.group(1));
            }

            String koordsRegex = "http:\\/\\/images\\.earthlost\\.de\\/images\\/planeten\\/[\\w\\W]+?\\.jpg[\\w\\W]+?<\\/td><td>[\\w\\W]*?<\\/td><td align=\"right\">(\\d{1,2}:\\d{1,3}:\\d{1,2})<\\/td><td align=\"right\">[\\d.]+<\\/td><\\/tr><\\/table><\\/div>";
            Matcher koordsMatcher = Pattern.compile(koordsRegex).matcher(convertFrom);
            if (koordsMatcher.find()) {
                convertTo.koords = koordsMatcher.group(1);
            }

            String offeneRessRegex = "Eisen[\\w\\W]*?([\\d.]+)&nbsp;[\\w\\W]*?Titan[\\w\\W]*?([\\d.]+)&nbsp;[\\w\\W]*?Wasser[\\w\\W]*?([\\d.]+)&nbsp;[\\w\\W]*?Wasserstoff[\\w\\W]*?([\\d.]+)&nbsp;[\\w\\W]*?Nahrung[\\w\\W]*?([\\d.]+)&nbsp;[\\w\\W]*?resource_header2\"[\\w\\W]*?Einwohner[\\w\\W]*?([\\d.]+) \\(([\\d.]+)%\\)<\\/td><\\/tr><\\/table><\\/div>";
            Matcher offeneRessMatcher = Pattern.compile(offeneRessRegex).matcher(convertFrom);
            if (offeneRessMatcher.find()) {
                convertTo.offeneRess.setEisen(Long.parseLong(offeneRessMatcher.group(1).replaceAll("\\D", "")));
                convertTo.offeneRess.setTitan(Long.parseLong(offeneRessMatcher.group(2).replaceAll("\\D", "")));
                convertTo.offeneRess.setWasser(Long.parseLong(offeneRessMatcher.group(3).replaceAll("\\D", "")));
                convertTo.offeneRess.setWasserstoff(Long.parseLong(offeneRessMatcher.group(4).replaceAll("\\D", "")));
                convertTo.offeneRess.setNahrung(Long.parseLong(offeneRessMatcher.group(5).replaceAll("\\D", "")));
                convertTo.offeneRess.setEinwohner(Long.parseLong(offeneRessMatcher.group(6).replaceAll("\\D", "")));
                convertTo.offeneRess.setEinwohnerProzent(Long.parseLong(offeneRessMatcher.group(7).replaceAll("\\D", "")));
            }

            String currentSpeedRegex = "<select id=\"speedselect\"[\\w\\W]*?SELECTED>(100|125|150|175|200)%[\\w\\W]*?<\\/select>";
            Matcher currentSpeedMatcher = Pattern.compile(currentSpeedRegex).matcher(convertFrom);
            if (currentSpeedMatcher.find()) {
                convertTo.prozentSpeed = Integer.parseInt(currentSpeedMatcher.group(1));
            }

            // addShipShort\((\d+), (1|0), ('?r?\d+'?),\s'[\W\w]+?">([\W\w]+?)<\/a>[\W\w]*?', (\d+), (\d+), (\d+), (\d+), (\d+), ([\d*]+), ([\d*]+)
            String regexAvailableShips = "addShipShort\\((\\d+), (1|0), ('?r?\\d+'?),\\s'[\\W\\w]+?\">([\\W\\w]+?)<\\/a>[\\W\\w]*?', (\\d+), (\\d+), (\\d+), (\\d+), (\\d+), ([\\d*]+), ([\\d*]+)";
            Matcher availableShipsMatcher = Pattern.compile(regexAvailableShips).matcher(convertFrom);
            while (availableShipsMatcher.find()) {
                String shipNumber = availableShipsMatcher.group(1);
                String highlight = availableShipsMatcher.group(2);
                String buildId = availableShipsMatcher.group(3);
                String shipName = availableShipsMatcher.group(4);

                String kostenEisen = availableShipsMatcher.group(5);
                String kostenTitan = availableShipsMatcher.group(5);
                String kostenWasser = availableShipsMatcher.group(7);
                String kostenWasserstoff = availableShipsMatcher.group(8);
                String kostenNahrung = availableShipsMatcher.group(9);
                String kostenCash = availableShipsMatcher.group(10); //multiplikator
                String kostenEinwohner = availableShipsMatcher.group(11); //multiplikator
//            System.out.println(String.format("%s %s %sFe %sTi %sWa %sH2 %sNa %s$ %sEW", shipName, buildId, kostenEisen, kostenTitan, kostenWasser, kostenWasserstoff, kostenNahrung, kostenCash, kostenEinwohner));
            }

            String regexSchleife = "\\[[\"]?(\\d+)[\"]?,\" (?:style|class)=\\\\\"[\\W\\w]+?\",(\\d+),(true|false),[\"]?([\\d.]*)[\"]?,\\W([\\d]+x?\\s?)?<a href[\\W\\w]+?(\\d+)'\\)\\\\\\\">[a-zA-Z -]+<\\\\\\/a>\",\"(\\d+)\\%\",\"(?:Cash: ([\\d.]*))?\"\\]";
            Matcher m = Pattern.compile(regexSchleife).matcher(convertFrom);
            while (m.find()) {
                SchiffsbauschleifenPaket schleife = new SchiffsbauschleifenPaket();
                schleife.id = m.group(1);
                schleife.sort = Integer.parseInt(m.group(2).replaceAll("\\D", ""));
                schleife.verschiebbar = m.group(3).equals("true");
                schleife.menge = Long.parseLong(m.group(4).replaceAll("\\D", ""));
                if (!schleife.verschiebbar) {
                    schleife.menge += 1;
                }
                String paket = m.group(5);
                if (paket == null || paket.isEmpty()) {
                    paket = "1";
                }
                schleife.paket = Integer.parseInt(paket.replaceAll("\\D", ""));
                schleife.schiffsTyp = Schiffstyp.values()[Integer.parseInt(m.group(6))];
                schleife.prozent = Integer.parseInt(m.group(7));
                schleife.cash = m.group(8);
                convertTo.schleifen.warteschlange.add(schleife);
            }

            String aktuellgebautesSchiff = "<div id=\"Prod\" class=\"cost\">([\\d]+)?(?:x )?<a href=\"javascript:popUp\\('ships\\.phtml\\?sid=[a-zA-Z0-9]+&ship=(\\d+)'";
            Matcher aktuellgebautesSchiffMatcher = Pattern.compile(aktuellgebautesSchiff).matcher(convertFrom);
            if (aktuellgebautesSchiffMatcher.find()) {
                convertTo.schleifen.aktuellesSchiff = new SchiffsbauschleifenPaket();
                convertTo.schleifen.aktuellesSchiff.id = "-1";
                convertTo.schleifen.aktuellesSchiff.sort = -1;
                String menge = aktuellgebautesSchiffMatcher.group(1);
                convertTo.schleifen.aktuellesSchiff.menge = 1;
                convertTo.schleifen.aktuellesSchiff.paket = (menge == null || menge.equals("")) ? 0 : Integer.parseInt(menge);
                convertTo.schleifen.aktuellesSchiff.schiffsTyp = Schiffstyp.values()[Integer.parseInt(aktuellgebautesSchiffMatcher.group(2))];
                convertTo.schleifen.aktuellesSchiff.prozent = 200;
            }

        } catch (ParseException | NumberFormatException e) {
            Log.err(e);
        }

    }

    private void getschleifen(String regex, String convertFrom, ProductionPage convertTo) {

    }

}
