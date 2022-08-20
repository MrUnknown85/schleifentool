package ELschleifentool.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nico
 */
public class DauerComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        int n1;
        int n2;
        Pattern dayHour = Pattern.compile("(\\d+)d (\\d+)h");
        Pattern hour = Pattern.compile("(\\d+)h");

        Matcher dayHour1 = dayHour.matcher(o1);
        Matcher dayHour2 = dayHour.matcher(o2);

        Matcher hour1 = hour.matcher(o1);
        Matcher hour2 = hour.matcher(o2);

        if (dayHour1.matches()) {
            n1 = Integer.parseInt(dayHour1.group(1)) * 24 + Integer.parseInt(dayHour1.group(2));
        } else if (hour1.matches()) {
            n1 = Integer.parseInt(hour1.group(1));
        } else {
            n1 = 0;
        }
        if (dayHour2.matches()) {
            n2 = Integer.parseInt(dayHour2.group(1)) * 24 + Integer.parseInt(dayHour2.group(2));
        } else if (hour2.matches()) {
            n2 = Integer.parseInt(hour2.group(1));
        } else {
            n2 = 0;
        }
        return n1 - n2;
    }
}
