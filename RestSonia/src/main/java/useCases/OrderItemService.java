package useCases;

import dao.JDBC.OrderItemDAOdb;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.OrderItem;

import java.util.List;

public class OrderItemService {
    private final OrderItemDAOdb orderItemDAOdb;
    @Inject
    public OrderItemService(OrderItemDAOdb orderItemDAOdb) {
        this.orderItemDAOdb = orderItemDAOdb;
    }
    public Either<RuntimeException, List<OrderItem>> getAll() {
        return orderItemDAOdb.getAll();
    }
    public Either<RuntimeException, List<OrderItem>> getAll(int orderID) {
        return orderItemDAOdb.getAll(orderID);
    }

    public Either<RuntimeException, Integer> delete(int orderID) {
        return orderItemDAOdb.deleteByOrder(orderID);
    }

    public Either<RuntimeException, OrderItem> get(int id) {
        return orderItemDAOdb.get(id);
    }
    public Either<RuntimeException, Integer> save(List<OrderItem> orderItem, int orderID){
        return orderItemDAOdb.insertByOrder(orderItem, orderID);
    }
    public Either<RuntimeException, Integer> update(OrderItem orderItem){
        return orderItemDAOdb.update(orderItem);
    }
}
