/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author DELL
 */
public class InvoRecordDialog extends JDialog{
    private JTextField itemName;
    private JTextField itemCount;
    private JTextField itemPrice;
    private JLabel itemNameLabel1;
    private JLabel itemCountLabel1;
    private JLabel itemPriceLbl;
    private JButton okBtn;
    private JButton cancelBtn;
    
    public InvoRecordDialog(InvoFrame frame) {
        itemName = new JTextField(20);
        itemNameLabel1 = new JLabel("Item Name");
        
        itemCount = new JTextField(20);
        itemCountLabel1 = new JLabel("Item Count");
        
        itemPrice = new JTextField(20);
        itemPriceLbl = new JLabel("Item Price");
        
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        
        okBtn.setActionCommand("createLineOK");
        cancelBtn.setActionCommand("createLineCancel");
        
        okBtn.addActionListener(frame.getListener());
        cancelBtn.addActionListener(frame.getListener());
        setLayout(new GridLayout(4, 2));
        
        add(itemNameLabel1);
        add(itemName);
        add(itemCountLabel1);
        add(itemCount);
        add(itemPriceLbl);
        add(itemPrice);
        add(okBtn);
        add(cancelBtn);
        
        pack();
    }

    public JTextField getItemNameField() {
        return itemName;
    }

    public JTextField getItemCountField() {
        return itemCount;
    }

    public JTextField getItemPriceField() {
        return itemPrice;
    }
}
