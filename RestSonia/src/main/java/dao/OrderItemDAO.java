package dao;

import io.vavr.control.Either;
import model.OrderItem;

import java.util.List;

public interface OrderItemDAO {
    Either<RuntimeException,List<OrderItem>> getAll();
    Either<RuntimeException, List<OrderItem>> getAll(int orderID);
    Either<RuntimeException, OrderItem> get(int orderID);
    Either<RuntimeException, Integer> deleteByOrder(int orderid);
    Either<RuntimeException, Integer> insertByOrder(List<OrderItem> orderItemList, int orderID);
    Either<RuntimeException, Integer> update(OrderItem orderItem);
}
