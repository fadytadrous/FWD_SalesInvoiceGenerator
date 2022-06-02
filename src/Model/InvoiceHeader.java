package Model;

public class InvoiceHeader {
    private String invoiceNumber;
    private String invoiceDate;
    private String customerName;
    private InvoiceLines[] invoiceItems;
    private int invoiceItemsCounter;

    public InvoiceHeader(String invoiceNumber, String invoiceDate, String customerName) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    private float totalInvoicePrice = 0;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public InvoiceLines[] getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(InvoiceLines[] invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public int getInvoiceItemsCounter() {
        return invoiceItemsCounter;
    }

    public void setInvoiceItemsCounter(int invoiceItemsCounter) {
        this.invoiceItemsCounter = invoiceItemsCounter;
    }

    public float getTotalInvoicePrice() {
        return totalInvoicePrice;
    }

    public void setTotalInvoicePrice(float totalInvoicePrice) {
        this.totalInvoicePrice = totalInvoicePrice;
    }
}
