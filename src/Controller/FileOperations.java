package Controller;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperations {
    public int fileSize;
    public String returnedData;
    public byte[] bytes;
    public FileInputStream fileInput;
    public FileOutputStream fileOutput;

    public String readFileContent(String filePath) throws IOException {
        fileInput = new FileInputStream(filePath);
        fileSize = fileInput.available();
        bytes = new byte[fileSize];
        fileInput.read(bytes);
        returnedData= new String(bytes);
        System.out.println(returnedData);
        fileInput.close();
        return returnedData;
    }

    public void writeFile(String path, JTable table, Boolean headers) throws IOException {
        TableModel m = table.getModel();
        if(!path.endsWith(".csv")){
            path = path + ".csv";
        }
        FileWriter fw = new FileWriter(path);
//        if(headers){
//            /*Writing Col names*/
//            for(int i = 0; i < m.getColumnCount(); i++){
//                fw.write(m.getColumnName(i) + ",");
//            }
//            fw.write("\n");
//
//        }
        /*Writing row names*/
        for(int i = 0; i < m.getRowCount(); i++) {
            for(int j=0; j < m.getColumnCount()-1; j++) {
                fw.write(m.getValueAt(i,j) +",");
            }
            fw.write("\n");
        }
        fw.close();
    }

}
