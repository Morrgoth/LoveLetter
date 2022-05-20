package bb.love_letter.user_interface.model_view;

import bb.love_letter.user_interface.model.ChatModel;
import bb.love_letter.user_interface.view.ChatView;



public class ChatViewModel{

    private ChatModel model;
    private ChatView view;

    public ChatViewModel(ChatModel chatModel, ChatView chatView) {
        model = chatModel;
        view = chatView;
    }

}
