package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.LoginRequest;
import bb.love_letter.networking.ServerEvent;
import bb.love_letter.networking.Envelope;
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
            ServerEvent serverEvent = new ServerEvent(user, ServerEvent.ServerEventType.LOGIN_REQUEST);
            Envelope request = new Envelope(serverEvent, Envelope.TypeEnum.USEREVENT);
            String jsonRequest = request.toJson();
            dataOutputStream.writeUTF(jsonRequest);
            String response = dataInputStream.readUTF();
            Envelope envelope = Envelope.deserializeEnvelopeFromJson(response);

            if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
                ServerEvent loginResponseEvent = (ServerEvent) envelope.getPayload();
                if (loginResponseEvent.getUserEventType() == ServerEvent.UserEventType.LOGIN_CONFIRMATION) {
                    NetworkConnection.getInstance().init(client, dataInputStream, dataOutputStream, user);
                    model.setSuccessfulLogin(true);
                } else if (loginResponseEvent.getUserEventType() == ServerEvent.UserEventType.LOGIN_ERROR) {
                    System.out.println("Error: The username " + user.getName() + " is already taken!");
                }
            } else {
                System.out.println("Error: Could Not Connect To Server!");
            }

        } catch (UnknownHostException ex) {
            model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            model.setErrorMessage("An I/O exception occured! (Check ip and port)");
        }
    }
}