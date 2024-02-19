#!/bin/bash

# Your path to the main dir */main
CP=

cd $CP

# Compile the Java classes
javac -d resources java/orlov641p/khai/edu/com/controller/lab4rmi/*.java java/orlov641p/khai/edu/com/model/*.java java/orlov641p/khai/edu/com/service/*.java

# Start the RMI registry in the background
rmiregistry &
RMIREG_PID=$!

# Wait for a moment to allow the registry to start
sleep 2

# Run the ServerRMI class in a new terminal
gnome-terminal -- java -classpath $CP/resources orlov641p.khai.edu.com.controller.lab4rmi.ServerRMI

# Run the ClientRMI class in a new terminal
gnome-terminal -- java -classpath $CP/resources orlov641p.khai.edu.com.controller.lab4rmi.ClientRMI

# Run the OrderRMI class in a new terminal
gnome-terminal -- java -classpath $CP/resources orlov641p.khai.edu.com.controller.lab4rmi.OrderRMI

gnome-terminal -- java -classpath $CP/resources orlov641p.khai.edu.com.controller.lab4rmi.FlightRMI

gnome-terminal -- java -classpath $CP/resources orlov641p.khai.edu.com.controller.lab4rmi.TicketRMI

read -p "Press Enter to stop the RMI registry..."

# Stop the rmiregistry process
kill $RMIREG_PID