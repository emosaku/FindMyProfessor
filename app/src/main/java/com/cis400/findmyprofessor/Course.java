package com.cis400.findmyprofessor;

public class Course {

    public String[] keywords;
    public String[] professors;

    public Course(String[] keywords, String[] professors) {
        this.keywords = keywords;
        this.professors = professors;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String[] getProfessors() {
        return professors;
    }

}
