/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.SchiffsbauschleifenPaket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nico
 */
public class Bauschleifen {

    public SchiffsbauschleifenPaket aktuellesSchiff;
    public List<SchiffsbauschleifenPaket> warteschlange = new ArrayList<>();
}
