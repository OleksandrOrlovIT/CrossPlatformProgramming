package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketService implements Service<Ticket> {

    List<Ticket> tickets;
    ClientService clientService;
    FlightService flightService;
    OrderService orderService;

    public TicketService(ClientService clientService, FlightService flightService, OrderService orderService) {
        tickets = new ArrayList<>();
        this.flightService = flightService;
        this.clientService = clientService;
        this.orderService = orderService;
    }

    @Override
    public boolean add(Ticket ticket) {
        if (ticket != null && checkAllIds(ticket) && getById(ticket.getTicketId()) == null) {
            String[] seats = flightService.getById(ticket.getFlightId()).getSeats();
            seats[ticket.getSeatNumber() - 1] = ticket.getTicketId();

            flightService.getById(ticket.getFlightId()).getTicketIdsList().add(ticket.getTicketId());

            clientService.getById(ticket.getClientId()).getTicketIdsList().add(ticket.getTicketId());

            orderService.getById(ticket.getOrderId()).getTicketIdsList().add(ticket.getTicketId());
            return tickets.add(ticket);
        } else {
            return false;
        }
    }

    @Override
    public Ticket getById(String id) {
        return tickets.stream()
                .filter(ticket -> ticket.getTicketId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(String id) {
        Ticket ticket = getById(id);
        if (ticket != null) {
            String[] seats = flightService.getById(ticket.getFlightId()).getSeats();
            seats[ticket.getSeatNumber() - 1] = null;

            clientService.getById(ticket.getClientId()).getTicketIdsList().remove(ticket.getTicketId());

            flightService.getById(ticket.getFlightId()).getTicketIdsList().remove(ticket.getTicketId());

            orderService.getById(ticket.getOrderId()).getTicketIdsList().remove(ticket.getTicketId());

            return tickets.remove(ticket);
        }

        return false;
    }

    public boolean update(Ticket ticket) {
        if (ticket != null && checkAllIds(ticket) && getById(ticket.getTicketId()) != null) {
            deleteById(ticket.getTicketId());
            return add(ticket);
        } else {
            return false;
        }
    }

    @Override
    public List<Ticket> findAll() {
        return tickets;
    }

    public boolean checkAllIds(Ticket ticket) {
        return clientService.getById(ticket.getClientId()) != null
                && flightService.getById(ticket.getFlightId()) != null
                && orderService.getById(ticket.getOrderId()) != null;
    }
}
