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
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            // The request to login with the provided username should be sent here
            User me = new User(username);
            UserEvent login = new UserEvent(me, UserEvent.UserEventType.LOGIN_REQUEST);
            Envelope envelope = new Envelope(login, Envelope.TypeEnum.USEREVENT);
            outputStream.writeObject(envelope);
            //Gson gson = new Gson();
            // Wait for response
            Envelope response = (Envelope) inputStream.readObject();
            UserEvent loginResponse = (UserEvent) response.getPayload();
            if (loginResponse.getUserEventType() == UserEvent.UserEventType.LOGIN_CONFIRMATION) {
                model.setSuccessfulLogin(true);
                NetworkConnection.getInstance().init(socket, inputStream, outputStream, me);
            }

        } catch (UnknownHostException ex) {
            model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            model.setErrorMessage("An I/O exception occured! (Check ip and port)");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}