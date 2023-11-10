package common;

public class Queries {
    //Oders
    public static final String GETALL_orders_QUERY="select*from orders";
    public static final String GETALL_oneCustomers_orders_QUERY="select*from orders where customer_id=?";
    public static final String SELECT_order_QUERY ="select * from orders where order_id= ?";
    public static final String UPDATE_order_QUERY ="UPDATE orders SET order_date = ?,customer_id = ?,table_id = ? WHERE order_id = ?";
    public static final String DELETE_order_QUERY = "DELETE FROM orders where order_id= ?";
    public static final String SAVE_order_QUERY= "INSERT INTO orders (order_date,customer_id, table_id) VALUES (?,?,?)";

    //OrderItems
    public static final String GETALLALL_orderItems_QUERY="select*from order_items";
    public static final String DELETEBYORDER_orderItem_QUERY="DELETE FROM order_items WHERE order_id= ?";
    public static final String GETALL_orderItems_QUERY="select*from order_items where order_id= ?";
    public static final String SELECT_orderItems_QUERY="select * from order_items where order_item_id= ?";
    public static final String SELECT_orderItemsByPriceOverX_QUERY="select * from order_items where price> ?";
    public static final String UPDATE_orderItem_QUERY="UPDATE order_items SET quantity = ?, item_name=?, item_description=?,price=? WHERE order_item_id = ?";
    public static final String SAVE_orderItem_QUERY="INSERT INTO order_items (order_id, quantity,item_name,item_description,price) VALUES (?,?,?,?,?)";
    public static final String DELETE_orderItemOrder_QUERY = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE order_id=?)";
}