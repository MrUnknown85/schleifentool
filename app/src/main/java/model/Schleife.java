/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.model;

import java.util.Date;

/**
 *
 * @author nico
 */
public class Schleife {

    public Schleife(Date endDate, String inhalt) {
        this.endDate = endDate;
        this.inhalt = inhalt;
    }

    public Date endDate;
    public String inhalt;
}
