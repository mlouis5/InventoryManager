/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets.observers;

import com.mac.inventorymanager.sockets.SocketClient;

/**
 *
 * @author Mac
 */
public interface StreamObserver {
    
    void streamReceived(String data);
    
    void disconnecting(SocketClient sc);
    
    void connecting(SocketClient sc);
}
