package rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.OrderItem;
import useCases.OrderItemService;

import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestOrderItems {

    private final OrderItemService orderItemService;

    @Inject
    public RestOrderItems(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }


    @GET
    public List<OrderItem> getAllItems() {
        return orderItemService.getAll().getOrNull();
    }

    @GET
    @Path("/{id}")
    public OrderItem getOrderItem(@PathParam("id") int id) {
        return orderItemService.get(id).getOrNull();
    }

/*
   @POST
    public Response addOrderItem(OrderItem orderItem) {
        orderItemService.save(orderItem);
        return Response.status(Response.Status.CREATED)
                .entity(orderItem).build();
    }
*/
    @PUT
    public OrderItem updateOrder(OrderItem orderItem, @QueryParam("id") int id,@QueryParam("itemName") String itemName,@QueryParam("itemDesc") String itemDesc,@QueryParam("quantity") int quantity,@QueryParam("price") float price) {
        orderItem = orderItemService.get(id).getOrNull();
        orderItem.setItemName(itemName);
        orderItem.setDescription(itemDesc);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItemService.update(orderItem);
        return orderItem;
    }
/*
    @DELETE
    @Path("/{id}")
    public Response delUsuario(@PathParam("id") String id) {
        List<Usuario> users = DaoErrores.usuarios.stream().filter(usuario -> !usuario.getId().equals(id)).collect(Collectors.toList());

        if (users.size() == DaoErrores.usuarios.size())
            return Response.status(Response.Status.NOT_FOUND).entity(null).build();
        else {
            DaoErrores.usuarios = users;
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

*/
}
