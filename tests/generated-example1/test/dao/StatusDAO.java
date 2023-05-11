package test.dao;

import test.dto.StatusDTO;
import test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatusDAO {

    private static final String getQuery = "SELECT status, statusName, discountRate FROM `Status` WHERE status=?";
    private static final String getAllQuery = "SELECT status, statusName, discountRate FROM `Status`";
    private static final String insertQuery = "INSERT INTO `Status` (status, statusName, discountRate) VALUES (?, ?, ?)";
    private static final String updateQuery = "UPDATE `Status` SET statusName=?, discountRate=? WHERE status=?";
    private static final String deleteQuery = "DELETE FROM `Status` WHERE status=?";

    public StatusDAO(){}

    public StatusDTO get(Connection connection, StatusDTO status){
        StatusDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setString(1, String.valueOf(status.getStatus()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<StatusDTO> getAll(Connection connection){
        Collection<StatusDTO> result = new HashSet<StatusDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, StatusDTO status){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setString(1, String.valueOf(status.getStatus()));
            statement.setString(2, status.getStatusName());
            statement.setFloat(3, status.getDiscountRate());
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, StatusDTO status){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setString(3, String.valueOf(status.getStatus()));
            statement.setString(1, status.getStatusName());
            statement.setFloat(2, status.getDiscountRate());
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, StatusDTO status){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setString(1, String.valueOf(status.getStatus()));
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private StatusDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        StatusDTO status = new StatusDTO();
        status.setStatus(resultSet.getString("status").charAt(0));
        status.setStatusName(resultSet.getString("statusName"));
        status.setDiscountRate(resultSet.getFloat("discountRate"));
        return status;
    }
}

