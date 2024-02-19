package orlov641p.khai.edu.com.model;

import java.io.Serializable;

public class Ticket implements Serializable {
    private String ticketId;
    private String clientId;
    private String flightId;
    private String orderId;
    private Integer seatNumber;

    public Ticket(String ticketId, String clientId, String flightId, String orderId, Integer seatNumber) {
        this.ticketId = ticketId;
        this.clientId = clientId;
        this.flightId = flightId;
        this.orderId = orderId;
        this.seatNumber = seatNumber;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", flightId='" + flightId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", seatNumber=" + seatNumber +
                '}';
    }
}
