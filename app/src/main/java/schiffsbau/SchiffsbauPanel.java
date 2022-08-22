/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package schiffsbau;

import schiffsbau.SchiffsbauTool;
import util.MyIntFilter;
import util.PathCellRenderer;
import util.TransparentListCellRenderer;
import util.DauerComparator;
import util.EinwohnerComparator;
import util.KoordsComparator;
import model.MyPlani;
import model.Schiffstyp;
import network.Repository;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import static javax.swing.event.RowSorterEvent.Type.SORTED;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author nico
 */
public class SchiffsbauPanel extends javax.swing.JPanel implements SchiffsbauUserinterface, DocumentListener {

    private AbstractTableModel tableModel;
    private SchiffsbauTool tool;

    @Override
    public void notifyDataChanged() {
        jTableShipBuild.invalidate();
        jTableShipBuild.repaint();
    }

    @Override
    public int getSchleifenMaxTage() {
        if (edt_schleifen_max_tage.getText().equals("")) {
            return 0;
        } else {
            return Integer.parseInt(edt_schleifen_max_tage.getText());
        }
    }

    @Override
    public boolean optionEinwohnerLeerSelected() {
        return cb_option_einwohner_leer.isSelected();
    }

    @Override
    public int getSchleifenEinwohnerMin() {
        if (edt_schleifen_einwohner_min.getText().equals("")) {
            return 0;
        } else {
            return Integer.parseInt(edt_schleifen_einwohner_min.getText());
        }
    }

    @Override
    public String getSchleifenFüllMenge() {
        return edt_menge.getText();
    }

    @Override
    public List<Schiffstyp> getShipOrder() {
        return dragDropList1.getOrder();
    }

    @Override
    public int cmb_schleifen_einwohner_min_prozent_anz_selected_index() {
        return cmb_schleifen_einwohner_min_prozent_anz.getSelectedIndex();
    }

    @Override
    public int cmb_schleifen_max_tage_stunden_selected_index() {
        return cmb_schleifen_max_tage_stunden.getSelectedIndex();
    }

    @Override
    public boolean cb_option_schleife_leer_selected() {
        return cb_option_schleife_leer.isSelected();
    }

    @Override
    public String cmb_prozent_selected_item() {
        return (String) cmb_prozent.getSelectedItem();
    }

    @Override
    public int cmb_paket_selected_index() {
        return cmb_paket.getSelectedIndex();
    }

    @Override
    public int cmb_schleifen_fill_art_selected_index() {
        return cmb_schleifen_fill_art.getSelectedIndex();
    }

    @Override
    public String cmb_schiffstyp_selected_item() {
        return (String) cmb_schiffstyp.getSelectedItem();
    }

    @Override
    public int cmb_schiffstyp_selected_index() {
        return cmb_schiffstyp.getSelectedIndex();
    }

    @Override
    public int[] jtableShipBuild_selected_rows() {
        return jTableShipBuild.getSelectedRows();
    }

    @Override
    public boolean rb_fill_all_planis_selected() {
        return rb_fill_all_planis.isSelected();
    }

    @Override
    public boolean sortByPercentageFirst() {
        return sortPercentage.isSelected();
    }

    @Override
    public boolean siedlerSchleifenIgnorieren() {
        return cb_siedler_schleifen_ignorieren.isSelected();
    }

    @Override
    public boolean löscheAktuell() {
        return cb_break_current.isSelected();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        String t = edt_schleifen_max_tage.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_max_tage_text", t.equals("") ? 0 : Long.parseLong(t));
        String ew = edt_schleifen_einwohner_min.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_einwohner_min_text", ew.equals("") ? 0 : Long.parseLong(ew));
        tool.updateProductionValues();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        String t = edt_schleifen_max_tage.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_max_tage_text", t.equals("") ? 0 : Long.parseLong(t));
        String ew = edt_schleifen_einwohner_min.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_einwohner_min_text", ew.equals("") ? 0 : Long.parseLong(ew));
        tool.updateProductionValues();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        String t = edt_schleifen_max_tage.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_max_tage_text", t.equals("") ? 0 : Long.parseLong(t));
        String ew = edt_schleifen_einwohner_min.getText();
        Preferences.userNodeForPackage(this.getClass()).putLong("edt_schleifen_einwohner_min_text", ew.equals("") ? 0 : Long.parseLong(ew));
        tool.updateProductionValues();
    }

    /**
     * Creates new form MainPanel
     *
     * @param sid
     */
    public SchiffsbauPanel(String sid) {
        initComponents();
        // cb_siedler_schleifen_ignorieren.setVisible(LoginRepository.getInstance().currentLogin.rasse != IntroPage.Response.Rasse.SIEDLER);
        // cb_siedler_schleifen_ignorieren.setSelected(LoginRepository.getInstance().currentLogin.rasse != IntroPage.Response.Rasse.SIEDLER);

        tool = new SchiffsbauTool(sid, this, new Repository());
        tableModel = new AbstractTableModel() {
            private final String[] columnsSort = new String[]{"Koords", "Einwohner", "Schleifen", "Status"};
            private final String[] columnsFill = new String[]{"Koords", "Einwohner", "Gefüllt", "Schiffstyp", "Prozent", "Menge", "Status"};

            @Override
            public int getRowCount() {
                return tool.listdata.size();
            }

            @Override
            public int getColumnCount() {
                boolean sort = jTabbedPane1.getSelectedIndex() == 1;
                return sort ? columnsSort.length : columnsFill.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                boolean sort = jTabbedPane1.getSelectedIndex() == 1;
                MyPlani plani = tool.listdata.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return plani;
                    case 1:
                        return tool.getEinwohner(plani);
                    case 2:
                        return sort ? tool.getSchleifen(plani) : tool.getFüllDauer(plani);
                    case 3:
                        return sort ? tool.getStatus(plani) : tool.getSchiffsTyp(plani);
                    case 4:
                        return sort ? "" : tool.getProzent(plani);
                    case 5:
                        return sort ? "" : tool.getMenge(plani);
                    case 6:
                        return sort ? "" : tool.getStatus(plani);
                    default:
                        return "";
                }
            }

            @Override
            public String getColumnName(int column) {
                boolean sort = jTabbedPane1.getSelectedIndex() == 1;
                return sort ? columnsSort[column] : columnsFill[column];
            }

        };

        jTableShipBuild.setModel(tableModel);

        jTableShipBuild.getSelectionModel().addListSelectionListener(this::tableSelectionChanged);
        jTableShipBuild.setFillsViewportHeight(true);

        JTableHeader header = jTableShipBuild.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);

        jTableShipBuild.getColumnModel().getColumn(0).setPreferredWidth(30);

//        jTableShipBuild.setOpaque(false);
//        jScrollPane2.setOpaque(false);
//        jScrollPane2.getViewport().setOpaque(false);
//        ((DefaultTableCellRenderer) jTableShipBuild.getDefaultRenderer(Object.class)).setOpaque(false);
//        PathCellRenderer cellRenderer = new PathCellRenderer();
//        cellRenderer.setOpaque(false);
//        jTableShipBuild.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTableShipBuild.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.BLACK);
                return c;
            }
        });

        jTabbedPane1.addChangeListener((ChangeEvent e) -> {
            tableModel.fireTableStructureChanged();
            tableModel.fireTableDataChanged();
            setTableSorter();
        });

        edt_schleifen_max_tage.getDocument().addDocumentListener(this);
        edt_menge.getDocument().addDocumentListener(this);
        edt_schleifen_einwohner_min.getDocument().addDocumentListener(this);

        ((PlainDocument) edt_schleifen_max_tage.getDocument()).setDocumentFilter(new MyIntFilter());
        ((PlainDocument) edt_schleifen_einwohner_min.getDocument()).setDocumentFilter(new MyIntFilter());

        //jScrollPane3.setOpaque(false);
        //jScrollPane3.getViewport().setOpaque(false);
        dragDropList1.setCellRenderer(new TransparentListCellRenderer());
        setTableSorter();
        loadSettings();
    }

    private void tableSelectionChanged(ListSelectionEvent lse) {
        if (!lse.getValueIsAdjusting()) {
            updateComboboxes();
            tool.updateProductionValues();
        }
    }

    private void setTableSorter() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(0, new KoordsComparator());

        TableColumnModel col = jTableShipBuild.getColumnModel();
        col.getColumn(0).setResizable(false);
        col.getColumn(0).setMinWidth(100);
        col.getColumn(0).setMaxWidth(100);

        col.getColumn(1).setResizable(false);
        col.getColumn(1).setMinWidth(150);
        col.getColumn(1).setMaxWidth(150);

        jTableShipBuild.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        if (jTabbedPane1.getSelectedIndex() == 0) {
            sorter.setComparator(1, new EinwohnerComparator());
            sorter.setComparator(2, new DauerComparator());
            sorter.setComparator(3, (String o1, String o2) -> o1.compareTo(o2));

            col.getColumn(2).setResizable(false);
            col.getColumn(2).setMinWidth(70);
            col.getColumn(2).setMaxWidth(70);

            col.getColumn(3).setResizable(false);
            col.getColumn(3).setMinWidth(120);
            col.getColumn(3).setMaxWidth(120);

            col.getColumn(4).setResizable(false);
            col.getColumn(4).setMinWidth(54);
            col.getColumn(4).setMaxWidth(54);

            col.getColumn(5).setResizable(false);
            col.getColumn(5).setMinWidth(80);
            col.getColumn(5).setMaxWidth(200);

        } else {
            col.getColumn(3).setResizable(false);
            col.getColumn(3).setMinWidth(200);
            col.getColumn(3).setMaxWidth(200);
        }
        jTableShipBuild.setRowSorter(sorter);

        sorter.addRowSorterListener((RowSorterEvent e) -> {
            ArrayList<MyPlani> copy = new ArrayList<>(tool.listdata);
            if (e.getType() == SORTED) {
                Collections.sort(tool.viewlistdata, (MyPlani o1, MyPlani o2) -> {
                    int i1 = copy.indexOf(o1);
                    int i2 = copy.indexOf(o2);

                    int m1 = jTableShipBuild.convertRowIndexToView(i1);
                    int m2 = jTableShipBuild.convertRowIndexToView(i2);
                    return Integer.compare(m1, m2);
                });
            }
        });

    }

    private void updateComboboxes() {
        boolean enabled = rb_fill_all_planis.isSelected() || jTableShipBuild.getSelectedRowCount() > 0;
        jPanel1.setEnabled(enabled);
        jLabel1.setEnabled(enabled);
        jLabel2.setEnabled(enabled);
        jLabel3.setEnabled(enabled);
        jLabel4.setEnabled(enabled);
        jLabel5.setEnabled(enabled);
        cmb_paket.setEnabled(enabled);
        cmb_prozent.setEnabled(enabled);
        cmb_schiffstyp.setEnabled(enabled);
        edt_menge.setEnabled(enabled);
        cmb_schleifen_fill_art.setEnabled(enabled);
    }

    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        rb_fill_selected_planis.setSelected(prefs.getBoolean("rb_fill_selected_planis_selected", true));
        rb_fill_all_planis.setSelected(prefs.getBoolean("rb_fill_all_planis_selected", true));
        cb_option_schleife_leer.setSelected(prefs.getBoolean("cb_option_schleife_leer_selected", true));
        cb_option_einwohner_leer.setSelected(prefs.getBoolean("cb_option_einwohner_leer_selected", true));
        edt_schleifen_max_tage.setText(String.valueOf(prefs.getLong("edt_schleifen_max_tage_text", 10)));
        cmb_schleifen_max_tage_stunden.setSelectedIndex(prefs.getInt("cmb_schleifen_max_tage_stunden_index", 0));
        edt_schleifen_einwohner_min.setText(String.valueOf(prefs.getLong("edt_schleifen_einwohner_min_text", 90)));
        cmb_schleifen_einwohner_min_prozent_anz.setSelectedIndex(prefs.getInt("cmb_schleifen_einwohner_min_prozent_anz_index", 0));
        cmb_schleifen_fill_art.setSelectedIndex(prefs.getInt("cmb_schleifen_fill_art_index", 0));
        cmb_schiffstyp.setSelectedIndex(prefs.getInt("cmb_schiffstyp_index", 0));
        cmb_prozent.setSelectedIndex(prefs.getInt("cmb_prozent_index", 0));
        cmb_paket.setSelectedIndex(prefs.getInt("cmb_paket_index", 0));

    }

    private void switchFillArt() {
        switch (cmb_schleifen_fill_art.getSelectedIndex()) {
            case 0:
                jLabel3.setText("Menge:");
                break;
            case 1:
                jLabel3.setText("EW-Rest (Anz):");
                break;
            case 2:
                jLabel3.setText("EW-Rest (%):");
                break;
            case 3:
                jLabel3.setText("Stunden:");
                break;
        }
        tool.updateProductionValues();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_reload_planilist = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableShipBuild = new javax.swing.JTable();
        btn_reset_table = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        rb_fill_selected_planis = new javax.swing.JRadioButton();
        btn_select_all = new javax.swing.JButton();
        btn_select_none = new javax.swing.JButton();
        rb_fill_all_planis = new javax.swing.JRadioButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cb_option_schleife_leer = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        edt_schleifen_max_tage = new javax.swing.JTextField();
        cmb_schleifen_max_tage_stunden = new javax.swing.JComboBox<>();
        cb_option_einwohner_leer = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        edt_schleifen_einwohner_min = new javax.swing.JTextField();
        cmb_schleifen_einwohner_min_prozent_anz = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        cmb_schiffstyp = new javax.swing.JComboBox<>();
        cmb_prozent = new javax.swing.JComboBox<>();
        cmb_paket = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        edt_menge = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmb_schleifen_fill_art = new javax.swing.JComboBox<>();
        btn_schleifen_füllen = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cb_break_current = new javax.swing.JCheckBox();
        btn_schleifen_füllen1 = new javax.swing.JButton();
        sortPercentage = new javax.swing.JRadioButton();
        sortShipType = new javax.swing.JRadioButton();
        btn_schleifen_füllen2 = new javax.swing.JButton();
        cb_siedler_schleifen_ignorieren = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        dragDropList1 = new util.DragDropList();

        setBackground(new java.awt.Color(0, 0, 0));

        btn_reload_planilist.setBackground(new java.awt.Color(0, 0, 0));
        btn_reload_planilist.setForeground(new java.awt.Color(255, 255, 255));
        btn_reload_planilist.setText("Planiliste neu einlesen");
        btn_reload_planilist.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_reload_planilist.setContentAreaFilled(false);
        btn_reload_planilist.setFocusPainted(false);
        btn_reload_planilist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reload_planilistActionPerformed(evt);
            }
        });

        jTableShipBuild.setBackground(new java.awt.Color(0, 0, 0));
        jTableShipBuild.setForeground(new java.awt.Color(255, 255, 255));
        jTableShipBuild.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Koords", "Einwohner", "Gefüllt", "Schiffstyp", "Prozent", "Menge", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableShipBuild.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTableShipBuild.setSelectionForeground(new java.awt.Color(0, 255, 255));
        jTableShipBuild.setShowVerticalLines(false);
        jTableShipBuild.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTableShipBuild);

        btn_reset_table.setBackground(new java.awt.Color(0, 0, 0));
        btn_reset_table.setForeground(new java.awt.Color(255, 255, 255));
        btn_reset_table.setText("Eingegebene Werte zurücksetzen");
        btn_reset_table.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_reset_table.setContentAreaFilled(false);
        btn_reset_table.setFocusPainted(false);
        btn_reset_table.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset_tableActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Auswahl", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel5.setOpaque(false);

        rb_fill_selected_planis.setForeground(new java.awt.Color(255, 255, 255));
        rb_fill_selected_planis.setText("Nur (in der Tabelle) ausgewählte Planis");
        rb_fill_selected_planis.setToolTipText("Wenn ausgewählt, werden nur die Schleifen von Planis geändert die in der Tabelle ausgewählt sind");
        rb_fill_selected_planis.setOpaque(false);
        rb_fill_selected_planis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_fill_selected_planisItemStateChanged(evt);
            }
        });

        btn_select_all.setBackground(new java.awt.Color(0, 0, 0));
        btn_select_all.setForeground(new java.awt.Color(255, 255, 255));
        btn_select_all.setText("Alle auswählen");
        btn_select_all.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_select_all.setContentAreaFilled(false);
        btn_select_all.setFocusPainted(false);
        btn_select_all.setMaximumSize(new java.awt.Dimension(81, 12));
        btn_select_all.setMinimumSize(new java.awt.Dimension(81, 12));
        btn_select_all.setPreferredSize(new java.awt.Dimension(81, 12));
        btn_select_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_select_allActionPerformed(evt);
            }
        });

        btn_select_none.setBackground(new java.awt.Color(0, 0, 0));
        btn_select_none.setForeground(new java.awt.Color(255, 255, 255));
        btn_select_none.setText("Keine auswählen");
        btn_select_none.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_select_none.setContentAreaFilled(false);
        btn_select_none.setFocusPainted(false);
        btn_select_none.setMaximumSize(new java.awt.Dimension(81, 12));
        btn_select_none.setMinimumSize(new java.awt.Dimension(81, 12));
        btn_select_none.setPreferredSize(new java.awt.Dimension(81, 12));
        btn_select_none.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_select_noneActionPerformed(evt);
            }
        });

        rb_fill_all_planis.setForeground(new java.awt.Color(255, 255, 255));
        rb_fill_all_planis.setText("Alle Planis");
        rb_fill_all_planis.setToolTipText("Wenn ausgewählt, werden die Schleifen aller Planis geändert, egal ob sie in der Tabelle ausgewählt sind oder nicht");
        rb_fill_all_planis.setOpaque(false);
        rb_fill_all_planis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_fill_all_planisItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btn_select_all, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_select_none, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rb_fill_selected_planis)
                    .addComponent(rb_fill_all_planis, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rb_fill_selected_planis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_select_all, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_select_none, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_fill_all_planis)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setOpaque(false);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fülloptionen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel3.setOpaque(false);

        cb_option_schleife_leer.setBackground(new java.awt.Color(0, 0, 0));
        cb_option_schleife_leer.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        cb_option_schleife_leer.setForeground(new java.awt.Color(255, 255, 255));
        cb_option_schleife_leer.setText("Nur füllen wenn Schleife leer in weniger als:");
        cb_option_schleife_leer.setToolTipText("Wenn ausgewählt, werden nur die Schleifen gefüllt die in x Tagen/Stunden (oder schneller) leer sind ");
        cb_option_schleife_leer.setOpaque(false);
        cb_option_schleife_leer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_option_schleife_leerItemStateChanged(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Dauer:");
        jLabel6.setEnabled(false);

        edt_schleifen_max_tage.setText("10");
        edt_schleifen_max_tage.setEnabled(false);

        cmb_schleifen_max_tage_stunden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tage(n)", "Stunde(n)" }));
        cmb_schleifen_max_tage_stunden.setEnabled(false);
        cmb_schleifen_max_tage_stunden.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_schleifen_max_tage_stundenItemStateChanged(evt);
            }
        });

        cb_option_einwohner_leer.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        cb_option_einwohner_leer.setForeground(new java.awt.Color(255, 255, 255));
        cb_option_einwohner_leer.setText("Nur füllen wenn vorhandene Einwohner größer als");
        cb_option_einwohner_leer.setToolTipText("Wenn ausgewählt werden die Schleifen nur auf den Planis gefüllt die mehr als x Einwohner haben");
        cb_option_einwohner_leer.setOpaque(false);
        cb_option_einwohner_leer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_option_einwohner_leerItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Einwohner:");
        jLabel7.setEnabled(false);

        edt_schleifen_einwohner_min.setText("90");
        edt_schleifen_einwohner_min.setEnabled(false);
        edt_schleifen_einwohner_min.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_schleifen_einwohner_minActionPerformed(evt);
            }
        });

        cmb_schleifen_einwohner_min_prozent_anz.setFont(new java.awt.Font("Lucida Grande", 1, 11)); // NOI18N
        cmb_schleifen_einwohner_min_prozent_anz.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Prozent", "Anzahl" }));
        cmb_schleifen_einwohner_min_prozent_anz.setEnabled(false);
        cmb_schleifen_einwohner_min_prozent_anz.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_schleifen_einwohner_min_prozent_anzItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_option_schleife_leer)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cb_option_einwohner_leer)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edt_schleifen_einwohner_min, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmb_schleifen_einwohner_min_prozent_anz, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(38, 38, 38)
                        .addComponent(edt_schleifen_max_tage, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_schleifen_max_tage_stunden, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cb_option_schleife_leer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(edt_schleifen_max_tage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_schleifen_max_tage_stunden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_option_einwohner_leer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(edt_schleifen_einwohner_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_schleifen_einwohner_min_prozent_anz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ändern der Werte für markierte Planis", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setEnabled(false);
        jPanel1.setOpaque(false);

        cmb_schiffstyp.setBackground(new java.awt.Color(0, 0, 0));
        cmb_schiffstyp.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        cmb_schiffstyp.setForeground(new java.awt.Color(255, 255, 255));
        cmb_schiffstyp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "unbemannte Sonde", "Snatcher", "Rettungsschiff", "Handelsschiff", "Handelsriese", "A-Glider", "Noulon", "schwerer Bomber", "Kolonisationsschiff", "Trugar", "Violo", "Narubu", "Neomar", "Bloodhound", "Kemzen", "Zemar", "Finur", "Luxor", "Grandor", "Invasionseinheit" }));
        cmb_schiffstyp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cmb_schiffstyp.setEnabled(false);
        cmb_schiffstyp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_schiffstypItemStateChanged(evt);
            }
        });

        cmb_prozent.setBackground(new java.awt.Color(0, 0, 0));
        cmb_prozent.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        cmb_prozent.setForeground(new java.awt.Color(255, 255, 255));
        cmb_prozent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "100", "125", "150", "175", "200" }));
        cmb_prozent.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cmb_prozent.setEnabled(false);
        cmb_prozent.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_prozentItemStateChanged(evt);
            }
        });

        cmb_paket.setBackground(new java.awt.Color(0, 0, 0));
        cmb_paket.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        cmb_paket.setForeground(new java.awt.Color(255, 255, 255));
        cmb_paket.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Einzelne", "Pakete" }));
        cmb_paket.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        cmb_paket.setEnabled(false);
        cmb_paket.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_paketItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Schiffstyp:");
        jLabel1.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Geschwindigkeit:");
        jLabel2.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Menge:");
        jLabel3.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Paket:");
        jLabel4.setEnabled(false);

        edt_menge.setBackground(new java.awt.Color(0, 0, 0));
        edt_menge.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        edt_menge.setForeground(new java.awt.Color(255, 255, 255));
        edt_menge.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        edt_menge.setEnabled(false);
        edt_menge.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Art:");
        jLabel5.setEnabled(false);

        cmb_schleifen_fill_art.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        cmb_schleifen_fill_art.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Anzahl Schiffspakete", "Einwohneruntergrenze (Anz.)", "Einwohneruntergrenze (%)", "Bauzeit (Stunden)" }));
        cmb_schleifen_fill_art.setEnabled(false);
        cmb_schleifen_fill_art.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_schleifen_fill_artItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edt_menge, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmb_paket, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_prozent, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_schiffstyp, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmb_schleifen_fill_art, javax.swing.GroupLayout.Alignment.TRAILING, 0, 195, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_schleifen_fill_art, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_schiffstyp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_prozent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmb_paket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edt_menge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_schleifen_füllen.setBackground(new java.awt.Color(0, 0, 0));
        btn_schleifen_füllen.setForeground(new java.awt.Color(255, 255, 255));
        btn_schleifen_füllen.setText("Schleifen füllen");
        btn_schleifen_füllen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_schleifen_füllen.setContentAreaFilled(false);
        btn_schleifen_füllen.setFocusPainted(false);
        btn_schleifen_füllen.setMaximumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen.setMinimumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen.setPreferredSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_schleifen_füllenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btn_schleifen_füllen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_schleifen_füllen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 158, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Schleifen füllen", jPanel2);

        jPanel4.setOpaque(false);

        cb_break_current.setForeground(new java.awt.Color(255, 255, 255));
        cb_break_current.setText("Gerade im Bau befindliches Schiff abbrechen");
        cb_break_current.setOpaque(false);

        btn_schleifen_füllen1.setBackground(new java.awt.Color(0, 0, 0));
        btn_schleifen_füllen1.setForeground(new java.awt.Color(255, 255, 255));
        btn_schleifen_füllen1.setText("Schleifen Sortieren");
        btn_schleifen_füllen1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_schleifen_füllen1.setContentAreaFilled(false);
        btn_schleifen_füllen1.setFocusPainted(false);
        btn_schleifen_füllen1.setMaximumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen1.setMinimumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen1.setPreferredSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_schleifen_füllen1ActionPerformed(evt);
            }
        });

        sortPercentage.setForeground(new java.awt.Color(255, 255, 255));
        sortPercentage.setSelected(true);
        sortPercentage.setText("Zuerst nach % sortieren, dann nach Schiffstyp");
        sortPercentage.setOpaque(false);

        sortShipType.setForeground(new java.awt.Color(255, 255, 255));
        sortShipType.setText("Zuerst nach Schiffstyp sortieren, dann nach %");
        sortShipType.setOpaque(false);

        btn_schleifen_füllen2.setBackground(new java.awt.Color(0, 0, 0));
        btn_schleifen_füllen2.setForeground(new java.awt.Color(255, 255, 255));
        btn_schleifen_füllen2.setText("Schleifen zusammenfassen");
        btn_schleifen_füllen2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        btn_schleifen_füllen2.setContentAreaFilled(false);
        btn_schleifen_füllen2.setFocusPainted(false);
        btn_schleifen_füllen2.setMaximumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen2.setMinimumSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen2.setPreferredSize(new java.awt.Dimension(81, 12));
        btn_schleifen_füllen2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_schleifen_füllen2ActionPerformed(evt);
            }
        });

        cb_siedler_schleifen_ignorieren.setForeground(new java.awt.Color(255, 255, 255));
        cb_siedler_schleifen_ignorieren.setText("Siedler Schleifen so lassen wie sie sind");
        cb_siedler_schleifen_ignorieren.setOpaque(false);

        jScrollPane1.setViewportView(dragDropList1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(btn_schleifen_füllen1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_schleifen_füllen2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sortShipType)
                            .addComponent(sortPercentage)
                            .addComponent(cb_break_current)
                            .addComponent(cb_siedler_schleifen_ignorieren))
                        .addGap(0, 42, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sortPercentage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortShipType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_break_current)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_siedler_schleifen_ignorieren)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_schleifen_füllen2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_schleifen_füllen1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Schleifen umstellen", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_reload_planilist, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reset_table, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_reload_planilist, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_reset_table, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_reload_planilistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reload_planilistActionPerformed
        tool.loadPlaniList();
    }//GEN-LAST:event_btn_reload_planilistActionPerformed

    private void btn_reset_tableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset_tableActionPerformed
        tool.resetValues();
    }//GEN-LAST:event_btn_reset_tableActionPerformed

    private void rb_fill_selected_planisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_fill_selected_planisItemStateChanged
        btn_select_all.setEnabled(rb_fill_selected_planis.isSelected());
        btn_select_none.setEnabled(rb_fill_selected_planis.isSelected());
        jTableShipBuild.setRowSelectionAllowed(rb_fill_selected_planis.isSelected());
        Preferences.userNodeForPackage(this.getClass()).putBoolean("rb_fill_selected_planis_selected", rb_fill_selected_planis.isSelected());
        jTableShipBuild.clearSelection();
        updateComboboxes();
        tool.updateProductionValues();
    }//GEN-LAST:event_rb_fill_selected_planisItemStateChanged

    private void btn_select_allActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_select_allActionPerformed
        jTableShipBuild.selectAll();
        updateComboboxes();
    }//GEN-LAST:event_btn_select_allActionPerformed

    private void btn_select_noneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_select_noneActionPerformed
        jTableShipBuild.clearSelection();
        updateComboboxes();
    }//GEN-LAST:event_btn_select_noneActionPerformed

    private void rb_fill_all_planisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_fill_all_planisItemStateChanged
        btn_select_all.setEnabled(rb_fill_selected_planis.isSelected());
        btn_select_none.setEnabled(rb_fill_selected_planis.isSelected());
        jTableShipBuild.setRowSelectionAllowed(rb_fill_selected_planis.isSelected());
        Preferences.userNodeForPackage(this.getClass()).putBoolean("rb_fill_all_planis_selected", rb_fill_all_planis.isSelected());
        updateComboboxes();
        tool.updateProductionValues();
    }//GEN-LAST:event_rb_fill_all_planisItemStateChanged

    private void cb_option_schleife_leerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_option_schleife_leerItemStateChanged
        jLabel6.setEnabled(cb_option_schleife_leer.isSelected());
        edt_schleifen_max_tage.setEnabled(cb_option_schleife_leer.isSelected());
        cmb_schleifen_max_tage_stunden.setEnabled(cb_option_schleife_leer.isSelected());
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putBoolean("cb_option_schleife_leer_selected", cb_option_schleife_leer.isSelected());
    }//GEN-LAST:event_cb_option_schleife_leerItemStateChanged

    private void cmb_schleifen_max_tage_stundenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_schleifen_max_tage_stundenItemStateChanged
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_schleifen_max_tage_stunden_index", cmb_schleifen_max_tage_stunden.getSelectedIndex());
    }//GEN-LAST:event_cmb_schleifen_max_tage_stundenItemStateChanged

    private void cb_option_einwohner_leerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_option_einwohner_leerItemStateChanged
        jLabel7.setEnabled(cb_option_schleife_leer.isSelected());
        edt_schleifen_einwohner_min.setEnabled(cb_option_einwohner_leer.isSelected());
        cmb_schleifen_einwohner_min_prozent_anz.setEnabled(cb_option_einwohner_leer.isSelected());
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putBoolean("cb_option_einwohner_leer_selected", cb_option_einwohner_leer.isSelected());
    }//GEN-LAST:event_cb_option_einwohner_leerItemStateChanged

    private void edt_schleifen_einwohner_minActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_schleifen_einwohner_minActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_schleifen_einwohner_minActionPerformed

    private void cmb_schleifen_einwohner_min_prozent_anzItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_schleifen_einwohner_min_prozent_anzItemStateChanged
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_schleifen_einwohner_min_prozent_anz_index", cmb_schleifen_einwohner_min_prozent_anz.getSelectedIndex());
    }//GEN-LAST:event_cmb_schleifen_einwohner_min_prozent_anzItemStateChanged

    private void cmb_schiffstypItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_schiffstypItemStateChanged
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_schiffstyp_index", cmb_schiffstyp.getSelectedIndex());
    }//GEN-LAST:event_cmb_schiffstypItemStateChanged

    private void cmb_prozentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_prozentItemStateChanged
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_prozent_index", cmb_prozent.getSelectedIndex());
    }//GEN-LAST:event_cmb_prozentItemStateChanged

    private void cmb_paketItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_paketItemStateChanged
        tool.updateProductionValues();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_paket_index", cmb_paket.getSelectedIndex());
    }//GEN-LAST:event_cmb_paketItemStateChanged

    private void cmb_schleifen_fill_artItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_schleifen_fill_artItemStateChanged
        tool.updateProductionValues();
        switchFillArt();
        Preferences.userNodeForPackage(this.getClass()).putInt("cmb_schleifen_fill_art_index", cmb_schleifen_fill_art.getSelectedIndex());
    }//GEN-LAST:event_cmb_schleifen_fill_artItemStateChanged

    private void btn_schleifen_füllenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_schleifen_füllenActionPerformed
        tool.fillSchleifen();
    }//GEN-LAST:event_btn_schleifen_füllenActionPerformed

    private void btn_schleifen_füllen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_schleifen_füllen1ActionPerformed
        tool.schleifenSortieren();
    }//GEN-LAST:event_btn_schleifen_füllen1ActionPerformed

    private void btn_schleifen_füllen2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_schleifen_füllen2ActionPerformed
        tool.schleifenZusammenfassen();
    }//GEN-LAST:event_btn_schleifen_füllen2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reload_planilist;
    private javax.swing.JButton btn_reset_table;
    private javax.swing.JButton btn_schleifen_füllen;
    private javax.swing.JButton btn_schleifen_füllen1;
    private javax.swing.JButton btn_schleifen_füllen2;
    private javax.swing.JButton btn_select_all;
    private javax.swing.JButton btn_select_none;
    private javax.swing.JCheckBox cb_break_current;
    private javax.swing.JCheckBox cb_option_einwohner_leer;
    private javax.swing.JCheckBox cb_option_schleife_leer;
    private javax.swing.JCheckBox cb_siedler_schleifen_ignorieren;
    private javax.swing.JComboBox<String> cmb_paket;
    private javax.swing.JComboBox<String> cmb_prozent;
    private javax.swing.JComboBox<String> cmb_schiffstyp;
    private javax.swing.JComboBox<String> cmb_schleifen_einwohner_min_prozent_anz;
    private javax.swing.JComboBox<String> cmb_schleifen_fill_art;
    private javax.swing.JComboBox<String> cmb_schleifen_max_tage_stunden;
    private util.DragDropList dragDropList1;
    private javax.swing.JTextField edt_menge;
    private javax.swing.JTextField edt_schleifen_einwohner_min;
    private javax.swing.JTextField edt_schleifen_max_tage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableShipBuild;
    private javax.swing.JRadioButton rb_fill_all_planis;
    private javax.swing.JRadioButton rb_fill_selected_planis;
    private javax.swing.JRadioButton sortPercentage;
    private javax.swing.JRadioButton sortShipType;
    // End of variables declaration//GEN-END:variables

}
