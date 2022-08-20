package ELschleifentool.util;

import ELschleifentool.model.MyPlani;
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
public class KoordsComparator implements Comparator<MyPlani> {

    @Override
    public int compare(MyPlani o1, MyPlani o2) {
        return o1.compareTo(o2);
    }
}
