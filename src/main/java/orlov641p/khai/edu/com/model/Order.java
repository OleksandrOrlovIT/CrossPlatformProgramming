package orlov641p.khai.edu.com.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
    private String orderId;
    private String creditCardId;
    private String deliveryAddress;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private List<String> ticketIdsList;

    public Order(String creditCardId, String deliveryAddress, LocalDateTime orderDate,
                 LocalDateTime deliveryDate) {
        this.orderId = UUID.randomUUID().toString();
        this.creditCardId = creditCardId;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        ticketIdsList = new ArrayList<>();
    }

    public Order(String orderId, String creditCardId, String deliveryAddress, LocalDateTime orderDate, LocalDateTime deliveryDate) {
        this.orderId = orderId;
        this.creditCardId = creditCardId;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        ticketIdsList = new ArrayList<>();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<String> getTicketIdsList() {
        return ticketIdsList;
    }

    public void setTicketIdsList(List<String> ticketIdsList) {
        this.ticketIdsList = ticketIdsList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", creditCardId='" + creditCardId + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", ticketIdsList=" + ticketIdsList +
                '}';
    }
}