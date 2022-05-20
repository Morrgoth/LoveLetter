module bb.love_letter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    exports bb.love_letter.user_interface;
    exports bb.love_letter.game.characters;
    opens bb.love_letter.user_interface to javafx.fxml;
    exports bb.love_letter.game;
    opens bb.love_letter.game to javafx.fxml;
}