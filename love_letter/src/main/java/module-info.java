module bb.love_letter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    exports bb.love_letter.user_interface;
    opens bb.love_letter.user_interface to javafx.fxml;
    exports bb.love_letter.game;
    opens bb.love_letter.game to javafx.fxml;
    exports bb.love_letter.networking.type_adapters;
    opens bb.love_letter.networking.type_adapters to javafx.fxml;
    exports bb.love_letter.networking.data;
    opens bb.love_letter.networking.data to javafx.fxml;
    exports bb.love_letter.user_interface.controller;
    opens bb.love_letter.user_interface.controller to javafx.fxml;
    exports bb.love_letter.user_interface.model;
    opens bb.love_letter.user_interface.model to javafx.fxml;
    exports bb.love_letter.user_interface.view;
    opens bb.love_letter.user_interface.view to javafx.fxml;
    exports bb.love_letter.user_interface.test;
    opens bb.love_letter.user_interface.test to javafx.fxml;
    exports bb.love_letter.networking.server;
    opens bb.love_letter.networking.server to javafx.fxml;
    exports bb.love_letter.networking.client;
    opens bb.love_letter.networking.client to javafx.fxml;
}