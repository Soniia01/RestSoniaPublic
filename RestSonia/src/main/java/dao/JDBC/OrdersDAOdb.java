package dao.JDBC;


import common.Queries;
import dao.OrderDAO;
import io.vavr.control.Either;
import lombok.Data;
import model.Order;
import model.OrderItem;
import useCases.OrderItemService;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Data
public class OrdersDAOdb implements OrderDAO {
    private final DBConnection db;

    public OrdersDAOdb(DBConnection db) {

        this.db = db;
    }

    @Override
    public Either<RuntimeException, List<Order>> getAll() {
        Either<RuntimeException, List<Order>> either;
        List<Order> orderList;
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(Queries.GETALL_orders_QUERY);
            orderList = readRS(resultSet);
            either = Either.right(orderList);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, List<Order>> getAll(int id) {
        Either<RuntimeException, List<Order>> either;
        List<Order> orderList;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement(Queries.GETALL_oneCustomers_orders_QUERY)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            orderList = readRS(resultSet);
            either = Either.right(orderList);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, Order> get(int id) {
        Either<RuntimeException, Order> either;
        Order order;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement(Queries.SELECT_order_QUERY)) {
            statement.setString(1, String.valueOf(id));
            ResultSet rs = statement.executeQuery();
            order = readRS(rs).stream().filter(order1 -> order1.getId() == id).findFirst().orElse(null);
            either = Either.right(order);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, Integer> save(Order c) {
        Either<RuntimeException, Integer> either;
        int rowsAffected;
        try (Connection con = db.getConnection()) {
            try (
                    PreparedStatement addOrderStatement = con.prepareStatement(Queries.SAVE_order_QUERY, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement addOrderItemsStatement = con.prepareStatement(Queries.SAVE_orderItem_QUERY, Statement.RETURN_GENERATED_KEYS)
            ) {
                addOrderStatement.setTimestamp(2, Timestamp.valueOf(c.getDateTime()));
                addOrderStatement.setInt(3, c.getCustomerID());
                addOrderStatement.setInt(4, c.getTableNumber());
                con.setAutoCommit(false);
                rowsAffected = addOrderStatement.executeUpdate();
                ResultSet rs = addOrderStatement.getGeneratedKeys();
                if (rs.next()) {
                    c.setId(rs.getInt(0));
                }
                for (OrderItem orderItem : c.getOrderItems()) {
                    addOrderItemsStatement.setInt(1, c.getId());
                    addOrderItemsStatement.setInt(2, orderItem.getQuantity());
                    addOrderItemsStatement.setString(3, orderItem.getItemName());
                    addOrderItemsStatement.setString(4, orderItem.getDescription());
                    addOrderItemsStatement.setFloat(5, orderItem.getPrice());
                    addOrderItemsStatement.executeUpdate();
                    ResultSet rS = addOrderItemsStatement.getGeneratedKeys();
                    if (rS.next()) {
                        orderItem.setOrderID(rs.getInt(0));
                    }
                }
                con.commit();

                either = Either.right(rowsAffected);
            } catch (Exception e) {
                throw new RuntimeException();
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, Integer> update(Order c) {
        Either<RuntimeException, Integer> either;
        int rowsAffected;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(Queries.UPDATE_order_QUERY)) {
            preparedStatement.setInt(4, c.getId());
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDateTime()));
            preparedStatement.setInt(2, c.getCustomerID());
            preparedStatement.setInt(3, c.getTableNumber());
            rowsAffected = preparedStatement.executeUpdate();
            either = Either.right(rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    @Override
    public Either<RuntimeException, Integer> delete(Order c) {
        Either<RuntimeException, Integer> either = null;
        int rowsAffected = 0;

        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement deleteOrderItems = con.prepareStatement(Queries.DELETE_orderItemOrder_QUERY);
                 PreparedStatement deleteOrders = con.prepareStatement(Queries.DELETE_order_QUERY)) {
                deleteOrders.setInt(1, c.getId());
                deleteOrderItems.setInt(1, c.getId());
                deleteOrderItems.executeUpdate();
                deleteOrders.executeUpdate();
                rowsAffected = deleteOrders.executeUpdate();
                con.commit();
                either = Either.right(rowsAffected);
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 1451) {
                        either = Either.left(new RuntimeException());
                    } else {
                        either = Either.left(new RuntimeException());
                    }
                }
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return either;
    }

    private List<Order> readRS(ResultSet resultSet) throws SQLException {
        OrderItemService orderItemServices = new OrderItemService(new OrderItemDAOdb(db));
        List<Order> orderList;
        orderList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("order_id");
            LocalDateTime localDate = resultSet.getTimestamp("order_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int customerID = resultSet.getInt("customer_id");
            int tableNumber = resultSet.getInt("table_id");
            Order order = new Order(id, localDate, customerID, tableNumber);
            order.setOrderItems(orderItemServices.getAll(id).getOrNull());
            orderList.add(order);
        }
        return orderList;
    }
}