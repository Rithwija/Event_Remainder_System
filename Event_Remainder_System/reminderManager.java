package Event_Remainder_System;

import java.util.ArrayList;

public class reminderManager {
    ArrayList<Event> arr;

    public reminderManager(){
        arr = new ArrayList<>();
    }

    public void addEvent(Event e){
        arr.add(e);
        System.out.println("✅ Event added!");
    }

    public void viewEvents(){
        if(arr.isEmpty()){
            System.out.println("⚠️ No events to display.");
            return;
        }

        for(int i = 0; i < arr.size(); i++){
            Event e = arr.get(i);
            System.out.println((i+1) + ". " + e.getTitle());
            System.out.println("   Description: " + e.getDescription());
            System.out.println("   Date: " + e.getDate());
            System.out.println("   Completed: " + (e.isCompleted() ? "Yes" : "No"));
        }
    }

    public void showEventAlreadyHappend(){
        boolean found = false;
        for(Event e : arr){
            if(e.isCompleted()){
                System.out.println("✔️ " + e.getTitle() + " - " + e.getDescription());
                found = true;
            }
        }
        if (!found) {
            System.out.println("⚠️ No completed events found.");
        }
    }

    public void updateEventStatus(int index){
        if(index < 1 || index > arr.size()){
            System.out.println("Invalid index!");
            return;
        }
        arr.get(index - 1).setCompleted(true);
        System.out.println("✅ Event marked as completed!");
    }

    public void removeEvent(int index){
        if(index < 1 || index > arr.size()){
            System.out.println("Invalid index!");
            return;
        }
        arr.remove(index - 1);
        System.out.println("🗑️ Event removed.");
    }
    public java.util.List<Event> getEvents() {
    return arr;
}

}
