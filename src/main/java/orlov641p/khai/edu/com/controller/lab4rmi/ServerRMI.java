package orlov641p.khai.edu.com.controller.lab4rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {
    public static void main(String[] args) {
        try {
            // Create an instance of the remote service implementation
            ServerImpl serverService = new ServerImpl();

            // Export the remote object
            ServerInterface clientStub = (ServerInterface) UnicastRemoteObject.exportObject(serverService, 0);

            // Create and start the RMI registry
            Registry registry = LocateRegistry.createRegistry(5555);

            // Bind the remote object to the registry
            registry.rebind("ServerRMI", clientStub);

            System.out.println("ServerRMI ready");
        } catch (Exception e) {
            System.err.println("ServerRMI exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
