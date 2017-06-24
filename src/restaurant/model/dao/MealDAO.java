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
import restaurant.exceptions.NoSuchMealException;
import restaurant.model.MealType;
import restaurant.model.dto.MealDTO;

/**
 *
 * @author igor
 */
public class MealDAO implements DAO<MealDTO>{
    private static final String SQL_INSERT = "insert into meals values(default,"
            + "?,?,?,?,?,?);";
    private static final String SQL_UPDATE = "update meals set name = ?, type = ?,"
            + "price = ?, isVegetarian = ?, isTransgenic = ?, isAvailable = ? "
            + " where id = ?;";
    private static final String SQL_DELETE = "delete from meals where id = ?;";
    private static final String SQL_READ = "select * from meals where id = ?";
    private static final String SQL_READ_ALL = "select * from meals;";
    private static final DBManager manager = DBManager.getInstance();
    private PreparedStatement cmd;
    private ResultSet rs;
    private String lastSQL = SQL_READ_ALL;

    @Override
    public boolean insert(MealDTO object){
        try {
            cmd = manager.getConnection().prepareStatement(SQL_INSERT);
            cmd.setString(1, object.getName());
            cmd.setString(2, String.valueOf(object.getType()));
            cmd.setDouble(3, object.getPrice());
            cmd.setBoolean(4, object.isVegetarian());
            cmd.setBoolean(5, object.isTransgenic());
            cmd.setBoolean(6, object.isAvailable());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "New meal was successfully inserted to the database",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean update(MealDTO object) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_UPDATE);
            cmd.setString(1, object.getName());
            cmd.setString(2, String.valueOf(object.getType()));
            cmd.setDouble(3, object.getPrice());
            cmd.setBoolean(4, object.isVegetarian());
            cmd.setBoolean(5, object.isTransgenic());
            cmd.setBoolean(6, object.isAvailable());
            cmd.setInt(7, object.getId());
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected meal was successfully updated",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public boolean delete(int id){
        try {
            cmd = manager.getConnection().prepareStatement(SQL_DELETE);
            cmd.setInt(1, id);
            if(cmd.executeUpdate() > 0){
                JOptionPane.showMessageDialog(
                    null,
                    "Selected meal was successfully deleted",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return false;
    }

    @Override
    public MealDTO read(int id) {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ);
            cmd.setInt(1, id);
            rs = cmd.executeQuery();
            if(rs.next()){
                MealDTO meal = readMealFromResultSet(rs);
                if(meal != null){
                    JOptionPane.showMessageDialog(
                        null,
                        "The meal was found",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    return meal;
                }
            } else {
                throw new NoSuchMealException("There are no meal with id = " + id);
            }
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMealException ex) {
            JOptionPane.showMessageDialog(
                null,
                ex.getMessage(),
                "The meal was not found",
                JOptionPane.ERROR_MESSAGE
            );
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }

    @Override
    public List<MealDTO> readAll() {
        try {
            cmd = manager.getConnection().prepareStatement(SQL_READ_ALL);
            rs = cmd.executeQuery();
            List<MealDTO> list = new ArrayList<>();
            while(rs.next()){
                list.add(readMealFromResultSet(rs));
            }
            lastSQL = SQL_READ_ALL;
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }
    
    public List<MealDTO> search(String regExp, boolean considerPrice,
            double priceMin, double priceMax, boolean onlyVegetarian){
            StringBuilder sb = new StringBuilder("select * from meals where "
                    + "name like '%" + regExp + "%' ");
            if(considerPrice){
                sb.append(String.format(" and (price >= %f and price <= %f) ",
                        priceMin, priceMax));
            }
            if(onlyVegetarian){
                sb.append(" and isVegetarian = true ");
            }
            sb.append(";");
        try {
            cmd = manager.getConnection().prepareStatement(sb.toString());
            lastSQL = sb.toString();
            System.out.println(String.format("SQL = %s ", sb.toString()));
            rs = cmd.executeQuery();
            List<MealDTO> list = new ArrayList<>();
            while(rs.next()){
                MealDTO meal = readMealFromResultSet(rs);
                list.add(meal);
            }
            if(list.isEmpty()){
                JOptionPane.showMessageDialog(
                    null,
                    "No meal was found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    list.size() + " results were found",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }
    
    public List<MealDTO> repeatLastSearch(){
        try {
            cmd = manager.getConnection().prepareStatement(lastSQL);
            rs = cmd.executeQuery();
            List<MealDTO> list = new ArrayList<>();
            while(rs.next()){
                MealDTO meal = readMealFromResultSet(rs);
                list.add(meal);
            }
            return list;
        } catch (SQLException ex) {
            showSQLErrorMessage(ex);
            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            manager.closeConnection();
        }
        return null;
    }
    
    private MealDTO readMealFromResultSet(ResultSet rs) throws SQLException{
        MealDTO meal = new MealDTO();
        meal.setId(rs.getInt("id"));
        meal.setName(rs.getString("name"));
        meal.setType(MealType.valueOf(rs.getString("type")));
        meal.setPrice(rs.getDouble("price"));
        meal.setVegetarian(rs.getBoolean("isVegetarian"));
        meal.setTransgenic(rs.getBoolean("isTransgenic"));
        meal.setAvailable(rs.getBoolean("isAvailable"));
        return meal;
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
//            MealDAO dao = new MealDAO();
//            //Insert
////            MealDTO newMeal = new MealDTO(24, "Vegetable soup", MealType.MAIN_COURSE,
////            15, true, false, false);
//            //dao.insert(newMeal);
//            //Update
//            //dao.update(newMeal);
//            //Delete
//            //dao.delete(15);
//            //Get by ID
////            MealDTO m14 = dao.read(14);
////            System.out.println("m14 = " + m14);
//            List<MealDTO> list = dao.search("Ok", true, 0.0, 100.0, false);
//            for(MealDTO meal: list){
//                System.out.println(meal);
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(MealDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
}
