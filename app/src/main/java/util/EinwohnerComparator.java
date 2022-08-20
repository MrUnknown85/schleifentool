package ELschleifentool.util;

import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nico
 */
public class EinwohnerComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        long n1 = 0;
        if (!o1.equals("")) {
            n1 = Long.parseLong(o1.split(" ")[0].replace(".", ""));
        }

        long n2 = 0;
        if (!o2.equals("")) {
            n2 = Long.parseLong(o2.split(" ")[0].replace(".", ""));
        }
        long n = n1 - n2;
        if (n > 1) {
            return 1;
        } else if (n < 1) {
            return -1;
        } else {
            return 0;
        }
    }
}
