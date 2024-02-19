package orlov641p.khai.edu.com.controller.lab4rmi;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import orlov641p.khai.edu.com.service.ClientService;
import orlov641p.khai.edu.com.service.FlightService;
import orlov641p.khai.edu.com.service.OrderService;
import orlov641p.khai.edu.com.service.TicketService;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerImpl implements ServerInterface {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ClientService clientService;
    private final FlightService flightService;
    private final OrderService orderService;
    private final TicketService ticketService;

    public ServerImpl() {
        clientService = new ClientService();
        flightService = new FlightService();
        orderService = new OrderService();
        ticketService = new TicketService(clientService, flightService, orderService);

        bootStrap(clientService, orderService, flightService, ticketService);
    }

    @Override
    public String sendClientMessage(String operation) throws RemoteException {
        String[] urlParts = operation.split("/");
        StringBuilder response = new StringBuilder("ClientRMI sent " + urlParts[0] + " operation\n");
        switch (urlParts[0]) {
            case "FIND_ALL" -> {
                for (Client client : clientService.findAll()) {
                    response.append(client).append("\n");
                }
            }
            case "GET_BY_ID" -> {
                response.append(clientService.getById(urlParts[1]));
            }
            case "DELETE_BY_ID" -> {
                if(clientService.deleteById(urlParts[1], ticketService)){
                    response.append("Successfully deleted client with id = ");
                } else {
                    response.append("Couldn`t delete client with id = ");
                }
                response.append(urlParts[1]);
            }
            case "ADD" -> {
                Client client = new Client(urlParts[1], urlParts[2], urlParts[3], urlParts[4]);

                boolean add = clientService.add(client);
                if (add) {
                    response.append("Successfully added client = ").append(clientService.getById(urlParts[1]));
                } else {
                    response.append("Couldn`t add this client = ").append(client);
                }
            }
            case "UPDATE" -> {
                Client client = new Client(urlParts[1], urlParts[2], urlParts[3], urlParts[4]);

                clientService.update(client);
                response.append("Successfully updated client = ").append(clientService.getById(urlParts[1]));
            }
            default -> {
                response.append("No operation was found = ").append(operation);
            }
        }

        return response.toString();
    }

    @Override
    public String sendOrderMessage(String operation) throws RemoteException {
        String[] urlParts = operation.split("/");
        StringBuilder response = new StringBuilder("OrderRMI sent " + urlParts[0] + " operation\n");
        switch (urlParts[0]) {
            case "FIND_ALL" -> {
                for (Order order : orderService.findAll()) {
                    response.append(order).append("\n");
                }
            }
            case "GET_BY_ID" -> {
                response.append(orderService.getById(urlParts[1]));
            }
            case "DELETE_BY_ID" -> {
                if(orderService.deleteById(urlParts[1], ticketService)){
                    response.append("Successfully deleted order with id = ");
                } else {
                    response.append("Couldn`t delete order with id = ");
                }
                response.append(urlParts[1]);
            }
            case "ADD" -> {
                Order order = getOrder(urlParts);

                boolean add = orderService.add(order);
                if (add) {
                    response.append("Successfully added order = ").append(orderService.getById(urlParts[1]));
                } else {
                    response.append("Couldn`t add this order = ").append(order);
                }
            }
            case "UPDATE" -> {
                Order order = getOrder(urlParts);

                orderService.update(order);
                response.append("Successfully updated order = ").append(orderService.getById(urlParts[1]));
            }
            default -> {
                response.append("No operation was found = ").append(operation);
            }
        }

        return response.toString();
    }

    @Override
    public String sendFlightMessage(String operation) throws RemoteException {
        String[] urlParts = operation.split("/");
        StringBuilder response = new StringBuilder("FlightRMI sent " + urlParts[0] + " operation\n");
        switch (urlParts[0]) {
            case "FIND_ALL" -> {
                for(Flight flight : flightService.findAll()){
                    response.append(flight).append("\n");
                }
            }
            case "GET_BY_ID" -> {
                response.append(flightService.getById(urlParts[1]));
            }
            case "DELETE_BY_ID" -> {
                if(flightService.deleteById(urlParts[1], ticketService)){
                    response.append("Successfully deleted flight with id = ");
                } else {
                    response.append("Couldn`t delete flight with id = ");
                }
                response.append(urlParts[1]);
            }
            case "ADD" -> {
                Flight flight = getFlight(urlParts);

                boolean add = flightService.add(flight);
                if (add) {
                    response.append("Successfully added flight = ").append(flightService.getById(urlParts[1]));
                } else {
                    response.append("Couldn`t add this flight = ").append(flight);
                }
            }
            case "UPDATE" -> {
                Flight flight = getFlight(urlParts);

                flightService.update(flight, ticketService);
                response.append("Successfully updated flight = ").append(flightService.getById(urlParts[1]));
            }
            default -> {
                response.append("No operation was found = ").append(operation);
            }
        }

        return response.toString();
    }

    @Override
    public String sendTicketMessage(String operation) throws RemoteException {
        String[] urlParts = operation.split("/");
        StringBuilder response = new StringBuilder("TicketRMI sent " + urlParts[0] + " operation\n");
        switch (urlParts[0]) {
            case "FIND_ALL" -> {
                for(Ticket ticket : ticketService.findAll()){
                    response.append(ticket).append("\n");
                }
            }
            case "GET_BY_ID" -> {
                response.append(ticketService.getById(urlParts[1]));
            }
            case "DELETE_BY_ID" -> {
                if(ticketService.deleteById(urlParts[1])){
                    response.append("Successfully deleted ticket with id = ");
                } else {
                    response.append("Couldn`t delete ticket with id = ");
                }
                response.append(urlParts[1]);
            }
            case "ADD" -> {
                Ticket ticket = getTicket(urlParts);

                boolean add = ticketService.add(ticket);
                if (add) {
                    response.append("Successfully added ticket = ").append(ticketService.getById(urlParts[1]));
                } else {
                    response.append("Couldn`t add this ticket = ").append(ticket);
                }
            }
            case "UPDATE" -> {
                Ticket ticket = getTicket(urlParts);

                ticketService.update(ticket);
                response.append("Successfully updated ticket = ").append(ticketService.getById(urlParts[1]));
            }
            default -> {
                response.append("No operation was found = ").append(operation);
            }
        }

        return response.toString();
    }

    private static Order getOrder(String[] urlParts) {
        try {
            LocalDateTime orderDate = LocalDateTime.parse(urlParts[4], formatter);
            LocalDateTime deliveryDate = LocalDateTime.parse(urlParts[5], formatter);
            return new Order(urlParts[1], urlParts[2], urlParts[3], orderDate, deliveryDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Flight getFlight(String[] urlParts) {
        try {
            LocalDateTime date = LocalDateTime.parse(urlParts[4], formatter);
            int numberOfSeats = Integer.parseInt(urlParts[5]);
            return new Flight(urlParts[1], urlParts[2], urlParts[3], date, numberOfSeats);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Ticket getTicket(String[] parts){
        try {
            return new Ticket(parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void bootStrap(ClientService clientService, OrderService orderService, FlightService flightService,
                                  TicketService ticketService) {
        Client client1 = new Client("1", "Orlov", "Sasha", "Oleksandrovich");
        Client client2 = new Client("2", "Semenova", "Vika", "Oleksandrivna");
        clientService.add(client1);
        clientService.add(client2);

        Order order1 = new Order("1", "12345678910", "Kharkiv",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        Order order2 = new Order("2", "10123345466", "Kiyv",
                LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        orderService.add(order1);
        orderService.add(order2);

        Flight flight1 = new Flight("1", "Kharkiv", "Kiyv",
                LocalDateTime.now().plusDays(10), 5);

        Flight flight2 = new Flight("2", "Berlin", "Madrid",
                LocalDateTime.now().plusDays(30), 3);

        flightService.add(flight1);
        flightService.add(flight2);

        Ticket ticket1 = new Ticket("1", client1.getClientId(), flight1.getFlightId(), order1.getOrderId(), 1);
        Ticket ticket2 = new Ticket("2", client2.getClientId(), flight2.getFlightId(), order2.getOrderId(), 2);

        ticketService.add(ticket1);
        ticketService.add(ticket2);
    }
}
