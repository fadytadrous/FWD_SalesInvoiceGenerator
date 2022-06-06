package Controller;

import Model.InvoiceHeader;
import Model.InvoiceLines;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class JtableController {

    private final Controller Controller = new Controller();


    public void showInvoicesTableRowDetails (
            DefaultTableModel invoicesTableModel, JTable invoicesTable,
            JLabel num, JTextField date,DefaultTableModel invoiceItemsTableModel,
            JTextField customer, JLabel total)
    {

        Object invoiceDataObj =  invoicesTableModel.getDataVector().get(invoicesTable.getSelectedRow());
        Vector invoiceData = (Vector)invoiceDataObj;
        String invoiceNo = (String) invoiceData.get(0);
        num.setText(invoiceNo);
        date.setText((String) invoiceData.get(1));
        customer.setText((String) invoiceData.get(2));
        total.setText(Float.toString((Float) invoiceData.get(3)));
        invoiceItemsTableModel.getDataVector().removeAllElements();
        Float totalInvoice = 0f;
        for (InvoiceLines item : Controller.items) {
            if(invoiceNo.matches(item.getInvoiceNumber())){
                String[] data = {item.getInvoiceNumber(),item.getItemName(),
                        item.getItemPrice(),item.getItemCount (),item.getTotalItemPrice() };
                invoiceItemsTableModel.addRow(data);
                totalInvoice+= Float.parseFloat(item.getTotalItemPrice());
                invoicesTableModel.setValueAt(totalInvoice,invoicesTable.getSelectedRow(),3);
            }
        }

    }



//    public void initiallyLoadInvoicesData(String[][] invoicesData, DefaultTableModel invoicesTableModel,
//                                          DefaultTableModel invoiceItemsTableModel, JTable invoiceItemsTable ) {
//        try {
//            invoicesData = Controller.preread("invoices");
//            addInvoicesToTable(invoicesData,invoicesTableModel );
//
//            invoicesData = Controller.preread("items");
//            addItemsToTable(invoicesData,invoiceItemsTableModel, invoiceItemsTable);
//            Controller.calculateInvoicesTotal(invoicesTableModel);
//        }catch (IOException ex) {
//            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
//        }
//    }

    public void addInvoicesToTable(String[][] rows,DefaultTableModel invoicesTableModel ){
        invoicesTableModel.getDataVector().removeAllElements();

        for (String[] row : rows) {
            invoicesTableModel.addRow(row);
        }
   }

    public void addItemsToTable(String[][] rows, DefaultTableModel invoiceItemsTableModel,
                                JTable invoiceItemsTable ){
        invoiceItemsTableModel.getDataVector().removeAllElements();
        for (String[] row : rows) {
            invoiceItemsTableModel.addRow(row);

        }

        /** Fill items total **/
        for (int i=0; i< invoiceItemsTable.getRowCount();i++)
        {
            float price = Float.parseFloat(invoiceItemsTable.getValueAt(i,2).toString());
            int no = Integer.parseInt(invoiceItemsTable.getValueAt(i,3).toString());
            invoiceItemsTable.setValueAt(price*no, i, 4);
        }
    }

}
