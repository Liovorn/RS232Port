package com.hotmail.solntsev_igor;

import jssc.SerialPortList;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by solncevigor on 3/31/17.
 */
public class AvaiblePorts {

    public void portsRunner(){

        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :(");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Available ports
        System.out.println("Available com-ports:");
        for (int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }
        System.out.println("Type port name, which you want to use, and press Enter...");
        Scanner in = new Scanner(System.in);
        String portName = in.next();

    }
}
