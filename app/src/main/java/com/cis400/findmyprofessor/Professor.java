package com.cis400.findmyprofessor;

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
        this.keywordMatches = keywordMatches;
    }

    public Integer getKeywordMatches() { return keywordMatches; }

    public void setKeywordMatches(Integer keywordMatches) {
        this.keywordMatches = keywordMatches;
    }

    public void incrementKeywordMatches() { this.keywordMatches = this.keywordMatches + 1; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOffice() {
        return office;
    }

    public String getTitle() {
        return title;
    }
}
