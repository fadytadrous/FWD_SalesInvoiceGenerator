package com.GUI;

import com.Controller.Controller;
import com.Controller.DateCellRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class GUI extends JFrame implements ActionListener {
    private final JPanel leftSidePanel;
    private final JPanel rightSidePanel;
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem loadMenuItem;
    private final JMenuItem saveMenuItem;

    private final JPanel leftSideBottomPanel;
    private final JPanel rightSideBottomPanel;
    private final JPanel rightSideTopPanel;
    private final JPanel invoiceNumPanel;
    private final JPanel invoiceDatePanel;
    private final JPanel customerNamePanel;
    private final JPanel invoiceTotalPanel;
    private final JPanel invoiceItemsTableLabelPanel;
    private final JTable invoicesTable;
    private final JTable invoiceItemsTable;
    private final JLabel invoicesTableLabel;
    private final JLabel invoiceItemsLabel;
    private final JLabel invoiceNumLabel;
    private final JLabel invoiceNumValue;
    private final JLabel invoiceDateLabel;
    private final JTextField invoiceDateInput;
    private final JLabel customerNameLabel;
    private final JTextField customerNameInput;
    private final JLabel invoiceTotalLabel;
    private final JLabel invoiceTotalValue;
    private final DefaultTableModel invoicesTableModel;
    private final DefaultTableModel invoiceItemsTableModel;
    private final JButton createNewInvoiceBtn;
    private final JButton deleteInvoiceBtn;
    private final JButton saveChangesBtn;
    private final JButton cancelChangesBtn;
    private final String[] invoicesTableColumns = {"No.","Date","Customer","Total"};
    private final String[] invoiceItemsTableColumns = {"No.","Item Name","Item Price","Count","Item Total"};
    private String filePath;
    private final Controller Controller = new Controller();
    private String[][] invoicesData;

    public GUI(){
        super("SIG App");
        setSize(1000,600);
        setLocation(130,40);
        setLayout(new GridLayout(0,2));

        menuBar = new JMenuBar();
        loadMenuItem = new JMenuItem("Load File");
        saveMenuItem = new JMenuItem("Save File");

        fileMenu = new JMenu("File");
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        leftSidePanel = new JPanel();
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel,BoxLayout.Y_AXIS));

        rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BoxLayout(rightSidePanel,BoxLayout.Y_AXIS));

        leftSideBottomPanel = new JPanel();
        rightSideBottomPanel = new JPanel();
        rightSideTopPanel = new JPanel();
        rightSideTopPanel.setLayout(new BoxLayout(rightSideTopPanel,BoxLayout.Y_AXIS));
        invoiceNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invoiceDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invoiceTotalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invoiceItemsTableLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invoicesTableLabel = new JLabel("Invoices Table");
        invoiceItemsLabel = new JLabel("Invoice Items");
        invoiceNumLabel = new JLabel("Invoice Number");
        invoiceNumValue = new JLabel("0");
        invoiceDateLabel = new JLabel("Invoice Date");
        invoiceDateInput = new JTextField(30);
        customerNameLabel = new JLabel("Customer Name");
        customerNameInput = new JTextField(30);
        invoiceTotalLabel = new JLabel("Invoice Total");
        invoiceTotalValue = new JLabel("0.00");
        invoiceDateInput.setSize(20,7);
        saveChangesBtn = new JButton("Save");
        cancelChangesBtn = new JButton("Cancel");
        createNewInvoiceBtn = new JButton("Create New Invoice");
        deleteInvoiceBtn = new JButton("Delete Invoice");
        /*Tables*/
        invoicesTableModel = new DefaultTableModel();
        invoicesTable = new JTable(invoicesTableModel);
        invoiceItemsTableModel = new DefaultTableModel();
        invoiceItemsTable = new JTable(invoiceItemsTableModel);
        invoiceItemsTableModel.addColumn(invoiceItemsTableColumns[0]);
        for (int i = 0;i<=3;i++){
            invoicesTableModel.addColumn(invoicesTableColumns[i]);
            invoiceItemsTableModel.addColumn(invoiceItemsTableColumns[i+1]);
        }



        loadMenuItem.setActionCommand("L");
        saveMenuItem.setActionCommand("S");
        saveChangesBtn.setActionCommand("I");
        cancelChangesBtn.setActionCommand("C");
        createNewInvoiceBtn.setActionCommand("N");
        deleteInvoiceBtn.setActionCommand("D");


        loadMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        saveChangesBtn.addActionListener(this);
        cancelChangesBtn.addActionListener(this);
        createNewInvoiceBtn.addActionListener(this);
        deleteInvoiceBtn.addActionListener(this);

        invoicesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInvoicesTableRowDetails();
            }
        });

        setJMenuBar(menuBar);
        add(leftSidePanel);
        add(rightSidePanel);

        leftSidePanel.add(new JScrollPane(invoicesTable));
        leftSideBottomPanel.add(createNewInvoiceBtn);
        leftSideBottomPanel.add(deleteInvoiceBtn);
        leftSidePanel.add(leftSideBottomPanel);
        invoiceNumPanel.add(invoiceNumLabel);
        invoiceNumPanel.add(invoiceNumValue);
        invoiceDatePanel.add(invoiceDateLabel);
        invoiceDatePanel.add(invoiceDateInput);
        customerNamePanel.add(customerNameLabel);
        customerNamePanel.add(customerNameInput);
        invoiceTotalPanel.add(invoiceTotalLabel);
        invoiceTotalPanel.add(invoiceTotalValue);
        rightSideTopPanel.add(invoiceNumPanel);
        rightSideTopPanel.add(invoiceDatePanel);
        rightSideTopPanel.add(customerNamePanel);
        rightSideTopPanel.add(invoiceTotalPanel);
        invoiceItemsTableLabelPanel.add(invoiceItemsLabel);
        rightSideTopPanel.add(invoiceItemsTableLabelPanel);
        rightSidePanel.add(rightSideTopPanel);
        rightSidePanel.add(new JScrollPane(invoiceItemsTable));
        rightSideBottomPanel.add(saveChangesBtn);
        rightSideBottomPanel.add(cancelChangesBtn);
        rightSidePanel.add(rightSideBottomPanel);

        initiallyLoadInvoicesData();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void showInvoicesTableRowDetails(){
        var invoiceData =  invoicesTableModel.getDataVector().get(invoicesTable.getSelectedRow());
        invoiceNumValue.setText(invoiceData.get(0).toString());
        invoiceDateInput.setText(invoiceData.get(1).toString());
        customerNameInput.setText(invoiceData.get(2).toString());
        invoiceTotalValue.setText(invoiceData.get(3).toString());
    }

    public void initiallyLoadInvoicesData() {
        try {
            invoicesData = Controller.preread("invoices");
            addInvoicesToTable(invoicesData);

            invoicesData = Controller.preread("items");
            addItemsToTable(invoicesData);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void saveInvoiceItemsChanges() {
        try {
            Controller.saveItems(invoiceItemsTable);
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setFileFilter( new FileNameExtensionFilter("CSV File","csv"));

        switch(e.getActionCommand()){
            case "L":
                JOptionPane.showMessageDialog(
                        null,"Please Add Invoice Header"
                        ,"Info",JOptionPane.PLAIN_MESSAGE);

                if(fileSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                    filePath=fileSelector.getSelectedFile().getPath();
                    if (!filePath.endsWith(".csv")){
                        JOptionPane.showMessageDialog(null,"Wrong File format",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    try {
                        invoicesData = Controller.loadFile(filePath);
                        addInvoicesToTable(invoicesData);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage(),
                                "Error",JOptionPane.PLAIN_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(
                        null,"Please Add Invoice Items"
                        ,"Info",JOptionPane.PLAIN_MESSAGE);

                if(fileSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    filePath=fileSelector.getSelectedFile().getPath();
                    if (!filePath.endsWith(".csv")){
                        JOptionPane.showMessageDialog(null,"Wrong File format",
                                "Error",JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    try {
                        invoicesData = Controller.loadFile(filePath);
                        addItemsToTable(invoicesData);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage(),
                                "Error",JOptionPane.PLAIN_MESSAGE);
                    }
                }
                break;

            case "S":
                if(fileSelector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    filePath=fileSelector.getSelectedFile().getPath();
                    try {
                        Controller.saveInvoicesToFile(filePath,invoicesTable);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
                    }
                }
                break;

            case "I":
                saveInvoiceItemsChanges();
                break;

            case "C":
                try {
                    invoicesData = Controller.preread("items");
                    addItemsToTable(invoicesData);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Error",JOptionPane.PLAIN_MESSAGE);
                }
                break;

            case "N":
                invoicesTableModel.addRow(new String[]{"", "", "", ""});
                break;

            case "D":
                if(invoicesTable.getSelectedRow()!=-1) {
                    invoicesTableModel.removeRow(invoicesTable.getSelectedRow());
                }
                break;

            default:
                break;
        }
    }

    public void addInvoicesToTable(String[][] rows){
        invoicesTableModel.getDataVector().removeAllElements();
        for (var row : rows) {
            invoicesTableModel.addRow(row);
        }
//        invoicesTable.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
    }

    public void addItemsToTable(String[][] rows){
        invoiceItemsTableModel.getDataVector().removeAllElements();
        for (var row : rows) {
            invoiceItemsTableModel.addRow(row);

        }
        fillItemsTotal();
    }

    public void fillItemsTotal(){
        for (int i=0; i< invoiceItemsTable.getRowCount();i++)
        {
            float price = Float.parseFloat(invoiceItemsTable.getValueAt(i,2).toString());
            int no = Integer.parseInt(invoiceItemsTable.getValueAt(i,3).toString());
            invoiceItemsTable.setValueAt(price*no, i, 4);
        }

    }

}
