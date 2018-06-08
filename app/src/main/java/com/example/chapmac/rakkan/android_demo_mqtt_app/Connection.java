package com.example.chapmac.rakkan.android_demo_mqtt_app;

public class Connection {

    public String host;
    public String user;
    public String pass;

    public Connection() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Connection(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }
}
