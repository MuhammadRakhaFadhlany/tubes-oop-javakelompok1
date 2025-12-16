package gui;

import model.*;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRole;

    public LoginForm() {
        setTitle("Login Sistem Perpustakaan");
        setSize(420, 330);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /* ================= HEADER (GRADIENT) ================= */
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(52, 152, 219),
                        getWidth(), getHeight(), new Color(155, 89, 182));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(400, 70));
        header.setLayout(new GridBagLayout());

        JLabel lblTitle = new JLabel("SISTEM PERPUSTAKAAN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(lblTitle);

        add(header, BorderLayout.NORTH);

        /* ================= CARD FORM ================= */
        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        card.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Username");
        JLabel lblPass = new JLabel("Password");
        JLabel lblRole = new JLabel("Role");

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        cbRole = new JComboBox<>(new String[]{"Petugas", "Anggota"});

        Font f = new Font("Segoe UI", Font.PLAIN, 13);
        lblUser.setFont(f);
        lblPass.setFont(f);
        lblRole.setFont(f);
        txtUser.setFont(f);
        txtPass.setFont(f);
        cbRole.setFont(f);

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(lblUser, gbc);
        gbc.gridx = 1;
        card.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        card.add(lblPass, gbc);
        gbc.gridx = 1;
        card.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        card.add(lblRole, gbc);
        gbc.gridx = 1;
        card.add(cbRole, gbc);

        add(card, BorderLayout.CENTER);

        /* ================= FOOTER ================= */
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(41, 128, 185)); // biru solid
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(120, 35));
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);

        btnLogin.addActionListener(e -> prosesLogin());

        footer.add(btnLogin);
        add(footer, BorderLayout.SOUTH);
    }

    private void prosesLogin() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());
        String role = cbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password wajib diisi");
            return;
        }

        try {
            if (role.equals("Petugas")) {
                Petugas p = new Petugas("P01", "Admin", "admin", "123");
                if (p.login(username, password)) {
                    new DashboardPetugas().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login petugas gagal");
                }
            } else {
                Anggota a = new Anggota("A01", "User", "anggota", "123");
                if (a.login(username, password)) {
                    new DashboardAnggota(a).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login anggota gagal");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }
}
