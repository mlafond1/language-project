package other.test.dao;

import other.test.dto.OtherDTO;
import other.test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OtherDAO {

    private static final String getQuery = "SELECT name, isOther, something1, something2, favoriteNumber FROM `Other` WHERE name=? And isOther=? And favoriteNumber=?";
    private static final String getAllQuery = "SELECT name, isOther, something1, something2, favoriteNumber FROM `Other`";
    private static final String insertQuery = "INSERT INTO `Other` (name, isOther, something1, something2, favoriteNumber) VALUES (?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `Other` SET something1=?, something2=? WHERE name=? And isOther=? And favoriteNumber=?";
    private static final String deleteQuery = "DELETE FROM `Other` WHERE name=? And isOther=? And favoriteNumber=?";

    public OtherDAO(){}

    public OtherDTO get(Connection connection, OtherDTO other){
        OtherDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setString(1, other.getName());
            statement.setBoolean(2, other.getIsOther());
            statement.setInt(3, other.getFavoriteNumber());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<OtherDTO> getAll(Connection connection){
        Collection<OtherDTO> result = new HashSet<OtherDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, OtherDTO other){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setString(1, other.getName());
            statement.setBoolean(2, other.getIsOther());
            statement.setInt(3, other.getSomething1());
            statement.setInt(4, other.getSomething2());
            statement.setInt(5, other.getFavoriteNumber());
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, OtherDTO other){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setString(3, other.getName());
            statement.setBoolean(4, other.getIsOther());
            statement.setInt(1, other.getSomething1());
            statement.setInt(2, other.getSomething2());
            statement.setInt(5, other.getFavoriteNumber());
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, OtherDTO other){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setString(1, other.getName());
            statement.setBoolean(2, other.getIsOther());
            statement.setInt(3, other.getFavoriteNumber());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private OtherDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        OtherDTO other = new OtherDTO();
        other.setName(resultSet.getString("name"));
        other.setIsOther(resultSet.getBoolean("isOther"));
        other.setSomething1(resultSet.getInt("something1"));
        other.setSomething2(resultSet.getInt("something2"));
        other.setFavoriteNumber(resultSet.getInt("favoriteNumber"));
        return other;
    }
}

