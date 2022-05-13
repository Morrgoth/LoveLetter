package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.NetworkConnection;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Bence Ament
 */
public class LoginController {
    public LoginModel model;

    public LoginController (LoginModel model) {
        this.model = model;
    }

    protected void connectToServer(String ip, int port, String username) {
        model.setIp(ip);
        model.setPort(port);
        model.setUsername(username);

        try {
            Socket client = new Socket(ip, port);
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            //request as a client
            User user = new User(username);
            LoginRequest loginRequest = new LoginRequest(user);
            dataOutputStream.writeUTF(loginRequest.toEnvelope().toJson());
            String response = dataInputStream.readUTF();
            Envelope envelope = Envelope.deserializeEnvelopeFromJson(response);
            if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                ServerEvent loginResponseEvent = (ServerEvent) envelope.getPayload();
                if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_CONFIRMATION) {
                    NetworkConnection.getInstance().init(client, dataInputStream, dataOutputStream, user);
                    model.setSuccessfulLogin(true);
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.NAME_ALREADY_TAKEN) {
                    model.setErrorMessage("Error: The username " + user.getName() + " is already taken!");
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_ERROR) {
                    model.setErrorMessage("Error: A login error occurred, please try again.");
                }
            } else {
                model.setErrorMessage("Error: Could Not Connect To Server!");
            }
        } catch (UnknownHostException ex) {
            model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            model.setErrorMessage("An I/O exception occurred! (Check ip and port)");
        }
    }
}