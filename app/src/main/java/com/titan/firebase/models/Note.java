package com.titan.firebase.models;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String documentId;
    private String title;
    private String description;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
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
}