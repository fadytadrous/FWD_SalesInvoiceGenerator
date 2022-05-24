package Controller;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.file.Paths;

public class Controller {
    FileOperations fileOperations = new FileOperations();

    private String[] rows;
    

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

        return fileOperations.readFileContent(filePath);
    }
    private String[][] fillTableData(String[] rows){
        String[][] tableData = new String[rows.length][rows[0].split(",").length];
        for (int rowsCounter = 0;rowsCounter<rows.length;rowsCounter++) {
            tableData[rowsCounter] = rows[rowsCounter].split(",");
        }
        return tableData;
    }
    public String[][] loadFile(String file) throws IOException {
        rows = fileOperations.readFileContent(file).split("\n");
        return fillTableData(rows);
    }

    



    public void saveItems(JTable table) throws IOException {
        String filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceItems.csv";
        fileOperations.writeFile(filePath, table, true);
    }

    public void saveInvoiceItemsChanges(JTable invoicesItemsTable) {
        try {
            saveItems(invoicesItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }
}
