package com.Controller;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.file.Paths;

public class Controller {
    File currentDir = new File(".");
    int fileSize;
    String returnedData;
    byte[] bytes;
    FileInputStream fileInput;
    FileOutputStream fileOutput;
    private String[] rows;
    private String[][] tableData;

    public String readFileContent(String filePath) throws IOException {
        fileInput = new FileInputStream(filePath);
        fileSize = fileInput.available();
        bytes = new byte[fileSize];
        fileInput.read(bytes);
        returnedData= new String(bytes);
//        System.out.println(returnedData);
        fileInput.close();
        return returnedData;
    }
    public String[][] preread(String fileType) throws IOException {

        rows = readInitialFileContent(fileType).split("\n");
        return fillTableData(rows);
    }
    public String readInitialFileContent(String fileType) throws IOException {
        String filePath = "";
        if (fileType == "invoices") {
            filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceHeader.csv";
        }
        else if (fileType == "items"){
            filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceLine.csv";

        }

        return readFileContent(filePath);
    }
    private String[][] fillTableData(String[] rows){
        String[][] tableData = new String[rows.length][rows[0].split(",").length];
        for (int rowsCounter = 0;rowsCounter<rows.length;rowsCounter++) {
            tableData[rowsCounter] = rows[rowsCounter].split(",");
        }
        return tableData;
    }
    public String[][] loadFile(String file) throws IOException {
        rows = readFileContent(file).split("\n");
        return fillTableData(rows);
    }

    

    public void saveInvoicesToFile(String path, JTable table) throws IOException {
        TableModel m = table.getModel();
        if(!path.endsWith(".csv")){
            path = path + ".csv";
        }
        FileWriter fw = new FileWriter(path);
        /*Writing Col names*/
        for(int i = 0; i < m.getColumnCount(); i++){
            fw.write(m.getColumnName(i) + ",");
        }
        fw.write("\n");
        /*Writing row names*/
        for(int i=0; i < m.getRowCount(); i++) {
            for(int j=0; j < m.getColumnCount(); j++) {
                fw.write(m.getValueAt(i,j) +",");
            }
            fw.write("\n");
        }
        fw.close();
    }


    public void saveItems(JTable table) throws IOException {
        String filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceItems.csv";
        saveInvoicesToFile(filePath, table);
    }

    public void saveInvoiceItemsChanges(JTable invoicesItemsTable) {
        try {
            saveItems(invoicesItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }
}
