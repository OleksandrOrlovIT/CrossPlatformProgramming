package orlov641p.khai.edu.com.controller.tcpip;

import orlov641p.khai.edu.com.model.Flight;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightTCPIP extends JFrame {
    private JTextField flightIdField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField flightDateField;
    private JTextField numberOfSeatsField;
    private JTextArea consoleTextArea;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;
    private ObjectInputStream serverInputStream;

    private static final String flightRequest = "FLIGHT_REQUEST";

    public FlightTCPIP() {
        setTitle("Flight Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
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

        try {
            clientSocket = new Socket("localhost", 5555);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            serverInputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject(flightRequest);
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
            String flightId = flightIdField.getText();

            outputStream.writeObject(operation);
            outputStream.writeObject(flightId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            Flight foundFlight = (Flight) serverInputStream.readObject();

            consoleTextArea.append(foundFlight.toString());

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            String flightId = flightIdField.getText();
            String origin = originField.getText();
            String destination = destinationField.getText();
            LocalDateTime flightDate = LocalDateTime.parse(flightDateField.getText(), formatter);
            Integer numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());

            Flight flight = new Flight(flightId, origin, destination, flightDate, numberOfSeats);

            outputStream.writeObject(flight);
            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Added or updated flight : " + flight + " successfully");
            } else {
                consoleTextArea.append("Couldn't add or update this flight " + flight);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteById(){
        String operation = "DELETE_BY_ID";
        try {
            outputStream.writeObject(operation);

            String flightId = flightIdField.getText();

            outputStream.writeObject(flightId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Deleted flight with id " + flightId + " successfully");
            } else {
                consoleTextArea.append("Couldn't delete flight with id " + flightId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightTCPIP());
    }
}
