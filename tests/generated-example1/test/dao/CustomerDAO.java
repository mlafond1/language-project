package test.dao;

import test.dto.CustomerDTO;
import test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.Timestamp;

public class CustomerDAO {

    private static final String getQuery = "SELECT id, email, firstName, lastName, password, status, lastLogin FROM `Customer` WHERE id=?";
    private static final String getAllQuery = "SELECT id, email, firstName, lastName, password, status, lastLogin FROM `Customer`";
    private static final String insertQuery = "INSERT INTO `Customer` (id, email, firstName, lastName, password, status, lastLogin) VALUES (NULL, ?, ?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `Customer` SET email=?, firstName=?, lastName=?, password=?, status=?, lastLogin=? WHERE id=?";
    private static final String deleteQuery = "DELETE FROM `Customer` WHERE id=?";

    public CustomerDAO(){}

    public CustomerDTO get(Connection connection, CustomerDTO customer){
        CustomerDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setInt(1, customer.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<CustomerDTO> getAll(Connection connection){
        Collection<CustomerDTO> result = new HashSet<CustomerDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, CustomerDTO customer){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getPassword());
            statement.setString(5, String.valueOf(customer.getStatus()));
            statement.setTimestamp(6, new Timestamp(customer.getLastLogin().getTime()));
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, CustomerDTO customer){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setInt(7, customer.getId());
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getPassword());
            statement.setString(5, String.valueOf(customer.getStatus()));
            statement.setTimestamp(6, new Timestamp(customer.getLastLogin().getTime()));
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, CustomerDTO customer){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setInt(1, customer.getId());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private CustomerDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(resultSet.getInt("id"));
        customer.setEmail(resultSet.getString("email"));
        customer.setFirstName(resultSet.getString("firstName"));
        customer.setLastName(resultSet.getString("lastName"));
        customer.setPassword(resultSet.getString("password"));
        customer.setStatus(resultSet.getString("status").charAt(0));
        customer.setLastLogin(new Date(resultSet.getTimestamp("lastLogin").getTime()));
        return customer;
    }
}

