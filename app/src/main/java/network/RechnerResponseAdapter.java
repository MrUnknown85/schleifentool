/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nico
 */
public class RechnerResponseAdapter extends BotschutzAdapter<RechnerResponse> {

    @Override
    public RechnerResponse provideInstance() {
        return new RechnerResponse();
    }

    @Override
    public void convertType(String convertFrom, RechnerResponse convertTo) throws IOException {
        Matcher kosten = Pattern.compile("(Baukosten für Stufe [\\d.]+: [\\d.]+ Eisen - [\\d.]+ Titan - [\\d.]+ Tage, \\d{2}:\\d{2}:\\d{2})<br>").matcher(convertFrom);
        if (kosten.find()) {
            convertTo.kosten = kosten.group(1);
        }

    }

}
