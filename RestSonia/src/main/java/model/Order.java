package model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private int id;
    private LocalDateTime dateTime;
    private int customerID;
    private int tableNumber;
    private List<OrderItem> orderItems;

    public Order(int id, LocalDateTime dateTime, int customerID, int tableNumber) {
        this.id = id;
        this.dateTime = dateTime;
        this.customerID = customerID;
        this.tableNumber = tableNumber;
    }
}
