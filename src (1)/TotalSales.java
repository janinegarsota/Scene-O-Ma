import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TotalSales extends JPanel {
    private Connection connection;
    private JFrame parentFrame;

    private static final double TICKET_PRICE = 300.00;

    public TotalSales() {
        this(null);
    }

    public TotalSales(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(20, 20));

        initializeDatabaseConnection();

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(55, 107, 180));
        JLabel titleLabel = new JLabel("Total Sales");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        String[] columnNames = {"Movie Name", "Tickets Sold", "Total Sales (PHP)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable salesTable = new JTable(tableModel);
        salesTable.setFont(new Font("Arial", Font.PLAIN, 18));
        salesTable.setRowHeight(30);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                headerLabel.setBackground(new Color(55, 107, 180));
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
                headerLabel.setHorizontalAlignment(JLabel.CENTER);
                return headerLabel;
            }
        };

        for (int i = 0; i < salesTable.getColumnCount(); i++) {
            salesTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < salesTable.getColumnCount(); i++) {
            salesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(salesTable);
        add(scrollPane, BorderLayout.CENTER);

        populateTable(tableModel);

        double totalSales = getTotalSales();
        JLabel totalLabel = new JLabel("Total Sales: PHP " + totalSales);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 30));
        totalLabel.setForeground(new Color(34, 139, 34));
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(totalLabel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);

        if (parentFrame != null) {
            JButton backButton = new JButton("Back to Dashboard");
            backButton.setFont(new Font("Arial", Font.BOLD, 20));
            backButton.setBackground(new Color(55, 107, 180));
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.setToolTipText("Click to return to the dashboard");
            backButton.addActionListener(e -> {
                parentFrame.setVisible(true);
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                currentFrame.dispose();
            });
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(backButton);

            southPanel.add(totalPanel, BorderLayout.NORTH);
            southPanel.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            southPanel.add(totalPanel, BorderLayout.NORTH);
        }

        add(southPanel, BorderLayout.SOUTH);
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MovieBooking", "root", "butike02");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTable(DefaultTableModel tableModel) {
        displayMovieSales(tableModel, "Mean Girls");
        displayMovieSales(tableModel, "Coraline");
        displayMovieSales(tableModel, "Pearl");
    }

    private void displayMovieSales(DefaultTableModel tableModel, String movieName) {
        try {
            String query = "SELECT COUNT(*) AS total FROM reservations WHERE movie_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, movieName);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int sales = resultSet.getInt("total");
                double salesTotal = sales * TICKET_PRICE;
                tableModel.addRow(new Object[]{movieName, sales, salesTotal});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getTotalSales() {
        double totalSales = 0;
        try {
            String query = "SELECT COUNT(*) AS total FROM reservations";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                totalSales = resultSet.getInt("total") * TICKET_PRICE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSales;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Total Sales");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TotalSales totalSalesPanel = new TotalSales();
        frame.getContentPane().add(totalSalesPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
