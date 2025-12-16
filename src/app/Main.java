package app;

import gui.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Tampilkan login form
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}