module ija.project.pacman_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires  javafx.base;
    requires  javafx.graphics;

    opens ija.project.pacman_project to javafx.fxml;

    exports ija.project.pacman_project;
}