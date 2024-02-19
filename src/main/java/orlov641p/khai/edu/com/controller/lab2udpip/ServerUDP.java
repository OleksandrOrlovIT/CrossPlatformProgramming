package orlov641p.khai.edu.com.controller.lab2udpip;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import orlov641p.khai.edu.com.service.ClientService;
import orlov641p.khai.edu.com.service.FlightService;
import orlov641p.khai.edu.com.service.OrderService;
import orlov641p.khai.edu.com.service.TicketService;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerUDP {
    private static final ClientService clientService = new ClientService();
    private static final OrderService orderService = new OrderService();
    private static final FlightService flightService = new FlightService();
    private static final TicketService ticketService = new TicketService(clientService, flightService, orderService);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9876);
            bootStrap();

            while (true) {
                byte[] buffer = new byte[1024];

                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

                socket.receive(receivedPacket);

                String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                String responseMessage = processMessage(receivedMessage);

                sendResponse(socket, receivedPacket.getAddress(), receivedPacket.getPort(), responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processMessage(String receivedMessage) {
        String[] parts = receivedMessage.split(",");

        String typeOfService = parts[0];
        String typeOfRequest = parts[1];

        switch (typeOfService) {
            case "CLIENT_REQUEST":
                return handleClientServiceRequest(typeOfRequest, parts);
            case "ORDER_REQUEST":
                return handleOrderServiceRequest(typeOfRequest, parts);
            case "FLIGHT_REQUEST":
                return handleFlightServiceRequest(typeOfRequest, parts);
            case "TICKET_REQUEST":
                return handleTicketServiceRequest(typeOfRequest, parts);
            default:
                return "Unknown request type: " + typeOfService;
        }
    }

    private static String handleClientServiceRequest(String typeOfRequest, String[] parts) {
        switch (typeOfRequest) {
            case "FIND_ALL" -> {
                StringBuilder sb = new StringBuilder();
                for (Client client : clientService.findAll()) {
                    sb.append(client).append("\n");
                }
                return sb.toString();
            }
            case "GET_BY_ID" -> {
                return clientService.getById(parts[2]).toString();
            }
            case "ADD" -> {
                Client client = new Client(parts[2], parts[3], parts[4], parts[5]);

                boolean add = clientService.add(client);
                if (add) {
                    return clientService.getById(parts[2]).toString();
                } else {
                    return "Couldn`t add this client = " + client;
                }
            }
            case "UPDATE" -> {
                Client client = new Client(parts[2], parts[3], parts[4], parts[5]);

                clientService.update(client);
                return clientService.getById(parts[2]).toString();
            }

            case "DELETE_BY_ID" -> {
                boolean result = clientService.deleteById(parts[2], ticketService);
                if (result) {
                    return "Deleted client with id = " + parts[2];
                } else {
                    return "Couldn`t delete client with id = " + parts[2];
                }
            }
            default -> {
                return "Unknown request for Client Request: " + typeOfRequest;
            }
        }
    }

    private static String handleOrderServiceRequest(String typeOfRequest, String[] parts) {
        switch (typeOfRequest) {
            case "FIND_ALL" -> {
                StringBuilder sb = new StringBuilder();
                for(Order order : orderService.findAll()){
                    sb.append(order).append("\n");
                }
                return sb.toString();
            }
            case "GET_BY_ID" -> {
                return orderService.getById(parts[2]).toString();
            }
            case "ADD" -> {
                Order order = getOrder(parts);

                boolean add = orderService.add(order);
                if (add) {
                    return orderService.getById(parts[2]).toString();
                } else {
                    return "Couldn`t add this order = " + order;
                }
            }
            case "UPDATE" -> {
                Order order = getOrder(parts);

                orderService.update(order);
                return orderService.getById(parts[2]).toString();
            }

            case "DELETE_BY_ID" -> {
                boolean result = orderService.deleteById(parts[2], ticketService);
                if (result) {
                    return "Deleted order with id = " + parts[2];
                } else {
                    return "Couldn`t delete order with id = " + parts[2];
                }
            }
            default -> {
                return "Unknown request for Order Request: " + typeOfRequest;
            }
        }
    }

    private static String handleFlightServiceRequest(String typeOfRequest, String[] parts) {
        switch (typeOfRequest) {
            case "FIND_ALL" -> {
                StringBuilder sb = new StringBuilder();
                for (Flight flight : flightService.findAll()) {
                    sb.append(flight).append("\n");
                }
                return sb.toString();
            }
            case "GET_BY_ID" -> {
                return flightService.getById(parts[2]).toString();
            }
            case "ADD" -> {
                Flight flight = getFlight(parts);

                boolean add = flightService.add(flight);
                if (add) {
                    return flightService.getById(parts[2]).toString();
                } else {
                    return "Couldn`t add this flight = " + flight;
                }
            }
            case "UPDATE" -> {
                Flight flight = getFlight(parts);

                flightService.update(flight, ticketService);
                return flightService.getById(parts[2]).toString();
            }

            case "DELETE_BY_ID" -> {
                boolean result = flightService.deleteById(parts[2], ticketService);
                if (result) {
                    return "Deleted flight with id = " + parts[2];
                } else {
                    return "Couldn`t delete flight with id = " + parts[2];
                }
            }
            default -> {
                return "Unknown request for Flight Request: " + typeOfRequest;
            }
        }
    }

    private static String handleTicketServiceRequest(String typeOfRequest, String[] parts) {
        switch (typeOfRequest) {
            case "FIND_ALL" -> {
                StringBuilder sb = new StringBuilder();
                for (Ticket ticket : ticketService.findAll()) {
                    sb.append(ticket).append("\n");
                }
                return sb.toString();
            }
            case "GET_BY_ID" -> {
                return ticketService.getById(parts[2]).toString();
            }
            case "ADD" -> {
                Ticket ticket = getTicket(parts);

                boolean add = ticketService.add(ticket);
                if (add) {
                    return ticketService.getById(parts[2]).toString();
                } else {
                    return "Couldn`t add this ticket = " + ticket;
                }
            }
            case "UPDATE" -> {
                Ticket ticket = getTicket(parts);

                ticketService.update(ticket);
                return ticketService.getById(parts[2]).toString();
            }

            case "DELETE_BY_ID" -> {
                boolean result = ticketService.deleteById(parts[2]);
                if (result) {
                    return "Deleted ticket with id = " + parts[2];
                } else {
                    return "Couldn`t delete ticket with id = " + parts[2];
                }
            }
            default -> {
                return "Unknown request for Ticket Request: " + typeOfRequest;
            }
        }
    }

    private static void sendResponse(DatagramSocket socket, InetAddress clientAddress, int clientPort, String response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            byte[] responseData = baos.toByteArray();

            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);

            socket.send(responsePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void bootStrap() {
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

    private static Flight getFlight(String[] parts) {
        try {
            LocalDateTime date = LocalDateTime.parse(parts[5], formatter);
            int numberOfSeats = Integer.parseInt(parts[6]);
            return new Flight(parts[2], parts[3], parts[4], date, numberOfSeats);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Order getOrder(String[] parts){
        try {
            LocalDateTime orderDate = LocalDateTime.parse(parts[5], formatter);
            LocalDateTime deliveryDate = LocalDateTime.parse(parts[6], formatter);
            return new Order(parts[2], parts[3], parts[4], orderDate, deliveryDate);
        }catch (Exception e){
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
}