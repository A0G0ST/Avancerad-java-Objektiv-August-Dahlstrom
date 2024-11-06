package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GUI {
    private JFrame frame;               // Huvudfönstret för användargränssnittet
    private JTable table;               // Tabellen där data visas
    private OrderDataModel model;       // Datahanteringsmodell för att hålla och sortera data

    public GUI() {
        
        frame = new JFrame("Objektivdataread - Data Visare");
        frame.setSize(800, 600); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setLayout(new BorderLayout()); 

        
        JButton fileButton = new JButton("Ladda Fil");
        fileButton.addActionListener(e -> loadFile()); 

       
        JComboBox<String> sortDropdown = new JComboBox<>(new String[]{"OrderDate", "Region", "Item"});
        sortDropdown.addActionListener(e -> sortData((String) sortDropdown.getSelectedItem()));
        

       
        JPanel topPanel = new JPanel();
        topPanel.add(fileButton);
        topPanel.add(sortDropdown);

        
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(topPanel, BorderLayout.NORTH); 
        frame.add(scrollPane, BorderLayout.CENTER); 
    }

    
    private void loadFile() {
        JFileChooser chooser = new JFileChooser(); 
        int option = chooser.showOpenDialog(frame); 
        if (option == JFileChooser.APPROVE_OPTION) { 
            File file = chooser.getSelectedFile(); 
            try {
                FileLoader loader = new FileLoader(); 
                List<OrderData> data = loader.loadFile(file.getName()); 
                model = new OrderDataModel(data); 
                updateTable(); 
            } catch (Exception ex) {
                
                JOptionPane.showMessageDialog(frame, "Kunde inte läsa filen: " + ex.getMessage());
            }
        }
    }

    
    private void sortData(String attribute) {
        if (model != null) { 
            model.sortBy(attribute); 
            updateTable(); 
        }
    }

    
    private void updateTable() {
        String[] columns = {"OrderDate", "Region", "Rep1", "Rep2", "Item", "Units", "UnitCost", "Total"};
        
        Object[][] data = model.getData().stream()
                .map(d -> new Object[]{d.getOrderDate(), d.getRegion(), d.getRep1(), d.getRep2(), d.getItem(),
                        d.getUnits(), d.getUnitCost(), d.getTotal()})
                .toArray(Object[][]::new);
        
        table.setModel(new DefaultTableModel(data, columns));
    }

    
    public void show() {
        frame.setVisible(true); 
    }
}
