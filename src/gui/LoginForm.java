package gui;

import model.*;
import javax.swing.*;

public class LoginForm extends JFrame {
    JTextField txtUser;
    JPasswordField txtPass;
    JComboBox<String> cbRole;

    public LoginForm() {
        setTitle("Login");
        setSize(300,250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        cbRole = new JComboBox<>(new String[]{"Petugas","Anggota"});
        JButton btnLogin = new JButton("Login");

        btnLogin.addActionListener(e -> prosesLogin());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Username"));
        add(txtUser);
        add(new JLabel("Password"));
        add(txtPass);
        add(new JLabel("Role"));
        add(cbRole);
        add(btnLogin);
    }

    private void prosesLogin() {
        String role = cbRole.getSelectedItem().toString();

        if (role.equals("Petugas")) {
            Petugas p = new Petugas("P01","Admin","admin","123");
            if (p.login(txtUser.getText(), new String(txtPass.getPassword()))) {
                new DashboardPetugas().setVisible(true);
                dispose();
            }
        } else {
            Anggota a = new Anggota("A01","User","anggota","123");
            if (a.login(txtUser.getText(), new String(txtPass.getPassword()))) {
                new DashboardAnggota().setVisible(true);
                dispose();
            }
        }
    }
}