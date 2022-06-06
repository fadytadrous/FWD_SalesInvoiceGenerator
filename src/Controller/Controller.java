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
    public static String headerFilePath = Paths.get("").toAbsolutePath() + "\\resources\\InvoiceHeader.csv";

    public static List<InvoiceLines> items = new ArrayList<InvoiceLines>();
    public static String itemsFilePath =Paths.get("").toAbsolutePath() + "\\resources\\InvoiceLine.csv";

    public String[][] preread(String fileType) throws IOException {

        rows = readInitialFileContent(fileType).split("\n");
        return fillTableData(rows, fileType);
    }
    public String readInitialFileContent(String fileType) throws IOException {
        String filePath = "";
        if (fileType == "invoices") {
            filePath = headerFilePath;
        }
        else if (fileType == "items"){
            filePath = itemsFilePath;
        }

        return fileOperations.readFileContent(filePath);
    }
    private String[][] fillTableData(String[] rows, String filetype){

        String[][] tableData = new String[rows.length][rows[0].split(",").length];
        if(filetype == "invoices"){
            header.clear();
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
            items.clear();
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
    public String[][] loadFile(String filepath, String fileType) throws IOException {

        rows = fileOperations.readFileContent(filepath).split("\n");
        if(fileType == "invoices"){
            headerFilePath = filepath;
        }
        else{
            itemsFilePath = filepath;
        }
        return fillTableData(rows, fileType);
    }

    
    public void calculateInvoicesTotal(DefaultTableModel invoicesTableModel){
        for(int i=0;i<invoicesTableModel.getRowCount();i++){
            invoicesTableModel.setValueAt(0f,i,3);
        }

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
        checkNewItems(table);
        fileOperations.writeFile(filePath, table, false, true);
    }

    public void saveInvoiceItemsChanges(JTable invoicesItemsTable) {
        try {
            saveItems(invoicesItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void deleteItem(JTable itemsTable, DefaultTableModel itemsTableModel) throws IOException {

        if(itemsTable.getSelectedRow() != -1){

            String selectedItemName = (String) itemsTable.getValueAt(itemsTable.getSelectedRow(),1);
            for(InvoiceLines item: items){
                if(item.getItemName().contains(selectedItemName)){
                    items.remove(item);
                    break;
                }
            }
            itemsTableModel.removeRow(itemsTable.getSelectedRow());
            fileOperations.writeFile(itemsFilePath, itemsTable, false, true);
        }
    }

    public void checkNewItems(JTable invoiceItemsTable) {
        for (int i=0; i< invoiceItemsTable.getRowCount();i++)
        {
            String itemInvNo = (String) invoiceItemsTable.getValueAt(i,0);
            String itemName =  (String) invoiceItemsTable.getValueAt(i,1);
            String itemPrice = (String) invoiceItemsTable.getValueAt(i, 2);
            String itemCount = (String) invoiceItemsTable.getValueAt(i, 3);

            Boolean itemExists = false;
            for(InvoiceLines item: Controller.items){
                if(item.getInvoiceNumber().contains(itemInvNo) && item.getItemName().contains(itemName))
                {
                    itemExists =true;
                    break;
                }
            }
            if (!itemExists){
                Controller.items.add(new InvoiceLines(itemInvNo,itemName, itemPrice,itemCount));
            }

        }
    }

    public void deleteHeader(JTable invoicesTable, DefaultTableModel invoicesTableModel,
                             JTable itemsTable, DefaultTableModel invoiceItemsTableModel) throws IOException {
        if(invoicesTable.getSelectedRow()!=-1) {
            String headerNo = (String) invoicesTableModel.getValueAt(invoicesTable.getSelectedRow(),0);
            invoicesTableModel.removeRow(invoicesTable.getSelectedRow());
            invoiceItemsTableModel.setNumRows(0);
            fileOperations.writeFile(headerFilePath, invoicesTable,false,false);
            items.removeIf(item -> item.getInvoiceNumber().contains(headerNo));
            fileOperations.writeFile(itemsFilePath, itemsTable, false, true);

        }
    }
}
