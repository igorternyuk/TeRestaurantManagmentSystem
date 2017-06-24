package restaurant.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import restaurant.connector.DBManager;
import restaurant.exceptions.NoSuchDrinkException;
import restaurant.model.dto.DrinkDTO;

/**
 *
 * @author igor
 */

public class DrinkDAO implements DAO<DrinkDTO>{
    private static final String SQL_INSERT = "insert into drinks values(default,"
            + "?,?,?,?)";
    private static final String SQL_UPDATE = "update drinks set name = ?,"
            + " price = ?, isAlcohol = ?, isAvailable = ? where id = ?;";
    private static final String SQL_DELETE = "delete from drinks where id = ?;";
    private static final String SQL_READ = "select * from drinks where id = ?;";
    private static final String SQL_READ_ALL = "select * from drinks;";
    private static final String SQL_SEARCH = "select * from drinks where name"
            + "like ? ";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    private String lastSQL = SQL_READ_ALL;
    
    @Override
    public boolean insert(DrinkDTO object) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, object.getName());
            cmd.setDouble(2, object.getPrice());
            cmd.setBoolean(3, object.isAlcohol());
            cmd.setBoolean(4, object.isAvailable());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New drink was successfully inserted to the database",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(DrinkDTO object) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, object.getName());
            cmd.setDouble(2, object.getPrice());
            cmd.setBoolean(3, object.isAlcohol());
            cmd.setBoolean(4, object.isAvailable());
            cmd.setInt(5, object.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected drink was successfully updated",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_DELETE);
            cmd.setInt(1, id);
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected drink was successfully deleted",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public DrinkDTO read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(
                    null,
                    "The drink was found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return readDrinkFromResultSet(rs);
            } else {
                throw new NoSuchDrinkException("There are no drinks with id = " + id);
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchDrinkException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                "The drink was not found",
                JOptionPane.ERROR_MESSAGE
            );
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<DrinkDTO> readAll() {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            List<DrinkDTO> list = new ArrayList<>();
            while(rs.next()){
                list.add(readDrinkFromResultSet(rs));
            }
            lastSQL = SQL_READ_ALL;
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }
    
    public List<DrinkDTO> search(String regExp, boolean considerPrice,
            double priceMin, double priceMax, boolean onlyNonAlcoholic){
        StringBuilder sb = new StringBuilder("select * from drinks where");
        sb.append(" name like '%").append(regExp).append("%' ");
        if(considerPrice){
            sb.append(String.format(" and (price >= %f and price <= %f) ",
                    priceMin, priceMax));
        }
        if(onlyNonAlcoholic){
            sb.append(" and isAlcohol = false ");
        }
        sb.append(";");
        System.out.println("SQL = " + sb.toString());
        lastSQL = sb.toString();
        try {
            cmd = manager.getConnection().prepareStatement(sb.toString());
            rs = cmd.executeQuery();
            List<DrinkDTO> list = new ArrayList<>();
            while(rs.next()){
                list.add(readDrinkFromResultSet(rs));
            }
            if(list.isEmpty()){
                JOptionPane.showMessageDialog(
                    null,
                    "No drink was found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    list.size() + " drinks were found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }
    
    public List<DrinkDTO> repeatLastSearch(){
        try {
            cmd = manager.getConnection().prepareStatement(lastSQL);
            rs = cmd.executeQuery();
            List<DrinkDTO> list = new ArrayList<>();
            while(rs.next()){
                list.add(readDrinkFromResultSet(rs));
            }
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private DrinkDTO readDrinkFromResultSet(ResultSet rs) throws SQLException{
        DrinkDTO drink = new DrinkDTO();
        drink.setId(rs.getInt("id"));
        drink.setName(rs.getString("name"));
        drink.setPrice(rs.getDouble("price"));
        drink.setAlcohol(rs.getBoolean("isAlcohol"));
        drink.setAvailable(rs.getBoolean("isAvailable"));
        return drink;
    }
    
    private void showSQLErrorMessage(SQLException ex){
    JOptionPane.showMessageDialog(
        null,
        ex.getMessage(),
        "Database error",
        JOptionPane.ERROR_MESSAGE
    );
    }
    
//    public static void main(String[] args) {
//        try {
//            DrinkDAO dao = new DrinkDAO();
//            DrinkDTO newDrink = new DrinkDTO(12, "Vodka", 300.0, true, false);
//            dao.update(newDrink);
//            //dao.delete(13);
//            DrinkDTO drink12 = dao.read(12);
//            System.out.println("drink12 = " + drink12);
//            List<DrinkDTO> list = dao.readAll();
//            for(DrinkDTO drink : list){
//                System.out.println(drink);
//            }
//        } catch (ClassNotFoundException | SQLException | IOException ex) {
//            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(DrinkDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
