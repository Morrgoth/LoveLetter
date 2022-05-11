package bb.love_letter.networking;

import bb.love_letter.game.User;


/**
 *
 * @author Zeynab
 */
public class Command {
    public enum CommandType{
        LOGOUT_COMMAND,
        PRIVATE_MESSAHECOMMAND,
        GAME_COMMAND,
        EMPTY_COMMAND,
    }
    private ChatMessage chatMessage;
    private CommandType commandType;
    private User userTarget;
    private ChatMessage privateMessage;

    public Command (ChatMessage chatMessage){
        this.chatMessage = chatMessage;
        this.commandType = interprete(chatMessage);
    }

    private CommandType interprete (ChatMessage chatMessage){
        String content = chatMessage.getMessage();
        if (content.equals("bye")){
            return CommandType.LOGOUT_COMMAND;
        }
        else if (content.charAt(0) == '@'){
            content =  content.substring(1);
            String [] parts = toString().split(" ");
            String target = parts [0];
            String message = parts [1];
            this.userTarget = new User(target);
            privateMessage = new ChatMessage(chatMessage.getSender(),message);
            return CommandType.PRIVATE_MESSAHECOMMAND;

        }
        else if (content.charAt(0) =='#') {
            return CommandType.GAME_COMMAND;
        }
        else {return CommandType.EMPTY_COMMAND;}

    }


}
