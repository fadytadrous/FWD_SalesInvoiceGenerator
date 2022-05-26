package Controller;

import Model.InvoiceHeader;
import Model.InvoiceLines;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Hashtable;

public class Controller {
    FileOperations fileOperations = new FileOperations();

    private String[] rows;

    public InvoiceHeader[] header = new InvoiceHeader[10];
    public InvoiceLines[] items = new InvoiceLines[10];
    public String[][] preread(String fileType) throws IOException {

        rows = readInitialFileContent(fileType).split("\n");
        return fillTableData(rows, fileType);
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
    private String[][] fillTableData(String[] rows, String filetype){

        String[][] tableData = new String[rows.length][rows[0].split(",").length];
        if(filetype == "invoices"){
            for (int rowsCounter = 0;rowsCounter<rows.length;rowsCounter++){
                String[] dataArray = rows[rowsCounter].split(",");
                header[rowsCounter] = new InvoiceHeader(dataArray[0],dataArray[1],dataArray[2]);
                header[rowsCounter].setTotalInvoicePrice(0);
                header[rowsCounter].setInvoiceItemsCounter(0);
                tableData[rowsCounter] = dataArray;
            }
        }
        else if (filetype=="items") {
            Hashtable<Integer, Integer> itemsCount = new Hashtable<Integer, Integer>();

            for (int rowsCounter = 0; rowsCounter < rows.length; rowsCounter++) {
                String[] dataArray = rows[rowsCounter].split(",");

                items[rowsCounter] = new InvoiceLines(dataArray[0], dataArray[1], dataArray[2],
                        dataArray[3]);

                /*Get number of invoice lines for each invoice*/
                int currentInvoice = Integer.parseInt(items[rowsCounter].getInvoiceNumber());
                /* if the map contains no mapping for the key,
                 map the key with a value of 1
                else increment the found value by 1 */
                itemsCount.merge(currentInvoice, 1, Integer::sum);
                header[currentInvoice - 1].setInvoiceItemsCounter(
                        header[currentInvoice-1].getInvoiceItemsCounter() + 1);
                tableData[rowsCounter] = dataArray;

            }

        }
        return tableData;
    }
    public String[][] loadFile(String file, String fileType) throws IOException {
        rows = fileOperations.readFileContent(file).split("\n");
        return fillTableData(rows, fileType);
    }

    



    public void saveItems(JTable table) throws IOException {
        String filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceLine.csv";
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
