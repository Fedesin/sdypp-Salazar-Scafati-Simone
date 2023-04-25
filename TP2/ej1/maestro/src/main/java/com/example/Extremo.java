package com.example;

public class Extremo {
    private String host;
    private int port;
    private String namefile;

    public Extremo(String host, int port,String namefile) {
        this.host = host;
        this.port = port;
        this.namefile = namefile;
    }

    public String host() {
        return this.host;
    }

    public int port() {
        return this.port;
    }
    
     public String namefile() {
        return this.namefile;
    }
}