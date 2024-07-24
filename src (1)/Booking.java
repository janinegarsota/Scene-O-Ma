import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Booking extends JPanel implements ActionListener {

    private JButton mLeft, mRight, bookMovie, cancel, trailerButton;
    private JLabel movieTitle;
    private JLabel genre;
    private JLabel mtrcb;
    private JTextArea movieDesc;
    private List<MoviePanel> moviePanels;
    private int currentMovieIndex = 0;
    private CircleLabel movieNumberLabel;

    public Booking() {
        setLayout(null);

        moviePanels = new ArrayList<>();
        moviePanels.add(new CoralinePanel());
        moviePanels.add(new PearlPanel());
        moviePanels.add(new MeanGirlsPanel());

        for (int i = 0; i < moviePanels.size(); i++) {
            MoviePanel panel = moviePanels.get(i);
            panel.setBounds(0, 0, 640, 780);
            if (i != currentMovieIndex) {
                panel.setVisible(false);
            }
            add(panel);
        }

        movieNumberLabel = new CircleLabel();
        movieNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
        movieNumberLabel.setBackground(new Color(230, 219, 198));
        movieNumberLabel.setForeground(new Color(1, 22, 92));
        movieNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        movieNumberLabel.setBounds(700, 80, 40, 40);
        add(movieNumberLabel);

        mLeft = new JButton("<");
        mLeft.setFont(new Font("Arial", Font.BOLD, 16));
        mLeft.setBackground(new Color(55, 107, 180));
        mLeft.setForeground(new Color(230, 219, 198));
        mLeft.setFocusPainted(false);
        mLeft.setBounds(1200, 600, 50, 50);
        mLeft.addActionListener(this);
        add(mLeft);

        mRight = new JButton(">");
        mRight.setFont(new Font("Arial", Font.BOLD, 16));
        mRight.setBackground(new Color(55, 107, 180));
        mRight.setForeground(new Color(230, 219, 198));
        mRight.setFocusPainted(false);
        mRight.setBounds(1260, 600, 50, 50);
        mRight.addActionListener(this);
        add(mRight);

        trailerButton = new JButton("Watch Trailer");
        trailerButton.setFont(new Font("Arial", Font.BOLD, 16));
        trailerButton.setBackground(new Color(230, 219, 198));
        trailerButton.setForeground(new Color(230, 219, 198));
        trailerButton.setBorder(BorderFactory.createLineBorder(new Color(230, 219, 198)));
        trailerButton.setContentAreaFilled(false);
        trailerButton.setFocusPainted(false);
        trailerButton.setBounds(900, 450, 150, 50);
        trailerButton.addActionListener(this);

        trailerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                trailerButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                trailerButton.setForeground(Color.decode("#E6DBC6"));
            }
        });

        add(trailerButton);

        movieTitle = new JLabel();
        movieTitle.setFont(new Font("Arial", Font.BOLD, 50));
        movieTitle.setForeground(new Color(230, 219, 198));
        movieTitle.setBounds(700, 140, 500, 50);
        add(movieTitle);

        mtrcb = new JLabel();
        mtrcb.setFont(new Font("Arial", Font.BOLD, 18));
        mtrcb.setBackground(new Color(1,22,92));
        mtrcb.setForeground(new Color(230, 219, 198));
        mtrcb.setBounds(700, 200, 50, 30);
        add(mtrcb);

        genre = new JLabel();
        genre.setFont(new Font("Arial", Font.ITALIC, 18));
        genre.setForeground(new Color(230, 219, 198));
        genre.setBounds(750, 200, 500, 30);
        add(genre);


        movieDesc = new JTextArea();
        movieDesc.setFont(new Font("Arial", Font.PLAIN, 16));
        movieDesc.setForeground(Color.white);
        movieDesc.setLineWrap(true);
        movieDesc.setWrapStyleWord(true);
        movieDesc.setEditable(false);
        movieDesc.setBounds(700, 280, 450, 200);
        movieDesc.setOpaque(false);
        add(movieDesc);

        bookMovie = new JButton("Book a Movie");
        bookMovie.setFont(new Font("Arial", Font.BOLD, 16));
        bookMovie.setBackground(new Color(230, 219, 198));
        bookMovie.setForeground(new Color(230, 219, 198));
        bookMovie.setBorder(BorderFactory.createLineBorder(new Color(230, 219, 198)));
        bookMovie.setContentAreaFilled(false);
        bookMovie.setFocusPainted(false);
        bookMovie.setBounds(700, 450, 150, 50);
        bookMovie.addActionListener(this);


        bookMovie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bookMovie.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                bookMovie.setForeground(Color.decode("#E6DBC6"));
            }
        });

        add(bookMovie);

        cancel = new JButton("Cancel");
        cancel.setFont(new Font("Arial", Font.BOLD, 16));
        cancel.setBackground(new Color(55, 107, 180));
        cancel.setForeground(new Color(230, 219, 198));
        cancel.setFocusPainted(false);
        cancel.setBounds(700, 610, 110, 40);
        cancel.addActionListener(this);
        add(cancel);

        updateMovieDetails();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mRight) {
            currentMovieIndex = (currentMovieIndex + 1) % moviePanels.size();
            switchMoviePanel();
        } else if (e.getSource() == mLeft) {
            currentMovieIndex = (currentMovieIndex - 1 + moviePanels.size()) % moviePanels.size();
            switchMoviePanel();
        } else if (e.getSource() == bookMovie) {
            openSeats();
        } else if (e.getSource() == cancel) {
            cancelBooking();
        } else if (e.getSource() == trailerButton) {
            openTrailer();
        }
    }

    private void switchMoviePanel() {
        for (int i = 0; i < moviePanels.size(); i++) {
            moviePanels.get(i).setVisible(i == currentMovieIndex);
        }
        updateMovieDetails();
    }

    private void updateMovieDetails() {
        Movie currentMovie = moviePanels.get(currentMovieIndex).getMovie();
        movieTitle.setText(currentMovie.getTitle());
        genre.setText(getGenre(currentMovie.getTitle()));
        mtrcb.setText(getMtrcb(currentMovie.getTitle()));
        movieDesc.setText(currentMovie.getDescription());
        movieNumberLabel.setText(String.valueOf(currentMovieIndex + 1));
        bookMovie.setText("Book Movie " + (currentMovieIndex + 1));
    }

    private void openSeats() {
        JFrame seatsFrame = new JFrame("Seats");
        seatsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        seatsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        switch (currentMovieIndex) {
            case 0:
                seatsFrame.add(new CoralineSeats());
                break;
            case 1:
                seatsFrame.add(new PearlSeats());
                break;
            case 2:
                seatsFrame.add(new MeanGirlsSeats());
                break;
        }
        seatsFrame.setSize(1280, 800);
        seatsFrame.setVisible(true);

        Window ancestor = SwingUtilities.getWindowAncestor(this);
        if (ancestor instanceof JFrame) {
            ((JFrame) ancestor).dispose();
        }
    }

    private void openTrailer() {
        String trailerUrl = getTrailerUrl(currentMovieIndex);
        try {
            Desktop.getDesktop().browse(new java.net.URI(trailerUrl));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cancelBooking() {
        JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JFrame newDashboardFrame = new JFrame("Dashboard");
        newDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newDashboardFrame.add(new Dashboard());
        newDashboardFrame.setSize(1280, 800);
        newDashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        newDashboardFrame.setVisible(true);
        dashboardFrame.dispose();
    }

    private String getGenre(String title) {
        switch (title) {
            case "Coraline":
                return "|    Dark Fantasy/Animation";
            case "Pearl":
                return "|    Horror/Drama";
            case "Mean Girls":
                return "|    Teen/Comedy";
            default:
                return "";
        }
    }

    private String getMtrcb(String title) {
        switch (title) {
            case "Coraline":
                return "PG";
            case "Pearl":
                return "R-18";
            case "Mean Girls":
                return "PG";
            default:
                return "";
        }
    }

    private String getTrailerUrl(int index) {
        switch (index) {
            case 0:
                return "https://youtu.be/m9bOpeuvNwY?si=-1lS4iXtVa8eqhpJ";
            case 1:
                return "https://youtu.be/L5PW5r3pEOg?si=opVK-jnvhYhB3JUR";
            case 2:
                return "https://youtu.be/oDU84nmSDZY?si=eCZk9EEZ6wwPkYZb";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Booking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Booking());
        frame.setSize(1200, 800);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private class CircleLabel extends JLabel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(230, 219, 198));
            g2d.fillOval(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }

        @Override
        public void setText(String text) {
            super.setText(text);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
        }
    }
}