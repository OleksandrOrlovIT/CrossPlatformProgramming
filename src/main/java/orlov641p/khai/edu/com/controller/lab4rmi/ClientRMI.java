package orlov641p.khai.edu.com.controller.lab4rmi;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI extends JFrame {
    private JTextField clientIdField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField secondNameField;
    private JTextArea consoleTextArea;

    ServerInterface serverInterface;

    public ClientRMI() {
        setTitle("Client Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();

        setVisible(true);

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5555);
            serverInterface = (ServerInterface) registry.lookup("ServerRMI");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() {
        JLabel clientIdLabel = new JLabel("Client ID:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel secondNameLabel = new JLabel("Second Name:");

        clientIdField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        secondNameField = new JTextField();
        consoleTextArea = new JTextArea();

        clientIdField.setColumns(25);
        clientIdField.setFont(clientIdField.getFont().deriveFont(14f));

        lastNameField.setColumns(25);
        lastNameField.setFont(lastNameField.getFont().deriveFont(14f));

        firstNameField.setColumns(25);
        firstNameField.setFont(firstNameField.getFont().deriveFont(14f));

        secondNameField.setColumns(25);
        secondNameField.setFont(secondNameField.getFont().deriveFont(14f));

        consoleTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        consoleTextArea.setFont(consoleTextArea.getFont().deriveFont(18f));

        JButton findAllButton = new JButton("Find All");
        JButton getByIdButton = new JButton("Get by Id");
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update by Id");
        JButton deleteByIdButton = new JButton("Delete by Id");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        panel.add(clientIdLabel);
        panel.add(clientIdField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(secondNameLabel);
        panel.add(secondNameField);
        panel.add(findAllButton);
        panel.add(getByIdButton);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteByIdButton);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.WEST);

        findAllButton.addActionListener(e -> findAll());
        getByIdButton.addActionListener(e -> getById());
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteByIdButton.addActionListener(e -> deleteById());
    }

    private void findAll() {
        try {
            consoleTextArea.setText(serverInterface.sendClientMessage("FIND_ALL"));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void getById() {
        String clientId = clientIdField.getText();
        try {
            consoleTextArea.setText(serverInterface.sendClientMessage( "GET_BY_ID/" + clientId));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteById() {
        String clientId = clientIdField.getText();

        try {
            consoleTextArea.setText(serverInterface.sendClientMessage( "DELETE_BY_ID/" + clientId));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAddOrUpdateRequest(String operation){
        String clientId = clientIdField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String secondName = secondNameField.getText();

        return operation + "/" + clientId + "/" + lastName + "/" + firstName + "/" + secondName;
    }

    private void add() {
        try {
            consoleTextArea.setText(serverInterface.sendClientMessage(getAddOrUpdateRequest("ADD")));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(){
        try {
            consoleTextArea.setText(serverInterface.sendClientMessage(getAddOrUpdateRequest("UPDATE")));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientRMI::new);
    }
}