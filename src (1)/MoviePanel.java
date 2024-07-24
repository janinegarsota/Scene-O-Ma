import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MoviePanel extends JPanel {
    private JLabel posterLabel;
    private Movie movie;
    private JLabel genreLabel;
    private JLabel mtrcbLabel;
    private JButton trailerButton;

    public MoviePanel(Movie movie, String genre, String mtrcb, String trailerUrl) {
        this.movie = movie;
        setLayout(null);
        setOpaque(false);

        posterLabel = new JLabel();
        posterLabel.setBounds(10, 10, 640, 780);
        add(posterLabel);

        genreLabel = new JLabel(genre);
        genreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        genreLabel.setForeground(new Color(230, 219, 198));
        genreLabel.setBounds(750, 200, 500, 30);
        add(genreLabel);

        mtrcbLabel = new JLabel(mtrcb);
        mtrcbLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        mtrcbLabel.setForeground(new Color(230, 219, 198));
        mtrcbLabel.setBackground(new Color(1, 22, 92));
        mtrcbLabel.setBounds(700, 200, 40, 30);
        add(mtrcbLabel);

        trailerButton = new JButton("Watch Trailer");
        trailerButton.setFont(new Font("Arial", Font.BOLD, 16));
        trailerButton.setBackground(new Color(85, 176, 255));
        trailerButton.setForeground(Color.WHITE);
        trailerButton.setFocusPainted(false);
        trailerButton.setBounds(700, 280, 200, 50);
        trailerButton.addActionListener(e -> openTrailer(trailerUrl));
        add(trailerButton);

        try {
            BufferedImage img = ImageIO.read(new File(movie.getImagePath()));
            Image resizedImage = img.getScaledInstance(posterLabel.getWidth(), posterLabel.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(resizedImage);
            posterLabel.setIcon(icon);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    public Movie getMovie() {
        return movie;
    }

    private void openTrailer(String trailerUrl) {
        try {
            Desktop.getDesktop().browse(new java.net.URI(trailerUrl));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}