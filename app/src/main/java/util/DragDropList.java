/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ELschleifentool.util;

/**
 *
 * @author Nico
 */
import ELschleifentool.model.Constants;
import ELschleifentool.model.Schiffstyp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class DragDropList extends JList {

    DefaultListModel model;
    private int startDragIndex;
    private final Gson gson;
    //private final Preferences prefs;

    public DragDropList() {
        super(new DefaultListModel());
        model = (DefaultListModel) getModel();
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);
        setOpaque(false);
        setTransferHandler(new MyListDropHandler(this));

        new MyDragListener(this);

        model.addElement("unbemannte Sonde");
        model.addElement("Snatcher");
        model.addElement("Rettungsschiff");
        model.addElement("Handelsschiff");
        model.addElement("Handelsriese");
        model.addElement("A-Glider");
        model.addElement("Noulon");
        model.addElement("schwerer Bomber");
        model.addElement("Kolonisationsschiff");
        model.addElement("Trugar");
        model.addElement("Violo");
        model.addElement("Narubu");
        model.addElement("Neomar");
        model.addElement("Bloodhound");
        model.addElement("Kemzen");
        model.addElement("Zemar");
        model.addElement("Finur");
        model.addElement("Luxor");
        model.addElement("Grandor");
        model.addElement("Invasionseinheit");

        //prefs = Preferences.userNodeForPackage(ELTools.class);
        gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

        java.lang.reflect.Type type = new TypeToken<DefaultListModel>() {
        }.getType();
        //String savedOrder = prefs.get("shipSortOrder", "");
        // if (savedOrder.equals("")) {
        //      prefs.put("shipSortOrder", gson.toJson(model));
        // } else {
        //     model = gson.fromJson(savedOrder, type);
        //     setModel(model);
        // }

    }

    public List<Schiffstyp> getOrder() {
        List<String> ships = Collections.list(model.elements());
        return ships.stream().map(Constants.shipTypeByName::get).collect(Collectors.toList());
    }

    class MyDragListener implements DragSourceListener, DragGestureListener {

        DragDropList list;

        DragSource ds = new DragSource();

        public MyDragListener(DragDropList list) {
            this.list = list;
            DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_MOVE, this);
        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            startDragIndex = list.getSelectedIndex();
            StringSelection transferable = new StringSelection(Integer.toString(startDragIndex));
            ds.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
            if (dsde.getDropSuccess()) {
                System.out.println("Succeeded");
            } else {
                System.out.println("Failed");
            }
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }

    class MyListDropHandler extends TransferHandler {

        DragDropList list;

        public MyListDropHandler(DragDropList list) {
            this.list = list;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            if (dl.getIndex() == -1) {
                return false;
            } else {
                return true;
            }
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            String indexString;
            try {
                indexString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            int index = Integer.parseInt(indexString);
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int dropTargetIndex = dl.getIndex();

            Object elementAt = model.getElementAt(startDragIndex);
            model.removeElement(elementAt);

            int size = model.getSize();
            if (dropTargetIndex > size) {
                model.add(size, elementAt);
            } else {
                model.insertElementAt(elementAt, dropTargetIndex);
            }
            // prefs.put("shipSortOrder", gson.toJson(model));
            System.out.println(dropTargetIndex + " : ");
            System.out.println("inserted");
            return true;
        }
    }
}
