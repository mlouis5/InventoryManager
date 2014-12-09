/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets.observers.impl;

import com.mac.inventorymanager.sockets.SocketClient;
import com.mac.inventorymanager.sockets.observers.StreamObserver;

/**
 *
 * @author MacDerson
 */
public class BarcodeStreamObserver implements StreamObserver{

    private String stream;
    @Override
    public void setStream(String data) {
        this.stream = data;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
