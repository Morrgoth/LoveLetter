package bb.love_letter.networking.server;

import bb.love_letter.game.GameAction;
import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;


/**
 *
 * @author Zeynab Baiani
 */
public class Command {




    public enum CommandType{
        LOGOUT_COMMAND,
        PRIVATE_MESSAGE_COMMAND,
        GAME_COMMAND,
        EMPTY_COMMAND,
    }
    public enum GameCommandType {
        CREATE,
        JOIN,
        START,
        DISCARD,
        HELP,
        SCORE,
        CARDS_INFO,
        HISTORY,
        ERROR,
    }
    private GameAction gameAction = null;
    private ChatMessage chatMessage;
    private CommandType commandType;
    private GameCommandType gameCommandType;
    private User user;

    private User targetUser;
    private ChatMessage privateMessage;


    public Command (ChatMessage chatMessage){
        this.chatMessage = chatMessage;
        this.commandType = interpret(chatMessage);
        this.user = chatMessage.getSender();
    }

    public CommandType getCommandType() {
        return commandType;
    }
    public User getUser() {
        return user;
    }

    public User getTargetUser() {
        return targetUser;
    }
    public ChatMessage getPrivateMessage() {
        return privateMessage;
    }
    public ChatMessage getChatMessage() {
        return chatMessage;
    }
    public GameCommandType getGameCommandType(){return gameCommandType;}

    private CommandType interpret(ChatMessage chatMessage){
        String content = chatMessage.getMessage();
        if (content.equals("bye")){
            return CommandType.LOGOUT_COMMAND;
        }
        else if (content.charAt(0) == '@'){
            content =  content.substring(1);
            String [] parts = content.split(" ", 2);
            String target = parts [0];
            String message = parts[1];
            this.targetUser = new User(target);
            privateMessage = new ChatMessage(chatMessage.getSender(), message, true);
            return CommandType.PRIVATE_MESSAGE_COMMAND;

        }
        else if (content.charAt(0) =='#') {
            content =  content.substring(1);
            String [] parts = content.split(" ");
            String command = parts[0];
            if (command.equals("create")){
                this.gameCommandType = GameCommandType.CREATE;
            }
            else if (command.equals("join")){
                this.gameCommandType = GameCommandType.JOIN;
            }
            else if (command.equals("start")){
                this.gameCommandType = GameCommandType.START;
            }
            else if (command.equals("discard")) {
                this.gameCommandType = GameCommandType.DISCARD;
                parseGameAction(parts);
            }
            else if (command.equals("help")) {
                this.gameCommandType = GameCommandType.HELP;
            }
            else if (command.equals("score")) {
                this.gameCommandType = GameCommandType.SCORE;
            }
            else if (command.equals("cards")) {
                this.gameCommandType = GameCommandType.CARDS_INFO;
            }
            else if (command.equals("history")){
                this.gameCommandType = GameCommandType.HISTORY;
            }
            return CommandType.GAME_COMMAND;
        }
        else {return CommandType.EMPTY_COMMAND;}

    }

    private void parseGameAction (String[] parts){
        if (checkCommandSyntax(parts)){

            if (parts.length == 2){
                this.gameAction = new GameAction(Integer.parseInt(parts[1]));
            }
            if (parts.length == 3){
                this.gameAction = new GameAction(Integer.parseInt(parts[1]), parts[2]);
            }
            if (parts.length == 4) {
                this.gameAction = new GameAction(Integer.parseInt(parts[1]), parts[2], parts[3]);
            }
        }
        else {this.gameCommandType = GameCommandType.ERROR;}

    }

    private  boolean checkCommandSyntax (String[] parts){
        if (parts[1].matches("\\d+")){
            return true;
        }
        return false;
    }


}
