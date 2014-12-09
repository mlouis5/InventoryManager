/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets;

import com.mac.inventorymanager.sockets.observers.StreamObserver;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author Mac
 */
public interface SocketClient {
    
    public void connect(InetAddress ip, int port) throws IOException;
    
    public void disconnect() throws IOException;
    
    public void addStreamObserver(StreamObserver streamObserver);
    
    public void removeStreamObserver(StreamObserver streamObserver);
    
    public void removeStreamObservers();
}
