package model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {
    private int id;
    private int orderID;
    private String itemName;
    private String description;
    private float price;
    private int quantity;
}
