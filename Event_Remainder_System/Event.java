package Event_Remainder_System;

import java.time.LocalDate;

public class Event {
    private String title;
    private String description;
    private LocalDate date;
    private boolean isCompleted;

    public Event(String title, String description, LocalDate date){
        this.title = title;
        this.description = description;
        this.date = date;
        isCompleted = false;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public boolean isCompleted(){
        return isCompleted;
    }

    public void setCompleted(boolean flag){
        this.isCompleted = flag;
    }
}
