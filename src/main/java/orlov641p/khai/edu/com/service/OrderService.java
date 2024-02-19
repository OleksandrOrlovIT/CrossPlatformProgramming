package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements Service<Order>{
    List<Order> orders;

    public OrderService() {
        orders = new ArrayList<>();
    }

    @Override
    public boolean add(Order order) {
        if(order != null && getById(order.getOrderId()) == null) {
            return orders.add(order);
        } else {
            return false;
        }
    }

    @Override
    public Order getById(String id) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(String id, TicketService ticketService) {
        Order foundOrder = getById(id);
        if(foundOrder != null){
            List<String> ids = new ArrayList<>(foundOrder.getTicketIdsList());
            for(String ticketsId : ids) {
                ticketService.deleteById(ticketsId);
            }
            orders.remove(foundOrder);
            return true;
        }

        return false;
    }

    public boolean update(Order order) {
        if(order != null) {
            Order foundOrder = getById(order.getOrderId());
            if (foundOrder != null) {
                foundOrder.setCreditCardId(order.getCreditCardId());
                foundOrder.setDeliveryAddress(order.getDeliveryAddress());
                foundOrder.setOrderDate(order.getOrderDate());
                foundOrder.setDeliveryDate(order.getDeliveryDate());
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Order> findAll() {
        return orders;
    }
}
