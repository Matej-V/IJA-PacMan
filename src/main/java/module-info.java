module ija.project.pacman_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens ija.project.pacman_project to javafx.fxml;
    exports ija.project.pacman_project;
}