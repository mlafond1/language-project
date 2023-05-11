package test.dao;

import test.dto.ProductDTO;
import test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDAO {

    private static final String getQuery = "SELECT id, name, quantity, price FROM `Product` WHERE id=?";
    private static final String getAllQuery = "SELECT id, name, quantity, price FROM `Product`";
    private static final String insertQuery = "INSERT INTO `Product` (id, name, quantity, price) VALUES (NULL, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `Product` SET name=?, quantity=?, price=? WHERE id=?";
    private static final String deleteQuery = "DELETE FROM `Product` WHERE id=?";

    public ProductDAO(){}

    public ProductDTO get(Connection connection, ProductDTO product){
        ProductDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setInt(1, product.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<ProductDTO> getAll(Connection connection){
        Collection<ProductDTO> result = new HashSet<ProductDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, ProductDTO product){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setString(1, product.getName());
            statement.setInt(2, product.getQuantity());
            statement.setDouble(3, product.getPrice());
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, ProductDTO product){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setInt(4, product.getId());
            statement.setString(1, product.getName());
            statement.setInt(2, product.getQuantity());
            statement.setDouble(3, product.getPrice());
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, ProductDTO product){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setInt(1, product.getId());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private ProductDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        ProductDTO product = new ProductDTO();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setQuantity(resultSet.getInt("quantity"));
        product.setPrice(resultSet.getDouble("price"));
        return product;
    }
}

