package bb.love_letter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientController {
    static final int PORT = 6868;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        try(Socket socket = new Socket("localhost", PORT)) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String msg = reader.readLine();
            welcomeText.setText(msg);

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}