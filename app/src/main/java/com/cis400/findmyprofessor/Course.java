package com.cis400.findmyprofessor;

import java.util.ArrayList;
import java.util.List;

public class Course {

    public String[] keywordsArray;
    public String[] professorsArray;

    public Course(String[] keywords, String[] professors) {
        this.keywordsArray = keywords;
        this.professorsArray = professors;
    }

    public String[] getKeywords() {
        return keywordsArray;
    }

    public String[] getProfessors() {
        return professorsArray;
    }

    public void setKeywords(String[] keywords) {
        this.keywordsArray = keywords;
    }

    public void setProfessors(String[] professors) {
        this.professorsArray = professors;
    }
}