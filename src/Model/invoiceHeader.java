package Model;

public class invoiceHeader {
    private String invoiceNumber;
    private String filePath;
    private invoiceLines[] invoiceItems;
    private int invoiceItemsCounter;

    private int totalInvoicePrice = 0;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public invoiceLines[] getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(invoiceLines[] invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public int getInvoiceItemsCounter() {
        return invoiceItemsCounter;
    }

    public void setInvoiceItemsCounter(int invoiceItemsCounter) {
        this.invoiceItemsCounter = invoiceItemsCounter;
    }

    public int getTotalInvoicePrice() {
        return totalInvoicePrice;
    }

    public void setTotalInvoicePrice(int totalInvoicePrice) {
        this.totalInvoicePrice = totalInvoicePrice;
    }
}
