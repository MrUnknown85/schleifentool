/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

import model.BotschutzResponse;
import model.NavigationPage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import okhttp3.ResponseBody;
import org.apache.commons.text.StringEscapeUtils;
import retrofit2.Converter;

/**
 *
 * @author nico
 * @param <T>
 */
public abstract class BotschutzAdapter<T extends BotschutzResponse> implements Converter<ResponseBody, BotschutzResponse> {

    private static final String SID_PATTERN = "sid=([a-zA-z0-9]{26})";
    private static final String CAPTCHA_PATTERN = "src=\\\"captcha\\.php\\?[\\W\\w]*?id=([\\d]+)(?:\\\"|&)";

    public abstract T provideInstance();

    public abstract void convertType(String convertFrom, T convertTo) throws IOException;

    @Override
    public T convert(ResponseBody response) throws IOException {
        String str1 = response.string();
        String responseStr = StringEscapeUtils.unescapeHtml4(str1);
        T r = provideInstance();
        boolean daneben = responseStr.contains("Das war leider daneben. Bitte versuchen Sie es erneut!");
        if (daneben) {
//                    BufferedImage lastShownBotschutz = ELTools.lastShownBotschutz;
//            try {
//                File outputfile = new File(ELTools.failedCaptchas, getHash(lastShownBotschutz) + ".png");
//                ImageIO.write(lastShownBotschutz, "png", outputfile);
//            } catch (Exception e) {
//            }

        }
        r.blocked = responseStr.contains("Sie haben diese Seite mehrmals hintereinander aufgerufen, sie wird allerdings im Hintergrund noch verarbeitet. Um einen Fehler zu vermeiden, wurde Ihre Formulareingabe bzw. Ihr Seitenaufruf blockiert");
        r.ausgeloggt = responseStr.contains("<h1>Nicht eingeloggt!</h1>");
        r.ipAbgelaufen = responseStr.contains("<h1>IP Adresse abgelaufen!");
        Matcher botschutzMatcher = getBotschutzMatcher(responseStr);
        Matcher sidMatcher = Pattern.compile(SID_PATTERN).matcher(responseStr);
        if (sidMatcher.find()) {
            r.sid = sidMatcher.group(1);
        } else {
            r.sid = "";
        }
         if (!(r instanceof NavigationPage)) {
            r.planiListStates = NavigationPageAdapter.getPlaniListStates(responseStr);
        }
        
        boolean hasBotschutz = botschutzMatcher.find();
        if (!hasBotschutz) {
            convertType(responseStr, r);
        } else if (daneben || hasBotschutz) {
            r.captchaId = botschutzMatcher.group(1);
            r.hasBotschutz = true;
        }
        return r;
    }

    private Matcher getBotschutzMatcher(String resultStr) {
        return Pattern.compile(CAPTCHA_PATTERN).matcher(resultStr);
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
    private String getHash(BufferedImage image) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] data = outputStream.toByteArray();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        byte[] hash = md.digest();
        return returnHex(hash);
    }

    static String returnHex(byte[] inBytes) throws Exception {
        String hexString = "";
        for (int i = 0; i < inBytes.length; i++) { //for loop ID:1
            hexString += Integer.toString((inBytes[i] & 0xff) + 0x100, 16).substring(1);
        }                                   // Belongs to for loop ID:1
        return hexString;
    }
}
