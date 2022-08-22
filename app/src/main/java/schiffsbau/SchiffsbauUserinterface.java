/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package schiffsbau;

import model.Schiffstyp;
import java.util.List;

/**
 *
 * @author nico
 */
public interface SchiffsbauUserinterface {

    /**
     * refresh the userInterface
     */
    public void notifyDataChanged();

    /**
     *
     * @return Schleifen nur füllen wenn sie in weniger als der angegebenen
     * Anzahl an Tagen leer sind
     */
    public int getSchleifenMaxTage();

    /**
     *
     * @return aktiviert oder deaktiviert die Option dass Schleifen nur gefüllt
     * werden wenn mehr als "getSchleifenEinwohnerMin()" Einwohner auf dem
     * Planeten vorhanden sind
     */
    public boolean optionEinwohnerLeerSelected();

    /**
     *
     * @return Schleifen nur füllen wenn mehr als x Einwohner auf dem Planeten
     * vorhanden sind
     */
    public int getSchleifenEinwohnerMin();

    /**
     *
     * @return Die Anzahl wieviele Pakete oder einzelne Schiffe gebaut werden
     * sollen
     */
    public String getSchleifenFüllMenge();

    List<Schiffstyp> getShipOrder();

    public int cmb_schleifen_einwohner_min_prozent_anz_selected_index();

    public int cmb_schleifen_max_tage_stunden_selected_index();

    public boolean cb_option_schleife_leer_selected();

    public String cmb_prozent_selected_item();

    public int cmb_paket_selected_index();

    public int cmb_schleifen_fill_art_selected_index();

    public String cmb_schiffstyp_selected_item();

    public int cmb_schiffstyp_selected_index();

    public int[] jtableShipBuild_selected_rows();

    public boolean rb_fill_all_planis_selected();

    public boolean sortByPercentageFirst();

    public boolean siedlerSchleifenIgnorieren();

    public boolean löscheAktuell();

}
