package com.example;
public class ActualizarRequest {
    private String filename;
    private String hostExtremo;
    private int portExtremo;
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
    public ActualizarRequest(String filename, String hostExtremo, int portExtremo) {
        this.filename = filename;
        this.hostExtremo = hostExtremo;
        this.portExtremo = portExtremo;
    }
}