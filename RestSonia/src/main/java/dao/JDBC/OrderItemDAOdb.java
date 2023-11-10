package dao.JDBC;

import common.Queries;
import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.Data;
import model.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Data
public class OrderItemDAOdb implements OrderItemDAO {
    private final DBConnection db;
    @Inject
    public OrderItemDAOdb(DBConnection db) {

        this.db = db;
    }
    @Override
    public Either<RuntimeException, List<OrderItem>> getAll() {
        Either<RuntimeException, List<OrderItem>> either;
        List<OrderItem> orderItemList;
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Queries.GETALLALL_orderItems_QUERY);
            orderItemList = readRS(resultSet);
            either = Either.right(orderItemList);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, List<OrderItem>> getAll(int orderID) {
        Either<RuntimeException,List<OrderItem>> either;
        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(Queries.GETALL_orderItems_QUERY)) {
            preparedStatement.setInt(1, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            either= Either.right(readRS(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, OrderItem> get(int id) {
        Either<RuntimeException,OrderItem> either;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(Queries.SELECT_orderItems_QUERY)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet rs = preparedStatement.executeQuery();
            either= Either.right(readRS(rs).stream().filter(orderItem -> orderItem.getId() == id).findFirst().orElse(null));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }
    @Override
    public Either<RuntimeException, Integer> update(OrderItem orderItem) {
        Either<RuntimeException,Integer> either = null;
        int rowsAffected;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(Queries.UPDATE_orderItem_QUERY)) {
            preparedStatement.setInt(1, orderItem.getQuantity());
            preparedStatement.setString(2, orderItem.getItemName());
            preparedStatement.setString(3, orderItem.getDescription());
            preparedStatement.setFloat(4, orderItem.getPrice());
            preparedStatement.setInt(5, orderItem.getId());
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                either=Either.right(rowsAffected);
            }else{
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }
    public Either<RuntimeException,Integer> deleteByOrder(int orderid){
        Either<RuntimeException,Integer> either;
        int rowsAffected;
        try (Connection con = db.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(Queries.DELETEBYORDER_orderItem_QUERY)) {
            preparedStatement.setInt(1, orderid);
            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                either=Either.right(rowsAffected);
            }else{
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    public Either<RuntimeException,Integer> insertByOrder(List<OrderItem> orderItemList, int orderID){
        Either<RuntimeException,Integer> either = null;
        int rowsAffected;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(Queries.SAVE_orderItem_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            for (OrderItem orderItem : orderItemList) {
                preparedStatement.setInt(1, orderID);
                preparedStatement.setInt(2, orderItem.getQuantity());
                preparedStatement.setString(3, orderItem.getItemName());
                preparedStatement.setString(4, orderItem.getDescription());
                preparedStatement.setFloat(5,orderItem.getPrice());
                rowsAffected = preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }
                if (rowsAffected>0){
                    either=Either.right(rowsAffected);
                }else{
                    throw new RuntimeException();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }
    public List<OrderItem> readRS(ResultSet resultSet) throws SQLException {
        List<OrderItem> orderItemList= new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("order_item_id");
            int orderId = resultSet.getInt("order_id");
            int quantity = resultSet.getInt("quantity");
            String itemName=resultSet.getString("item_name");
            String itemDescription=resultSet.getString("item_description");
            float price=resultSet.getFloat("price");
            orderItemList.add(new OrderItem(id, orderId, itemName,itemDescription,price,quantity));
        }
        return orderItemList;
    }
}