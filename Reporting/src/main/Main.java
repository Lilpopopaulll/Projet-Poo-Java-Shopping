package main;

import controller.MainController;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainController().startApplication();
        });
    }
}