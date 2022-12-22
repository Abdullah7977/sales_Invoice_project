/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.InvoCustomer;
import model.CustomerInvoGridModel;
import model.InvoRecord;
import model.InvoRecordsGridModel;
import views.InvoCustomerDialog;
import views.InvoRecordDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import views.InvoFrame;

/**
 *
 * @author DELL
 */
public class ProductsInvoListener implements ActionListener, ListSelectionListener  {
    private InvoFrame frame;
    private DateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy");
    
    public ProductsInvoListener(InvoFrame frame) {
        this.frame = frame;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "CreateNewInvoice":
                displayNewInvoiceDialog();
                break;
            case "DeleteInvoice":
                deleteInvoice();
                break;
            case "CreateNewLine":
                displayNewLineDialog();
                break;
            case "DeleteLine":
                deleteLine();
                break;
            case "LoadFile":
                loadFile();
                break;
            case "SaveFile":
                saveData();
                break;
            case "createInvCancel":
                createInvCancel();
                break;
            case "createInvOK":
                createInvOK();
                break;
            case "createLineCancel":
                createInvoiceCancel();
                break;
            case "createLineOK":
                createLineOK();
                break;
        }
    }

    
    private void loadFile() {
        JOptionPane.showMessageDialog(frame, "Please, select invoices List file!", "Attension", JOptionPane.WARNING_MESSAGE);
        JFileChooser openFile = new JFileChooser();
        int result = openFile.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = openFile.getSelectedFile();
            try {
                FileReader headerFr = new FileReader(headerFile);
                BufferedReader headerBr = new BufferedReader(headerFr);
                String headerLine = null;

                while ((headerLine = headerBr.readLine()) != null) {
                    String[] headerParts = headerLine.split(",");
                    String invNumStr = headerParts[0];
                    String invDateStr = headerParts[1];
                    String custName = headerParts[2];

                    int invNum = Integer.parseInt(invNumStr);
                    Date invDate = dateFormate.parse(invDateStr);

                    InvoCustomer inv = new InvoCustomer(invNum, custName, invDate);
                    frame.getInvoListing().add(inv);
                }

                JOptionPane.showMessageDialog(frame, "Please, select Item List Info file!", "Attension", JOptionPane.WARNING_MESSAGE);
                result = openFile.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = openFile.getSelectedFile();
                    BufferedReader linesBr = new BufferedReader(new FileReader(linesFile));
                    String linesLine = null;
                    while ((linesLine = linesBr.readLine()) != null) {
                        String[] lineParts = linesLine.split(",");
                        String invNumStr = lineParts[0];
                        String itemName = lineParts[1];
                        String itemPriceStr = lineParts[2];
                        String itemCountStr = lineParts[3];

                        int invNum = Integer.parseInt(invNumStr);
                        double itemPrice = Double.parseDouble(itemPriceStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        InvoCustomer header = findInvoiceByNum(invNum);
                        InvoRecord invLine = new InvoRecord(itemName, itemPrice, itemCount, header);
                        header.getLines().add(invLine);
                    }
                   
                    frame.setClientInvoTable(new CustomerInvoGridModel(frame.getInvoListing()));
                   //frame.getInvoicesTable.setModel(frame.getInvoiceHeaderTableModel());
                    frame.getInvoGrid().setModel(frame.getClientInvoGridModel());
                   
                    frame.getInvoGrid().validate();
                }
                System.out.println("Check");
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Date Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayInvoices();
    }
    public void defultinvoicesGrid() {
        
                   
                    frame.setClientInvoTable(new CustomerInvoGridModel(frame.getInvoListing()));
                   //frame.getInvoicesTable.setModel(frame.getInvoiceHeaderTableModel());
                    frame.getInvoGrid().setModel(frame.getClientInvoGridModel());
                   
                    frame.getInvoGrid().validate();
               
                    displayInvoices();
    }

    private void saveData() {
        String headers = "";
        String lines = "";
        for (InvoCustomer header : frame.getInvoListing()) {
            headers += header.getDataAsCSV();
            headers += "\n";
            for (InvoRecord line : header.getLines()) {
                lines += line.getDataAsCSV();
                lines += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, "Please, select file to save header data!", "Attension", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = fileChooser.getSelectedFile();
            try {
                FileWriter hFW = new FileWriter(headerFile);
                hFW.write(headers);
                hFW.flush();
                hFW.close();

                JOptionPane.showMessageDialog(frame, "Please, select file to save lines data!", "Attension", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(linesFile);
                    lFW.write(lines);
                    lFW.flush();
                    lFW.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(frame, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

    }

    private InvoCustomer findInvoiceByNum(int invNum) {
        InvoCustomer header = null;
        for (InvoCustomer inv : frame.getInvoListing()) {
            if (invNum == inv.getInvNum()) {
                header = inv;
                break;
            }
        }
        return header;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoice Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = frame.getInvoGrid().getSelectedRow();
        if (selectedRowIndex >= 0) {
            InvoCustomer row = frame.getClientInvoGridModel().getInvoicesList().get(selectedRowIndex);
            frame.getClientNameTF().setText(row.getCustomerName());
            frame.getInvoDateTF().setText(dateFormate.format(row.getInvDate()));
            frame.getInvoNumLbl().setText("" + row.getInvNum());
            frame.getInvoTotalLbl().setText("" + row.getInvTotal());
            ArrayList<InvoRecord> lines = row.getLines();
            frame.setInvoRecordsGridModel(new InvoRecordsGridModel(lines));
            frame.getInvoLnsTable().setModel(frame.getInvoLnsTableModel());
            frame.getInvoLnsTableModel().fireTableDataChanged();
        }
    }

    private void displayNewInvoiceDialog() {
        frame.setClientInvoDialog(new InvoCustomerDialog(frame));
        frame.getCustomerDialog().setVisible(true);
    }

    private void displayNewLineDialog() {
        frame.setInvoDialog(new InvoRecordDialog(frame));
        frame.getInvoDialog().setVisible(true);
    }

    private void createInvCancel() {
        frame.getCustomerDialog().setVisible(false);
        frame.getCustomerDialog().dispose();
        frame.setClientInvoDialog(null);
    }

    private void createInvOK() {
        
        
        String custName = frame.getCustomerDialog().getCustNameField().getText();
        
        
        
       
        String invDateStr = frame.getCustomerDialog().getInvDateField().getText();
        frame.getCustomerDialog().setVisible(false);
        frame.getCustomerDialog().dispose();
        frame.setClientInvoDialog(null);
        try {
            Date invDate = dateFormate.parse(invDateStr);
            int invNum = getNextInvoiceNumber();
            InvoCustomer invoiceHeader = new InvoCustomer(invNum, custName, invDate);
            
           if(custName.equals("") || custName.isEmpty()){
        
            JOptionPane.showMessageDialog(frame, "Please Enter Customer Name", "Warning", JOptionPane.WARNING_MESSAGE);
           
        
        
        }else{
            
            frame.getInvoListing().add(invoiceHeader);
            frame.getClientInvoGridModel().fireTableDataChanged();
            
           }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        displayInvoices();
    }

    private int getNextInvoiceNumber() {
        int max = 0;
        for (InvoCustomer header : frame.getInvoListing()) {
            if (header.getInvNum() > max) {
                max = header.getInvNum();
            }
        }
        return max + 1;
    }

    private void createInvoiceCancel() {
        frame.getInvoDialog().setVisible(false);
        frame.getInvoDialog().dispose();
        frame.setInvoDialog(null);
    }

    private void createLineOK() {
        
        
        try{
        String itemName = frame.getInvoDialog().getItemNameField().getText();
        String itemCountStr = frame.getInvoDialog().getItemCountField().getText();
        String itemPriceStr = frame.getInvoDialog().getItemPriceField().getText();
        frame.getInvoDialog().setVisible(false);
        frame.getInvoDialog().dispose();
        frame.setInvoDialog(null);
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = frame.getInvoGrid().getSelectedRow();
        InvoCustomer invoice = frame.getClientInvoGridModel().getInvoicesList().get(headerIndex);

        InvoRecord invoiceLine = new InvoRecord(itemName, itemPrice, itemCount, invoice);
        invoice.addInvLine(invoiceLine);
        frame.getInvoLnsTableModel().fireTableDataChanged();
        frame.getClientInvoGridModel().fireTableDataChanged();
        frame.getInvoTotalLbl().setText("" + invoice.getInvTotal());
        
        }catch(Exception ex){
            
         JOptionPane.showMessageDialog(frame, "please select customer invoice to add the item", "Warning", JOptionPane.WARNING_MESSAGE);
         ex.printStackTrace();
        
        }
        displayInvoices();
    }

    private void deleteInvoice() {
        
        
        try{
        
        int invIndex = frame.getInvoGrid().getSelectedRow();
        InvoCustomer header = frame.getClientInvoGridModel().getInvoicesList().get(invIndex);
        frame.getClientInvoGridModel().getInvoicesList().remove(invIndex);
        frame.getClientInvoGridModel().fireTableDataChanged();
        frame.setInvoRecordsGridModel(new InvoRecordsGridModel(new ArrayList<InvoRecord>()));
        frame.getInvoLnsTable().setModel(frame.getInvoLnsTableModel());
        frame.getInvoLnsTableModel().fireTableDataChanged();
        frame.getClientNameTF().setText("");
        frame.getInvoDateTF().setText("");
        frame.getInvoNumLbl().setText("");
        frame.getInvoTotalLbl().setText("");
        displayInvoices();
        
        }catch(Exception ex){
            
            JOptionPane.showMessageDialog(frame, "please select the customer invoice which added to delete it", "Warning", JOptionPane.WARNING_MESSAGE);
             ex.printStackTrace();
        
        }
        
    }

    private void deleteLine() {
        
        
  try{
        int lineIndex = frame.getInvoLnsTable().getSelectedRow();
        InvoRecord line = frame.getInvoLnsTableModel().getInvoLines().get(lineIndex);
        frame.getInvoLnsTableModel().getInvoLines().remove(lineIndex);
        frame.getInvoLnsTableModel().fireTableDataChanged();
        frame.getClientInvoGridModel().fireTableDataChanged();
        frame.getInvoTotalLbl().setText("" + line.getHeader().getInvTotal());
       
  } catch (Exception ex){
        
        JOptionPane.showMessageDialog(frame, "please select the item which added to delete it", "Warning", JOptionPane.WARNING_MESSAGE);
         ex.printStackTrace();
        }
  
   displayInvoices();
    }

    private void displayInvoices() {
        System.out.println("***************************");
        for (InvoCustomer header : frame.getInvoListing()) {
            System.out.println(header);
        }
        System.out.println("***************************");
    }
    
}
