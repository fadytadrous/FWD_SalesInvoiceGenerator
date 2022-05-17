package Controller;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;

public class DateCellRenderer extends DefaultTableCellRenderer {
    public DateCellRenderer() { super(); }

    @Override
    public void setValue(Object value) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        setText((value == null) ? "" : sdf.format(value));
    }
}
