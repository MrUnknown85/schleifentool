/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;


public class Event implements Serializable {
    @SerializedName("date")
    public String date = "";
    public Date parsed;

    public long initialTimestamp = System.currentTimeMillis();

    @SerializedName("remainingTime")
    public String initialRemainingTime = "";

    public long initialMillisLeft = 0;

    @SerializedName("zeit")
    public String zeit = "";

    @SerializedName("type")
    public String type = "";

    @SerializedName("title")
    public String title = "";

    @SerializedName("spioId")
    public String spioId = "";

    //Flotten zurückrufen
    public Integer cancel = null;

    //kolo zurückrufen
    public Integer redraw = null;
    

    private boolean isSelected;


    public Event() {

    }

    public String getId() {
        return date + "_" + type + "_" + title + "_" + spioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return date.equals(event.date) &&
                type.equals(event.type) &&
                title.equals(event.title) &&
                spioId.equals(event.spioId) &&
                initialRemainingTime.equals(event.initialRemainingTime);
    }


    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean value) {
        this.isSelected = value;
    }


}
