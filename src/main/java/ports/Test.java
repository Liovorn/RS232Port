package ports;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by solncevigor on 3/15/17.
 */
public class Test {
    public static void main(String[] args) {
        Services services = new Services();

        final String host = "raukab.at.ua";
        int startPort = 0;
        int endPort = 30000;
        final int timeOut = 2000;  // 1.5 seconds

        for (int port = startPort; port <= endPort; port++) {
            Boolean isOpen = Boolean.TRUE;

            System.out.print("checking port " + port + " ... ");

            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), timeOut);
            } catch (Throwable cause) {
                isOpen = Boolean.FALSE;
            }

            if (isOpen) {
                String serviceName = (String) services.get(Integer.toString(port));

                if (serviceName != null) {
                    String[] service = serviceName.split("/");

                    System.out.println("is open (" + service[0] + ", " + service[1] + ")");
                } else {
                    System.out.println("is open (unknown)");
                }
            } else {
                System.out.println("is closed");
            }
        }
    }
}
