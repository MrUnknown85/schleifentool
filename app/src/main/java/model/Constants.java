/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.model;

import ELschleifentool.model.GebäudeTyp;
import ELschleifentool.model.Ress;
import ELschleifentool.model.ForschungsTyp;
import ELschleifentool.model.TurmTyp;
import ELschleifentool.model.RessTyp;
import ELschleifentool.model.Schiffstyp;
import static java.util.Arrays.asList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String namePattern = "Spielerinformationen[\\W\\w]*?Name<\\/td>[\\W\\w]*?>([\\W\\w]*?)<\\/td>";
    public static final String rassePattern = "Spielerinformationen[\\W\\w]*?Rasse<\\/td>[\\W\\w]*?>([\\W\\w]*?)<\\/td>";
    public static final String allianzPattern = "Spielerinformationen[\\W\\w]*?Allianz<\\/td>[\\W\\w]*?allianz_info\\.phtml\\?name=([\\W\\w]*?)\\&sid=[\\W\\w]*?\">([\\W\\w]*?)<\\/a>";
    public static final String handelsvereinigungPattern = "Spielerinformationen[\\W\\w]*?Handelsvereinigung<\\/td>[\\W\\w]*?allianz_info\\.phtml\\?name=([\\W\\w]*?)\\&sid=[\\W\\w]*?\">([\\W\\w]*?)<\\/a>";
    public static final String ordenPattern = "Spielerinformationen[\\W\\w]*?Orden<\\/td>[\\W\\w]*?allianz_info\\.phtml\\?name=([\\W\\w]*?)\\&sid=[\\W\\w]*?\">([\\W\\w]*?)<\\/a>";
    public static final String syndikatPattern = "Spielerinformationen[\\W\\w]*?Syndikat<\\/td>[\\W\\w]*?allianz_info\\.phtml\\?name=([\\W\\w]*?)\\&sid=[\\W\\w]*?\">([\\W\\w]*?)<\\/a>";
    public static final String mainPattern = "Spielerinformationen[\\W\\w]+?Hauptplanet[\\W\\w]*?<b>[\\W\\w]*?\\((\\d{1,2}:\\d{1,3}:\\d{1,2})\\) in Universum (\\d)<\\/b>";
    public static final String tor1Pattern = "Spielerinformationen[\\W\\w]+?<br>Tor in Universum 1:[\\W\\w]*?\\((\\d{1,2}:\\d{1,3}:\\d{1,2})\\)";
    public static final String tor2Pattern = "Spielerinformationen[\\W\\w]+?<br>Tor in Universum 2:[\\W\\w]*?\\((\\d{1,2}:\\d{1,3}:\\d{1,2})\\)";
    public static final String tor3Pattern = "Spielerinformationen[\\W\\w]+?<br>Tor in Universum 3:[\\W\\w]*?\\((\\d{1,2}:\\d{1,3}:\\d{1,2})\\)";
    public static final String cashPattern = "Spielerinformationen[\\W\\w]*?Cash<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String planiAnzahlPattern = "Spielerinformationen[\\W\\w]*?Planeten<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String planiPunktePattern = "Spielerinformationen[\\W\\w]*?Punkte aller Planeten<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String forschungsPunktePattern = "Spielerinformationen[\\W\\w]*?Forschungspunkte<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String gesamtPunktePattern = "Spielerinformationen[\\W\\w]*?Punkte gesamt<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String highScorePattern = "Spielerinformationen[\\W\\w]*?Highscoreplatz<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String kePattern = "Spielerinformationen[\\W\\w]*?Kampferfahrung<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String heldisPattern = "Spielerinformationen[\\W\\w]*?Heldenpunkte<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String krimisPattern = "Spielerinformationen[\\W\\w]*?Kriminalität<\\/td>[\\W\\w]*?<td>([\\W\\w]*?)<\\/td>";
    public static final String spielerInfoplaniListe = "<tr[\\w\\W]*?><td align=\"center\">\\d+<\\/td><td>(\\d)*<\\/td><td align=\"center\">[\\w\\W]*?(\\d+:\\d+:\\d+)[\\w\\W]*?<td align=\"right\">(\\d+)<\\/td>";

    public static Map<Integer, Map<Schiffstyp, Ress>> prozenzBauRessMap = new HashMap<>();

    public static EnumMap<RessTyp, String> ressNamen = new EnumMap<>(RessTyp.class);

    public static EnumMap<TurmTyp, String> turmNamen = new EnumMap<>(TurmTyp.class);
    public static EnumMap<TurmTyp, List<ForschungsTyp>> turmwaffenForschung = new EnumMap<>(TurmTyp.class);
    public static EnumMap<TurmTyp, ForschungsTyp> turmschildForschung = new EnumMap<>(TurmTyp.class);
    public static EnumMap<TurmTyp, Integer> turmdeffWerte = new EnumMap<>(TurmTyp.class);
    public static EnumMap<TurmTyp, Integer> turmschildWerte = new EnumMap<>(TurmTyp.class);

    public static Map<String, Schiffstyp> shipTypeByName = new HashMap<>();
    public static EnumMap<Schiffstyp, String> schiffsNamen = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, String> schiffsKurzNamen = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, List<ForschungsTyp>> schiffswaffenForschung = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, ForschungsTyp> schiffsschildForschung = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, Integer> schiffsdeffWerte = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, Integer> schiffsattWerte = new EnumMap<>(Schiffstyp.class);
    public static EnumMap<Schiffstyp, Integer> schiffsschildWerte = new EnumMap<>(Schiffstyp.class);

    public static EnumMap<Schiffstyp, Long> schiffsBaseProdSpeedSekunden = new EnumMap<>(Schiffstyp.class);

    public static EnumMap<GebäudeTyp, String> gebäudeNamen = new EnumMap<>(GebäudeTyp.class);
    public static EnumMap<ForschungsTyp, String> forschungsNamen = new EnumMap<>(ForschungsTyp.class);

    static {
        initRess();
        initGebäude();
        initForschung();
        initBauress();
        initTürme();
        initSchiffe();
    }

    private static void initRess() {
        ressNamen.put(RessTyp.EISEN, "Eisen");
        ressNamen.put(RessTyp.TITAN, "Titan");
        ressNamen.put(RessTyp.WASSER, "Wasser");
        ressNamen.put(RessTyp.WASSERSTOFF, "Wasserstoff");
        ressNamen.put(RessTyp.NAHRUNG, "Nahrung");
        ressNamen.put(RessTyp.EINWOHNER, "Einwohner");

    }

    private static void initTürme() {
        turmNamen.put(TurmTyp.IMPULSLASERTURM, "Impulslaserturm");
        turmNamen.put(TurmTyp.HIGSLASERTURM, "HiGS-Laserturm");
        turmNamen.put(TurmTyp.ELEKTRONENKANONE, "Elektronenkanone");
        turmNamen.put(TurmTyp.TACHYONENWERFER, "Tachyonenwerfer");
        turmNamen.put(TurmTyp.FLICKSWERFER, "Flickswerfer");
        turmNamen.put(TurmTyp.RAKETENABSCHUSSRAMPE, "Raketenabschussrampe");
        turmNamen.put(TurmTyp.PLASMAKANONE, "Plasmakanone");
        turmwaffenForschung.put(TurmTyp.IMPULSLASERTURM, asList(ForschungsTyp.IMPULSLASER));
        turmwaffenForschung.put(TurmTyp.HIGSLASERTURM, asList(ForschungsTyp.HIGS));
        turmwaffenForschung.put(TurmTyp.ELEKTRONENKANONE, asList(ForschungsTyp.ELEKTRO));
        turmwaffenForschung.put(TurmTyp.TACHYONENWERFER, asList(ForschungsTyp.TACHYO));
        turmwaffenForschung.put(TurmTyp.FLICKSWERFER, asList(ForschungsTyp.FLICKS));
        turmwaffenForschung.put(TurmTyp.RAKETENABSCHUSSRAMPE, asList(ForschungsTyp.PHOTONENRAKETE));
        turmwaffenForschung.put(TurmTyp.PLASMAKANONE, asList(ForschungsTyp.ELEKTRO, ForschungsTyp.PHOTONENRAKETE));
        turmdeffWerte.put(TurmTyp.IMPULSLASERTURM, 50);
        turmdeffWerte.put(TurmTyp.HIGSLASERTURM, 100);
        turmdeffWerte.put(TurmTyp.ELEKTRONENKANONE, 500);
        turmdeffWerte.put(TurmTyp.TACHYONENWERFER, 800);
        turmdeffWerte.put(TurmTyp.FLICKSWERFER, 1500);
        turmdeffWerte.put(TurmTyp.RAKETENABSCHUSSRAMPE, 3000);
        turmdeffWerte.put(TurmTyp.PLASMAKANONE, 25000);
        turmschildWerte.put(TurmTyp.IMPULSLASERTURM, 100);
        turmschildWerte.put(TurmTyp.HIGSLASERTURM, 250);
        turmschildWerte.put(TurmTyp.ELEKTRONENKANONE, 1000);
        turmschildWerte.put(TurmTyp.TACHYONENWERFER, 1500);
        turmschildWerte.put(TurmTyp.FLICKSWERFER, 4000);
        turmschildWerte.put(TurmTyp.RAKETENABSCHUSSRAMPE, 8000);
        turmschildWerte.put(TurmTyp.PLASMAKANONE, 10000);
    }

    private static void initSchiffe() {
        schiffsNamen.put(Schiffstyp.SONDE, "unbemannte Sonde");
        schiffsNamen.put(Schiffstyp.SNATCHER, "Snatcher");
        schiffsNamen.put(Schiffstyp.RETTUNGSSCHIFF, "Rettungsschiff");
        schiffsNamen.put(Schiffstyp.HANDELSSCHIFF, "Handelsschiff");
        schiffsNamen.put(Schiffstyp.HANDELSRIESE, "Handelsriese");
        schiffsNamen.put(Schiffstyp.AGLIDER, "A-Glider");
        schiffsNamen.put(Schiffstyp.NOULON, "Noulon");
        schiffsNamen.put(Schiffstyp.BOMBER, "schwerer Bomber");
        schiffsNamen.put(Schiffstyp.KOLO, "Kolonisationsschiff");
        schiffsNamen.put(Schiffstyp.TRUGAR, "Trugar");
        schiffsNamen.put(Schiffstyp.VIOLO, "Violo");
        schiffsNamen.put(Schiffstyp.NARUBU, "Narubu");
        schiffsNamen.put(Schiffstyp.NEOMAR, "Neomar");
        schiffsNamen.put(Schiffstyp.BLOODHOUND, "Bloodhound");
        schiffsNamen.put(Schiffstyp.KEMZEN, "Kemzen");
        schiffsNamen.put(Schiffstyp.ZEMAR, "Zemar");
        schiffsNamen.put(Schiffstyp.FINUR, "Finur");
        schiffsNamen.put(Schiffstyp.LUXOR, "Luxor");
        schiffsNamen.put(Schiffstyp.GRANDOR, "Grandor");
        schiffsNamen.put(Schiffstyp.INVA, "Invasionseinheit");
        schiffsNamen.put(Schiffstyp.FUSIONATOR, "Fusionator");

        schiffsNamen.keySet().stream().forEach(type -> {
            shipTypeByName.put(schiffsNamen.get(type), type);
        });

        schiffsKurzNamen.put(Schiffstyp.SONDE, "Son");
        schiffsKurzNamen.put(Schiffstyp.SNATCHER, "Sna");
        schiffsKurzNamen.put(Schiffstyp.RETTUNGSSCHIFF, "Ret");
        schiffsKurzNamen.put(Schiffstyp.HANDELSSCHIFF, "HS");
        schiffsKurzNamen.put(Schiffstyp.HANDELSRIESE, "HR");
        schiffsKurzNamen.put(Schiffstyp.AGLIDER, "A-G");
        schiffsKurzNamen.put(Schiffstyp.NOULON, "Nou");
        schiffsKurzNamen.put(Schiffstyp.BOMBER, "sBo");
        schiffsKurzNamen.put(Schiffstyp.KOLO, "Kol");
        schiffsKurzNamen.put(Schiffstyp.TRUGAR, "Tru");
        schiffsKurzNamen.put(Schiffstyp.VIOLO, "Vio");
        schiffsKurzNamen.put(Schiffstyp.NARUBU, "Nar");
        schiffsKurzNamen.put(Schiffstyp.NEOMAR, "Neo");
        schiffsKurzNamen.put(Schiffstyp.BLOODHOUND, "Blo");
        schiffsKurzNamen.put(Schiffstyp.KEMZEN, "Kem");
        schiffsKurzNamen.put(Schiffstyp.ZEMAR, "Zem");
        schiffsKurzNamen.put(Schiffstyp.FINUR, "Fin");
        schiffsKurzNamen.put(Schiffstyp.LUXOR, "Lux");
        schiffsKurzNamen.put(Schiffstyp.GRANDOR, "Gran");
        schiffsKurzNamen.put(Schiffstyp.INVA, "Inv");
        schiffsKurzNamen.put(Schiffstyp.FUSIONATOR, "Fus");

        schiffswaffenForschung.put(Schiffstyp.SONDE, null);
        schiffswaffenForschung.put(Schiffstyp.RETTUNGSSCHIFF, null);
        schiffswaffenForschung.put(Schiffstyp.HANDELSSCHIFF, null);
        schiffswaffenForschung.put(Schiffstyp.HANDELSRIESE, null);
        schiffswaffenForschung.put(Schiffstyp.KOLO, null);
        schiffswaffenForschung.put(Schiffstyp.FUSIONATOR, null);
        schiffswaffenForschung.put(Schiffstyp.SNATCHER, null);
        schiffswaffenForschung.put(Schiffstyp.INVA, asList(ForschungsTyp.HIGS, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.AGLIDER, asList(ForschungsTyp.IMPULSLASER));
        schiffswaffenForschung.put(Schiffstyp.NOULON, asList(ForschungsTyp.HIGS));
        schiffswaffenForschung.put(Schiffstyp.BOMBER, asList(ForschungsTyp.ATOMSPRENGKOPF));
        schiffswaffenForschung.put(Schiffstyp.TRUGAR, asList(ForschungsTyp.HIGS, ForschungsTyp.WASSERSTOFFRAKETE));
        schiffswaffenForschung.put(Schiffstyp.VIOLO, asList(ForschungsTyp.TACHYO, ForschungsTyp.WASSERSTOFFRAKETE));
        schiffswaffenForschung.put(Schiffstyp.NARUBU, asList(ForschungsTyp.ELEKTRO, ForschungsTyp.WASSERSTOFFRAKETE));
        schiffswaffenForschung.put(Schiffstyp.NEOMAR, asList(ForschungsTyp.ELEKTRO, ForschungsTyp.WASSERSTOFFRAKETE));
        schiffswaffenForschung.put(Schiffstyp.BLOODHOUND, asList(ForschungsTyp.TACHYO, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.KEMZEN, asList(ForschungsTyp.HIGS, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.ZEMAR, asList(ForschungsTyp.TACHYO, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.FINUR, asList(ForschungsTyp.TACHYO, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.LUXOR, asList(ForschungsTyp.FLICKS, ForschungsTyp.PHOTONENRAKETE));
        schiffswaffenForschung.put(Schiffstyp.GRANDOR, asList(ForschungsTyp.FLICKS, ForschungsTyp.PHOTONENRAKETE));

        schiffsschildForschung.put(Schiffstyp.SONDE, null);
        schiffsschildForschung.put(Schiffstyp.RETTUNGSSCHIFF, null);
        schiffsschildForschung.put(Schiffstyp.HANDELSSCHIFF, null);
        schiffsschildForschung.put(Schiffstyp.HANDELSRIESE, ForschungsTyp.PHASENWECHSELSCHILD);
        schiffsschildForschung.put(Schiffstyp.KOLO, ForschungsTyp.ROTATIONSSCHILD);
        schiffsschildForschung.put(Schiffstyp.FUSIONATOR, null);
        schiffsschildForschung.put(Schiffstyp.SNATCHER, null);
        schiffsschildForschung.put(Schiffstyp.INVA, ForschungsTyp.PHASENWECHSELSCHILD);
        schiffsschildForschung.put(Schiffstyp.AGLIDER, ForschungsTyp.ROTATIONSSCHILD);
        schiffsschildForschung.put(Schiffstyp.NOULON, ForschungsTyp.ROTATIONSSCHILD);
        schiffsschildForschung.put(Schiffstyp.BOMBER, ForschungsTyp.QUANTENSCHILD);
        schiffsschildForschung.put(Schiffstyp.TRUGAR, ForschungsTyp.PHASENWECHSELSCHILD);
        schiffsschildForschung.put(Schiffstyp.VIOLO, ForschungsTyp.PHASENWECHSELSCHILD);
        schiffsschildForschung.put(Schiffstyp.NARUBU, ForschungsTyp.PHASENWECHSELSCHILD);
        schiffsschildForschung.put(Schiffstyp.NEOMAR, ForschungsTyp.PLASMASCHILD);
        schiffsschildForschung.put(Schiffstyp.BLOODHOUND, ForschungsTyp.QUANTENSCHILD);
        schiffsschildForschung.put(Schiffstyp.KEMZEN, ForschungsTyp.QUANTENSCHILD);
        schiffsschildForschung.put(Schiffstyp.ZEMAR, ForschungsTyp.PLASMASCHILD);
        schiffsschildForschung.put(Schiffstyp.FINUR, ForschungsTyp.NEODEMSCHILD);
        schiffsschildForschung.put(Schiffstyp.LUXOR, ForschungsTyp.PLASMASCHILD);
        schiffsschildForschung.put(Schiffstyp.GRANDOR, ForschungsTyp.PLASMASCHILD);

        schiffsdeffWerte.put(Schiffstyp.SONDE, 0);
        schiffsdeffWerte.put(Schiffstyp.SNATCHER, 15);
        schiffsdeffWerte.put(Schiffstyp.RETTUNGSSCHIFF, 0);
        schiffsdeffWerte.put(Schiffstyp.HANDELSSCHIFF, 0);
        schiffsdeffWerte.put(Schiffstyp.HANDELSRIESE, 0);
        schiffsdeffWerte.put(Schiffstyp.AGLIDER, 120);
        schiffsdeffWerte.put(Schiffstyp.NOULON, 218);
        schiffsdeffWerte.put(Schiffstyp.BOMBER, 1000);
        schiffsdeffWerte.put(Schiffstyp.KOLO, 0);
        schiffsdeffWerte.put(Schiffstyp.TRUGAR, 1400);
        schiffsdeffWerte.put(Schiffstyp.VIOLO, 1900);
        schiffsdeffWerte.put(Schiffstyp.NARUBU, 1000);
        schiffsdeffWerte.put(Schiffstyp.NEOMAR, 8000);
        schiffsdeffWerte.put(Schiffstyp.BLOODHOUND, 500);
        schiffsdeffWerte.put(Schiffstyp.KEMZEN, 5000);
        schiffsdeffWerte.put(Schiffstyp.ZEMAR, 50000);
        schiffsdeffWerte.put(Schiffstyp.FINUR, 25000);
        schiffsdeffWerte.put(Schiffstyp.LUXOR, 20000);
        schiffsdeffWerte.put(Schiffstyp.GRANDOR, 1800000);
        schiffsdeffWerte.put(Schiffstyp.INVA, 0);
        schiffsdeffWerte.put(Schiffstyp.FUSIONATOR, 0);

        schiffsattWerte.put(Schiffstyp.SONDE, 0);
        schiffsattWerte.put(Schiffstyp.SNATCHER, 0);
        schiffsattWerte.put(Schiffstyp.RETTUNGSSCHIFF, 0);
        schiffsattWerte.put(Schiffstyp.HANDELSSCHIFF, 0);
        schiffsattWerte.put(Schiffstyp.HANDELSRIESE, 0);
        schiffsattWerte.put(Schiffstyp.AGLIDER, 100);
        schiffsattWerte.put(Schiffstyp.NOULON, 218);
        schiffsattWerte.put(Schiffstyp.BOMBER, 1000);
        schiffsattWerte.put(Schiffstyp.KOLO, 0);
        schiffsattWerte.put(Schiffstyp.TRUGAR, 1300);
        schiffsattWerte.put(Schiffstyp.VIOLO, 1600);
        schiffsattWerte.put(Schiffstyp.NARUBU, 1600);
        schiffsattWerte.put(Schiffstyp.NEOMAR, 4000);
        schiffsattWerte.put(Schiffstyp.BLOODHOUND, 10000);
        schiffsattWerte.put(Schiffstyp.KEMZEN, 7500);
        schiffsattWerte.put(Schiffstyp.ZEMAR, 15000);
        schiffsattWerte.put(Schiffstyp.FINUR, 30000);
        schiffsattWerte.put(Schiffstyp.LUXOR, 50000);
        schiffsattWerte.put(Schiffstyp.GRANDOR, 1800000);
        schiffsattWerte.put(Schiffstyp.INVA, 0);
        schiffsattWerte.put(Schiffstyp.FUSIONATOR, 0);

        schiffsschildWerte.put(Schiffstyp.SONDE, 50);
        schiffsschildWerte.put(Schiffstyp.SNATCHER, 100);
        schiffsschildWerte.put(Schiffstyp.RETTUNGSSCHIFF, 400);
        schiffsschildWerte.put(Schiffstyp.HANDELSSCHIFF, 500);
        schiffsschildWerte.put(Schiffstyp.HANDELSRIESE, 1500);
        schiffsschildWerte.put(Schiffstyp.AGLIDER, 300);
        schiffsschildWerte.put(Schiffstyp.NOULON, 500);
        schiffsschildWerte.put(Schiffstyp.BOMBER, 2000);
        schiffsschildWerte.put(Schiffstyp.KOLO, 800);
        schiffsschildWerte.put(Schiffstyp.TRUGAR, 2600);
        schiffsschildWerte.put(Schiffstyp.VIOLO, 3100);
        schiffsschildWerte.put(Schiffstyp.NARUBU, 2000);
        schiffsschildWerte.put(Schiffstyp.NEOMAR, 17000);
        schiffsschildWerte.put(Schiffstyp.BLOODHOUND, 9000);
        schiffsschildWerte.put(Schiffstyp.KEMZEN, 12000);
        schiffsschildWerte.put(Schiffstyp.ZEMAR, 66000);
        schiffsschildWerte.put(Schiffstyp.FINUR, 32000);
        schiffsschildWerte.put(Schiffstyp.LUXOR, 20000);
        schiffsschildWerte.put(Schiffstyp.GRANDOR, 2000000);
        schiffsschildWerte.put(Schiffstyp.INVA, 10000);
        schiffsschildWerte.put(Schiffstyp.FUSIONATOR, 10000);

        schiffsBaseProdSpeedSekunden.put(Schiffstyp.SONDE, zeitInSekunden(0, 0, 5, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.SNATCHER, zeitInSekunden(0, 0, 25, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.RETTUNGSSCHIFF, zeitInSekunden(0, 0, 25, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.HANDELSSCHIFF, zeitInSekunden(0, 0, 30, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.HANDELSRIESE, zeitInSekunden(0, 1, 30, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.AGLIDER, zeitInSekunden(0, 0, 30, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.NOULON, zeitInSekunden(0, 0, 45, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.BOMBER, zeitInSekunden(0, 16, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.KOLO, zeitInSekunden(15, 0, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.TRUGAR, zeitInSekunden(0, 4, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.VIOLO, zeitInSekunden(0, 4, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.NARUBU, zeitInSekunden(0, 5, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.NEOMAR, zeitInSekunden(0, 10, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.BLOODHOUND, zeitInSekunden(0, 12, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.KEMZEN, zeitInSekunden(0, 10, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.ZEMAR, zeitInSekunden(1, 11, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.FINUR, zeitInSekunden(1, 11, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.LUXOR, zeitInSekunden(2, 2, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.GRANDOR, zeitInSekunden(60, 0, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.INVA, zeitInSekunden(30, 0, 0, 0));
        schiffsBaseProdSpeedSekunden.put(Schiffstyp.FUSIONATOR, zeitInSekunden(30, 0, 0, 0));
    }

    private static long zeitInSekunden(int tage, int std, int min, int sek) {
        long val = sek;
        val += (min * 60);
        val += (std * 3600);
        val += (tage * 86400);
        return val;
    }

    public static int getPaketgröße(Schiffstyp typ, long sf, int prozent) {
        long sekunden = schiffsBaseProdSpeedSekunden.get(typ);
        double pr = prozent / 100d;
        double effektiveSf = sf * pr;
        double effektiveSekunden = sekunden / effektiveSf;
        if (effektiveSekunden <= 60) {
            return 1000;
        } else if (effektiveSekunden <= 600) {
            return 100;
        } else {
            return 1;
        }
    }

    public static double getRabatt(Schiffstyp typ, long sf, int prozent) {
        int paketGröße = getPaketgröße(typ, sf, prozent);
        switch (paketGröße) {
            case 1000:
                return 0.95;
            case 100:
                return 0.99;
            default:
                return 1;
        }
    }

    public static double getRabattByPaketGröße(long paketGröße) {
        switch ((int) paketGröße) {
            case 1000:
                return 0.95;
            case 100:
                return 0.99;
            default:
                return 1;
        }
    }

    private static void initBauress() {
        Map<Schiffstyp, Ress> bauRess100Prozent = new HashMap<>();
        bauRess100Prozent.put(Schiffstyp.SONDE, new Ress(25, 100, 0, 0));
        bauRess100Prozent.put(Schiffstyp.SNATCHER, new Ress(1000, 100, 0, 2));
        bauRess100Prozent.put(Schiffstyp.RETTUNGSSCHIFF, new Ress(1500, 150, 0, 4));
        bauRess100Prozent.put(Schiffstyp.HANDELSSCHIFF, new Ress(1000, 150, 0, 10));
        bauRess100Prozent.put(Schiffstyp.HANDELSRIESE, new Ress(10000, 2000, 0, 20));
        bauRess100Prozent.put(Schiffstyp.AGLIDER, new Ress(1000, 50, 0, 10));
        bauRess100Prozent.put(Schiffstyp.NOULON, new Ress(1500, 100, 0, 4));
        bauRess100Prozent.put(Schiffstyp.BOMBER, new Ress(15000, 5000, 0, 20));
        bauRess100Prozent.put(Schiffstyp.KOLO, new Ress(10000, 10000, 0, 100));
        bauRess100Prozent.put(Schiffstyp.TRUGAR, new Ress(5000, 1000, 0, 15));
        bauRess100Prozent.put(Schiffstyp.VIOLO, new Ress(4000, 3000, 0, 20));
        bauRess100Prozent.put(Schiffstyp.NARUBU, new Ress(6000, 2500, 0, 25));
        bauRess100Prozent.put(Schiffstyp.NEOMAR, new Ress(8000, 10000, 0, 140));
        bauRess100Prozent.put(Schiffstyp.BLOODHOUND, new Ress(15000, 3000, 0, 135));
        bauRess100Prozent.put(Schiffstyp.KEMZEN, new Ress(9000, 7000, 0, 130));
        bauRess100Prozent.put(Schiffstyp.ZEMAR, new Ress(20000, 31000, 0, 250));
        bauRess100Prozent.put(Schiffstyp.FINUR, new Ress(28000, 16000, 0, 250));
        bauRess100Prozent.put(Schiffstyp.LUXOR, new Ress(15000, 3000, 0, 250));
        bauRess100Prozent.put(Schiffstyp.GRANDOR, new Ress(1000000, 1000000, 0, 20000));
        bauRess100Prozent.put(Schiffstyp.INVA, new Ress(100000, 100000, 0, 2500));
        prozenzBauRessMap.put(100, bauRess100Prozent);

        Map<Schiffstyp, Ress> bauRess125Prozent = new HashMap<>();
        bauRess125Prozent.put(Schiffstyp.SONDE, new Ress(49, 195, 119, 0));
        bauRess125Prozent.put(Schiffstyp.SNATCHER, new Ress(1953, 195, 1048, 2));
        bauRess125Prozent.put(Schiffstyp.RETTUNGSSCHIFF, new Ress(2930, 293, 1573, 4));
        bauRess125Prozent.put(Schiffstyp.HANDELSSCHIFF, new Ress(1953, 293, 1096, 10));
        bauRess125Prozent.put(Schiffstyp.HANDELSRIESE, new Ress(19530, 3906, 11438, 20));
        bauRess125Prozent.put(Schiffstyp.AGLIDER, new Ress(1953, 98, 1001, 2));
        bauRess125Prozent.put(Schiffstyp.NOULON, new Ress(2930, 195, 1525, 4));
        bauRess125Prozent.put(Schiffstyp.BOMBER, new Ress(29295, 9765, 19063, 20));
        bauRess125Prozent.put(Schiffstyp.KOLO, new Ress(19530, 19530, 19063, 100));
        bauRess125Prozent.put(Schiffstyp.TRUGAR, new Ress(9765, 1953, 5719, 15));
        bauRess125Prozent.put(Schiffstyp.VIOLO, new Ress(7812, 5859, 6672, 20));
        bauRess125Prozent.put(Schiffstyp.NARUBU, new Ress(11718, 4883, 8102, 25));
        bauRess125Prozent.put(Schiffstyp.NEOMAR, new Ress(15624, 19530, 17156, 140));
        bauRess125Prozent.put(Schiffstyp.BLOODHOUND, new Ress(29295, 5859, 17156, 135));
        bauRess125Prozent.put(Schiffstyp.KEMZEN, new Ress(17577, 13671, 15250, 130));
        bauRess125Prozent.put(Schiffstyp.ZEMAR, new Ress(39060, 60543, 48609, 250));
        bauRess125Prozent.put(Schiffstyp.FINUR, new Ress(54684, 31248, 41938, 250));
        bauRess125Prozent.put(Schiffstyp.LUXOR, new Ress(101557, 27342, 62906, 250));
        bauRess125Prozent.put(Schiffstyp.GRANDOR, new Ress(1953125, 1953125, 1906250, 20000));
        bauRess125Prozent.put(Schiffstyp.INVA, new Ress(195302, 195302, 190625, 2500));
        prozenzBauRessMap.put(125, bauRess125Prozent);

        Map<Schiffstyp, Ress> bauRess150Prozent = new HashMap<>();
        bauRess150Prozent.put(Schiffstyp.SONDE, new Ress(84, 338, 297, 0));
        bauRess150Prozent.put(Schiffstyp.SNATCHER, new Ress(3375, 338, 5225, 2));
        bauRess150Prozent.put(Schiffstyp.RETTUNGSSCHIFF, new Ress(5063, 506, 3919, 4));
        bauRess150Prozent.put(Schiffstyp.HANDELSSCHIFF, new Ress(3375, 506, 2731, 10));
        bauRess150Prozent.put(Schiffstyp.HANDELSRIESE, new Ress(33750, 6750, 28500, 20));
        bauRess150Prozent.put(Schiffstyp.AGLIDER, new Ress(3375, 169, 2494, 2));
        bauRess150Prozent.put(Schiffstyp.NOULON, new Ress(5063, 338, 3800, 4));
        bauRess150Prozent.put(Schiffstyp.BOMBER, new Ress(50625, 16875, 47499, 20));
        bauRess150Prozent.put(Schiffstyp.KOLO, new Ress(33750, 33750, 47499, 100));
        bauRess150Prozent.put(Schiffstyp.TRUGAR, new Ress(16875, 3375, 14250, 15));
        bauRess150Prozent.put(Schiffstyp.VIOLO, new Ress(13500, 10125, 16625, 20));
        bauRess150Prozent.put(Schiffstyp.NARUBU, new Ress(20250, 8438, 20187, 25));
        bauRess150Prozent.put(Schiffstyp.NEOMAR, new Ress(27000, 33750, 42749, 140));
        bauRess150Prozent.put(Schiffstyp.BLOODHOUND, new Ress(50625, 10125, 42749, 135));
        bauRess150Prozent.put(Schiffstyp.KEMZEN, new Ress(30375, 23625, 37999, 250));
        bauRess150Prozent.put(Schiffstyp.ZEMAR, new Ress(67500, 104625, 121123, 250));
        bauRess150Prozent.put(Schiffstyp.FINUR, new Ress(94500, 54000, 104498, 250));
        bauRess150Prozent.put(Schiffstyp.LUXOR, new Ress(175500, 47250, 156747, 250));
        bauRess150Prozent.put(Schiffstyp.GRANDOR, new Ress(3375000, 3375000, 4749920, 20000));
        bauRess150Prozent.put(Schiffstyp.INVA, new Ress(337500, 337500, 474992, 2500));
        prozenzBauRessMap.put(150, bauRess150Prozent);

        Map<Schiffstyp, Ress> bauRess175Prozent = new HashMap<>();
        bauRess175Prozent.put(Schiffstyp.SONDE, new Ress(134, 536, 545, 0));
        bauRess175Prozent.put(Schiffstyp.SNATCHER, new Ress(5359, 536, 9591, 2));
        bauRess175Prozent.put(Schiffstyp.RETTUNGSSCHIFF, new Ress(8039, 804, 7193, 4));
        bauRess175Prozent.put(Schiffstyp.HANDELSSCHIFF, new Ress(5359, 804, 5013, 10));
        bauRess175Prozent.put(Schiffstyp.HANDELSRIESE, new Ress(53594, 10719, 52313, 20));
        bauRess175Prozent.put(Schiffstyp.AGLIDER, new Ress(5359, 268, 4577, 2));
        bauRess175Prozent.put(Schiffstyp.NOULON, new Ress(8039, 536, 6975, 4));
        bauRess175Prozent.put(Schiffstyp.BOMBER, new Ress(80391, 26797, 87188, 20));
        bauRess175Prozent.put(Schiffstyp.KOLO, new Ress(53594, 53594, 87188, 100));
        bauRess175Prozent.put(Schiffstyp.TRUGAR, new Ress(26797, 5359, 26156, 15));
        bauRess175Prozent.put(Schiffstyp.VIOLO, new Ress(21438, 16078, 30516, 20));
        bauRess175Prozent.put(Schiffstyp.NARUBU, new Ress(32156, 13398, 37055, 25));
        bauRess175Prozent.put(Schiffstyp.NEOMAR, new Ress(42875, 53594, 78469, 140));
        bauRess175Prozent.put(Schiffstyp.BLOODHOUND, new Ress(80391, 16078, 78469, 135));
        bauRess175Prozent.put(Schiffstyp.KEMZEN, new Ress(48234, 37516, 69750, 250));
        bauRess175Prozent.put(Schiffstyp.ZEMAR, new Ress(107188, 166141, 222328, 250));
        bauRess175Prozent.put(Schiffstyp.FINUR, new Ress(150063, 85750, 191813, 250));
        bauRess175Prozent.put(Schiffstyp.LUXOR, new Ress(278688, 75031, 287719, 250));
        bauRess175Prozent.put(Schiffstyp.GRANDOR, new Ress(5359375, 5359375, 8718750, 20000));
        bauRess175Prozent.put(Schiffstyp.INVA, new Ress(535938, 535938, 871875, 2500));
        prozenzBauRessMap.put(175, bauRess175Prozent);

        Map<Schiffstyp, Ress> bauRess200Prozent = new HashMap<>();
        bauRess200Prozent.put(Schiffstyp.SONDE, new Ress(200, 800, 875, 0));
        bauRess200Prozent.put(Schiffstyp.SNATCHER, new Ress(8000, 800, 7700, 2));
        bauRess200Prozent.put(Schiffstyp.RETTUNGSSCHIFF, new Ress(12000, 1200, 11550, 4));
        bauRess200Prozent.put(Schiffstyp.HANDELSSCHIFF, new Ress(8000, 1200, 8050, 10));
        bauRess200Prozent.put(Schiffstyp.HANDELSRIESE, new Ress(80000, 16000, 84000, 20));
        bauRess200Prozent.put(Schiffstyp.AGLIDER, new Ress(8000, 400, 7350, 2));
        bauRess200Prozent.put(Schiffstyp.NOULON, new Ress(12000, 800, 11200, 4));
        bauRess200Prozent.put(Schiffstyp.BOMBER, new Ress(120000, 40000, 140000, 20));
        bauRess200Prozent.put(Schiffstyp.KOLO, new Ress(80000, 80000, 140000, 100));
        bauRess200Prozent.put(Schiffstyp.TRUGAR, new Ress(40000, 8000, 42000, 15));
        bauRess200Prozent.put(Schiffstyp.VIOLO, new Ress(32000, 24000, 49000, 20));
        bauRess200Prozent.put(Schiffstyp.NARUBU, new Ress(48000, 20000, 59500, 25));
        bauRess200Prozent.put(Schiffstyp.NEOMAR, new Ress(64000, 80000, 126000, 140));
        bauRess200Prozent.put(Schiffstyp.BLOODHOUND, new Ress(120000, 24000, 126000, 135));
        bauRess200Prozent.put(Schiffstyp.KEMZEN, new Ress(72000, 56000, 112000, 130));
        bauRess200Prozent.put(Schiffstyp.ZEMAR, new Ress(160000, 248000, 357000, 250));
        bauRess200Prozent.put(Schiffstyp.FINUR, new Ress(224000, 128000, 308000, 250));
        bauRess200Prozent.put(Schiffstyp.LUXOR, new Ress(416000, 112000, 462000, 250));
        bauRess200Prozent.put(Schiffstyp.GRANDOR, new Ress(8000000, 8000000, 14000000, 20000));
        bauRess200Prozent.put(Schiffstyp.INVA, new Ress(800000, 800000, 1400000, 2500));
        prozenzBauRessMap.put(200, bauRess200Prozent);
    }

    private static void initGebäude() {
        gebäudeNamen.put(GebäudeTyp.HAUPTQUARTIER, "Hauptquartier");
        gebäudeNamen.put(GebäudeTyp.BIOZELLE, "Biozelle");
        gebäudeNamen.put(GebäudeTyp.BUNKER, "Bunker");
        gebäudeNamen.put(GebäudeTyp.FARM, "Farm");
        gebäudeNamen.put(GebäudeTyp.EISENMINE, "Eisenmine");
        gebäudeNamen.put(GebäudeTyp.TITANMINE, "Titanmine");
        gebäudeNamen.put(GebäudeTyp.BOHRTURM, "Bohrturm");
        gebäudeNamen.put(GebäudeTyp.CHEMIEFABRIK, "Chemiefabrik");
        gebäudeNamen.put(GebäudeTyp.RECYCLINGCENTER, "Recylingcenter");
        gebäudeNamen.put(GebäudeTyp.NAHRUNGSSILO, "Nahrungssilo");
        gebäudeNamen.put(GebäudeTyp.EISENSPEICHER, "Eisenspeicher");
        gebäudeNamen.put(GebäudeTyp.TITANSPEICHER, "Titanspeicher");
        gebäudeNamen.put(GebäudeTyp.WASSERSPEICHER, "Wasserspeicher");
        gebäudeNamen.put(GebäudeTyp.WASSERSTOFFSPEICHER, "Wasserstoffspeicher");
        gebäudeNamen.put(GebäudeTyp.UNIVERSITÄT, "Universität");
        gebäudeNamen.put(GebäudeTyp.VERGNÜGUNGSZENTRUM, "Vergnügungszentrum");
        gebäudeNamen.put(GebäudeTyp.SCHIFFSFABRIK, "Schiffsfabrik");
        gebäudeNamen.put(GebäudeTyp.VERTEIDIGUNGSSTATION, "Verteidigungsstation");
        gebäudeNamen.put(GebäudeTyp.FLOTTENKONTROLLZENTRUM, "Flottenkontrollzentrum");
        gebäudeNamen.put(GebäudeTyp.SCHILDGENERATOR, "Schildgenerator");
        gebäudeNamen.put(GebäudeTyp.WELTRAUMHAFEN, "Intergalaktischer Weltraumhafen");
        gebäudeNamen.put(GebäudeTyp.PALAST, "Palast");
        gebäudeNamen.put(GebäudeTyp.KERNFORSCHUNGSZENTRUM, "Kernforschungszentrum");
        gebäudeNamen.put(GebäudeTyp.STATUE, "Statue des Imperators");
    }

    private static void initForschung() {
        forschungsNamen.put(ForschungsTyp.FESTSTOFF, "Feststoffantrieb");
        forschungsNamen.put(ForschungsTyp.IONEN, "Ionenantrieb");
        forschungsNamen.put(ForschungsTyp.TESONEN, "Tesonenantrieb");
        forschungsNamen.put(ForschungsTyp.VERZERRER, "Verzerrungsantrieb");
        forschungsNamen.put(ForschungsTyp.IMPULSLASER, "Impulslaserkanone");
        forschungsNamen.put(ForschungsTyp.HIGS, "HiGS-Laserkanone");
        forschungsNamen.put(ForschungsTyp.ELEKTRO, "Elektronenkanone");
        forschungsNamen.put(ForschungsTyp.TACHYO, "Tachyonenwerfer");
        forschungsNamen.put(ForschungsTyp.FLICKS, "Flickswerfer");
        forschungsNamen.put(ForschungsTyp.WASSERSTOFFRAKETE, "Wasserstoffrakete");
        forschungsNamen.put(ForschungsTyp.PHOTONENRAKETE, "Photonenrakete");
        forschungsNamen.put(ForschungsTyp.ATOMSPRENGKOPF, "Atomsprengkopf");
        forschungsNamen.put(ForschungsTyp.ROTATIONSSCHILD, "Rotationsschild");
        forschungsNamen.put(ForschungsTyp.PHASENWECHSELSCHILD, "Phasenwechselschild");
        forschungsNamen.put(ForschungsTyp.QUANTENSCHILD, "Quantenschild");
        forschungsNamen.put(ForschungsTyp.NEODEMSCHILD, "Neodemschild");
        forschungsNamen.put(ForschungsTyp.PLASMASCHILD, "Plasmaschild");
        forschungsNamen.put(ForschungsTyp.RADARTECH, "Radartechnologie");
        forschungsNamen.put(ForschungsTyp.SPIO, "Spionagetechnologie");
        forschungsNamen.put(ForschungsTyp.ORBITALTRANSMITTER, "Orbitaltransmitter");
        forschungsNamen.put(ForschungsTyp.PLANITECH, "Planetentechnologie");
        forschungsNamen.put(ForschungsTyp.POLI, "Politik");
        forschungsNamen.put(ForschungsTyp.HANDELSMACHT, "Handelsmacht");
    }

    public static String getSchiffsName(Schiffstyp typ) {
        return schiffsNamen.get(typ);
    }

    public static Schiffstyp getTyp(int index) {
        switch (index) {
            case 0:
                return Schiffstyp.SONDE;
            case 1:
                return Schiffstyp.SNATCHER;
            case 2:
                return Schiffstyp.RETTUNGSSCHIFF;
            case 3:
                return Schiffstyp.HANDELSSCHIFF;
            case 4:
                return Schiffstyp.HANDELSRIESE;
            case 5:
                return Schiffstyp.AGLIDER;
            case 6:
                return Schiffstyp.NOULON;
            case 7:
                return Schiffstyp.BOMBER;
            case 8:
                return Schiffstyp.KOLO;
            case 9:
                return Schiffstyp.TRUGAR;
            case 10:
                return Schiffstyp.VIOLO;
            case 11:
                return Schiffstyp.NARUBU;
            case 12:
                return Schiffstyp.NEOMAR;
            case 13:
                return Schiffstyp.BLOODHOUND;
            case 14:
                return Schiffstyp.KEMZEN;
            case 15:
                return Schiffstyp.ZEMAR;
            case 16:
                return Schiffstyp.FINUR;
            case 17:
                return Schiffstyp.LUXOR;
            case 18:
                return Schiffstyp.GRANDOR;
            case 19:
                return Schiffstyp.INVA;
            case 20:
                return Schiffstyp.FUSIONATOR;
            default:
                throw new IllegalArgumentException("Ungültiger Wert");
        }
    }

}
