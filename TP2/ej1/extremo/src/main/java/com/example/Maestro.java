package com.example;

public class Maestro {
    private String host;
    private int port;

    public extremo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String host() {
        return this.host;
    }

    public int port() {
        return this.port;
    }
}
