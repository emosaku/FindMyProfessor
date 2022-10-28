//package com.cis400.FindMyProf;
package com.cis400.findmyprofessor;

public class User {
    //Create string variables for Fullname and Age, Email
    public String fullName, age, email, password;

    public User(){

    }

    public User(String fullName, String age, String email, String password){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
    }
}
