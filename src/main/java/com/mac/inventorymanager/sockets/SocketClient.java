/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets;

import com.mac.inventorymanager.sockets.observers.StreamObserver;

/**
 *
 * @author Mac
 */
public interface SocketClient {
    
    public void connect(String ip, int port);
    
    public void disconnect();
    
    public void addStreamObserver(StreamObserver streamObserver);
    
    public void removeStreamObserver(StreamObserver streamObserver);
    
    public void removeStreamObservers();
}
