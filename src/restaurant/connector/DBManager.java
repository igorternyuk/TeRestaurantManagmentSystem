package restaurant.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author igor
 */
public class DBManager {
    private final String PATH_TO_CONFIG_FILE = "config/config.dat";
    private static DBManager instance = null;
    private Connection con = null;
    private PreparedStatement cmd;
    private ResultSet rs;
    private String server, port, database, user, pass, url;
    
    public synchronized static DBManager getInstance() {
        if(instance == null){
            try {
                instance = new DBManager();
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not read database settings config file",
                    "Database error",
                    JOptionPane.ERROR_MESSAGE
                );
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }

    public String getServer() {
        return server;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getUrl() {
        return url;
    }
    
    public ResultSet executeSelect(String sql) throws SQLException{
        cmd = con.prepareStatement(sql);
        rs = cmd.executeQuery();
        return rs;
    }
    
    public int executeUpdate(String sql) throws SQLException{
        cmd = con.prepareStatement(sql);
        int res = cmd.executeUpdate();
        closeConnection();
        return res;        
    }
    
    public void closeConnection(){
//        cmd.close();
//        con.close();
        instance = null;
    }

    private DBManager() throws ClassNotFoundException, SQLException, IOException{
        readConnectionSettingsFromFile(PATH_TO_CONFIG_FILE);
        url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        System.out.println("url = " + url + " user= " + user + " pass = " + pass);
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, user, pass);
    }
    
    private enum LoadState{
        SERVER,
        PORT,
        USER,
        PASS,
        DATABASE,
        STOP
    }
    
    private void readConnectionSettingsFromFile(String path) throws IOException{
        try(InputStream in = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "UTF-8"))){
            String str;
            LoadState ls;
            lbl: while((str = br.readLine()) != null){
                if(str.equalsIgnoreCase("[Server]")){
                    ls = LoadState.SERVER;
                } else if(str.equalsIgnoreCase("[Port]")){
                    ls = LoadState.PORT;
                } else if(str.equalsIgnoreCase("[User]")){
                    ls = LoadState.USER;
                } else if(str.equalsIgnoreCase("[Password]")){
                    ls = LoadState.PASS;
                } else if(str.equalsIgnoreCase("[Database]")){
                    ls = LoadState.DATABASE;
                } else {
                    ls = LoadState.STOP;
                }
                str = br.readLine();
                switch(ls){
                    case SERVER :
                        server = str;
                        break;
                    case PORT :
                        port = str;
                        break;
                    case USER :
                        user = str;
                        break;
                    case PASS :
                        pass = str;
                        break;
                    case DATABASE :
                        database = str;
                        break;
                    case STOP:
                        break lbl;
                }
            }
        }
    }
    
//    public static void main(String[] args) {
//        try {
//            DBManager manager = DBManager.getInstance();
//            String sql = "select * from drinks";
//            ResultSet rs = manager.executeSelect(sql);
//            while(rs.next()){
//                System.out.println("id = " + rs.getInt("id") + 
//                " name= " + rs.getString("name") + " price = " + 
//                rs.getDouble("price") + " isAlcohol = " + 
//                rs.getBoolean("isAlcohol") + " isAvailable = " +
//                rs.getBoolean("isAvailable"));
//            }
//        } catch (ClassNotFoundException | SQLException | IOException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
}
