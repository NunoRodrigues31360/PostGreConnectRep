package postgreconnect;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

public class AppGUI_1 extends javax.swing.JFrame {

    Connection con = null;
    String selectedTable;
    Statement stmt;
    ResultSet rs;
    int a;
    String key, p1, p2, p3, title, file, sql;
    ConstructXML fileResult;
    ConnectDB connection;
    DatabaseMetaData dbmd;
    boolean isPeople;

    public AppGUI_1(String bd, String user, String pass, String file, String title) {
        initComponents();
//        p1 = "jdbc:postgresql://localhost:5432/MyDB1";  //bd
//        p2 = "postgres";                                //user
//        p3 = "nuno";                                    //pass
        p1 = bd;                                  //bd
        p2 = user;                                //user
        p3 = pass; 
        this.file = file;
        connection = new ConnectDB(p1, p2, p3);
        con = connection.getCon();
        dbmd = connection.getDbmd();
        stmt = connection.getStmt();
        this.setTitle(title);
        ArrayList<String> keys = connection.getPKeys();
        System.out.println(keys.toString());
        try {
            //fileResult = new ConstructXML("E:/SyncData/file_1.xml");  
            fileResult = new ConstructXML(file); 
        } catch (TransformerException | SAXException | IOException ex) {
            Logger.getLogger(AppGUI_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tablesJCombo = new javax.swing.JComboBox();
        campo1 = new javax.swing.JTextField();
        campo2 = new javax.swing.JTextField();
        campo3 = new javax.swing.JTextField();
        insertBt = new javax.swing.JButton();
        updateBt = new javax.swing.JButton();
        deletetBt = new javax.swing.JButton();
        cancelBt = new javax.swing.JButton();
        PkLabel = new javax.swing.JLabel();
        nomeLabel = new javax.swing.JLabel();
        ordemLabel = new javax.swing.JLabel();
        syncBt = new javax.swing.JButton();
        campo4 = new javax.swing.JTextField();
        moradaLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(getTitle());
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tablesJCombo.setMinimumSize(new java.awt.Dimension(28, 30));
        tablesJCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablesJComboMouseClicked(evt);
            }
        });
        tablesJCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tablesJComboActionPerformed(evt);
            }
        });

        campo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campo1ActionPerformed(evt);
            }
        });

        campo3.setEditable(false);

        insertBt.setText("Insert");
        insertBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertBtActionPerformed(evt);
            }
        });

        updateBt.setText("Update");
        updateBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtActionPerformed(evt);
            }
        });

        deletetBt.setText("Delete");
        deletetBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletetBtActionPerformed(evt);
            }
        });

        cancelBt.setText("Cancel");
        cancelBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtActionPerformed(evt);
            }
        });

        PkLabel.setLabelFor(campo1);
        PkLabel.setText("Pk");

        nomeLabel.setLabelFor(campo2);
        nomeLabel.setText("Nome");

        ordemLabel.setLabelFor(campo3);
        ordemLabel.setText("Ordem");

        syncBt.setText("Sync");
        syncBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syncBtActionPerformed(evt);
            }
        });

        campo4.setToolTipText("");

        moradaLabel.setText("Morada");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(insertBt)
                        .addGap(59, 59, 59)
                        .addComponent(updateBt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deletetBt)
                        .addGap(63, 63, 63)
                        .addComponent(cancelBt))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(syncBt))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tablesJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(PkLabel))
                                    .addComponent(moradaLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(nomeLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(campo2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(campo1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(campo4)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(ordemLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campo3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128)))
                        .addGap(25, 25, 25)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tablesJCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PkLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomeLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(moradaLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ordemLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(syncBt)
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertBt)
                    .addComponent(updateBt)
                    .addComponent(deletetBt)
                    .addComponent(cancelBt))
                .addGap(32, 32, 32))
        );

        setBounds(450, 100, 506, 351);
    }// </editor-fold>//GEN-END:initComponents

    private void tablesJComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tablesJComboActionPerformed
        selectedTable = (String) tablesJCombo.getSelectedItem();
        System.out.println(selectedTable);    
        try {           
            rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
        if ("people".equalsIgnoreCase(selectedTable)){
            isPeople = true;
            moradaLabel.setVisible(isPeople);
            campo4.setVisible(isPeople);
        }
        else{
            isPeople = false;
            moradaLabel.setVisible(isPeople);
            campo4.setVisible(isPeople);
        }
        System.out.println(isPeople);
    }//GEN-LAST:event_tablesJComboActionPerformed

    private void tablesJComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablesJComboMouseClicked
        clearTxtField();
        ResultSet tables;
        tablesJCombo.removeAllItems();
        try {
            tables = dbmd.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                tablesJCombo.addItem(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
    }//GEN-LAST:event_tablesJComboMouseClicked

    private void campo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campo1ActionPerformed
        key = campo1.getText();
        System.out.println(key);
        try {
            rs.first();
            while (!rs.isAfterLast()) {
                String aux = rs.getString(1);
                System.out.println(aux);
                if (aux.equalsIgnoreCase(key)) {
                    campo2.setText(rs.getString(2));
                    campo3.setText(rs.getString(3));
                    campo4.setText(rs.getString(4));
                }
                rs.next();
            }
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
    }//GEN-LAST:event_campo1ActionPerformed

    private void insertBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBtActionPerformed
        try {
            campo3.setText("1");
            if (isPeople)
                sql = "INSERT INTO " + selectedTable + " VALUES ('" + campo1.getText() + "','"
                    + campo2.getText() + "','" + campo3.getText() + "','" + campo4.getText() + "')";
            else
                sql = "INSERT INTO " + selectedTable + " VALUES ('" + campo1.getText() + "','"
                    + campo2.getText() + "','" + campo3.getText() + "')";           
            System.out.print(sql);
            connection.executeSQLCommand(sql);
            fileResult.queryProcess(sql, selectedTable, campo1.getText(), campo3.getText());
            rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
            rs.refreshRow();
            clearTxtField();
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
    }//GEN-LAST:event_insertBtActionPerformed

    private void cancelBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtActionPerformed
        clearTxtField();
    }//GEN-LAST:event_cancelBtActionPerformed

    private void updateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtActionPerformed
        int order = Integer.parseInt(campo3.getText());
        order++;
        campo3.setText(Integer.toString(order));
        try {
            if (isPeople)
                sql = "UPDATE " + selectedTable + " SET " + rs.getMetaData().getColumnLabel(2) + "='"
                    + campo2.getText() + "'," + rs.getMetaData().getColumnLabel(3) + "='" + campo3.getText() +
                        "'," + rs.getMetaData().getColumnLabel(4) + "='" + campo4.getText() +"' WHERE("
                        + rs.getMetaData().getColumnName(1) + "='" + campo1.getText() + "')";
            else
                sql = "UPDATE " + selectedTable + " SET " + rs.getMetaData().getColumnLabel(2) + "='"
                    + campo2.getText() + "'," + rs.getMetaData().getColumnLabel(3) + "='" + campo3.getText() + "' WHERE("
                    + rs.getMetaData().getColumnName(1) + "='" + campo1.getText() + "')";
            connection.executeSQLCommand(sql);
            fileResult.queryProcess(sql, selectedTable, campo1.getText(), campo3.getText());
            rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
            rs.refreshRow();
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
        clearTxtField();
    }//GEN-LAST:event_updateBtActionPerformed

    private void deletetBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletetBtActionPerformed
        int order = Integer.parseInt(campo3.getText());
        order++;
        campo3.setText(Integer.toString(order));
        try {
            String sql = "DELETE FROM " + selectedTable + " WHERE("
                    + rs.getMetaData().getColumnName(1) + "='" + campo1.getText() + "')";
            connection.executeSQLCommand(sql);
            fileResult.queryProcess(sql, selectedTable, campo1.getText(), campo3.getText());
            rs = stmt.executeQuery("SELECT * FROM " + selectedTable);
            rs.refreshRow();
        } catch (SQLException ex) {
            ConnectDB.printSQLException(ex);
        }
        clearTxtField();
    }//GEN-LAST:event_deletetBtActionPerformed

    private void syncBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncBtActionPerformed
        //new Sync_1("E:/SyncData/file_1.xml");
        new Sync_1(file);
        try {
            JOptionPane.showMessageDialog(null, "Finalizado");
            //Files.delete(Paths.get(URI.create("file:/E:/SyncData/file_1.xml/")));
            Files.delete(Paths.get(URI.create("file:"+file)));
            //fileResult = new ConstructXML("E:/SyncData/file_1.xml"); 
            fileResult = new ConstructXML(file); 
        } catch (TransformerException | SAXException | IOException ex) {
            Logger.getLogger(AppGUI_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_syncBtActionPerformed

    private void clearTxtField() {
        campo1.setText("");
        campo2.setText("");
        campo3.setText("");
        campo4.setText("");
    }
    
     public void close(){
        connection.closeDB();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PkLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField campo1;
    private javax.swing.JTextField campo2;
    private javax.swing.JTextField campo3;
    private javax.swing.JTextField campo4;
    private javax.swing.JButton cancelBt;
    private javax.swing.JButton deletetBt;
    private javax.swing.JButton insertBt;
    private javax.swing.JLabel moradaLabel;
    private javax.swing.JLabel nomeLabel;
    private javax.swing.JLabel ordemLabel;
    private javax.swing.JButton syncBt;
    private javax.swing.JComboBox tablesJCombo;
    private javax.swing.JButton updateBt;
    // End of variables declaration//GEN-END:variables
}
