package postgreconnect;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Sync_1 {

    DocumentBuilder dBuilder;
    File fXmlFile;
    ConnectDB connection;
    String p1, p2, p3;
    Connection con;
    JFrame decision;

    Sync_1(String xmlFile) {
        try {
            fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            p1 = "jdbc:postgresql://localhost:5432/MyDB2";   //bd
            p2 = "postgres";                                //user
            p3 = "nuno";
            connection = new ConnectDB(p1, p2, p3);
            con = connection.getCon();

            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Sync_1.class.getName()).log(Level.SEVERE, null, ex);
            }

            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Tabela");

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                System.out.println("\nCurrent Element :" + n.getNodeName() + " " + n.getChildNodes().item(0).getTextContent() + " " + n.getChildNodes().item(1).getTextContent());
                String tableName = n.getChildNodes().item(0).getTextContent();
                String operation = n.getChildNodes().item(1).getTextContent();
                String vts = n.getChildNodes().item(2).getTextContent();
                String key = n.getChildNodes().item(3).getTextContent();
                String query = n.getChildNodes().item(4).getTextContent();
                switch (operation) {
                    case "+":
                        insertOperation(tableName, key, query);
                        break;
                    case "-":
                        deleteOperation(tableName, key, query);
                        break;
                    case "*":
                        updateOperation(tableName, key, query, vts);
                        break;
                }
            }
        } catch (SAXException | IOException ex) {
            Logger.getLogger(Sync_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertOperation(String table, String key, String query) {
        String sqlQuery = "SELECT * FROM " + table + " WHERE (" + connection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet rs = connection.executeSQLCommand(sqlQuery);
        int nRows;
        try {
            nRows = connection.printResultSet(rs);
            if (nRows > 0) {                         // regito já existe
                if (JOptionPane.showConfirmDialog(decision, "Encontrado: " + table + " -- " + connection.getResultset() + "\n"
                        + "Substituir por: " + query, null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // apaga registo no destino e volta a criar
                    connection.executeSQLCommand("DELETE FROM " + table + " WHERE " + connection.getTablePKeys(table) + "= '" + key + "'");
                    connection.executeSQLCommand(query);
                }
            } else {
                connection.executeSQLCommand(query); // lida do xml 
            }
        } catch (SQLException ex) {
            connection.printSQLException(ex);
        }
    }

    private void deleteOperation(String table, String key, String query) {
        String sqlQuery = "SELECT * FROM " + table + " WHERE (" + connection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet rs = connection.executeSQLCommand(sqlQuery);
        int nRows;
        try {
            nRows = connection.printResultSet(rs);
            if (nRows == 0) {                         // regito não existe
                JOptionPane.showMessageDialog(decision, "O registo " + table + " -- " + " " + connection.getTablePKeys(table) + " " + key
                        + " não existe no destino");

            } else {
                connection.executeSQLCommand(query); // lida do xml 
            }
        } catch (SQLException ex) {
            connection.printSQLException(ex);
        }
    }

    private void updateOperation(String table, String key, String query, String vts) {

        String sqlQuery = "SELECT * FROM " + table + " WHERE (" + connection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet rs = connection.executeSQLCommand(sqlQuery);
        int nRows;
        try {
            nRows = connection.printResultSet(rs);
            if (nRows == 0) {                         // regito não existe
//                JOptionPane.showMessageDialog(decision, "O registo " + table + " -- " + " " + connection.getTablePKeys(table) + " " + key
//                        + " não existe no destino");
                if (JOptionPane.showConfirmDialog(decision, "O registo " + table + " -- " +   connection.getTablePKeys(table) + " " + key
                        + " não existe no destino. Pretende cria-lo?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    String modifiedQuery = "INSERT INTO " + table + " VALUES (";
                    String[] test = query.split("'");

                    modifiedQuery += "'" + key + "','" + test[1] + "','" + Integer.parseInt(vts) + "')";
//                    for (int i =1; i<test.length; i+=2){
//                        modifiedQuery+="'"+test[i]+"'";                     
//                    }
                    System.out.println("MODIFY  " + modifiedQuery);
                    connection.executeSQLCommand(modifiedQuery); // nova instrução               
                }
            } else {
                int vtsVal = connection.getVts(rs);
                if (Integer.parseInt(vts) > vtsVal) {
                    System.out.println("VTS " + vtsVal);
                    connection.executeSQLCommand(query); // lida do xml
                } else if (Integer.parseInt(vts) == vtsVal) {
                    String [] value1 = query.split("SET");
                    String [] value2 = value1[1].split("WHERE");
                    if (JOptionPane.showConfirmDialog(decision, "O registo " + table + " -- " + " " + 
                            " " + connection.getResultset() +" existe com a mesma versão.\n " + 
                            "Pretende substituir por? " + value2[0], null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        connection.executeSQLCommand(query);
                    }
                }
            }
        } catch (SQLException ex) {
            connection.printSQLException(ex);
        }
    }
}
