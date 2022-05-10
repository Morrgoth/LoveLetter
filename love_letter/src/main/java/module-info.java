module bb.love_letter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    exports bb.love_letter.networking;
    opens bb.love_letter.networking to javafx.fxml;
    exports bb.love_letter.user_interface;
    opens bb.love_letter.user_interface to javafx.fxml;
    exports bb.love_letter.game;
    opens bb.love_letter.game to javafx.fxml;
}