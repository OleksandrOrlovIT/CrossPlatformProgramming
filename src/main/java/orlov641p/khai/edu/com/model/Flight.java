package orlov641p.khai.edu.com.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Flight implements Serializable {
    private String flightId;
    private String origin;
    private String destination;
    private LocalDateTime flightDate;
    private String[] seats;
    private List<String> ticketIdsList;

    public Flight(String origin, String destination, LocalDateTime flightDate, Integer numberOfSeats) {
        this.flightId = UUID.randomUUID().toString();
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        seats = new String[numberOfSeats];
        ticketIdsList = new ArrayList<>();
    }

    public Flight(String flightId, String origin, String destination, LocalDateTime flightDate, Integer numberOfSeats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        seats = new String[numberOfSeats];
        ticketIdsList = new ArrayList<>();
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDateTime flightDate) {
        this.flightDate = flightDate;
    }

    public String[] getSeats() {
        return seats;
    }

    public void setSeats(String[] seats) {
        this.seats = seats;
    }

    public List<String> getTicketIdsList() {
        return ticketIdsList;
    }

    public void setTicketIdsList(List<String> ticketIdsList) {
        this.ticketIdsList = ticketIdsList;
    }

    public String getFormattedSeats(){
        StringBuilder sb = new StringBuilder("{");

        for(int i = 0; i < seats.length; i++){
            sb.append(i+1);
            if(seats[i] == null){
                sb.append("=free");
            } else {
                sb.append("=taken");
            }

            if(i < seats.length-1){
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", flightDate=" + flightDate +
                ", seats=" + getFormattedSeats() +
                ", ticketIdsList=" + ticketIdsList +
                '}';
    }
}