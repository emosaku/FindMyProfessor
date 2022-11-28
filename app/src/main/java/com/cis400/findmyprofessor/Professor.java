package com.cis400.findmyprofessor;

import android.content.Intent;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Professor {

    public String name, email, office, title;
    public Integer keywordMatches;

    public Professor(){

    }

    public Professor(String fullName, String email, String office, String title, Integer keywordMatches){
        this.name = fullName;
        this.email = email;
        this.office = office;
        this.title = title;
        this.keywordMatches =  keywordMatches;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOffice() {
        return office;
    }

    public String getTitle() { return title; }

    public Integer getKeywordMatches() { return keywordMatches; }

    public void setKeywordMatches(Integer keywordMatches) {
        this.keywordMatches = keywordMatches;
    }
}
