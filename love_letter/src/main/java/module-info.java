module bb.love_letter {
    requires javafx.controls;
    requires javafx.fxml;


    opens bb.love_letter to javafx.fxml;
    exports bb.love_letter;
}