package orlov641p.khai.edu.com.controller.lab3url;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import orlov641p.khai.edu.com.service.ClientService;
import orlov641p.khai.edu.com.service.FlightService;
import orlov641p.khai.edu.com.service.OrderService;
import orlov641p.khai.edu.com.service.TicketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ServerHTTP {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        int port = 8080;

        ClientService clientService = new ClientService();
        OrderService orderService = new OrderService();
        FlightService flightService = new FlightService();
        TicketService ticketService = new TicketService(clientService, flightService, orderService);

        bootStrap(clientService, orderService, flightService, ticketService);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String requestLine = reader.readLine();
                String url = requestLine.substring(5, requestLine.length()-9);
                String[] urlParts = url.split("/");
                System.out.println("url parts = " + Arrays.toString(urlParts));
                switch (urlParts[0]) {
                    case "CLIENT_REQUEST" -> new Thread(() -> handleClientRequest(clientSocket, clientService,
                            ticketService, urlParts)).start();
                    case "ORDER_REQUEST" -> new Thread(() -> handleOrderRequest(clientSocket, orderService,
                            ticketService, urlParts)).start();
                    case "FLIGHT_REQUEST" -> new Thread(() -> handleFlightRequest(clientSocket, flightService,
                            ticketService, urlParts)).start();
                    case "TICKET_REQUEST" -> new Thread(() -> handleTicketRequest(clientSocket, ticketService,
                            urlParts)).start();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket, ClientService clientService,
                                            TicketService ticketService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nCLIENT_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Client client : clientService.findAll()) {
                        response.append(client).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(clientService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(clientService.deleteById(urlParts[2], ticketService)){
                        response.append("Successfully deleted client with id = ");
                    } else {
                        response.append("Couldn`t delete client with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Client client = new Client(urlParts[2], urlParts[3], urlParts[4], urlParts[5]);

                    boolean add = clientService.add(client);
                    if (add) {
                        response.append(clientService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this client = ").append(client);
                    }
                }
                case "UPDATE" -> {
                    Client client = new Client(urlParts[2], urlParts[3], urlParts[4], urlParts[5]);

                    clientService.update(client);
                    response.append(clientService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleOrderRequest(Socket clientSocket, OrderService orderService,
                                            TicketService ticketService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nORDER_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Order order : orderService.findAll()) {
                        response.append(order).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(orderService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(orderService.deleteById(urlParts[2], ticketService)){
                        response.append("Successfully deleted order with id = ");
                    } else {
                        response.append("Couldn`t delete order with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Order order = getOrder(urlParts);

                    boolean add = orderService.add(order);
                    if (add) {
                        response.append(orderService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this order = ").append(order);
                    }
                }
                case "UPDATE" -> {
                    Order order = getOrder(urlParts);

                    orderService.update(order);
                    response.append(orderService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleFlightRequest(Socket clientSocket, FlightService flightService,
                                           TicketService ticketService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nFLIGHT_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for (Flight flight : flightService.findAll()) {
                        response.append(flight).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(flightService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(flightService.deleteById(urlParts[2], ticketService)){
                        response.append("Successfully deleted flight with id = ");
                    } else {
                        response.append("Couldn`t delete flight with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Flight flight = getFlight(urlParts);

                    boolean add = flightService.add(flight);
                    if (add) {
                        response.append(flightService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this flight = ").append(flight);
                    }
                }
                case "UPDATE" -> {
                    Flight flight = getFlight(urlParts);

                    flightService.update(flight, ticketService);
                    response.append(flightService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleTicketRequest(Socket clientSocket, TicketService ticketService, String[] urlParts) {
        String operation = urlParts[1];
        try {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\nTICKET_REQUEST " + operation + "\n");
            switch (operation) {
                case "FIND_ALL" -> {
                    for(Ticket ticket : ticketService.findAll()){
                        response.append(ticket).append("\n");
                    }
                }
                case "GET_BY_ID" -> {
                    response.append(ticketService.getById(urlParts[2]));
                }
                case "DELETE_BY_ID" -> {
                    if(ticketService.deleteById(urlParts[2])){
                        response.append("Successfully deleted ticket with id = ");
                    } else {
                        response.append("Couldn`t delete ticket with id = ");
                    }
                    response.append(urlParts[2]);
                }
                case "ADD" -> {
                    Ticket ticket = getTicket(urlParts);

                    boolean add = ticketService.add(ticket);
                    if (add) {
                        response.append(ticketService.getById(urlParts[2]));
                    } else {
                        response.append("Couldn`t add this ticket = ").append(ticket);
                    }
                }
                case "UPDATE" -> {
                    Ticket ticket = getTicket(urlParts);

                    ticketService.update(ticket);
                    response.append(ticketService.getById(urlParts[2]));
                }
                default -> {
                    response.append("No operation found = ").append(operation);
                }
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            System.out.println("Response = " + response);

            outputStream.write(response.toString().getBytes());

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Order getOrder(String[] urlParts) {
        try {
            LocalDateTime orderDate = LocalDateTime.parse(urlParts[5], formatter);
            LocalDateTime deliveryDate = LocalDateTime.parse(urlParts[6], formatter);
            return new Order(urlParts[2], urlParts[3], urlParts[4], orderDate, deliveryDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Flight getFlight(String[] urlParts) {
        try {
            LocalDateTime date = LocalDateTime.parse(urlParts[5], formatter);
            int numberOfSeats = Integer.parseInt(urlParts[6]);
            return new Flight(urlParts[2], urlParts[3], urlParts[4], date, numberOfSeats);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Ticket getTicket(String[] parts){
        try {
            return new Ticket(parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6]));
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