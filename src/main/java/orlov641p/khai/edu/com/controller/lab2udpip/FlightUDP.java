package orlov641p.khai.edu.com.controller.lab2udpip;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FlightUDP extends JFrame{
    private JTextField flightIdField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField flightDateField;
    private JTextField numberOfSeatsField;
    private JTextArea consoleTextArea;

    private static final String FLIGHT_REQUEST = "FLIGHT_REQUEST";
    private static final int serverPort = 9876;
    DatagramSocket socket;
    InetAddress serverAddress;

    public FlightUDP() {
        setTitle("Flight Application");
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
        JLabel flightIdLabel = new JLabel("Flight ID:");
        JLabel originLabel = new JLabel("Origin:");
        JLabel destinationLabel = new JLabel("Destination:");
        JLabel flightDateLabel = new JLabel("Order Date (YYYY-MM-DD HH:mm)");
        JLabel availableSeatsLabel = new JLabel("Number of seats");

        flightIdField = new JTextField();
        originField = new JTextField();
        destinationField = new JTextField();
        flightDateField = new JTextField();
        numberOfSeatsField = new JTextField();
        consoleTextArea = new JTextArea();

        flightIdField.setColumns(25);
        flightIdField.setFont(flightIdField.getFont().deriveFont(14f));

        originField.setColumns(25);
        originField.setFont(originField.getFont().deriveFont(14f));

        destinationField.setColumns(25);
        destinationField.setFont(destinationField.getFont().deriveFont(14f));

        flightDateField.setColumns(25);
        flightDateField.setFont(flightDateField.getFont().deriveFont(14f));

        numberOfSeatsField.setColumns(25);
        numberOfSeatsField.setFont(numberOfSeatsField.getFont().deriveFont(14f));

        consoleTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        consoleTextArea.setFont(consoleTextArea.getFont().deriveFont(18f));

        JButton findAllButton = new JButton("Find All");
        JButton getByIdButton = new JButton("Get by Id");
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update by id");
        JButton deleteByIdButton = new JButton("Delete by Id");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        panel.add(flightIdLabel);
        panel.add(flightIdField);
        panel.add(originLabel);
        panel.add(originField);
        panel.add(destinationLabel);
        panel.add(destinationField);
        panel.add(flightDateLabel);
        panel.add(flightDateField);
        panel.add(availableSeatsLabel);
        panel.add(numberOfSeatsField);
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
            consoleTextArea.setText(FLIGHT_REQUEST + " " + request + "\n");
            request = FLIGHT_REQUEST + "," + request;
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
        String flightId = flightIdField.getText();
        sendRequest(socket, serverAddress, serverPort, "GET_BY_ID," + flightId);
    }

    private String getAddOrUpdateRequest(String operation){
        String flightId = flightIdField.getText();
        String origin = originField.getText();
        String destination = destinationField.getText();
        String date = flightDateField.getText();
        String numberOfSeats = numberOfSeatsField.getText();

        return operation + "," + flightId + "," + origin + "," + destination + "," + date + "," + numberOfSeats;
    }

    private void add() {
        sendRequest(socket, serverAddress, serverPort, getAddOrUpdateRequest("ADD"));
    }

    private void update(){
        sendRequest(socket, serverAddress, serverPort, getAddOrUpdateRequest("UPDATE"));
    }

    private void deleteById() {
        String flightId = flightIdField.getText();

        sendRequest(socket, serverAddress, serverPort, "DELETE_BY_ID," + flightId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlightUDP::new);
    }
}