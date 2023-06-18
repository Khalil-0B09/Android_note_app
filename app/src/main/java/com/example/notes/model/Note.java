package com.example.notes.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Note {
    private String titre;
    private String description;
    private Date date;

    public Note(String titre,String description, Date date){
        this.titre = titre;
        this.description = description;
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
