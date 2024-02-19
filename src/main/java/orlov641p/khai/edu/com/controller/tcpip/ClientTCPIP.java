package orlov641p.khai.edu.com.controller.tcpip;

import orlov641p.khai.edu.com.model.Client;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTCPIP extends JFrame {
    private JTextField clientIdField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField secondNameField;
    private JTextArea consoleTextArea;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;
    private ObjectInputStream serverInputStream;
    private static final String orderRequest = "CLIENT_REQUEST";

    public ClientTCPIP() {
        setTitle("Client Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
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
        JButton updateButton = new JButton("Update by id");
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

        try {
            clientSocket = new Socket("localhost", 5555);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            serverInputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject(orderRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void findAll() {
        String operation = "FIND_ALL";
        try {
            outputStream.writeObject(operation);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            consoleTextArea.append((String) serverInputStream.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getById(){
        String operation = "GET_BY_ID";
        try {
            String clientId = clientIdField.getText();

            outputStream.writeObject(operation);
            outputStream.writeObject(clientId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            Client foundClient = (Client) serverInputStream.readObject();

            consoleTextArea.append(foundClient.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add(){
        addOrUpdate("ADD");
    }

    private void update(){
        addOrUpdate("UPDATE");
    }

    private void addOrUpdate(String operation){
        try{
            outputStream.writeObject(operation);

            consoleTextArea.setText("Request that was sent to server: " + operation + "\n");

            String clientId = clientIdField.getText();
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String secondName = secondNameField.getText();

            Client client = new Client(clientId, lastName, firstName, secondName);

            outputStream.writeObject(client);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Added or updated client : " + client + " successfully");
            } else {
                consoleTextArea.append("Couldn't add or update this client " + client);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteById(){
        String operation = "DELETE_BY_ID";
        try {
            outputStream.writeObject(operation);

            String clientId = clientIdField.getText();

            outputStream.writeObject(clientId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Deleted client with id " + clientId + " successfully");
            } else {
                consoleTextArea.append("Couldn't delete client with id " + clientId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientTCPIP());
    }
}