package bb.love_letter;


public class ChatController {
    public ChatModel model;


    public ChatController(ChatModel chatModel) {
        this.model= chatModel;
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.model.addChatMessage(chatMessage);
    }
    public void addUser(User user){
        this.model.addUser(user);
    }
    }




