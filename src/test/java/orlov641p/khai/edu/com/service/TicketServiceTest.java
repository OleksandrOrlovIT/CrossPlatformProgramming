package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Client;
import orlov641p.khai.edu.com.model.Flight;
import orlov641p.khai.edu.com.model.Order;
import orlov641p.khai.edu.com.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import orlov641p.khai.edu.com.service.ClientService;
import orlov641p.khai.edu.com.service.FlightService;
import orlov641p.khai.edu.com.service.OrderService;
import orlov641p.khai.edu.com.service.TicketService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    private static final ClientService clientService = new ClientService();
    private static final FlightService flightService = new FlightService();
    private static final OrderService orderService = new OrderService();
    private static final TicketService ticketService = new TicketService(clientService, flightService, orderService);

    Client client1 = new Client("1", "Orlov", "Sasha", "Oleksandrovich");
    Client client2 = new Client("2", "Semenova", "Vika", "Oleksandrivna");

    Order order1 = new Order("1", "12345678910", "Kharkiv",
            LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    Order order2 = new Order("2", "10123345466", "Kiyv",
            LocalDateTime.now(), LocalDateTime.now().plusDays(2));

    Flight flight1 = new Flight("1", "Kharkiv", "Kiyv",
            LocalDateTime.now().plusDays(10), 5);

    Flight flight2 = new Flight("2", "Berlin", "Madrid",
            LocalDateTime.now().plusDays(30), 3);

    Ticket ticket1 = new Ticket("1", client1.getClientId(), flight1.getFlightId(), order1.getOrderId(), 1);
    Ticket ticket2 = new Ticket("2", client2.getClientId(), flight2.getFlightId(), order2.getOrderId(), 2);


    @BeforeEach
    void setUp() {
        clientService.clients = new ArrayList<>();
        flightService.flights = new ArrayList<>();
        orderService.orders = new ArrayList<>();
        ticketService.tickets = new ArrayList<>();
    }

    @Test
    void addWithoutClientId() {
        flightService.add(flight1);
        orderService.add(order1);

        ticketService.add(ticket1);
        assertEquals(ticketService.findAll().size(), 0);
    }

    @Test
    void addWithoutFlightId() {
        clientService.add(client1);
        orderService.add(order1);

        ticketService.add(ticket1);
        assertEquals(ticketService.findAll().size(), 0);
    }

    @Test
    void addWithoutOrderId() {
        clientService.add(client1);
        flightService.add(flight1);

        ticketService.add(ticket1);
        assertEquals(ticketService.findAll().size(), 0);
    }

    @Test
    void addWithAllIds() {
        clientService.add(client1);
        orderService.add(order1);
        flightService.add(flight1);

        ticketService.add(ticket1);
        assertEquals(ticketService.findAll().size(), 1);
        assertEquals(ticket1, ticketService.getById("1"));
    }

    @Test
    void addDuplicateIdTicket() {
        clientService.add(client1);
        orderService.add(order1);
        flightService.add(flight1);

        ticketService.add(ticket1);
        ticketService.add(ticket1);
        assertEquals(ticketService.findAll().size(), 1);
        assertEquals(ticket1, ticketService.getById("1"));
    }

    @Test
    void addThirdTicketUsingOldIds() {
        clientService.add(client1);
        clientService.add(client2);
        orderService.add(order1);
        orderService.add(order2);
        flightService.add(flight1);
        flightService.add(flight2);

        ticketService.add(ticket1);
        ticketService.add(ticket2);

        Ticket ticket3 = new Ticket("3", client1.getClientId(), flight2.getFlightId(), order2.getOrderId(), 3);
        ticketService.add(ticket3);
        assertEquals(ticketService.findAll().size(), 3);
        assertEquals(ticket3, ticketService.getById("3"));
    }

    @Test
    void deleteClientDeletesAllTicketWithClientId() {
        clientService.add(client1);
        clientService.add(client2);
        orderService.add(order1);
        orderService.add(order2);
        flightService.add(flight1);
        flightService.add(flight2);

        ticketService.add(ticket1);
        ticketService.add(ticket2);

        Ticket ticket3 = new Ticket("3", client1.getClientId(), flight2.getFlightId(), order2.getOrderId(), 3);
        ticketService.add(ticket3);

        clientService.deleteById("1", ticketService);
        assertEquals(1, ticketService.findAll().size());
    }

    @Test
    void updateTicketNormal() {
        clientService.add(client1);
        clientService.add(client2);
        orderService.add(order1);
        orderService.add(order2);
        flightService.add(flight1);
        flightService.add(flight2);

        ticketService.add(ticket1);
        Ticket ticket3 = new Ticket("1", "2", "2", "2", 1);
        ticketService.update(ticket3);

        assertEquals(ticketService.findAll().size(), 1);
        assertEquals(ticket3, ticketService.getById("1"));
    }

    @Test
    void updateTicketWithWrongClientId() {
        clientService.add(client1);
        orderService.add(order1);
        orderService.add(order2);
        flightService.add(flight1);
        flightService.add(flight2);

        ticketService.add(ticket1);
        Ticket ticket3 = new Ticket("1", "2", "2", "2", 1);
        ticketService.update(ticket3);

        assertEquals(ticketService.findAll().size(), 1);
        assertEquals(ticket1, ticketService.getById("1"));
    }

    @Test
    void updateTicketWithNewClientNewFlightNewOrder() {
        clientService.add(client1);
        clientService.add(client2);
        orderService.add(order1);
        orderService.add(order2);
        flightService.add(flight1);
        flightService.add(flight2);

        ticketService.add(ticket1);
        ticketService.add(ticket2);
        Ticket ticket3 = new Ticket("1", "2", "2", "2", 1);
        ticketService.update(ticket3);

        assertEquals(ticketService.findAll().size(), 2);
        assertEquals(ticket3, ticketService.getById("1"));
        assertEquals(client1.getTicketIdsList().size(), 0);
        assertEquals(client2.getTicketIdsList().size(), 2);
        assertEquals(flight1.getTicketIdsList().size(), 0);
        assertEquals(flight2.getTicketIdsList().size(), 2);
        assertEquals(order1.getTicketIdsList().size(), 0);
        assertEquals(order2.getTicketIdsList().size(), 2);
    }
}