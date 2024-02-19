package orlov641p.khai.edu.com.controller.lab4rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    String sendClientMessage(String operation) throws RemoteException;

    String sendOrderMessage(String operation) throws RemoteException;

    String sendFlightMessage(String operation) throws RemoteException;

    String sendTicketMessage(String operation) throws RemoteException;
}
