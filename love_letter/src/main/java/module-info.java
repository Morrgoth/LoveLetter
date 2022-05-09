module bb.love_letter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens bb.love_letter to javafx.fxml;
    exports bb.love_letter;
}