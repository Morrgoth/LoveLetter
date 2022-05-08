package bb.love_letter;

import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.google.gson.Gson;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

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
            // Request for Login is made here
            User me = new User(username);
            UserEvent login = new UserEvent(me, UserEvent.UserEventType.LOGIN_REQUEST);
            Envelope request = new Envelope(login, Envelope.TypeEnum.USEREVENT);
            Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
            String requestJson = gson.toJson(request);
            System.out.println(requestJson);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(requestJson + "\n");
            out.flush();
            // Wait for response of the Server
            System.out.println("sds");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String responseJson = in.readLine();
            Envelope response = Util.jsonToEnvelope(responseJson);
            UserEvent loginResponse = (UserEvent) response.getPayload();
            System.out.println(loginResponse.getUser().getName());
            if (loginResponse.getUserEventType() == UserEvent.UserEventType.LOGIN_CONFIRMATION) {
                model.setSuccessfulLogin(true);
                NetworkConnection.getInstance().init(socket, me);
            }

        } catch (UnknownHostException ex) {
            model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            model.setErrorMessage("An I/O exception occured! (Check ip and port)");
        }
    }
}