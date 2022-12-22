
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoCustomer {
    private int invNum;
    private String custName;
    private Date invDate;
    private ArrayList<InvoRecord> lines;  

    public InvoCustomer(int invNum, String customerName, Date invDate) {
        this.invNum = invNum;
        this.custName = customerName;
        this.invDate = invDate;
        //this.lines = new ArrayList<>();   // eager creation
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public int getInvNum() {
        return invNum;
    }

    public void setInvNum(int invNum) {
        this.invNum = invNum;
    }

    public String getCustomerName() {
        return custName;
    }

    public void setCustomerName(String customerName) {
        this.custName = customerName;
    }

    @Override
    public String toString() {
        String str = "InvoiceHeader{" + "invNum=" + invNum + ", customerName=" + custName + ", invDate=" + invDate + '}';
        for (InvoRecord line : getLines()) {
            str += "\n\t" + line;
        }
        return str;
    }

    public ArrayList<InvoRecord> getLines() {
        if (lines == null)
            lines = new ArrayList<>();  // lazy creation
        return lines;
    }

    public void setLines(ArrayList<InvoRecord> lines) {
        this.lines = lines;
    }

    public double getInvTotal() {
        double total = 0.0;
        for (InvoRecord line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    public void addInvLine(InvoRecord line) {
        getLines().add(line);
    }
    
    public String getDataAsCSV() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return "" + getInvNum() + "," + df.format(getInvDate()) + "," + getCustomerName();
    }
    
}
