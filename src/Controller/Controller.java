package Controller;

import Model.InvoiceHeader;
import Model.InvoiceLines;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Controller {

    FileOperations fileOperations = new FileOperations();

    private String[] rows;
    public static List<InvoiceHeader> header = new ArrayList<InvoiceHeader>();
    public static List<InvoiceLines> items = new ArrayList<InvoiceLines>();


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
                header.add(new InvoiceHeader(dataArray[0],dataArray[1],dataArray[2]));
                header.get(rowsCounter).setTotalInvoicePrice(0);
                header.get(rowsCounter).setInvoiceItemsCounter(0);
                tableData[rowsCounter] = dataArray;
            }
        }
        else if (filetype=="items") {
            Hashtable<Integer, Integer> itemsCount = new Hashtable<Integer, Integer>();

            for (int rowsCounter = 0; rowsCounter < rows.length; rowsCounter++) {
                String[] dataArray = rows[rowsCounter].split(",");

                items.add(new InvoiceLines(dataArray[0], dataArray[1], dataArray[2],
                        dataArray[3])) ;

                /*Get number of invoice lines for each invoice*/
                int currentInvoice = Integer.parseInt(items.get(rowsCounter).getInvoiceNumber());
                /* if the map contains no mapping for the key,
                 map the key with a value of 1
                else increment the found value by 1 */
                itemsCount.merge(currentInvoice, 1, Integer::sum);
                header.get(currentInvoice - 1).setInvoiceItemsCounter(
                        header.get(currentInvoice - 1).getInvoiceItemsCounter() + 1);

                tableData[rowsCounter] = dataArray;

            }
        }

        return tableData;
    }
    public String[][] loadFile(String file, String fileType) throws IOException {
        rows = fileOperations.readFileContent(file).split("\n");
        return fillTableData(rows, fileType);
    }

    
    public void calculateInvoicesTotal(DefaultTableModel invoicesTableModel){
        for (InvoiceLines item : items) {
            int invNoForItem = Integer.parseInt(item.getInvoiceNumber());
            header.get(invNoForItem-1).setTotalInvoicePrice(
                header.get(invNoForItem-1).getTotalInvoicePrice()+
                        Float.parseFloat(item.getTotalItemPrice()));
            invoicesTableModel.setValueAt(header.get(invNoForItem-1)
                    .getTotalInvoicePrice(),invNoForItem-1,3);
        }

    }


    public void saveItems(JTable table) throws IOException {
        String filePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceLine.csv";
        fileOperations.writeFile(filePath, table, true, true);
    }

    public void saveInvoiceItemsChanges(JTable invoicesItemsTable) {
        try {
            saveItems(invoicesItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void deleteItem(JTable itemsTable, DefaultTableModel itemsTableModel){

        if(itemsTable.getSelectedRow() != -1){

            String selectedItemName = (String) itemsTable.getValueAt(itemsTable.getSelectedRow(),1);
            for(InvoiceLines item: items){
                if(item.getItemName().contains(selectedItemName)){
                    items.remove(item);
                }
            }
            itemsTableModel.removeRow(itemsTable.getSelectedRow());
        }
    }
}
