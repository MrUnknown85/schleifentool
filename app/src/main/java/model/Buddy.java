/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author nico
 */
public class Buddy implements Serializable {
    @SerializedName("online")
    public boolean online;

    @SerializedName("name")
    public String name;

    @SerializedName("zeit")
    public String zeit;

    @SerializedName("from")
    public boolean from;

    @SerializedName("to")
    public boolean to;

}

