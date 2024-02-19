package orlov641p.khai.edu.com.controller.lab3url;

import javax.swing.*;
import java.awt.*;

import static orlov641p.khai.edu.com.controller.lab3url.BaseClient.makeConnection;

public class TicketHTTP extends JFrame {
    private static String baseUrl = "http://localhost:8080/TICKET_REQUEST/";

    private JTextField ticketIdField;
    private JTextField clientIdField;
    private JTextField flightIdField;
    private JTextField orderIdField;
    private JTextField seatNumberField;
    private JTextArea consoleTextArea;


    public TicketHTTP() {
        setTitle("Ticket Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();

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


    private void findAll() {
        consoleTextArea.setText(makeConnection(baseUrl + "FIND_ALL"));
    }

    private void getById() {
        String ticketId = ticketIdField.getText();

        consoleTextArea.setText(makeConnection(baseUrl + "GET_BY_ID/" + ticketId));
    }

    private void deleteById() {
        String ticketId = ticketIdField.getText();

        consoleTextArea.setText(makeConnection(baseUrl + "DELETE_BY_ID/" + ticketId));
    }

    private String getAddOrUpdateRequest(String operation){
        String ticketId = ticketIdField.getText();
        String clientId = clientIdField.getText();
        String flightId = flightIdField.getText();
        String orderId = orderIdField.getText();
        String seatNumber = seatNumberField.getText();

        return operation + "/" + ticketId + "/" + clientId + "/" + flightId + "/" + orderId + "/" + seatNumber;
    }

    private void add() {
        consoleTextArea.setText(makeConnection(baseUrl + getAddOrUpdateRequest("ADD")));
    }

    private void update(){
        consoleTextArea.setText(makeConnection(baseUrl + getAddOrUpdateRequest("UPDATE")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicketHTTP::new);
    }
}
