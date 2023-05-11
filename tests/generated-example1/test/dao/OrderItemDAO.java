package test.dao;

import test.dto.OrderItemDTO;
import test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderItemDAO {

    private static final String getQuery = "SELECT orderId, productId, quantity, subTotal, hasBeenRefunded FROM `OrderItem` WHERE orderId=? And productId=?";
    private static final String getAllQuery = "SELECT orderId, productId, quantity, subTotal, hasBeenRefunded FROM `OrderItem`";
    private static final String insertQuery = "INSERT INTO `OrderItem` (orderId, productId, quantity, subTotal, hasBeenRefunded) VALUES (?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `OrderItem` SET quantity=?, subTotal=?, hasBeenRefunded=? WHERE orderId=? And productId=?";
    private static final String deleteQuery = "DELETE FROM `OrderItem` WHERE orderId=? And productId=?";

    public OrderItemDAO(){}

    public OrderItemDTO get(Connection connection, OrderItemDTO orderItem){
        OrderItemDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<OrderItemDTO> getAll(Connection connection){
        Collection<OrderItemDTO> result = new HashSet<OrderItemDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, OrderItemDTO orderItem){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getSubTotal());
            statement.setBoolean(5, orderItem.getHasBeenRefunded());
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, OrderItemDTO orderItem){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setInt(4, orderItem.getOrderId());
            statement.setInt(5, orderItem.getProductId());
            statement.setInt(1, orderItem.getQuantity());
            statement.setDouble(2, orderItem.getSubTotal());
            statement.setBoolean(3, orderItem.getHasBeenRefunded());
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, OrderItemDTO orderItem){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private OrderItemDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setOrderId(resultSet.getInt("orderId"));
        orderItem.setProductId(resultSet.getInt("productId"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setSubTotal(resultSet.getDouble("subTotal"));
        orderItem.setHasBeenRefunded(resultSet.getBoolean("hasBeenRefunded"));
        return orderItem;
    }
}

