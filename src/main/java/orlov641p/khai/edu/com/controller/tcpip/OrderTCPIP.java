package orlov641p.khai.edu.com.controller.tcpip;

import orlov641p.khai.edu.com.model.Order;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderTCPIP extends JFrame {
    private JTextField orderIdField;
    private JTextField creditCardIdField;
    private JTextField deliveryAddressField;
    private JTextField orderDateField;
    private JTextField deliveryDateField;
    private JTextArea consoleTextArea;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;
    private ObjectInputStream serverInputStream;

    private static final String orderRequest = "ORDER_REQUEST";

    public OrderTCPIP() {
        setTitle("Order Application");
        setSize(2000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
    }

    private void initializeComponents() {
        JLabel orderIdLabel = new JLabel("Order ID:");
        JLabel creditCardIdLabel = new JLabel("Credit Card ID:");
        JLabel deliveryAddressLabel = new JLabel("Delivery Address:");
        JLabel orderDateLabel = new JLabel("Order Date (YYYY-MM-DD HH:mm):");
        JLabel deliveryDateLabel = new JLabel("Delivery Date (YYYY-MM-DD HH:mm):");

        orderIdField = new JTextField();
        creditCardIdField = new JTextField();
        deliveryAddressField = new JTextField();
        orderDateField = new JTextField();
        deliveryDateField = new JTextField();
        consoleTextArea = new JTextArea();

        orderIdField.setColumns(25);
        orderIdField.setFont(orderIdField.getFont().deriveFont(14f));

        creditCardIdField.setColumns(25);
        creditCardIdField.setFont(creditCardIdField.getFont().deriveFont(14f));

        deliveryAddressField.setColumns(25);
        deliveryAddressField.setFont(deliveryAddressField.getFont().deriveFont(14f));

        orderDateField.setColumns(25);
        orderDateField.setFont(orderDateField.getFont().deriveFont(14f));

        deliveryDateField.setColumns(25);
        deliveryDateField.setFont(deliveryDateField.getFont().deriveFont(14f));

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

        panel.add(orderIdLabel);
        panel.add(orderIdField);
        panel.add(creditCardIdLabel);
        panel.add(creditCardIdField);
        panel.add(deliveryAddressLabel);
        panel.add(deliveryAddressField);
        panel.add(orderDateLabel);
        panel.add(orderDateField);
        panel.add(deliveryDateLabel);
        panel.add(deliveryDateField);
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
            String orderId = orderIdField.getText();

            outputStream.writeObject(operation);
            outputStream.writeObject(orderId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            Order foundOrder = (Order) serverInputStream.readObject();

            consoleTextArea.append(foundOrder.toString());

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

            String orderId = orderIdField.getText();
            String creditCardId = creditCardIdField.getText();
            String deliveryAddress = deliveryAddressField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime orderDate = LocalDateTime.parse(orderDateField.getText(), formatter);
            LocalDateTime deliveryDate = LocalDateTime.parse(deliveryDateField.getText(), formatter);

            Order order = new Order(orderId, creditCardId, deliveryAddress, orderDate, deliveryDate);

            outputStream.writeObject(order);
            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Added or updated order : " + order + " successfully");
            } else {
                consoleTextArea.append("Couldn't add or update this order " + order);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteById(){
        String operation = "DELETE_BY_ID";
        try {
            outputStream.writeObject(operation);

            String orderId = orderIdField.getText();

            outputStream.writeObject(orderId);

            consoleTextArea.setText(serverInputStream.readObject().toString());

            if(serverInputStream.readObject().toString().equals("true")){
                consoleTextArea.append("Deleted order with id " + orderId + " successfully");
            } else {
                consoleTextArea.append("Couldn't delete order with id " + orderId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderTCPIP());
    }
}