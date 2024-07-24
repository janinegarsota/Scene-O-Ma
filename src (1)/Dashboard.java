import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Dashboard extends JPanel implements ActionListener {
    private JButton book;
    private JButton logout;
    private JButton accessDatabase;
    private BufferedImage backgroundImage;
    private JPanel posterPanel;
    private JLabel[] posterLabels;
    private int currentIndex;
    private String[] posterPaths = new String[] {"C:\\Users\\Administrator\\Downloads\\TPSPresentation\\coraline (1).jpg",
            "C:\\Users\\Administrator\\Downloads\\TPSPresentation\\mean girls (1).jpg",
            "C:\\Users\\Administrator\\Downloads\\TPSPresentation\\pearl (1).jpg",
            "C:\\Users\\Administrator\\Downloads\\TPSPresentation\\dark.jpg",
            "C:\\Users\\Administrator\\Downloads\\TPSPresentation\\inside.jpg",
            "C:\\Users\\Administrator\\Downloads\\TPSPresentation\\weathering.jpg"};
    private static final int POSTER_WIDTH = 280;
    private static final int POSTER_HEIGHT = 380;
    private static final int POSTER_PADDING = 2;

    public Dashboard() {
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);

        BufferedImage titleImage;
        try {
            titleImage = ImageIO.read(new File("C:\\Users\\Administrator\\Downloads\\TPSPresentation\\Untitled design (1).png"));
        } catch (IOException e) {
            titleImage = null;
            e.printStackTrace();
        }

        JLabel titleLabel = titleImage != null ? new JLabel(new ImageIcon(titleImage)) : new JLabel("Title Image Not Found");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel);
        this.add(titlePanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        book = new JButton("Book A Movie");
        book.setFont(new Font("Arial", Font.BOLD, 18));
        book.setBackground(Color.decode("#376BB4"));
        book.setForeground(new Color (230, 219, 198));
        book.setFocusPainted(false);
        book.setMaximumSize(new Dimension(200, 70));
        book.setAlignmentX(CENTER_ALIGNMENT);
        book.addActionListener(this);

        logout = new JButton("Log-out");
        logout.setFont(new Font("Arial", Font.BOLD, 15));
        logout.setBackground(new Color(179, 19, 18));
        logout.setForeground(new Color (230, 219, 198));
        logout.setFocusPainted(false);
        logout.setMaximumSize(new Dimension(100, 50));
        logout.setAlignmentX(CENTER_ALIGNMENT);
        logout.addActionListener(this);

        accessDatabase = new JButton("Total Sales");
        accessDatabase.setFont(new Font("Arial", Font.BOLD, 18));
        accessDatabase.setBackground(Color.decode("#376BB4"));
        accessDatabase.setForeground(new Color (230, 219, 198));
        accessDatabase.setFocusPainted(false);
        accessDatabase.setMaximumSize(new Dimension(200, 70));
        accessDatabase.setAlignmentX(CENTER_ALIGNMENT);
        accessDatabase.addActionListener(this);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(this.book);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(this.accessDatabase);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(this.logout);
        buttonPanel.add(Box.createVerticalGlue());

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        posterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPosters(g);
            }
        };
        posterPanel.setLayout(new BorderLayout());
        posterPanel.setOpaque(false);
        mainPanel.add(posterPanel, BorderLayout.CENTER);

        JButton leftButton = new JButton("<");
        JButton rightButton = new JButton(">");

        leftButton.setFont(new Font("Arial", Font.BOLD, 21));
        rightButton.setFont(new Font("Arial", Font.BOLD, 21));

        leftButton.setForeground(new Color (230, 219, 198));
        rightButton.setForeground(new Color (230, 219, 198));

        leftButton.addActionListener(e -> rotateLeft());
        rightButton.addActionListener(e -> rotateRight());

        leftButton.setBackground(Color.decode("#01165C"));
        rightButton.setBackground(Color.decode("#01165C"));

        mainPanel.add(leftButton, BorderLayout.WEST);
        mainPanel.add(rightButton, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        loadPosters();
        currentIndex = 0;
        updatePosters();
    }

    private void loadPosters() {
        this.posterLabels = new JLabel[this.posterPaths.length];
        for (int i = 0; i < this.posterPaths.length; ++i) {
            try {
                BufferedImage img = ImageIO.read(new File(this.posterPaths[i]));
                if (img != null) {
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(POSTER_WIDTH, POSTER_HEIGHT, BufferedImage.SCALE_SMOOTH));
                    this.posterLabels[i] = new JLabel(icon);
                } else {
                    this.posterLabels[i] = new JLabel("Image Not Found");
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.posterLabels[i] = new JLabel("Image Not Found");
            }
        }
    }

    private void drawPosters(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = this.posterPanel.getWidth();
        int height = this.posterPanel.getHeight();
        int totalPosterWidth = 3 * POSTER_WIDTH + 2 * POSTER_PADDING;
        int x = (width - totalPosterWidth) / 2;
        int y = (height - POSTER_HEIGHT) / 2;

        int prevIndex = (this.currentIndex - 1 + this.posterLabels.length) % this.posterLabels.length;
        g2.drawImage(((ImageIcon) this.posterLabels[prevIndex].getIcon()).getImage(), x, y, POSTER_WIDTH, POSTER_HEIGHT, null);

        int nextIndex = (this.currentIndex + 1) % this.posterLabels.length;
        g2.drawImage(((ImageIcon) this.posterLabels[nextIndex].getIcon()).getImage(), x + 2 * (POSTER_WIDTH + POSTER_PADDING), y, POSTER_WIDTH, POSTER_HEIGHT, null);

        int centralX = x + POSTER_WIDTH + POSTER_PADDING;
        int centralY = y - 25;
        int centralWidth = POSTER_WIDTH + 50;
        int centralHeight = POSTER_HEIGHT + 50;

        int shadowX = centralX - 5;
        int shadowY = centralY - 5;
        int shadowWidth = centralWidth + 10;
        int shadowHeight = centralHeight + 10;
        g2.setColor(new Color(230, 219, 198));
        g2.fillRoundRect(shadowX, shadowY, shadowWidth, shadowHeight, 10, 10);

        g2.drawImage(((ImageIcon) this.posterLabels[this.currentIndex].getIcon()).getImage(), centralX, centralY, centralWidth, centralHeight, null);
    }


    private void updatePosters() {
        this.posterPanel.repaint();
    }

    private void rotateLeft() {
        this.currentIndex = (this.currentIndex - 1 + this.posterLabels.length) % this.posterLabels.length;
        this.updatePosters();
    }

    private void rotateRight() {
        this.currentIndex = (this.currentIndex + 1) % this.posterLabels.length;
        this.updatePosters();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.logout) {
            Window ancestor = SwingUtilities.getWindowAncestor(this);
            if (ancestor instanceof JFrame) {
                ((JFrame) ancestor).dispose();
            }

            JFrame loginFrame = new JFrame("Movie Ticket Booking System");
            loginFrame.setSize(1000, 850);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.add(new Login());
            loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loginFrame.setVisible(true);
        } else if (e.getSource() == this.book) {
            this.openBookingPanel();
        } else if (e.getSource() == this.accessDatabase) {
            this.openTotalSales();
        }
    }

    private void openBookingPanel() {
        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof JFrame) {
            ((JFrame) ancestor).dispose();
        }

        JFrame bookingFrame = new JFrame("Booking");
        bookingFrame.setSize(1000, 850);
        bookingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bookingFrame.add(new Booking());
        bookingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        bookingFrame.setVisible(true);
    }

    private void openTotalSales() {
        JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JFrame totalSalesFrame = new JFrame("Total Sales");
        totalSalesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        totalSalesFrame.getContentPane().add(new TotalSales(dashboardFrame));
        totalSalesFrame.setSize(800, 600);
        totalSalesFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        totalSalesFrame.setVisible(true);
        dashboardFrame.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, new Color(1, 22, 92), 0, h, new Color(1, 22, 92));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.add(new Dashboard());
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
