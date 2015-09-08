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
    File fXmlFile, rXmlFile;
    ConnectDB connection;
    String p1, p2, p3, lp1;
    Connection con;
    JFrame decision;

    Sync_1(String xmlFile) {
        try {
            fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            System.out.println(xmlFile.charAt(xmlFile.length() - 5));
            if (xmlFile.charAt(xmlFile.length() - 5) == '1') {
                p1 = "jdbc:postgresql://localhost:5432/MyDB2";   //bd
                lp1 = "jdbc:postgresql://localhost:5432/MyDB1";
                rXmlFile = new File("/SyncData/file_2.xml");
            } else if (xmlFile.charAt(xmlFile.length() - 5) == '2') {
                p1 = "jdbc:postgresql://localhost:5432/MyDB1";   //bd
                lp1 = "jdbc:postgresql://localhost:5432/MyDB2";
                rXmlFile = new File("/SyncData/file_1.xml");
            } else {
                System.exit(0);
            }
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
                        deleteOperation(tableName, key, query, vts); ///////
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
        try {
            if (connection.printResultSet(rs) > 0) {                         // regito já existe
                int ver = connection.getVts(rs);
                if (ver == 1) {                           // não existem updates
                    if (JOptionPane.showConfirmDialog(decision, "Encontrado: " + table + " -- " + connection.getResultset() + "\n"
                            + "Substituir por: " + query, null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // apaga registo no destino e volta a criar
                        connection.executeSQLCommand("DELETE FROM " + table + " WHERE " + connection.getTablePKeys(table) + "= '" + key + "'");
                        connection.executeSQLCommand(query);
                        alterVts(table, key, 1);
                    } else {
                        alterVts(table, key, -1);
                    }
                } else if (ver < 1) {
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

    private void deleteOperation(String table, String key, String query, String vts) {
        String sqlQuery = "SELECT * FROM " + table + " WHERE (" + connection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet rs = connection.executeSQLCommand(sqlQuery);
        int nRows;
        try {
            nRows = connection.printResultSet(rs);
            if (nRows == 0) {                         // regito não existe
                JOptionPane.showMessageDialog(decision, "O registo " + table + " -- " + " " + connection.getTablePKeys(table) + " " + key
                        + " não existe no destino");
            } else {
                int vt = connection.getVts(rs);
                if (vt < Integer.parseInt(vts)) {
                    connection.executeSQLCommand(query); // lida do xml 
                } else {
                    rs.first();
                    String modifiedQuery = "INSERT INTO " + table + " VALUES ('";
                    int idx = 1;
                    while (idx <= rs.getMetaData().getColumnCount()) {
                        System.out.println(rs.getString(idx));
                        modifiedQuery += rs.getString(idx);
                        System.out.println(modifiedQuery);
                        if (idx < rs.getMetaData().getColumnCount()) {
                            modifiedQuery += "','";
                        }
                        idx++;
                    }
                    modifiedQuery += "')";
                    System.out.println(modifiedQuery);
                    writeLocalBD(modifiedQuery);
                }
            }
        } catch (SQLException ex) {
            connection.printSQLException(ex);
        }
    }

    private void updateOperation(String table, String key, String query, String vts) {

        String sqlQuery = "SELECT * FROM " + table + " WHERE (" + connection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet rs = connection.executeSQLCommand(sqlQuery);
        int nRows;
        int rXmlVts = 0;        //
        try {
            nRows = connection.printResultSet(rs);
            if (nRows == 0) {                         // regito não existe
//                if (JOptionPane.showConfirmDialog(decision, "O registo " + table + " -- " + connection.getTablePKeys(table) + " " + key
//                        + " não existe no destino. Pretende cria-lo?", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                    String modifiedQuery = "INSERT INTO " + table + " VALUES (";
//                    String[] test = query.split("'");
//
//                    modifiedQuery += "'" + key + "','" + test[1] + "','" + Integer.parseInt(vts) + "')";
//                    System.out.println("MODIFY  " + modifiedQuery);
//                    connection.executeSQLCommand(modifiedQuery); // nova instrução               
//                } else {                                   // delete em localBD
//                    String[] value1 = sqlQuery.split("\\*");
//                    writeLocalBD("DELETE " + value1[1]);
//                }
                rXmlVts = (getVtsFromDeletedValue(rXmlFile, table, "-", key));
                String[] aux = query.split("'");
                if (rXmlVts <= Integer.parseInt(vts)){
                    String modifiedQuery = "INSERT INTO " + table + " VALUES (";                    
                    modifiedQuery += "'" + key + "','" + aux[1] + "','" + Integer.parseInt(vts) + "')";
                    connection.executeSQLCommand(modifiedQuery); // nova instrução                   
                }
                else {
                    String[] value1 = sqlQuery.split("\\*");
                    writeLocalBD("DELETE " + value1[1]);
                }
            } else {
                int vtsVal = connection.getVts(rs);
                if (Integer.parseInt(vts) > vtsVal) {
                    System.out.println("VTS " + vtsVal);
                    connection.executeSQLCommand(query); // lida do xml
                } else if (Integer.parseInt(vts) == vtsVal) {
                    String[] value1 = query.split("SET");
                    String[] value2 = value1[1].split("WHERE");
                    if (JOptionPane.showConfirmDialog(decision, "O registo " + table + " -- " + " "
                            + " " + connection.getResultset() + " existe com a mesma versão.\n "
                            + "Pretende substituir por " + value2[0], null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        connection.executeSQLCommand(query);
                    } else {
                        System.out.println(value1[0]);
                        vtsVal--;
                        query = value1[0] + "SET ver='" + vtsVal + "' " + "WHERE " + value2[1];
                        writeLocalBD(query);
                    }
                }
            }
        } catch (SQLException ex) {
            connection.printSQLException(ex);
        } catch (IOException ex) {
            System.err.println("Ficheiro inexistente");
            ex.printStackTrace();
        }
    }

    void alterVts(String table, String key, int vts) {
        int vt = 1;
        vt += vts;
        ConnectDB localConnection = new ConnectDB(lp1, p2, p3);
        String localSqlQuery = "SELECT * FROM " + table + " WHERE (" + localConnection.getTablePKeys(table) + " = '" + key + "')";
        ResultSet localRs = localConnection.executeSQLCommand(localSqlQuery);
        int rs = localConnection.getVts(localRs);
        if (rs == 1 && vts == 1) {
            localConnection.executeSQLCommand("UPDATE " + table + " SET ver = '" + vt + "' WHERE ("
                    + localConnection.getTablePKeys(table) + " = '" + key + "')");
            connection.executeSQLCommand("UPDATE " + table + " SET ver = '" + vt + "' WHERE ("
                    + connection.getTablePKeys(table) + " = '" + key + "')");
        }
        if (rs == 1 && vts == -1) {
            localConnection.executeSQLCommand("UPDATE " + table + " SET ver = '" + vt + "' WHERE ("
                    + localConnection.getTablePKeys(table) + " = '" + key + "')");
        }
    }

    void writeLocalBD(String query) {
        ConnectDB localConnection = new ConnectDB(lp1, p2, p3);
        ResultSet localRs = localConnection.executeSQLCommand(query);
        localConnection.closeDB();
    }

    int getVtsFromDeletedValue(File rXml, String table, String op, String k) throws IOException {
        int vtsVal = 0;
        DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder rdBuilder = dbFac.newDocumentBuilder();
            Document doc = rdBuilder.parse(rXml);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Tabela");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                System.out.println("\nCurrent Element :" + n.getNodeName() + " " + n.getChildNodes().item(0).getTextContent() + " " + n.getChildNodes().item(1).getTextContent());
                String tableName = n.getChildNodes().item(0).getTextContent();
                String operation = n.getChildNodes().item(1).getTextContent();
                String vts = n.getChildNodes().item(2).getTextContent();
                String key = n.getChildNodes().item(3).getTextContent();
                String query = n.getChildNodes().item(4).getTextContent();               
                if (tableName.equals(table) && operation.equals(op) && key.equals(k)){
                    if (Integer.parseInt(vts) > vtsVal)
                        vtsVal = Integer.parseInt(vts);
                }
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Sync_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Sync_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vtsVal;
    }
}
