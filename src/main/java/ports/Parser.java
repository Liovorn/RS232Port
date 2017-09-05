package ports;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by solncevigor on 3/21/17.
 */
public class Parser {

    private static String fileName = "";

    private static String reader = "";

    public static void dataParser() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(fileName));

        while((reader = bf.readLine()) != null){
            System.out.println(reader);

            String delims = "[ ? , . * / ; = ]+";
            String[] tokens = reader.split(delims);

            for (int i = 0; i < tokens.length; i++){
                System.out.println(tokens[i]);
            }
        }
        bf.close();
    }


}
