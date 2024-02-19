package orlov641p.khai.edu.com.service;

import orlov641p.khai.edu.com.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService implements Service<Client>{
    List<Client> clients;

    public ClientService() {
        clients = new ArrayList<>();
    }

    @Override
    public boolean add(Client client) {
        if(client != null && getById(client.getClientId()) == null) {
            return clients.add(client);
        } else {
            return false;
        }
    }

    @Override
    public Client getById(String id) {
        return clients.stream()
                .filter(client -> client.getClientId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(String id, TicketService ticketService) {
        Client foundClient = getById(id);
        if(foundClient != null){
            List<String> ids = new ArrayList<>(foundClient.getTicketIdsList());
            for(String ticketsId : ids) {
                ticketService.deleteById(ticketsId);
            }
            clients.remove(foundClient);
            return true;
        }

        return false;
    }

    public boolean update(Client client) {
        if(client != null) {
            Client foundClient = getById(client.getClientId());
            if (foundClient != null) {
                foundClient.setFirstName(client.getFirstName());
                foundClient.setLastName(client.getLastName());
                foundClient.setSecondName(client.getSecondName());
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Client> findAll() {
        return clients;
    }
}