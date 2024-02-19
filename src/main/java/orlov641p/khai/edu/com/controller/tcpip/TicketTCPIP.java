package orlov641p.khai.edu.com.controller.tcpip;

import orlov641p.khai.edu.com.model.Ticket;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TicketTCPIP extends JFrame {
    private JTextField ticketIdField;
    private JTextField clientIdField;
    private JTextField flightIdField;
    private JTextField orderIdField;
    private JTextField seatNumberField;
    private JTextArea consoleTextArea;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;
    private ObjectInputStream serverInputStream;
    private static final String orderRequest = "TICKET_REQUEST";

    public TicketTCPIP() {
        setTitle("Ticket Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
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
            String ticketId = ticketIdField.getText();

            outputStream.writeObject(operation);
            outputStream.writeObject(ticketId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            Ticket foundTicket = (Ticket) serverInputStream.readObject();

            consoleTextArea.append(foundTicket.toString());

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

            String ticketId = ticketIdField.getText();
            String clientId = clientIdField.getText();
            String flightId = flightIdField.getText();
            String orderId = orderIdField.getText();
            Integer seatNumber = Integer.parseInt(seatNumberField.getText());

            Ticket ticket = new Ticket(ticketId, clientId, flightId, orderId, seatNumber);

            outputStream.writeObject(ticket);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Added or updated ticket : " + ticket + " successfully");
            } else {
                consoleTextArea.append("Couldn't add or update this ticket " + ticket);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteById(){
        String operation = "DELETE_BY_ID";
        try {
            outputStream.writeObject(operation);

            String ticketId = ticketIdField.getText();

            outputStream.writeObject(ticketId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Deleted ticket with id " + ticketId + " successfully");
            } else {
                consoleTextArea.append("Couldn't delete ticket with id " + ticketId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketTCPIP());
    }
}
