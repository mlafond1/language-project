package test.dao;

import test.dto.OrderDTO;
import test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class OrderDAO {

    private static final String getQuery = "SELECT id, customerId, total, orderDate, deliveryDate FROM `Orders` WHERE id=?";
    private static final String getAllQuery = "SELECT id, customerId, total, orderDate, deliveryDate FROM `Orders`";
    private static final String insertQuery = "INSERT INTO `Orders` (id, customerId, total, orderDate, deliveryDate) VALUES (NULL, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `Orders` SET customerId=?, total=?, orderDate=?, deliveryDate=? WHERE id=?";
    private static final String deleteQuery = "DELETE FROM `Orders` WHERE id=?";

    public OrderDAO(){}

    public OrderDTO get(Connection connection, OrderDTO order){
        OrderDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setInt(1, order.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<OrderDTO> getAll(Connection connection){
        Collection<OrderDTO> result = new HashSet<OrderDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, OrderDTO order){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setInt(1, order.getCustomerId());
            statement.setDouble(2, order.getTotal());
            statement.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            statement.setDate(4, new java.sql.Date(order.getDeliveryDate().getTime()));
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, OrderDTO order){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setInt(5, order.getId());
            statement.setInt(1, order.getCustomerId());
            statement.setDouble(2, order.getTotal());
            statement.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            statement.setDate(4, new java.sql.Date(order.getDeliveryDate().getTime()));
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, OrderDTO order){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setInt(1, order.getId());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private OrderDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        OrderDTO order = new OrderDTO();
        order.setId(resultSet.getInt("id"));
        order.setCustomerId(resultSet.getInt("customerId"));
        order.setTotal(resultSet.getDouble("total"));
        order.setOrderDate(new Date(resultSet.getDate("orderDate").getTime()));
        order.setDeliveryDate(new Date(resultSet.getDate("deliveryDate").getTime()));
        return order;
    }
}

