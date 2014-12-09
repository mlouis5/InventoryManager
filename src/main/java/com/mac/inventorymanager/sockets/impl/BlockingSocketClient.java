/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets.impl;

import com.google.common.base.Preconditions;
import com.mac.inventorymanager.sockets.SocketClient;
import com.mac.inventorymanager.sockets.observers.StreamObserver;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MacDerson
 */
public class BlockingSocketClient implements SocketClient {

    private static final int[] PORT_RANGE = {9000, 9999};
    private Socket client;
    private List<StreamObserver> observers;

    public static int[] getValidPortRange() {
        return PORT_RANGE;
    }

    private BlockingSocketClient() {
        observers = new ArrayList(1);
    }

    /**
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    @Override
    public void connect(InetAddress ip, int port) throws IOException {
        Preconditions.checkNotNull(ip);
        Preconditions.checkArgument(port >= PORT_RANGE[0] 
                && port <= PORT_RANGE[1]);
        String[] systemBreakdown = InetAddress.getLocalHost()
                .getHostAddress().split("\\.");
        String[] inputBreakdown = ip.getHostAddress().split("\\.");
        Preconditions.checkNotNull(systemBreakdown);
        Preconditions.checkNotNull(inputBreakdown);
        Preconditions.checkArgument(systemBreakdown.length == 4);
        Preconditions.checkArgument(inputBreakdown.length == 4);
        Preconditions.checkArgument(systemBreakdown[0].equals(inputBreakdown[0]) 
                && systemBreakdown[1].equals(inputBreakdown[1]));
        Preconditions.checkArgument(ip.isReachable(10000));
        if (Objects.isNull(client)) {
            client = new Socket(ip, port);
        } else {
            if (client.isClosed()) {                
                client.connect(new InetSocketAddress(ip, port));
            }
        }
    }

    @Override
    public void disconnect() throws IOException{
        if(Objects.nonNull(client)){
            if(client.isConnected()){
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(BlockingSocketClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        client = null;
    }

    @Override
    public void addStreamObserver(StreamObserver streamObserver) {
        if (Objects.nonNull(streamObserver)) {
            observers.add(streamObserver);
        }
    }

    @Override
    public void removeStreamObserver(StreamObserver streamObserver) {
        if (Objects.nonNull(streamObserver)) {
            observers.remove(streamObserver);
        }
    }

    @Override
    public void removeStreamObservers() {
        observers.clear();
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

}
