/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import model.IntroPage;
import util.Log;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Nico
 */
public class IntroPageAdapter extends BotschutzAdapter<IntroPage> {

    public final SimpleDateFormat uhrzeitFormat = new SimpleDateFormat("HH:mm:ss");
    public String sidPattern = "sid=([a-zA-z0-9]{26})";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss.SSS");
    public static long start = System.currentTimeMillis();
    private int seconds = -1;

    @Override
    public IntroPage provideInstance() {
        return new IntroPage();
    }

    @Override
    public void convertType(String convertFrom, IntroPage convertTo) throws IOException {
        if (convertFrom.contains("<h1>IP Adresse abgelaufen!")) {
            convertTo.errorMessage = "IP adresse abgelaufen";
        } else if (convertFrom.contains("<h1>Nicht eingeloggt!</h1>")) {
            convertTo.errorMessage = "Nicht eingeloggt!";
        } else {
            Matcher sidMatcher = Pattern.compile(sidPattern).matcher(convertFrom);
            if (sidMatcher.find()) {
                convertTo.sid = sidMatcher.group(1);
            }

            String pattern = "user_info\\.phtml\\?name=([\\W\\w]+?)&sid=[A-Za-z0-9]+\">([\\W\\w]+?)<\\/a>";
            Matcher userNameMatcher = Pattern.compile(pattern).matcher(convertFrom);
            if (userNameMatcher.find()) {
                convertTo.userInfoName = userNameMatcher.group(1);
                convertTo.username = userNameMatcher.group(2);
            }
            Matcher zeitMatcher = Pattern.compile("Zeitrechnung der Erde: (\\d+:\\d+:\\d+)").matcher(convertFrom);
            long end = System.currentTimeMillis();
            if (zeitMatcher.find()) {
                String zeit = zeitMatcher.group(1);
                int aktSeconds = Integer.parseInt(zeit.split(":")[2]);
                if (seconds == -1) {
                    seconds = aktSeconds;
                    // System.out.println(zeit + " = startzeit");
//                    getTime(callback);
                } else if (seconds == aktSeconds) {
                    // System.out.println(zeit + " = zwischenzeit");
//                    getTime(callback);
                } else if (seconds + 1 == aktSeconds) {
                    seconds = -1;
                    //  System.out.println(zeit + " = zeit sekundenwechsel");
                    Calendar currentDate = Calendar.getInstance();
                    int day = currentDate.get(Calendar.DAY_OF_MONTH);
                    int month = currentDate.get(Calendar.MONTH);
                    int year = currentDate.get(Calendar.YEAR);
                    Matcher work = Pattern.compile("Ausf&uuml;hrungszeit: ([\\d.]+)").matcher(convertFrom);
                    //   System.out.println(sdf.format(new Date(start)) + " = start " + start);
                    //  System.out.println(sdf.format(new Date(end)) + " = end " + end);
                    //   System.out.println(sdf.format(new Date(end - start)) + " = end-start " + (end - start));
                    if (work.find()) {
                        //       System.out.println(work.group(1) + " = Ausführungszeit");
                    }
                    try {
                        currentDate.setTime(uhrzeitFormat.parse(zeit));
                    } catch (ParseException ex) {
                        Log.err(ex);
                    }
                    currentDate.set(Calendar.DAY_OF_MONTH, day);
                    currentDate.set(Calendar.MONTH, month);
                    currentDate.set(Calendar.YEAR, year);

//                    end - start
//                    callback.timeSync(seconds, -300);
                } else {
//                    callback.fehler();
                }
            } else {
//                callback.fehler();
            }
        }

    }

}
