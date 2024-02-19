package orlov641p.khai.edu.com.controller.lab2udpip;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TicketUDP extends JFrame {
    private JTextField ticketIdField;
    private JTextField clientIdField;
    private JTextField flightIdField;
    private JTextField orderIdField;
    private JTextField seatNumberField;
    private JTextArea consoleTextArea;
    private static final String TICKET_REQUEST = "TICKET_REQUEST";
    private static final int serverPort = 9876;
    DatagramSocket socket;
    InetAddress serverAddress;

    public TicketUDP() {
        setTitle("Ticket Application");
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
        JLabel ticketIdLabel = new JLabel("Ticket ID:");
        JLabel clientIdLabel = new JLabel("Client ID:");
        JLabel flightIdLabel = new JLabel("Flight ID:");
        JLabel seatNumberLabel = new JLabel("Seat number:");
        JLabel orderIdLabel = new JLabel("Order id:");

        ticketIdField = new JTextField();
        clientIdField = new JTextField();
        flightIdField = new JTextField();
        orderIdField = new JTextField();
        seatNumberField = new JTextField();
        consoleTextArea = new JTextArea();

        ticketIdField.setColumns(25);
        ticketIdField.setFont(ticketIdField.getFont().deriveFont(14f));

        clientIdField.setColumns(25);
        clientIdField.setFont(clientIdField.getFont().deriveFont(14f));

        flightIdField.setColumns(25);
        flightIdField.setFont(flightIdField.getFont().deriveFont(14f));

        orderIdField.setColumns(25);
        orderIdField.setFont(seatNumberField.getFont().deriveFont(14f));

        seatNumberField.setColumns(25);
        seatNumberField.setFont(seatNumberField.getFont().deriveFont(14f));


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

        panel.add(ticketIdLabel);
        panel.add(ticketIdField);
        panel.add(clientIdLabel);
        panel.add(clientIdField);
        panel.add(flightIdLabel);
        panel.add(flightIdField);
        panel.add(orderIdLabel);
        panel.add(orderIdField);
        panel.add(seatNumberLabel);
        panel.add(seatNumberField);
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
            consoleTextArea.setText(TICKET_REQUEST + " " + request + "\n");
            request = TICKET_REQUEST + "," + request;
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
        String ticketId = ticketIdField.getText();
        sendRequest(socket, serverAddress, serverPort, "GET_BY_ID," + ticketId);
    }

    private String getAddOrUpdateRequest(String operation){
        String ticketId = ticketIdField.getText();
        String clientId = clientIdField.getText();
        String flightId = flightIdField.getText();
        String orderId = orderIdField.getText();
        String seatNumber = seatNumberField.getText();

        return operation + "," + ticketId + "," + clientId + "," + flightId + "," + orderId + "," + seatNumber;
    }

    private void add() {
        sendRequest(socket, serverAddress, serverPort, getAddOrUpdateRequest("ADD"));
    }

    private void update(){
        sendRequest(socket, serverAddress, serverPort, getAddOrUpdateRequest("UPDATE"));
    }

    private void deleteById() {
        String ticketId = ticketIdField.getText();

        sendRequest(socket, serverAddress, serverPort, "DELETE_BY_ID," + ticketId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicketUDP::new);
    }
}
