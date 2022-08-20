/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.model;

import java.awt.image.BufferedImage;

/**
 *
 * @author nico
 */
public class BotschutzParams {

    public int x;
    public int y;
    public String sid;
    public String captchaId;
    public String hash;
    public boolean solved = false;
    public BufferedImage image;
    public String title = "TestTitle";

    public BotschutzParams(String sid, String captchaId) {
        this.sid = sid;
        this.captchaId = captchaId;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }

}
