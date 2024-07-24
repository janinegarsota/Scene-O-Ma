import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Login extends JPanel implements ActionListener {
    private JPasswordField passwordText;
    private JButton login;
    private JLabel success;
    private ShadowedLabel cinemaText;
    private JLabel subtitleText;

    private RoundedPanel borderedPanel;
    private JLabel paragraph;
    private JLabel paragraph2;
    private JLabel logoLabel;
    private BufferedImage backgroundImage;
    private JCheckBox showPassword;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Movie Ticket Booking System");
        frame.setSize(1000, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Login());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public Login() {
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Administrator\\Downloads\\TPSPresentation\\blue.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(null);

        try {
            BufferedImage logoImage = ImageIO.read(new File("C:\\Users\\Administrator\\Downloads\\TPSPresentation\\logo.png"));
            Image scaledImage = logoImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon logoIcon = new ImageIcon(scaledImage);
            logoLabel = new JLabel(logoIcon);
            logoLabel.setBounds(240, 180, 300, 300);
            add(logoLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        paragraph = new JLabel("Welcome to our cinematic universe! Book your favorite movies");
        paragraph.setBounds(120, 480, 1000, 100);
        paragraph.setFont(new Font("Tahoma", Font.ITALIC, 18));
        paragraph.setForeground(new Color(255, 255, 255));

        paragraph2 = new JLabel("effortlessly with our easy-to-use platform!");
        paragraph2.setBounds(210, 510, 1000, 100);
        paragraph2.setFont(new Font("Tahoma", Font.ITALIC, 18));
        paragraph2.setForeground(new Color(255, 255, 255));

        borderedPanel = new RoundedPanel(30, new Color(229, 225, 225, 128));
        borderedPanel.setLayout(null);
        borderedPanel.setBounds(850, 160, 500, 480);

        cinemaText = new ShadowedLabel("Admin Log-in");
        cinemaText.setFont(new Font("Arial", Font.BOLD, 40));
        cinemaText.setForeground(new Color(255, 255, 255));
        cinemaText.setShadowColor(Color.BLACK);
        cinemaText.setShadowOffset(2);
        cinemaText.setBounds(120, 130, 300, 50);
        borderedPanel.add(cinemaText);

        subtitleText = new JLabel("Browse the dashboard and start booking seats now");
        subtitleText.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleText.setForeground(new Color(255, 255, 255));
        subtitleText.setBounds(70, 180, 400, 30);
        borderedPanel.add(subtitleText);

        passwordText = new RoundedPasswordField(15);
        passwordText.setFont(new Font("Arial", Font.BOLD, 18));
        passwordText.setBackground(Color.WHITE);
        passwordText.setForeground(Color.BLACK);
        passwordText.setBounds(50, 220, 400, 35);
        passwordText.setToolTipText("Enter password here");
        borderedPanel.add(passwordText);

        showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        showPassword.setForeground(Color.WHITE);
        showPassword.setOpaque(false);
        showPassword.setBounds(180, 270, 150, 30);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordText.setEchoChar((char) 0);
            } else {
                passwordText.setEchoChar('*');
            }
        });
        borderedPanel.add(showPassword);

        login = new JButton("Log-in");
        login.setFont(new Font("Arial", Font.BOLD, 18));
        login.setBackground(new Color(54, 84, 255));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.setBounds(120, 350, 250, 40);
        login.addActionListener(this);

        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(31, 62, 211));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(62, 89, 255));
            }
        });

        borderedPanel.add(login);

        success = new JLabel("");
        success.setFont(new Font("Arial", Font.PLAIN, 18));
        success.setForeground(Color.RED);
        success.setBounds(170, 310, 300, 30);
        borderedPanel.add(success);

        add(borderedPanel);
        add(paragraph);
        add(paragraph2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient = new GradientPaint(0, 0, new Color(54, 84, 255), width, height, new Color(54, 84, 255).darker());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, width, height, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String password = new String(passwordText.getPassword());

        if (password.equals("AdminCS")) {
            success.setText("Login Successful!");
            success.setFont(new Font("Arial", Font.BOLD, 18));
            success.setForeground(new Color(69, 203, 24));

            openDashboard();
        } else {
            success.setText("Incorrect Credentials!");
            success.setFont(new Font("Arial", Font.BOLD, 16));
            success.setForeground(Color.RED);
        }
    }

    private void openDashboard() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof JFrame) {
            ((JFrame) ancestor).dispose();
        }

        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setSize(1200, 800);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null);
        Dashboard dashboard = new Dashboard();
        dashboardFrame.add(dashboard);
        dashboardFrame.setVisible(true);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


    class ShadowedLabel extends JLabel {
        private Color shadowColor = Color.BLACK;
        private int shadowOffset = 2;

        public ShadowedLabel(String text) {
            super(text);
        }

        public void setShadowColor(Color shadowColor) {
            this.shadowColor = shadowColor;
        }

        public void setShadowOffset(int shadowOffset) {
            this.shadowOffset = shadowOffset;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(shadowColor);
            g2d.drawString(getText(), getInsets().left + shadowOffset, getInsets().top + g2d.getFontMetrics().getAscent() + shadowOffset);

            g2d.setColor(getForeground());
            g2d.drawString(getText(), getInsets().left, getInsets().top + g2d.getFontMetrics().getAscent());
            g2d.dispose();
        }
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int cornerRadius, Color backgroundColor) {
            this.cornerRadius = cornerRadius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (backgroundColor != null) {
                graphics.setColor(backgroundColor);
            } else {
                graphics.setColor(getBackground());
            }
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
            graphics.setColor(getForeground());
            graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        }
    }

    class RoundedTextField extends JTextField {
        private int cornerRadius;

        public RoundedTextField(int cornerRadius) {
            super();
            this.cornerRadius = cornerRadius;
            setOpaque(false);
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    setBackground(new Color(235, 235, 235));
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    setBackground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2d.setColor(getForeground());
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
    }

    class RoundedPasswordField extends JPasswordField {
        private int cornerRadius;

        public RoundedPasswordField(int cornerRadius) {
            super();
            this.cornerRadius = cornerRadius;
            setOpaque(false);
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    setBackground(new Color(235, 235, 235));
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    setBackground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2d.setColor(getForeground());
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            g2d.dispose();
            super.paintComponent(g);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
    }
}

