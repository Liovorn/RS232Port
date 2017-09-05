package com.hotmail.solntsev_igor;

import jssc.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static com.hotmail.solntsev_igor.DataBaseManager.addData;
import static com.hotmail.solntsev_igor.DataBaseManager.initDB;

/**
 * Created by solncevigor on 3/26/17.
 */
public class Buttons {

    private static SerialPort serialPort = new SerialPort("COM8") ;

    public static void frame(){
        ButtonFrame frame= new ButtonFrame();//frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//end of the program
        frame.setVisible(true);

}
static class ButtonFrame extends JFrame{
    public ButtonFrame(){
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);//frame size
        setTitle("Scale");
        ButtonPanel panel=new ButtonPanel();
        add(panel);

    }
    public static final  int DEFAULT_WIDTH=700;
    public static final  int DEFAULT_HEIGHT=250;
}
static class ButtonPanel extends JPanel{// Frame
    JTextField textField=new JTextField(20);
    JTextArea textArea=new JTextArea(10,50);
    public ButtonPanel(){ //constructor of panel add(textField);

        JButton RunButton = new JButton("Пуск"); //create buttons
        JButton StopButton = new JButton("Стоп");
        JButton SaveButton = new JButton("Сохранить");
        JButton CheckPorts = new JButton("Получить список доступных портов");

        add(RunButton); //adding button on a panel
        add(StopButton);
        add(SaveButton);
        add(CheckPorts);

        ButtonActionRun RunAction = new ButtonActionRun();
        ButtonActionStop StopAction = new ButtonActionStop();
        ButtonActionSave SaveAction = new ButtonActionSave();
        ButtonActionCheckPorts CheckAction = new ButtonActionCheckPorts();

        RunButton.addActionListener(RunAction); //set action for buttons
        StopButton.addActionListener(StopAction);
        SaveButton.addActionListener(SaveAction);
        CheckPorts.addActionListener(CheckAction);

        add(textArea);
        textArea.setLineWrap(true);

    }

    private class ButtonActionRun implements ActionListener{
        private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        private Date date = new Date();
        private String s = dateFormat.format(date);
        protected String portOpenFail;
        private String fromPort;
        @Override
        public void actionPerformed(ActionEvent event){ //Button reaction
            try {
                serialPort.openPort();
                serialPort.setParams(
                        SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(
                        SerialPort.FLOWCONTROL_RTSCTS_IN |
                        SerialPort.FLOWCONTROL_RTSCTS_OUT);

                serialPort.writeString("R"); // Data for scale(R - get data from device)
                byte [] buffer = serialPort.readBytes(8);

                System.out.println(buffer.toString());
                serialPort.addEventListener(new EventListener(), serialPort.MASK_RXCHAR);

                serialPort.closePort();
            } catch (SerialPortException ex) {
                portOpenFail = "Port"+ " " + serialPort.getPortName()+ " " + "not found" + " " + ex;
                System.out.println(portOpenFail);
            }
            try {
                textField.setText(fromPort = serialPort.readString());
                textField.setText(fromPort + s);
                textField.setText(fromPort);
                textArea.append(fromPort + s + "\n");
            } catch (SerialPortException e) {
                textField.setText("No Data from " + serialPort.getPortName());
                textArea.setText("No Data from " + serialPort.getPortName() + "\n");
                System.out.println(e);
            }
        }
    }

    private class ButtonActionStop implements ActionListener{
        private String actionStop;
        private String actionStopFail;

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                serialPort.closePort();
                actionStop = "Port Closed!" + serialPort.getPortName();
                textField.setText(actionStop);
                textArea.append(actionStop + "\n");
            } catch (SerialPortException e) {
                actionStopFail = "Port not closed" + " " + serialPort.getPortName();
               textField.setText(actionStopFail);
               textArea.append(actionStopFail + "\n");
                System.out.println("Port "+serialPort.getPortName()+e);
            }

        }

    }

    private class ButtonActionSave implements ActionListener{
        private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        private Date date = new Date();
        private String dateDB = dateFormat.format(date);
        private String actionSave;
        private String dataSavedToDB;
        private String dataNotSavedToDB;
        private String dataReceived;


        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                serialPort.addEventListener(new EventListener());
                dataReceived = serialPort.readString();

            } catch (SerialPortException e) {
                textField.setText("Check Serial Port! " + e);
                textArea.setText("Check Serial Port! " + e + "\n");
                System.out.println("Port " + serialPort.getPortName() + " " + e);
            }
            try {
                initDB();
                addData(dataReceived, dateDB);

                dataSavedToDB = "Data successfully saved to Data Base!";

                textField.setText(dataSavedToDB + " " + dataReceived + " " + " date:" + dateDB);
                textArea.setText(dataSavedToDB + " " + dataReceived + " " + " date:" + dateDB + "\n");
            } catch (SQLException e) {
                dataNotSavedToDB = "Fail to save data!";
                textField.setText(dataNotSavedToDB);
                textArea.setText(dataNotSavedToDB + "\n");
                System.out.println("SQL EXEPTION " + e);
            }

        }

    }

    private class ButtonActionCheckPorts implements ActionListener{

        private String actionCheck;
        private String portsList;
        private String portName = serialPort.getPortName();

        @Override
        public void actionPerformed(ActionEvent event) {
            String[] portNames = SerialPortList.getPortNames();

            if (portNames.length == 0) {
                actionCheck = "There are no serial-ports :(";
                textField.setText(actionCheck);
                textArea.setText(actionCheck + "\n");
                System.out.println("There are no serial-ports :(");
                return;
            }

            // Available ports
            actionCheck = "Available com-ports:";
            for (int i = 0; i < portNames.length; i++){
                portsList = portNames[i];
            }

            actionCheck = "Type port name, which you want to use, and press Enter...";
            textField.setText(actionCheck);
            textArea.setText(actionCheck + "\n");

            Scanner in = new Scanner(System.in);
            portName = in.next();

            textField.setText(in.next());
            textArea.append(in.next() + "\n");

            textField.setText(portsList);
            textArea.append(portsList);

        }

    }

    private class EventListener implements SerialPortEventListener {
        public byte [] receivedData;
        public String noDataFromPort;

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0){
                try {
                    receivedData = serialPort.readBytes(8);
                    textField.setText(receivedData.toString());
                    textArea.setText(receivedData.toString() + "\n");
                    System.out.println("Data from port"+ " " +serialPort.getPortName()+ " " +receivedData.toString());
                    serialPort.closePort();
                }
                catch (SerialPortException ex) {
                    noDataFromPort = "No data received";
                    textField.setText(noDataFromPort + " " + ex);
                    textArea.setText(noDataFromPort + " " + ex + "\n");
                }
            }
        }
    }
  }
}
