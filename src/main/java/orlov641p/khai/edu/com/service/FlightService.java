package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Flight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlightService implements Service<Flight>{
    List<Flight> flights;

    public FlightService() {
        flights = new ArrayList<>();
    }

    @Override
    public boolean add(Flight flight) {
        if(flight != null && getById(flight.getFlightId()) == null) {
            return flights.add(flight);
        } else {
            return false;
        }
    }

    @Override
    public Flight getById(String id) {
        return flights.stream()
                .filter(flight -> flight.getFlightId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(String id, TicketService ticketService) {
        Flight foundFlight = getById(id);
        if(foundFlight != null){
            List<String> ids = new ArrayList<>(foundFlight.getTicketIdsList());
            for(String ticketsId : ids) {
                ticketService.deleteById(ticketsId);
            }
            flights.remove(foundFlight);
            return true;
        }

        return false;
    }

    public boolean update(Flight flight, TicketService ticketService) {
        if(flight != null) {
            Flight foundFlight = getById(flight.getFlightId());
            if (foundFlight != null) {
                foundFlight.setOrigin(flight.getOrigin());
                foundFlight.setDestination(flight.getDestination());
                foundFlight.setFlightDate(flight.getFlightDate());

                String[] oldArray = foundFlight.getSeats();
                int oldArrayLength = oldArray.length, newArrayLength = flight.getSeats().length;
                if (oldArrayLength < newArrayLength) {
                    foundFlight.setSeats(Arrays.copyOf(oldArray, newArrayLength));
                } else {
                    for (int i = newArrayLength; i < oldArrayLength; i++) {
                        if (oldArray[i] != null) {
                            ticketService.deleteById(oldArray[i]);
                        }
                    }
                    foundFlight.setSeats(Arrays.copyOf(oldArray, newArrayLength));
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Flight> findAll() {
        return flights;
    }
}
