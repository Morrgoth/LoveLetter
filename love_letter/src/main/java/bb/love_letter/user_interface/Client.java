package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.client.ClientReaderThread;
import bb.love_letter.networking.client.ClientWriterThread;
import bb.love_letter.networking.client.NetworkConnection;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.model.ChatModel;
import bb.love_letter.user_interface.model.LoginModel;
import bb.love_letter.user_interface.model_view.ChatViewModel;
import bb.love_letter.user_interface.model_view.LoginViewModel;
import bb.love_letter.user_interface.view.ChatView;
import bb.love_letter.user_interface.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Bence Ament
 * @author Zeynab Baiani
 */
public class Client extends Application {

    private Scene currentScene;
    private LoginModel loginModel;

    private ChatModel chatModel;
    @Override
    public void start(Stage stage) throws IOException {
        openLoginWindow(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Handles the logging in of users and handles possible login errors
     * @param ip
     * @param port
     * @param username
     */
    public void login(String ip, int port, String username) {
        try {
            Socket client = new Socket(ip, port);
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            //request as a client
            User user = new User(username);
            LoginRequest loginRequest = new LoginRequest(user);
            dataOutputStream.writeUTF(loginRequest.toEnvelope().toJson());
            String response = dataInputStream.readUTF();
            Envelope envelope = Envelope.fromJson(response);
            if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                ServerEvent loginResponseEvent = (ServerEvent) envelope.getPayload();
                if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_CONFIRMATION) {
                    System.out.println("Successful Login!");
                    NetworkConnection.getInstance().init(client, dataInputStream, dataOutputStream, user);
                    loginModel.setLoginConfirmation(loginResponseEvent);
                    loginModel.setErrorMessage("");
                    openChatWindow(new Stage());
                    chatModel.addDisplayItem(loginModel.getLoginConfirmation());
                    ClientReaderThread readerThread = new ClientReaderThread(chatModel);
                    ClientWriterThread writerThread = new ClientWriterThread(chatModel);
                    readerThread.start();
                    writerThread.start();
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.NAME_ALREADY_TAKEN) {
                    loginModel.setErrorMessage("Error: The username " + user.getName() + " is already taken!");
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_ERROR) {
                    loginModel.setErrorMessage("Error: A login error occurred, please try again.");
                }
            } else {
                loginModel.setErrorMessage("Error: Could Not Connect To Server!");
            }
        } catch (UnknownHostException ex) {
            loginModel.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            loginModel.setErrorMessage("An I/O exception occurred! (Check ip and port)");
        }
    }

    /**
     * Handles logging out of users and prepares the app for a new Login
     */
    public void logout() {
        NetworkConnection.getInstance().reset();
        openLoginWindow(new Stage());
    }

    /**
     * Opens a new LoginView and sets up LoginModel and LoginViewModel
     * @param stage
     */
    private void openLoginWindow(Stage stage) {
        loginModel = new LoginModel();
        LoginView loginView = new LoginView();
        LoginViewModel loginViewModel=new LoginViewModel(this,loginModel,loginView);
        if (currentScene != null) {
            currentScene.getWindow().hide();
        }
        currentScene = new Scene(loginView.asParent(), 300, 350);
        currentScene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setTitle("Login");
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * Opens a new ChatView and sets up ChatModel and ChatViewModel
     * @param stage
     */
    private void openChatWindow(Stage stage) {
        chatModel = new ChatModel();
        ChatView chatView = new ChatView();
        ChatViewModel chatViewModel = new ChatViewModel(this, chatModel, chatView);
        stage.setTitle("Chat");
        currentScene.getWindow().hide();
        currentScene = new Scene(chatView.asParent(), 700, 500);
        currentScene.setFill(Color.TRANSPARENT);
        currentScene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setScene(currentScene);
        stage.show();
    }

}