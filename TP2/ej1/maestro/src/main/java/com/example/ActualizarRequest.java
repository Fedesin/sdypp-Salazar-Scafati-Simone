package com.example;
public class ActualizarRequest {
    private String filename;
    private String hostExtremo;
    private int portExtremo;
    private int id_usuario;

    public int getId_usuario() {
        return this.id_usuario;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getHostExtremo() {
        return hostExtremo;
    }
    public void setHostExtremo(String hostExtremo) {
        this.hostExtremo = hostExtremo;
    }
    public int getPortExtremo() {
        return portExtremo;
    }
    public void setPortExtremo(int portExtremo) {
        this.portExtremo = portExtremo;
    }
    public ActualizarRequest(String filename, String hostExtremo, int portExtremo,int id_usuario) {
        this.filename = filename;
        this.hostExtremo = hostExtremo;
        this.portExtremo = portExtremo;
        this.id_usuario = id_usuario;
    }
}