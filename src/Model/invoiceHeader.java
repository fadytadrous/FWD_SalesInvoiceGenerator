package Model;

public class invoiceHeader {
    private String invoiceNumber;
    private String filePath;
    private invoiceLines[] invoiceItems;
    private int invoiceItemsCounter;

    private int totalInvoicePrice = 0;
}
