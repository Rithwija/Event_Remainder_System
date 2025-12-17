package Event_Remainder_System;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Executer {
    public static void main(String[] args) {
        Scanner sc = null;
        try {
            sc = new Scanner(System.in);
            reminderManager rm = new reminderManager();

            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (true) {
                System.out.println("\n*** Event Menu ***");
                System.out.println("1. Add event");
                System.out.println("2. Display events");
                System.out.println("3. Show completed events");
                System.out.println("4. Remove event");
                System.out.println("5. Exit");

                System.out.print("Enter choice: ");
                int ch = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (ch) {
                    case 1:
                        System.out.print("Enter title: ");
                        String t = sc.nextLine();

                        System.out.print("Enter description: ");
                        String d = sc.nextLine();

                        System.out.print("Enter date (yyyy-MM-dd): ");
                        String dateStr = sc.nextLine();

                        try {
                            LocalDate date = LocalDate.parse(dateStr, pattern);
                            rm.addEvent(new Event(t, d, date));
                        } catch (DateTimeParseException e) {
                            System.out.println("❌ Invalid date format.");
                        }
                        break;

                    case 2:
                        rm.viewEvents();
                        break;

                    case 3:
                        rm.showEventAlreadyHappend();
                        break;

                    case 4:
                        rm.viewEvents();
                        System.out.print("Enter event number to remove: ");
                        int index = sc.nextInt();
                        sc.nextLine(); // consume newline
                        rm.removeEvent(index);
                        break;

                    case 5:
                        System.out.println("👋 Exiting...");
                        return;

                    default:
                        System.out.println("❌ Invalid choice!");
                }
            }
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}