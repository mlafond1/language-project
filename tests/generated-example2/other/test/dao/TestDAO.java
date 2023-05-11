package other.test.dao;

import other.test.dto.TestDTO;
import other.test.dao.exception.DAOException;
import java.util.Collection;
import java.util.HashSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.Timestamp;

public class TestDAO {

    private static final String getQuery = "SELECT id, myDate, myTime, myBool, myChar, myString, mySizedString, myInt, mySizedInt, myFloat, myDouble FROM `Toto` WHERE id=?";
    private static final String getAllQuery = "SELECT id, myDate, myTime, myBool, myChar, myString, mySizedString, myInt, mySizedInt, myFloat, myDouble FROM `Toto`";
    private static final String insertQuery = "INSERT INTO `Toto` (id, myDate, myTime, myBool, myChar, myString, mySizedString, myInt, mySizedInt, myFloat, myDouble) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String updateQuery = "UPDATE `Toto` SET myDate=?, myTime=?, myBool=?, myChar=?, myString=?, mySizedString=?, myInt=?, mySizedInt=?, myFloat=?, myDouble=? WHERE id=?";
    private static final String deleteQuery = "DELETE FROM `Toto` WHERE id=?";

    public TestDAO(){}

    public TestDTO get(Connection connection, TestDTO test){
        TestDTO result = null;
        try(PreparedStatement statement = connection.prepareStatement(getQuery)){
            statement.setInt(1, test.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) result = extractFromResultSet(resultSet);
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public Collection<TestDTO> getAll(Connection connection){
        Collection<TestDTO> result = new HashSet<TestDTO>();
        try(PreparedStatement statement = connection.prepareStatement(getAllQuery)){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) result.add(extractFromResultSet(resultSet));
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return result;
    }

    public boolean insert(Connection connection, TestDTO test){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setDate(1, new java.sql.Date(test.getMyDate().getTime()));
            statement.setTimestamp(2, new Timestamp(test.getMyTime().getTime()));
            statement.setBoolean(3, test.getMyBool());
            statement.setString(4, String.valueOf(test.getMyChar()));
            statement.setString(5, test.getMyString());
            statement.setString(6, test.getMySizedString());
            statement.setInt(7, test.getMyInt());
            statement.setInt(8, test.getMySizedInt());
            statement.setFloat(9, test.getMyFloat());
            statement.setDouble(10, test.getMyDouble());
            int nbRowAdded = statement.executeUpdate();
            if(nbRowAdded == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean update(Connection connection, TestDTO test){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            statement.setInt(11, test.getId());
            statement.setDate(1, new java.sql.Date(test.getMyDate().getTime()));
            statement.setTimestamp(2, new Timestamp(test.getMyTime().getTime()));
            statement.setBoolean(3, test.getMyBool());
            statement.setString(4, String.valueOf(test.getMyChar()));
            statement.setString(5, test.getMyString());
            statement.setString(6, test.getMySizedString());
            statement.setInt(7, test.getMyInt());
            statement.setInt(8, test.getMySizedInt());
            statement.setFloat(9, test.getMyFloat());
            statement.setDouble(10, test.getMyDouble());
            int nbRowUpdated = statement.executeUpdate();
            if(nbRowUpdated == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    public boolean delete(Connection connection, TestDTO test){
        boolean success = false;
        try(PreparedStatement statement = connection.prepareStatement(deleteQuery)){
            statement.setInt(1, test.getId());
            int nbRowDeleted = statement.executeUpdate();
            if(nbRowDeleted == 1) success = true;
        } catch(SQLException exception){
            throw new DAOException(exception);
        }
        return success;
    }

    private TestDTO extractFromResultSet(ResultSet resultSet) throws SQLException {
        TestDTO test = new TestDTO();
        test.setId(resultSet.getInt("id"));
        test.setMyDate(new Date(resultSet.getDate("myDate").getTime()));
        test.setMyTime(new Date(resultSet.getTimestamp("myTime").getTime()));
        test.setMyBool(resultSet.getBoolean("myBool"));
        test.setMyChar(resultSet.getString("myChar").charAt(0));
        test.setMyString(resultSet.getString("myString"));
        test.setMySizedString(resultSet.getString("mySizedString"));
        test.setMyInt(resultSet.getInt("myInt"));
        test.setMySizedInt(resultSet.getInt("mySizedInt"));
        test.setMyFloat(resultSet.getFloat("myFloat"));
        test.setMyDouble(resultSet.getDouble("myDouble"));
        return test;
    }
}

