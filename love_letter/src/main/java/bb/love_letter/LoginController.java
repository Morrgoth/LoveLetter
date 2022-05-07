package bb.love_letter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginController {
    public LoginModel model;

    public LoginController (LoginModel model) {
        this.model = model;
    }

    protected void connectToServer(String ip, int port, String username) {
        model.setIp(ip);
        model.setPort(port);
        model.setUsername(username);

        try(Socket socket = new Socket(model.getIp(), model.getPort())) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            // The request to login with the provided username should be sent here
            System.out.println("Connection");
            String msg = inputStream.readUTF();
            if (msg.equals("Hi there, I am the server! beep boop")) {
                model.setSuccessfulLogin(true);
            }
            model.setErrorMessage(msg);

        } catch (UnknownHostException ex) {
            model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            model.setErrorMessage("An I/O exception occured! (Check ip and port)");
        }
    }
}