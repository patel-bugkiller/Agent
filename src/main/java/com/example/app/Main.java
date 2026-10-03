package com.example.app;

public class Main {
    public String getGreeting(){
        return "Application is running";
    }

    public static void main(String[] args){
        System.out.println(new Main().getGreeting());
    }
}