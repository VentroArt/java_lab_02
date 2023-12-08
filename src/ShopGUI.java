import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShopGUI extends JFrame {
    private TestConnection testConnection;

    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton addButton;
    private JButton viewButton;
    private JTextField idField;
    private JButton editButton;
    private JButton updateButton;


    public ShopGUI() {
        // Ініціалізація вікна
        setTitle("Shop GUI");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Ініціалізація компонентів
        nameField = new JTextField(20);
        quantityField = new JTextField(20);
        priceField = new JTextField(20);
        addButton = new JButton("Add to Database");
        viewButton = new JButton("View Database");
        idField = new JTextField(20);
        editButton = new JButton("Edit Item");
        updateButton = new JButton("Update Database");

        // Ініціалізація з'єднання з базою даних
        testConnection = new TestConnection();

        // Додавання обробників подій
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataToDatabase();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDatabase();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editItem();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDatabase();
            }
        });

        // Додавання компонентів на форму
        setLayout(new FlowLayout());
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel("Price:"));
        add(priceField);
        add(addButton);
        add(viewButton);
        add(new JLabel("ID:"));
        add(idField);
        add(editButton);
        add(updateButton);
    }

    private void addDataToDatabase() {
        try {
            String name = nameField.getText();
            String quantity = quantityField.getText();
            String price = priceField.getText();

            String query = String.format("INSERT INTO shop (name, quantity, price) VALUES ('%s', '%s', '%s')", name, quantity, price);

            testConnection.statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Data added to database!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding data to database.");
        }
    }

    private void editItem() {
        String idInput = JOptionPane.showInputDialog(this, "Enter Item ID to Edit:");
        if (idInput != null && !idInput.isEmpty()) {
            int id = Integer.parseInt(idInput);
            try {
                String query = String.format("SELECT * FROM shop WHERE id = %d", id);
                ResultSet resultSet = testConnection.statement.executeQuery(query);
                if (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    quantityField.setText(resultSet.getString("quantity"));
                    priceField.setText(resultSet.getString("price"));
                } else {
                    JOptionPane.showMessageDialog(this, "Item with ID " + id + " not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error editing item.");
            }
        }
    }

    private void updateDatabase() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            String quantity = quantityField.getText();
            String price = priceField.getText();

            // Перевіряємо, чи всі необхідні поля для оновлення задані
            if (id.isEmpty() || name.isEmpty() || quantity.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            String query = String.format("UPDATE shop SET name = '%s', quantity = '%s', price = '%s' WHERE id = %s",
                    name, quantity, price, id);

            testConnection.statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Data updated in database!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating data in database.");
        }
    }

    private void viewDatabase() {
        try {
            ResultSet resultSet = testConnection.statement.executeQuery("SELECT * FROM shop");

            StringBuilder result = new StringBuilder("Database Content:\n");
            while (resultSet.next()) {
                result.append(resultSet.getString(2)).append(" ")
                        .append(resultSet.getString(3)).append(" ")
                        .append(resultSet.getString(4)).append("\n");
            }

            JOptionPane.showMessageDialog(this, result.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error viewing database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShopGUI().setVisible(true);
            }
        });
    }
}
