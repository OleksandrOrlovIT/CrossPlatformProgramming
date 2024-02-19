package orlov641p.khai.edu.com.controller.tcpip;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import orlov641p.khai.edu.com.service.ClientService;
import orlov641p.khai.edu.com.service.FlightService;
import orlov641p.khai.edu.com.service.OrderService;
import orlov641p.khai.edu.com.service.TicketService;

public class ServerTCPIP {
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            ClientService clientService = new ClientService();
            OrderService orderService = new OrderService();
            FlightService flightService = new FlightService();
            TicketService ticketService = new TicketService(clientService, flightService, orderService);

            bootStrap(clientService, orderService, flightService, ticketService);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

                String requestType = (String) inputStream.readObject();

                switch (requestType) {
                    case "CLIENT_REQUEST" -> new Thread(() ->
                            handleClientRequests(inputStream, outputStream, clientService, ticketService)).start();
                    case "FLIGHT_REQUEST" -> new Thread(() ->
                            handleFlightRequests(inputStream, outputStream, flightService, ticketService)).start();
                    case "ORDER_REQUEST" -> new Thread(() ->
                            handleOrderRequests(inputStream, outputStream, orderService, ticketService)).start();
                    case "TICKET_REQUEST" -> new Thread(() -> handleTicketRequests(inputStream, outputStream, ticketService)).start();
                    default -> outputStream.writeObject("Invalid request type: " + requestType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClientRequests(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                                             ClientService clientService, TicketService ticketService) {
        try {
            while (true) {
                String request = (String) inputStream.readObject();
                System.out.println("Inside handleClientRequests");
                outputStream.writeObject("Received client request: " + request + "\n");

                switch (request) {
                    case "FIND_ALL" -> {
                        StringBuilder sb = new StringBuilder();
                        for (Client client : clientService.findAll()){
                            sb.append(client).append("\n");
                        }
                        outputStream.writeObject(sb.toString());
                    }
                    case "GET_BY_ID" -> {
                        String clientId = (String) inputStream.readObject();
                        outputStream.writeObject(clientService.getById(clientId));
                    }
                    case "ADD" -> {
                        Client client = (Client) inputStream.readObject();
                        boolean result = clientService.add(client);
                        outputStream.writeObject(result);
                    }
                    case "UPDATE" -> {
                        Client client = (Client)  inputStream.readObject();
                        boolean result = clientService.update(client);
                        outputStream.writeObject(result);
                    }
                    case "DELETE_BY_ID" -> {
                        String clientId = (String) inputStream.readObject();
                        boolean result = clientService.deleteById(clientId, ticketService);
                        outputStream.writeObject(result);
                    }
                    default -> {
                        outputStream.writeObject("Couldn`t find any suitable request for " + request);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void handleFlightRequests(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                                             FlightService flightService, TicketService ticketService) {
        try {
            while (true) {
                String request = (String) inputStream.readObject();
                System.out.println("Inside handleFlightRequests");
                outputStream.writeObject("Received flight request: " + request + "\n");

                switch (request) {
                    case "FIND_ALL" -> {
                        StringBuilder sb = new StringBuilder();
                        for (Flight flight : flightService.findAll()){
                            sb.append(flight).append("\n");
                        }
                        outputStream.writeObject(sb.toString());
                    }
                    case "GET_BY_ID" -> {
                        String flightId = (String) inputStream.readObject();
                        outputStream.writeObject(flightService.getById(flightId));
                    }
                    case "ADD" -> {
                        Flight flight = (Flight) inputStream.readObject();
                        boolean result = flightService.add(flight);
                        outputStream.writeObject(result);
                    }
                    case "UPDATE" -> {
                        Flight flight = (Flight) inputStream.readObject();
                        boolean result = flightService.update(flight, ticketService);
                        outputStream.writeObject(result);
                    }
                    case "DELETE_BY_ID" -> {
                        String flightId = (String) inputStream.readObject();
                        boolean result = flightService.deleteById(flightId, ticketService);
                        outputStream.writeObject(result);
                    }
                    default -> {
                        outputStream.writeObject("Couldn`t find any suitable request for " + request);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void handleOrderRequests(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                                            OrderService orderService, TicketService ticketService) {
        try {
            while (true) {
                String request = (String) inputStream.readObject();
                System.out.println("Inside handleOrderRequests");
                outputStream.writeObject("Received  request: " + request + "\n");

                switch (request) {
                    case "FIND_ALL" -> {
                        StringBuilder sb = new StringBuilder();
                        for (Order order : orderService.findAll()){
                            sb.append(order).append("\n");
                        }
                        outputStream.writeObject(sb.toString());
                    }
                    case "GET_BY_ID" -> {
                        String orderId = (String) inputStream.readObject();
                        outputStream.writeObject(orderService.getById(orderId));
                    }
                    case "ADD" -> {
                        Order order = (Order) inputStream.readObject();
                        boolean result = orderService.add(order);
                        outputStream.writeObject(result);
                    }
                    case "UPDATE" -> {
                        Order order = (Order) inputStream.readObject();
                        boolean result = orderService.update(order);
                        outputStream.writeObject(result);
                    }
                    case "DELETE_BY_ID" -> {
                        String orderId = (String) inputStream.readObject();
                        boolean result = orderService.deleteById(orderId, ticketService);
                        outputStream.writeObject(result);
                    }
                    default -> {
                        outputStream.writeObject("Couldn`t find any suitable request for " + request);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void handleTicketRequests(ObjectInputStream inputStream, ObjectOutputStream outputStream, TicketService ticketService) {
        try {
            while (true) {
                String request = (String) inputStream.readObject();
                System.out.println("Inside handleTicketRequests");
                outputStream.writeObject("Received  request: " + request + "\n");

                switch (request) {
                    case "FIND_ALL" -> {
                        StringBuilder sb = new StringBuilder();
                        for (Ticket ticket : ticketService.findAll()){
                            sb.append(ticket).append("\n");
                        }
                        outputStream.writeObject(sb.toString());
                    }
                    case "GET_BY_ID" -> {
                        String ticketId = (String) inputStream.readObject();
                        outputStream.writeObject(ticketService.getById(ticketId));
                    }
                    case "ADD" -> {
                        Ticket ticket = (Ticket) inputStream.readObject();
                        boolean result = ticketService.add(ticket);
                        outputStream.writeObject(result);
                    }
                    case "UPDATE" -> {
                        Ticket ticket = (Ticket) inputStream.readObject();
                        boolean result = ticketService.update(ticket);
                        outputStream.writeObject(result);
                    }
                    case "DELETE_BY_ID" -> {
                        String ticketId = (String) inputStream.readObject();
                        boolean result = ticketService.deleteById(ticketId);
                        outputStream.writeObject(result);
                    }
                    default -> {
                        outputStream.writeObject("Couldn`t find any suitable request for " + request);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void bootStrap(ClientService clientService, OrderService orderService, FlightService flightService,
                                  TicketService ticketService){
        Client client1 = new Client("1", "Orlov", "Sasha", "Oleksandrovich");
        Client client2 = new Client("2","Semenova", "Vika", "Oleksandrivna");
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
