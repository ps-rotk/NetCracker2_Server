package main;

import java.io.Serializable;

public class Connection implements Serializable {
    private int clientConnection;
    private int schedulerConnection;

    public Connection(){
        clientConnection = 1023;
        schedulerConnection = 1024;
    }

    public Connection (int clientConnection, int schedulerConnection){
        this.clientConnection = clientConnection;
        this.schedulerConnection = schedulerConnection;
    }

    public int getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(int clientConnection) {
        this.clientConnection = clientConnection;
    }

    public int getSchedulerConnection() {
        return schedulerConnection;
    }

    public void setSchedulerConnection(int schedulerConnection) {
        this.schedulerConnection = schedulerConnection;
    }

}
