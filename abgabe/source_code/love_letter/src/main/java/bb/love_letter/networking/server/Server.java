package bb.love_letter.networking.server;

import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.User;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
            ServerEvent loginConfirmation = new ServerEvent("Welcome " + user.getName() + "! You can send " +
                    "private messages with @username, and find more information about the Game by using #help!",
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
            GameEvent logoutEvent = game.removeLoggedOutUser(command.getUser());
            if(logoutEvent != null) {
                ServerEvent serverEvent = new ServerEvent(logoutEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(logoutEvent.getTarget()),null);
            }
        } else if (command.getCommandType()== Command.CommandType.PRIVATE_MESSAGE_COMMAND) {
            Envelope privateMessageEnvelope = new Envelope(command.getPrivateMessage(),
                    Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(privateMessageEnvelope, new User[] {command.getUser(), command.getTargetUser()}, null);
        }  else if (command.getCommandType()== Command.CommandType.EMPTY_COMMAND) {
            Envelope messageEnvelope = new Envelope(command.getChatMessage(), Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(messageEnvelope, null, null);
        } else if (command.getCommandType()== Command.CommandType.GAME_COMMAND) {
            if (command.getGameCommandType()== Command.GameCommandType.HELP){
                GameEvent gameEvent = game.getHelp(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.SCORE){
                GameEvent gameEvent = game.getScore(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.CARDS_INFO){
                GameEvent gameEvent = game.getCards(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.HAND){
                GameEvent gameEvent = game.getHand(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.HISTORY) {
                GameEvent gameEvent = game.getHistory(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.CREATE) {
                GameEvent gameEvent = game.init();
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.JOIN) {
                GameEvent gameEvent= game.addPlayer(command.getUser());
                ServerEvent serverEvent = new ServerEvent(gameEvent);
                broadcast(serverEvent.toEnvelope(), asUserArray(gameEvent.getTarget()),null);
            } else if (command.getGameCommandType()== Command.GameCommandType.START) {
                ArrayList<GameEvent> startGameEvents = game.startGame();
                for (GameEvent startGameEvent: startGameEvents) {
                    broadcast(new ServerEvent(startGameEvent).toEnvelope(), asUserArray(startGameEvent.getTarget()),null);
                }
            } else if (command.getGameCommandType()== Command.GameCommandType.DISCARD) {
                ArrayList<GameEvent> gameEvents = game.playCard(command.getUser(), command.getGameAction());
                ArrayList<GameEvent> extraEvents = new ArrayList<>();
                for (GameEvent gameEvent: gameEvents) {
                    broadcast(new ServerEvent(gameEvent).toEnvelope(), asUserArray(gameEvent.getTarget()),null);
                    if (gameEvent.getGameEventType() == GameEvent.GameEventType.VALID_ACTION) {
                        GameEvent finishTurn = game.finishTurn();
                        extraEvents.add(finishTurn);
                        if (finishTurn.getGameEventType() == GameEvent.GameEventType.TURN_ENDED) {
                            ArrayList<GameEvent> gameEvents1 = game.startTurn();
                            extraEvents.addAll(gameEvents1);
                        } else if (finishTurn.getGameEventType() == GameEvent.GameEventType.ROUND_ENDED) {
                            ArrayList<GameEvent> gameEvents1 = game.startRound();
                            extraEvents.addAll(gameEvents1);
                        } else if (finishTurn.getGameEventType() == GameEvent.GameEventType.GAME_ENDED) {
                            // GAME OVER
                        }
                    }
                }
                for (GameEvent gameEvent: extraEvents) {
                    broadcast(new ServerEvent(gameEvent).toEnvelope(), asUserArray(gameEvent.getTarget()),null);
                }
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
            for (User recipient: whitelist) {
                if (clientList.getClientSocket(recipient) != null ){
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

    private User[] asUserArray(User user) {
        if (user != null) {
            return new User[]{user};
        } else {
            return null;
        }
    }
}
