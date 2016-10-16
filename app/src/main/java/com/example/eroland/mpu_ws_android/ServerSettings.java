package com.example.eroland.mpu_ws_android;

/**
 * Created by eroland on 10/16/16.
 */

public class ServerSettings {
    private String ip;
    private int port;
    private int sensors;

    public ServerSettings(String ip, int port, int sensors){
        this.ip = ip;
        this.port = port;
        this.sensors = sensors;
    }

    public String getIp(){
        return this.ip;
    }

    public int getPort() {
        return port;
    }

    public int getSensors() {
        return sensors;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSensors(int sensors) {
        this.sensors = sensors;
    }
}
