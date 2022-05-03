package bb.love_letter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginController {
    static final int PORT = 6868;
    @FXML
    private Label welcomeText;
    public LoginModel model;

    public LoginController (LoginModel model) {
        this.model = model;
    }


    @FXML
    protected void onHelloButtonClick() {
        try(Socket socket = new Socket("localhost", PORT)) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String msg = inputStream.readUTF();
            welcomeText.setText(msg);

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}