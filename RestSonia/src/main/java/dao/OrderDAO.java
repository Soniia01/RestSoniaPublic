package dao;

import io.vavr.control.Either;
import model.Order;

import java.util.List;

public interface OrderDAO {
    Either<RuntimeException, List<Order>> getAll();

    Either<RuntimeException, List<Order>> getAll(int id);

    Either<RuntimeException, Order> get(int id);

    Either<RuntimeException, Integer> save(Order c);

    Either<RuntimeException, Integer> update(Order c);

    Either<RuntimeException, Integer> delete(Order c);
}
