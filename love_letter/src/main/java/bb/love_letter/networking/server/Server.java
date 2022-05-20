package bb.love_letter.networking.server;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.User;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * This is the primary Server Thread, it handles the logging in of new Users and upon successful registers the user.
 *
 * @author Bence Ament
 * @author Zeynab Baiani
 */
public class Server {
    private final int PORT = 6868;
    public ClientList clientList = new ClientList();
    private Game game = new Game();
    public static void main(String[] args){
        Server server = new Server();
        server.registerUsers();
    }

    /**
     * Waits for and handles the Login Requests of Users.
     */
    public void registerUsers() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Server started running on " + inetAddress.getHostAddress() + ":" + PORT);
            while(true) {
                Socket clientSocket = server.accept();
                if(clientSocket != null) {
                    DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                    String json = dataInputStream.readUTF();
                    Envelope envelope = Envelope.fromJson(json);
                    if (envelope.getType()== Envelope.EnvelopeType.LOGIN_REQUEST) {
                        LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
                        User user = loginRequest.getUser();
                        handleLoginRequest(user, clientSocket);
                    } else {
                        System.out.println("Received Invalid LoginRequest!");
                        clientSocket.close();
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("ServerError: " +  e.getMessage());
        }
    }

    /**
     * Processes valid LoginRequests, upon successful login starts a new ServerThread for the User and sends
     * out notifications. Upon unsuccessful login attempt it sends out an error message.
     * @param user
     * @param socket
     * @throws IOException
     */
    private void handleLoginRequest(User user, Socket socket) throws IOException {
        if (!clientList.containsClient(user)) {
            clientList.addClient(user, socket);
            ServerEvent loginConfirmation = new ServerEvent("Welcome " + user.getName() + "!",
                    ServerEvent.ServerEventType.LOGIN_CONFIRMATION);
            ServerEvent newUserNotification = new ServerEvent(user.getName() + " joined the room!",
                    ServerEvent.ServerEventType.NEW_PLAYER_NOTIFICATION);
            broadcast(loginConfirmation.toEnvelope(), new User[] {user}, null);
            broadcast(newUserNotification.toEnvelope(), null, new User[] {user});
            ServerThread messageRouterThread = new ServerThread(this, socket);
            messageRouterThread.start();
        } else {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ServerEvent loginError = new ServerEvent("Username is already in use. Please " +
                    "choose another username.",
                    ServerEvent.ServerEventType.NAME_ALREADY_TAKEN);
            dataOutputStream.writeUTF(loginError.toEnvelope().toJson()); // LOGIN_ERROR
            socket.close();
        }
    }

    /**
     * Executes the parsed Commands sent by Users, normal ChatMessages are executed as EMPTY_COMMANDS
     * @param command
     * @throws IOException
     */
    public synchronized void execute(Command command) throws IOException {
        if (command.getCommandType()== Command.CommandType.LOGOUT_COMMAND){
            ServerEvent logOutConfirmation = new ServerEvent("You have successfully logged out.",
                    ServerEvent.ServerEventType.LOGOUT_CONFIRMATION);
            ServerEvent userLeftNotification = new ServerEvent(command.getUser().getName() +
                    " left the room.", ServerEvent.ServerEventType.PLAYER_LEFT_NOTIFICATION);
            broadcast(logOutConfirmation.toEnvelope(), new User[] {command.getUser()}, null);
            broadcast(userLeftNotification.toEnvelope(), null, new User[] {command.getUser()});
            this.clientList.removeClient(command.getUser());
        } else if (command.getCommandType()== Command.CommandType.PRIVATE_MESSAGE_COMMAND) {
            Envelope privateMessageEnvelope = new Envelope(command.getPrivateMessage(),
                    Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(privateMessageEnvelope, new User[] {command.getUser(), command.getTargetUser()}, null);
        }  else if (command.getCommandType()== Command.CommandType.EMPTY_COMMAND) {
            Envelope messageEnvelope = new Envelope(command.getChatMessage(), Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(messageEnvelope, null, null);
        } else if (command.getCommandType()== Command.CommandType.GAME_COMMAND) {
            if (command.getGameCommandType()== Command.GameCommandType.HELP){
                GameEvent gameEvent = game.infoPost("#help");
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                Envelope envelope = serverEvent.toEnvelope();
                broadcast(envelope, null,null);
            }
        }
    }

    /**
     * This method can be used to broadcast messages to subsets of all users.
     * @param envelope The message to be broadcast
     * @param whitelist The list of users who must receive the message
     * @param blacklist the list of users who mustn't recieve the message
     * @throws IOException
     */
    private void broadcast(Envelope envelope, User[] whitelist, User[] blacklist) throws IOException {
        if (whitelist != null) {
            for (User recipient: clientList.getUsers()) {
                if (Arrays.asList(whitelist).contains(recipient)){
                    DataOutputStream dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                    dataOutputStream.writeUTF(envelope.toJson());
                }
            }
        } else if (blacklist != null) {
            for (User recipient: clientList.getUsers()) {
                if (!Arrays.asList(blacklist).contains(recipient)) {
                    DataOutputStream dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                    dataOutputStream.writeUTF(envelope.toJson());
                }
            }
        }
        else {
            for (User recipient: clientList.getUsers()) {
                DataOutputStream dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                dataOutputStream.writeUTF(envelope.toJson());
            }
        }
    }
}
