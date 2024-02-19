package orlov641p.khai.edu.com.controller.lab2udpip;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP extends JFrame {
    private static final String CLIENT_REQUEST = "CLIENT_REQUEST";
    private static final int serverPort = 9876;

    DatagramSocket socket;
    InetAddress serverAddress;

    private JTextField clientIdField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField secondNameField;
    private JTextArea consoleTextArea;

    public ClientUDP() {
        setTitle("Client Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();

        try {
            socket = new DatagramSocket();

            serverAddress = InetAddress.getByName("localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
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

    private void sendRequest(DatagramSocket socket, InetAddress serverAddress, int serverPort, String request) {
        try {
            consoleTextArea.setText(CLIENT_REQUEST + " " + request + "\n");
            request = CLIENT_REQUEST + "," + request;
            byte[] requestData = request.getBytes();

            DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);

            socket.send(requestPacket);

            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            byte[] responseData = responsePacket.getData();
            ByteArrayInputStream bais = new ByteArrayInputStream(responseData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            String response = (String) ois.readObject();

            consoleTextArea.append(response);
            System.out.println("Response from server for " + request + ": " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAll() {
        sendRequest(socket, serverAddress, serverPort, "FIND_ALL");
    }

    private void getById() {
        String clientId = clientIdField.getText();
        sendRequest(socket, serverAddress, serverPort, "GET_BY_ID," + clientId);
    }

    private void add() {
        String clientId = clientIdField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String secondName = secondNameField.getText();

        String request = "ADD," + clientId + "," + lastName + "," + firstName + "," + secondName;
        sendRequest(socket, serverAddress, serverPort, request);
    }

    private void update(){
        String clientId = clientIdField.getText();
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String secondName = secondNameField.getText();

        String request = "UPDATE," + clientId + "," + lastName + "," + firstName + "," + secondName;
        sendRequest(socket, serverAddress, serverPort, request);
    }

    private void deleteById() {
        String clientId = clientIdField.getText();

        sendRequest(socket, serverAddress, serverPort, "DELETE_BY_ID," + clientId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientUDP::new);
    }
}