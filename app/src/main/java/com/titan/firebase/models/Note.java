package com.titan.firebase.models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Note {

    private String documentId;
    private String title;
    private String description;
    private int priority;

    List<String> tags;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description, int priority, List<String> tags) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPriority() {
        return priority;
    }

    public List<String> getTags() {
        return tags;
    }
}
