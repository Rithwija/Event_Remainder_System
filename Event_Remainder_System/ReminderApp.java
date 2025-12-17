package Event_Remainder_System;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;

public class ReminderApp extends JFrame {
    private reminderManager manager;
    private JTextArea eventDisplay;
    private static final String FILE_NAME = "events.txt";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReminderApp() {
        manager = new reminderManager();
        setupUI();
        loadEventsFromFile();
        new EventCheckerThread(manager).start();
    }

    private void setupUI() {
        setTitle("📅 Event Reminder System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Custom background panel with image and pattern
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            private BufferedImage backgroundImage;
            {
                try {
                    backgroundImage = ImageIO.read(new File("eventwebsite 2.jpeg")); // Place a background image file named "background.jpg" in your project directory
                } catch (IOException e) {
                    backgroundImage = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2d.setColor(new Color(240, 248, 255)); // Fallback light blue if image fails
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                // Add colorful pattern
                g2d.setColor(new Color(255, 182, 193, 50)); // Light pink with transparency
                for (int y = 0; y < getHeight(); y += 20) {
                    for (int x = 0; x < getWidth(); x += 20) {
                        g2d.fillOval(x, y, 10, 10);
                    }
                }
                g2d.setColor(new Color(144, 238, 144, 50)); // Light green with transparency
                for (int y = 10; y < getHeight(); y += 20) {
                    for (int x = 10; x < getWidth(); x += 20) {
                        g2d.fillRect(x, y, 5, 5);
                    }
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Top title with custom styling
        JLabel titleLabel = new JLabel("Event Reminder System", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204)); // Dark blue text
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 255, 255, 180)); // Semi-transparent white background
        titleLabel.setBorder(new EmptyBorder(15, 10, 15, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel with custom styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 15, 15));
        buttonPanel.setOpaque(false); // Make panel transparent to show background
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addBtn = new JButton("➕ Add Event");
        JButton viewBtn = new JButton("👁 View Events");
        JButton removeBtn = new JButton("🗑 Remove Event");
        JButton exitBtn = new JButton("🚪 Exit");

        styleButton(addBtn, new Color(46, 139, 87), new Color(34, 139, 34)); // SeaGreen to ForestGreen
        styleButton(viewBtn, new Color(0, 191, 255), new Color(0, 139, 139)); // DeepSkyBlue to DarkTurquoise
        styleButton(removeBtn, new Color(255, 69, 0), new Color(205, 55, 0)); // OrangeRed to DarkOrange
        styleButton(exitBtn, new Color(255, 20, 147), new Color(139, 0, 139)); // DeepPink to DarkMagenta

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(exitBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Make panel transparent
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Event display area with custom styling
        eventDisplay = new JTextArea();
        eventDisplay.setEditable(false);
        eventDisplay.setFont(new Font("Monospaced", Font.PLAIN, 14));
        eventDisplay.setBackground(new Color(255, 255, 240, 200)); // Ivory with transparency
        eventDisplay.setForeground(new Color(0, 100, 0)); // Dark green text
        eventDisplay.setMargin(new Insets(15, 15, 15, 15));
        eventDisplay.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        JScrollPane scrollPane = new JScrollPane(eventDisplay);
        add(scrollPane, BorderLayout.CENTER);

        // Action listeners
        addBtn.addActionListener(e -> addEventDialog());
        viewBtn.addActionListener(e -> refreshEventDisplay());
        removeBtn.addActionListener(e -> removeEventDialog());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void styleButton(JButton button, Color background, Color hover) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
            }
        });
    }

    private void addEventDialog() {
        String title = JOptionPane.showInputDialog(this, "Enter Event Name:");
        if (title == null || title.isEmpty()) return;

        String desc = JOptionPane.showInputDialog(this, "Enter description:");
        if (desc == null || desc.isEmpty()) return;

        String dateStr = JOptionPane.showInputDialog(this, "Enter date (yyyy-MM-dd):");
        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            Event e = new Event(title, desc, date);
            manager.addEvent(e);
            saveEventToFile(e);
            refreshEventDisplay();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid input or date format!");
        }
    }

    private void removeEventDialog() {
        String indexStr = JOptionPane.showInputDialog(this, "Enter Index event number to remove:");
        try {
            int index = Integer.parseInt(indexStr);
            manager.removeEvent(index);
            refreshEventDisplay();
            saveAllEventsToFile();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid index!");
        }
    }

    private void refreshEventDisplay() {
        eventDisplay.setText("");
        java.util.List<Event> all = manager.getEvents();
        if (all.isEmpty()) {
            eventDisplay.append("⚠️ No events.\n");
        } else {
            for (int i = 0; i < all.size(); i++) {
                Event e = all.get(i);
                eventDisplay.append(String.format("%2d. %-20s %-12s %s\n", (i + 1), e.getTitle(), e.getDate(), e.getDescription()));
            }
        }
    }

    private void saveEventToFile(Event e) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(e.getTitle() + "|" + e.getDescription() + "|" + e.getDate() + "\n");
        } catch (IOException ex) {
            System.out.println("❌ Failed to write event to file.");
        }
    }

    private void saveAllEventsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Event e : manager.getEvents()) {
                bw.write(e.getTitle() + "|" + e.getDescription() + "|" + e.getDate() + "\n");
            }
        } catch (IOException ex) {
            System.out.println("❌ Failed to rewrite event file.");
        }
    }

    private void loadEventsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String title = parts[0];
                    String desc = parts[1];
                    LocalDate date = LocalDate.parse(parts[2], formatter);
                    manager.addEvent(new Event(title, desc, date));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading events from file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReminderApp::new);
    }
}