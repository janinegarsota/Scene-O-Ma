
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;


public class CoralineSeats extends JPanel {
    private static final int ROWS = 10;
    private static final int COLS = 14;
    private JButton[][] seats = new JButton[ROWS][COLS];
    private JButton proceedToPaymentButton;
    private JButton resetButton;
    private JButton cancelButton;
    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private Set<String> selectedSeats = new HashSet<>();
    private Connection connection;


    public CoralineSeats() {
        setBackground(new Color(1, 22, 92));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 3, 2);


        initializeDatabaseConnection();



        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = COLS + 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel screenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(85, 176, 255));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                String text = "SCREEN";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
            }
        };
        screenPanel.setPreferredSize(new Dimension((COLS + 2) * 70, 30));
        add(screenPanel, gbc);
        gbc.gridwidth = 1;



        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel availableLabel = new JLabel("Available");
        availableLabel.setForeground(Color.WHITE);
        availableLabel.setIcon(createColorIcon(new Color(85, 176, 255)));
        JLabel unavailableLabel = new JLabel("Unavailable");
        unavailableLabel.setForeground(Color.WHITE);
        unavailableLabel.setIcon(createColorIcon(new Color(179, 19, 18)));
        labelPanel.add(availableLabel);
        labelPanel.add(unavailableLabel);
        add(labelPanel, gbc);


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (col == 3 || col == 10) {
                    gbc.gridx = col + 1;
                    gbc.gridy = row + 2;
                    JLabel aisle = new JLabel(" ");
                    add(aisle, gbc);
                    continue;
                }
                String seatLabel = (char) ('A' + row) + String.valueOf(col + 1);
                if (isSeatDeleted(seatLabel)) {
                    seats[row][col] = null;
                    continue;
                }
                seats[row][col] = new JButton(seatLabel);
                seats[row][col].setPreferredSize(new Dimension(60, 40));
                seats[row][col].setFont(new Font("Arial", Font.BOLD, 12));
                seats[row][col].setBackground(new Color(85, 176, 255));
                seats[row][col].setForeground(new Color(4, 4, 4, 154));
                seats[row][col].setText(seatLabel);
                seats[row][col].addActionListener(new SeatButtonListener(row, col));
                gbc.gridx = col + 1;
                gbc.gridy = row + 2;
                add(seats[row][col], gbc);
            }
        }

        gbc.gridx = + 6;
        gbc.gridy = ROWS + 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        dateComboBox = new JComboBox<>(new String[]{"Select a Date", "2023-06-01", "2023-06-02", "2023-06-03"});
        dateComboBox.addActionListener(e -> updateSeatAvailability());
        dateComboBox.setBackground(Color.lightGray);
        add(dateComboBox, gbc);


        gbc.gridy = ROWS + 3;
        gbc.gridx = + 8;
        timeComboBox = new JComboBox<>(new String[]{"Select Time", "10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});
        timeComboBox.addActionListener(e -> updateSeatAvailability());
        timeComboBox.setBackground(Color.lightGray);
        add(timeComboBox, gbc);


        gbc.gridy = ROWS + 3;
        gbc.gridx = 0;
        gbc.gridwidth = COLS + 2;
        add(Box.createVerticalStrut(20), gbc);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setOpaque(false);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(179, 19, 18));
        cancelButton.setForeground(new Color(230, 219, 198));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame bookingFrame = new JFrame("Booking");
                bookingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bookingFrame.getContentPane().add(new Booking());
                bookingFrame.setSize(1280, 720);
                bookingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                bookingFrame.setVisible(true);
                SwingUtilities.getWindowAncestor(CoralineSeats.this).dispose();
            }
        });


        proceedToPaymentButton = new JButton("Proceed to Payment");
        proceedToPaymentButton.setEnabled(false);
        proceedToPaymentButton.setBackground(new Color(55, 107, 180));
        proceedToPaymentButton.setForeground(new Color(230, 219, 198));
        proceedToPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dateComboBox.getSelectedIndex() == 0 || timeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a valid date and time.");
                    return;
                }


                double totalPrice = selectedSeats.size() * 300.00;
                String selectedDate = (String) dateComboBox.getSelectedItem();
                String selectedTime = (String) timeComboBox.getSelectedItem();
                String seatDetails = String.join(", ", selectedSeats);


                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Movie: Coraline\n" +
                                "Seats: " + seatDetails + "\n" +
                                "Total Price: PHP" + totalPrice + "\n" +
                                "Date: " + selectedDate + "\n" +
                                "Time: " + selectedTime,
                        "Confirm Booking",
                        JOptionPane.YES_NO_OPTION);


                if (dialogResult == JOptionPane.YES_OPTION) {
                    saveSelectedSeats();
                    Window currentWindow = SwingUtilities.getWindowAncestor(CoralineSeats.this);
                    if (currentWindow != null) {
                        currentWindow.dispose();
                    }
                    JFrame paymentFrame = new JFrame("Payment");
                    paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    paymentFrame.getContentPane().add(new CoralinePayment("Coraline", totalPrice, selectedDate, selectedTime, seatDetails));
                    paymentFrame.setSize(1280, 720);
                    paymentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    paymentFrame.setBackground(new Color(87, 131, 193));
                    paymentFrame.setForeground(new Color(230, 219, 198));
                    paymentFrame.setVisible(true);
                } else {
                    selectedSeats.clear();
                    updateSeatAvailability();
                }
            }
        });


        GridBagConstraints gbcCancelButton = new GridBagConstraints();
        gbcCancelButton.gridx = 0;
        gbcCancelButton.gridy = 0;
        gbcCancelButton.insets = new Insets(5, 5, 5, 5);
        bottomPanel.add(cancelButton, gbcCancelButton);


        GridBagConstraints gbcProceedButton = new GridBagConstraints();
        gbcProceedButton.gridx = 1;
        gbcProceedButton.gridy = 0;
        gbcProceedButton.anchor = GridBagConstraints.LINE_END;
        gbcProceedButton.insets = new Insets(5, 850, 5, 5);
        bottomPanel.add(proceedToPaymentButton, gbcProceedButton);


        gbc.gridx = 0;
        gbc.gridy = ROWS + 4;
        gbc.gridwidth = COLS + 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(bottomPanel, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        resetButton = new JButton("X");
        resetButton.setBackground(new Color(179, 19, 18));
        resetButton.setForeground(new Color(230, 219, 198));
        resetButton.setBorderPainted(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "This will delete all reserved seats. Are you sure?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    removeAllSeatsFromDatabase();
                    updateSeatAvailability();
                }
            }
        });
        buttonPanel.add(resetButton);


        gbc.gridx = COLS + 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(buttonPanel, gbc);




        loadSeatAvailability();
    }


    private boolean isSeatDeleted(String seatLabel) {
        return switch (seatLabel) {
            case "A1", "B1", "A5", "A6", "B5", "A9", "A10", "B10", "A14", "B14" -> true;
            default -> false;
        };
    }


    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MovieBooking", "root", "butike02");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("MySQL JDBC driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
        }
    }


    private void updateSeatAvailability() {
        String selectedDate = (String) dateComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Time sqlTime;
        try {
            sqlTime = new Time(timeFormat.parse(selectedTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


        try {
            String query = "SELECT seat_label FROM reservations WHERE movie_name = ? AND date = ? AND time = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "Coraline");
            statement.setDate(2, Date.valueOf(selectedDate));
            statement.setTime(3, sqlTime);


            ResultSet resultSet = statement.executeQuery();


            Set<String> reservedSeats = new HashSet<>();
            while (resultSet.next()) {
                reservedSeats.add(resultSet.getString("seat_label"));
            }


            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (seats[row][col] != null) {
                        String seatLabel = (char) ('A' + row) + String.valueOf(col + 1);
                        if (reservedSeats.contains(seatLabel)) {
                            seats[row][col].setBackground(new Color(179, 19, 18));
                            seats[row][col].setForeground(Color.WHITE);
                            seats[row][col].setEnabled(false);
                        } else {
                            seats[row][col].setBackground(new Color(85, 176, 255));
                            seats[row][col].setForeground(new Color(4, 4, 4, 154));
                            seats[row][col].setEnabled(true);
                        }
                    }
                }
            }


            for (String seatLabel : selectedSeats) {
                for (int row = 0; row < ROWS; row++) {
                    for (int col = 0; col < COLS; col++) {
                        if (seats[row][col] != null && seats[row][col].getText().equals(seatLabel)) {
                            seats[row][col].setBackground(new Color(179, 19, 18));
                            seats[row][col].setForeground(Color.WHITE);
                        }
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void saveSelectedSeats() {
        String selectedDate = (String) dateComboBox.getSelectedItem();
        String selectedTime = (String) timeComboBox.getSelectedItem();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Time sqlTime;
        try {
            sqlTime = new Time(timeFormat.parse(selectedTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


        try {
            String insertQuery = "INSERT INTO reservations (movie_name, date, time, seat_label) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);


            for (String seat : selectedSeats) {
                statement.setString(1, "Coraline");
                statement.setDate(2, Date.valueOf(selectedDate));
                statement.setTime(3, sqlTime);
                statement.setString(4, seat);
                statement.addBatch();
            }


            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        selectedSeats.clear();
    }


    private void removeAllSeatsFromDatabase() {
        try {
            String deleteQuery = "DELETE FROM reservations WHERE movie_name = 'Coraline'";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadSeatAvailability() {
        updateSeatAvailability();
    }


    private Icon createColorIcon(Color color) {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, 16, 16);
        g2.dispose();
        return new ImageIcon(image);
    }


    private class SeatButtonListener implements ActionListener {
        private int row;
        private int col;


        public SeatButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String seatLabel = button.getText();
            if (selectedSeats.contains(seatLabel)) {
                selectedSeats.remove(seatLabel);
                button.setBackground(new Color(85, 176, 255));
                button.setForeground(new Color(4, 4, 4, 154));
            } else {
                selectedSeats.add(seatLabel);
                button.setBackground(new Color(78, 166, 103));
                button.setForeground(new Color(230, 219, 198));
            }
            proceedToPaymentButton.setEnabled(!selectedSeats.isEmpty());
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Coraline Seats");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new CoralineSeats());
        frame.setSize(1280, 720);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
