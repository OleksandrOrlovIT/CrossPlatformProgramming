package orlov641p.khai.edu.com.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client implements Serializable {
    private String clientId;
    private String lastName;
    private String firstName;
    private String secondName;
    private List<String> ticketIdsList;

    public Client(String lastName, String firstName, String secondName) {
        this.clientId = UUID.randomUUID().toString();
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        ticketIdsList = new ArrayList<>();
    }

    public Client(String clientId, String lastName, String firstName, String secondName) {
        this.clientId = clientId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        ticketIdsList = new ArrayList<>();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<String> getTicketIdsList() {
        return ticketIdsList;
    }

    public void setTicketIdsList(List<String> ticketIdsList) {
        this.ticketIdsList = ticketIdsList;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", clientTicketsIds=" + ticketIdsList +
                '}';
    }
}