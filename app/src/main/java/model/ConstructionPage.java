package ELschleifentool.model;

import java.util.ArrayList;
import java.util.List;


public class ConstructionPage extends BotschutzResponse {
    public List<ConstructionPageItem> constructionItemList = new ArrayList<>();
    public boolean nahrungKnapp;
    public boolean wasserKnapp;
    public String constructionKey;

    public String weiterText = "";
    public String zurueckText = "";
    public String durchschalten = "";

    public String weiterPlaniId;
    public String zurueckPlaniId;

    public String currentKoords;
    public int durchschaltenMode;

    public boolean hasCancelQuestion = false;
    public String cancelQuestion;
    public String cancelWhat;

    public String getPrevText() {
        if (zurueckText.equals("")) {
            return "";
        } else {
            return "⇐\nzurück zu\n" + zurueckText;
        }

    }

    public String getNextText() {
        if (weiterText.equals("")) {
            return "";
        } else {
            return "⇒\nweiter zu\n" + weiterText;
        }
    }

}
