package bb.love_letter.networking;

import bb.love_letter.game.User;


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
    private ChatMessage chatMessage;
    private CommandType commandType;
    private User user;
    private ChatMessage privateMessage;


    public Command (ChatMessage chatMessage){
        this.chatMessage = chatMessage;
        this.commandType = interprete(chatMessage);
    }

    public CommandType getCommandType() {
        return commandType;
    }
    public User getUser() {
        return user;
    }
    public ChatMessage getPrivateMessage() {
        return privateMessage;
    }
    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    private CommandType interprete (ChatMessage chatMessage){
        String content = chatMessage.getMessage();
        if (content.equals("bye")){
            this.user = chatMessage.getSender();
            return CommandType.LOGOUT_COMMAND;
        }
        else if (content.charAt(0) == '@'){
            content =  content.substring(1);
            String [] parts = toString().split(" ");
            String target = parts [0];
            String message = parts [1];
            this.user = new User(target);
            privateMessage = new ChatMessage(chatMessage.getSender(),message);
            return CommandType.PRIVATE_MESSAGE_COMMAND;

        }
        else if (content.charAt(0) =='#') {
            return CommandType.GAME_COMMAND;
        }
        else {return CommandType.EMPTY_COMMAND;}

    }


}
