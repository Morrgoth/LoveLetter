package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.client.ClientReaderThread;
import bb.love_letter.networking.client.ClientWriterThread;
import bb.love_letter.networking.client.NetworkConnection;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.controller.ChatController;
import bb.love_letter.user_interface.controller.LoginController;
import bb.love_letter.user_interface.model.ChatModel;
import bb.love_letter.user_interface.model.LoginModel;
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
    private LoginController loginController;
    private ChatController chatController;
    @Override
    public void start(Stage stage) throws IOException {
        openLoginWindow(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

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
                    loginController.model.setLoginConfirmation(loginResponseEvent);
                    loginController.model.setErrorMessage("");
                    openChatWindow(new Stage());
                    chatController.addDisplayItem(loginController.getModel().getLoginConfirmation());
                    ClientReaderThread readerThread = new ClientReaderThread(chatController);
                    ClientWriterThread writerThread = new ClientWriterThread(chatController);
                    readerThread.start();
                    writerThread.start();
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.NAME_ALREADY_TAKEN) {
                    loginController.model.setErrorMessage("Error: The username " + user.getName() + " is already taken!");
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_ERROR) {
                    loginController.model.setErrorMessage("Error: A login error occurred, please try again.");
                }
            } else {
                loginController.model.setErrorMessage("Error: Could Not Connect To Server!");
            }
        } catch (UnknownHostException ex) {
            loginController.model.setErrorMessage("Server was not found! (Check ip and port)");
        } catch (IOException ex) {
            loginController.model.setErrorMessage("An I/O exception occurred! (Check ip and port)");
        }
    }
    public void logout() {
        NetworkConnection.getInstance().reset();
        openLoginWindow(new Stage());
    }

    private void openLoginWindow(Stage stage) {
        LoginModel loginModel = new LoginModel();
        loginController = new LoginController(loginModel, this);
        LoginView loginView = new LoginView(loginModel, loginController);
        if (currentScene != null) {
            currentScene.getWindow().hide();

        }
        currentScene = new Scene(loginView.asParent(), 300, 350);
        currentScene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setTitle("Login");
        stage.setScene(currentScene);
        stage.show();
    }

    private void openChatWindow(Stage stage) {
        ChatModel chatModel = new ChatModel();
        chatController = new ChatController(chatModel, this);
        ChatView chatView = new ChatView(chatModel, chatController);
        stage.setTitle("Chat");
        currentScene.getWindow().hide();
        currentScene = new Scene(chatView.asParent(), 700, 500);
        currentScene.setFill(Color.TRANSPARENT);
        currentScene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setScene(currentScene);
        stage.show();
    }

}