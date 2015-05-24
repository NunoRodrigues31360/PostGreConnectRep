package postgreconnect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ConnectDB {

    Connection con;
    Statement stmt;
    DatabaseMetaData dbmd;
    String resultset;
    //ResultSet rs;

    ConnectDB(String p1, String p2, String p3) {
        try {
            con = DriverManager.getConnection(p1, p2, p3);
            stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            dbmd = con.getMetaData();
        } catch (SQLException ex) {
            printSQLException(ex);
        }
    }

    static void printSQLException(SQLException ex) {
        while (ex != null) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getSQLState());
            System.out.println(ex.getErrorCode());
            // Obter a proxima excepcao contida no objecto
            ex = ex.getNextException();
        }
    }
    
    ResultSet executeSQLCommand(String cmd) {
        Statement stamt;
        ResultSet rs = null;       
        try {
            stamt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                      ResultSet.CONCUR_UPDATABLE);
            rs = stamt.executeQuery(cmd);        
        } catch (SQLException ex) {
            printSQLException(ex);           
        }
       return rs;     
    }
    
    public int printResultSet(ResultSet rs) throws SQLException {
        resultset="";
	ResultSetMetaData rsMetaData = rs.getMetaData();
	int columnNumber = rsMetaData.getColumnCount();
	String columnName[] = new String[columnNumber];
	for (int i = 0; i < columnNumber; i++) {
		columnName[i] = rsMetaData.getColumnName(i + 1);
	}
	int row = 1;
	while (rs.next()) {
		System.out.print("Row [" + row++ + "] -> ");
		for (int i = 0; i < columnNumber; i++) {
			if (i > 0)
				System.out.print(", ");
			System.out.print(columnName[i] + " " + rs.getObject(i + 1));
                        resultset=resultset+columnName[i] + " " + rs.getObject(i + 1)+",";
		}
		System.out.println();
	}
	System.out.println((row - 1) + " rows were retrieved");
        rs.beforeFirst();
        return row-1;
}

    public Connection getCon() {
        return con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public DatabaseMetaData getDbmd() {
        return dbmd;
    }
    
    public String getResultset(){
        return resultset;
    }

    public ArrayList<String> getPKeys() {
        ArrayList<String> keys = new ArrayList<>();
        try {
            ResultSet rsTables = dbmd.getTables(null, null, "%", new String[]{"TABLE"});
            while (rsTables.next()) {
                String tableName = rsTables.getString("TABLE_NAME");
                ResultSet rsPkeys = dbmd.getPrimaryKeys(null, null, tableName);
                while (rsPkeys.next()) {
                    String columnName = rsPkeys.getString("COLUMN_NAME");
                    System.out.println("getPrimaryKeys(): columnName=" + tableName + "." + columnName);
                    keys.add(tableName + "." + columnName);
                }
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return keys;
    }

    public String getTablePKeys(String tableName) {
        ResultSet rsPkeys;
        try {
            rsPkeys = dbmd.getPrimaryKeys(null, null, tableName);
            while (rsPkeys.next()) {
                return rsPkeys.getString("COLUMN_NAME");
            }
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return null;
    }
    
     public int getVts(ResultSet rs){
        int aux=0;
        try {
            while(rs.next())
          aux = Integer.parseInt(rs.getString("ver"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return aux;
    }
     
     public void closeDB(){
        try {
            con.close();
        } catch (SQLException ex) {
          printSQLException(ex);  
        }
     }
}
