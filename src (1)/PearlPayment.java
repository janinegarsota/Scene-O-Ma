import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PearlPayment extends JPanel implements Printable {
    private JButton backButton;
    private JButton printButton;
    private BufferedImage backgroundImage;
    private BufferedImage logoImage;
    private String movieName;
    private double price;
    private double totalPrice;
    private String receiptNumber;
    private String seat;
    private String dateTime;
    private JLabel totalLabel;

    public PearlPayment(String movieName, double price, String selectedDate, String selectedTime, String seatDetails) {
        this.movieName = movieName;
        this.price = price;
        this.totalPrice = price;
        this.receiptNumber = generateReceiptNumber();
        this.dateTime = selectedDate + " " + selectedTime;
        this.seat = seatDetails;

        try {
            logoImage = ImageIO.read(new File("C:\\Users\\Administrator\\Downloads\\TPSPresentation\\logogo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Logo image file not found!");
        }

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        centerPanel.setOpaque(false);

        RoundedPanel receiptPanel = new RoundedPanel();
        receiptPanel.setPreferredSize(new Dimension(400, 580));
        receiptPanel.setLayout(null);
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int labelX = 20;
        int labelY = 150;
        int labelWidth = 360;
        int labelHeight = 25;

        if (logoImage != null) {
            Image scaledLogoImage = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogoImage));
            logoLabel.setBounds(280, 20, 100, 100);
            receiptPanel.add(logoLabel);
        }

        JLabel cinemaNameLabel = new JLabel("Scene O' Ma");
        cinemaNameLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        cinemaNameLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(cinemaNameLabel);

        labelY += 40;
        JLabel addressLabel = new JLabel("143 Islang Pantropiko, Tondo Manila");
        addressLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        addressLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(addressLabel);

        labelY += 25;
        JLabel phoneLabel = new JLabel("(801) 67-22");
        phoneLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        phoneLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(phoneLabel);

        labelY += 40;
        JLabel separator = new JLabel("----------------------------------------");
        separator.setFont(new Font("Monospaced", Font.PLAIN, 14));
        separator.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(separator);

        labelY += 25;
        JLabel dateTimeLabel = new JLabel("Date & Time: " + dateTime);
        dateTimeLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        dateTimeLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(dateTimeLabel);

        labelY += 25;
        JLabel movieNameLabel = new JLabel("Movie: " + movieName);
        movieNameLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        movieNameLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        receiptPanel.add(movieNameLabel);

        labelY += 25;
        JTextArea seatLabel = new JTextArea("Seats: " + seatDetails);
        seatLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        seatLabel.setForeground(Color.black);
        seatLabel.setLineWrap(true);
        seatLabel.setWrapStyleWord(true);
        seatLabel.setEditable(false);
        seatLabel.setBounds(labelX, labelY, 360, 250);
        seatLabel.setOpaque(false);
        receiptPanel.add(seatLabel);

        seatLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                totalLabel.setBounds(labelX, seatLabel.getY() + seatLabel.getHeight() + 25, labelWidth, labelHeight);
            }
        });

        totalLabel = new JLabel("Seat Price: PHP" + String.format("%.2f", totalPrice));
        totalLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        totalLabel.setBounds(190, labelY + 215, labelWidth, labelHeight);
        receiptPanel.add(totalLabel);

        centerPanel.add(receiptPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 10, 0);

        printButton = new JButton("Print Receipt");
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.setBackground(new Color(55, 107, 180));
        printButton.setForeground(new Color(230, 219, 198));
        printButton.setBorder(BorderFactory.createEmptyBorder(5, 17, 5, 17));
        printButton.setContentAreaFilled(true);
        printButton.setFocusPainted(false);
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPrintable(PearlPayment.this);
                boolean doPrint = printerJob.printDialog();
                if (doPrint) {
                    try {
                        printerJob.print();
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(null, "Printing failed: " + ex.getMessage());
                    }
                }
            }
        });

        printButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                printButton.setBackground(new Color(30, 72, 155));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                printButton.setBackground(new Color(55, 107, 180));
            }
        });

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(179, 19, 18));
        backButton.setForeground(new Color(230, 219, 198));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        backButton.setContentAreaFilled(true);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PearlPayment.this);
            frame.dispose();
            JFrame dashboardFrame = new JFrame("Dashboard");
            dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dashboardFrame.getContentPane().add(new Dashboard());
            dashboardFrame.setSize(1280, 720);
            dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            dashboardFrame.setVisible(true);
        });

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(139, 0, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(179, 19, 18));
            }
        });

        buttonPanel.add(printButton, gbc);
        buttonPanel.add(backButton, gbc);

        add(buttonPanel, BorderLayout.SOUTH);

        centerPanel.setPreferredSize(new Dimension(400, 800));
        setBackground(new Color(1, 22, 92));
    }

    private String generateReceiptNumber() {
        return "RCPT" + System.currentTimeMillis();
    }

    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.scale(0.75, 0.75);

        this.printAll(g2d);

        return PAGE_EXISTS;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pearl Payment");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PearlPayment paymentPanel = new PearlPayment("Pearl", 300.00, "2023-06-01", "10:00 AM", "A4");
            frame.getContentPane().add(paymentPanel);
            frame.getContentPane().setBackground(new Color(0x01165CFF));
            frame.setSize(1280, 720);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }
}
