package com.cis400.findmyprofessor;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Professor {

    public String name, email, office, title;

    public Professor(){

    }

    public Professor(String fullName, String email, String office, String title){
        this.name = name;
        this.email = email;
        this.office = office;
        this.title = title;
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

    public String getTitle() {
        return title;
    }
}
