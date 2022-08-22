/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

import model.ConstructionPage;
import model.ConstructionPageItem;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nico
 */
public class ConstructionPageAdapter extends BotschutzAdapter<ConstructionPage> {

    Pattern nahrungPattern = Pattern.compile("var nahrungknapp=(\\d)");
    Pattern wasserPattern = Pattern.compile("var wasserknapp=(\\d)");
    Pattern haseisenPattern = Pattern.compile("haseisen=(\\d+)");
    Pattern hastitanPattern = Pattern.compile("hastitan=(\\d+)");
    Pattern haswasserstoffPattern = Pattern.compile("haswasserstoff=(\\d+)");
    Pattern hascashPattern = Pattern.compile("hascash=(\\d+)");
    Pattern weiterPattern = Pattern.compile("planetindex=(\\d+)&sid=[a-zA-Z0-9]{26}\" class=\"smalllink\">[\\w\\W]{1,3}<br>weiter zu<br>((?:\\d{1,2}:\\d{1,3}:\\d{1,2}))<br>");
    Pattern zurueckPattern = Pattern.compile("<a href=\"\\?planetindex=(\\d+)&sid=[a-zA-Z0-9]+\" class=\"smalllink\">[\\w\\W]{1,3}<br>zurück zu<br>((?:\\d{1,2}:\\d{1,3}:\\d{1,2}))<br>");
    Pattern durchschaltenPattern = Pattern.compile("<a href=\"\\?switch=(1|0)&sid=[a-zA-Z0-9]+\"[\\W\\w]+>((?:erweitertes|normales) Durchschalten)</");
    Pattern currentKoordsPattern = Pattern.compile("div class=\"resource_header2\"[\\W\\w]*?<td align=\"right\">[\\W\\w]*?(\\d{1,2}:\\d{1,3}:\\d{1,2})[\\W\\w]*?</td>");
    Pattern cancelPattern = Pattern.compile("(Möchten Sie den aktuellen Gebäudebau wirklich abbrechen\\?<br><br>Fertigstellung: \\d+%)<form action=\"\\?cancel=(\\d)\"");
    Pattern constructionPattern = Pattern.compile("gebaeude\\((\\d+), '<a href[\\w\\W]+?>([\\w\\W]+?)<\\/a>',([\\w\\W]+?)', ([\\d.]+), ([\\d.]+), ([\\d.]+), ([-\\d.]+), ([-\\d.]*), '(?:<span style=\"[\\w\\W]*?\">)?([\\w\\W]*?)(?:<\\/span> \\(in Auftrag\\))?', ([\\d.]+), ([\\w\\W]*?), ([\\d.]+), ([\\d.]+), '(?:<span style=\"[\\w\\W]*?\">)?([\\w\\W]*?)(?:<\\/span> \\(in Auftrag\\))?'\\);");

    Pattern obfusPattern = Pattern.compile("return output;\\} eval\\([\\W\\w]+?\\('([\\W\\w]+?)'\\)\\);<\\/script>");
    Pattern keyPattern = Pattern.compile("name=\"key\" value=\"([\\W\\w]+?)\">'\\+end;");

    @Override
    public ConstructionPage provideInstance() {
        return new ConstructionPage();
    }

    @Override
    public void convertType(String convertFrom, ConstructionPage convertTo) throws IOException {
        convertTo.nahrungKnapp = nahrungPattern.matcher(convertFrom).find();
        convertTo.wasserKnapp = wasserPattern.matcher(convertFrom).find();

        long hasEisen = 0L;
        long hasTitan = 0L;
        long hasWasserstoff = 0L;
        long hasCash = 0L;

        Matcher eisenMatcher = haseisenPattern.matcher(convertFrom);
        if (eisenMatcher.find()) {
            hasEisen = Long.valueOf(eisenMatcher.group(1));
        }
        Matcher titanMatcher = hastitanPattern.matcher(convertFrom);
        if (titanMatcher.find()) {
            hasTitan = Long.valueOf(titanMatcher.group(1));
        }
        Matcher wasserstoffMatcher = haswasserstoffPattern.matcher(convertFrom);
        if (wasserstoffMatcher.find()) {
            hasWasserstoff = Long.valueOf(wasserstoffMatcher.group(1));
        }
        Matcher cashMatcher = hascashPattern.matcher(convertFrom);
        if (cashMatcher.find()) {
            hasCash = Long.valueOf(cashMatcher.group(1));
        }

        Matcher weiterMatcher = weiterPattern.matcher(convertFrom);
        if (weiterMatcher.find()) {
            convertTo.weiterPlaniId = weiterMatcher.group(1);
            convertTo.weiterText = weiterMatcher.group(2);
        }
        Matcher zurueckMatcher = zurueckPattern.matcher(convertFrom);
        if (zurueckMatcher.find()) {
            convertTo.zurueckPlaniId = zurueckMatcher.group(1);
            convertTo.zurueckText = zurueckMatcher.group(2);
        }
        Matcher durchschaltenMatcher = durchschaltenPattern.matcher(convertFrom);
        if (durchschaltenMatcher.find()) {
            convertTo.durchschaltenMode = Integer.valueOf(durchschaltenMatcher.group(1));
            convertTo.durchschalten = durchschaltenMatcher.group(2);
        }
        Matcher currentKoordsMatcher = currentKoordsPattern.matcher(convertFrom);
        if (currentKoordsMatcher.find()) {
            convertTo.currentKoords = currentKoordsMatcher.group(1);
        }

        Matcher cancelMatcher = cancelPattern.matcher(convertFrom);
        convertTo.hasCancelQuestion = cancelMatcher.find();
        if (convertTo.hasCancelQuestion) {
            convertTo.cancelQuestion = cancelMatcher.group(1);
            convertTo.cancelWhat = cancelMatcher.group(2);
        }

        Matcher constructionMatcher = constructionPattern.matcher(convertFrom);
        while (constructionMatcher.find()) {

            ConstructionPageItem item = new ConstructionPageItem();
            try {
                item.num = Integer.valueOf(constructionMatcher.group(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            item.name = constructionMatcher.group(2);
            item.description = constructionMatcher.group(3);

            try {
                item.currentLevel = Integer.valueOf(constructionMatcher.group(4));
            } catch (Exception e) {
                e.printStackTrace();
            }

            item.fe_needed = constructionMatcher.group(5);
            item.ti_needed = constructionMatcher.group(6);
            item.doBuild = Integer.valueOf(constructionMatcher.group(7));
            item.timeToBuild = constructionMatcher.group(8);
            item.needsTime = constructionMatcher.group(9);
            item.compact = constructionMatcher.group(10);
            if (constructionMatcher.group(11).equals("nahrungknapp") || constructionMatcher.group(11).equals("wasserknapp")) {
                item.levelTextColorHex = "FF0000";
            } else {
                item.levelTextColorHex = "FFFFFF";
            }
            item.next_fe = constructionMatcher.group(12);
            item.next_ti = constructionMatcher.group(13);
            item.next_time = constructionMatcher.group(14);
            item.hasEisen = hasEisen;
            item.hasTitan = hasTitan;

//            item.button1Visible.set(item.doBuild == "-1" || item.doBuild == "-2");
//            item.button2Visible.set(item.timeToBuild != "-1" || item.doBuild == "1");
//            switch (item.doBuild) {
//                case "-1" : item.buttonAuftragText.set("Bauauftrag"); break;
//                case  "-2" : item.buttonAuftragText.set("Auftrag abbrechen"); break;
//                default :item.buttonAuftragText.set("");
//            }
//
//            when {
//                item.timeToBuild != "-1" -> item.buttonBuildText.set("abbrechen");
//                item.doBuild == "1" -> item.buttonBuildText.set("bauen");
//                else -> item.buttonBuildText.set("");
//            }
            convertTo.constructionItemList.add(item);
        }
//
//        $this->getPlaniStates($html, $result);
//

        Matcher obfusMatcher = obfusPattern.matcher(convertFrom);
        if (obfusMatcher.find()) {
            String functionParam = obfusMatcher.group(1);
            String resolved = obfus(functionParam);
            Matcher keyMatcher = keyPattern.matcher(resolved);
            if (keyMatcher.find()) {
                convertTo.constructionKey = keyMatcher.group(1);
            } else {
                convertTo.constructionKey = null;
            }
        } else {
            convertTo.constructionKey = null;
        }
    }

    private String obfus(String input) {
        String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String output = "";
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        String regex = "\"[^A-Za-z0-9+/=]\"";
        input = input.replaceAll(regex, "");
        do {
            enc1 = keyStr.indexOf(input.charAt(i++));
            enc2 = keyStr.indexOf(input.charAt(i++));
            enc3 = keyStr.indexOf(input.charAt(i++));
            enc4 = keyStr.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + Character.toString((char) chr1);
            if (enc3 != 64) {
                output = output + Character.toString((char) chr2);
            }
            if (enc4 != 64) {
                output = output + Character.toString((char) chr3);
            }
        } while (i < input.length());
        return output;

    }
}
