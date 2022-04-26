package com.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.Vector;

public class JtableController {
    private final Controller Controller = new Controller();

    public void showInvoicesTableRowDetails(
            DefaultTableModel invoicesTableModel, JTable invoicesTable,
            JLabel num, JTextField date,
            JTextField customer, JLabel total)
    {
        Object invoiceDataObj =  invoicesTableModel.getDataVector().get(invoicesTable.getSelectedRow());
        Vector invoiceData = (Vector)invoiceDataObj;
        num.setText(invoiceData.get(0).toString());
        date.setText(invoiceData.get(1).toString());
        customer.setText(invoiceData.get(2).toString());
        total.setText(invoiceData.get(3).toString());
    }

    public void initiallyLoadInvoicesData(String[][] invoicesData, DefaultTableModel invoicesTableModel,
                                          DefaultTableModel invoiceItemsTableModel, JTable invoiceItemsTable ) {
        try {
            invoicesData = Controller.preread("invoices");
            addInvoicesToTable(invoicesData,invoicesTableModel );

            invoicesData = Controller.preread("items");
            addItemsToTable(invoicesData,invoiceItemsTableModel, invoiceItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void addInvoicesToTable(String[][] rows,DefaultTableModel invoicesTableModel){
        invoicesTableModel.getDataVector().removeAllElements();
        for (String[] row : rows) {
            invoicesTableModel.addRow(row);
        }
//        invoicesTable.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
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
