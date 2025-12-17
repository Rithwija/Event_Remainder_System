package Event_Remainder_System;

import javax.swing.*;
import java.time.LocalDate;

public class EventCheckerThread extends Thread {
    private reminderManager manager;

    public EventCheckerThread(reminderManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (true) {
            for (Event e : manager.getEvents()) {
                if (!e.isCompleted() && e.getDate().isEqual(LocalDate.now())) {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, "📅 Today: " + e.getTitle() + "\n" + e.getDescription(), "Event Reminder", JOptionPane.INFORMATION_MESSAGE)
                    );
                    e.setCompleted(true); // Mark as notified
                }
            }

            try {
                Thread.sleep(60000); // check every 60 seconds
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
