/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.inventorymanager.sockets.impl;

import com.google.common.base.Preconditions;
import com.mac.inventorymanager.sockets.SocketClient;
import com.mac.inventorymanager.sockets.observers.StreamObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MacDerson
 */
public class SocketClientImpl extends Thread implements SocketClient {

    private static final int[] PORT_RANGE = {9000, 9999};
    private Socket client;
    private final List<StreamObserver> observers;
    private boolean isLocked;
    private boolean isParametersSet;
    private ExecutorService executor;
    private int port;
    private InetAddress ip;

    public static int[] getValidPortRange() {
        return PORT_RANGE;
    }

    private SocketClientImpl() {
        observers = new ArrayList(1);
        isLocked = false;
        isParametersSet = false;
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void connect() throws IOException {
        if (unlocked()) {
            Preconditions.checkArgument(isParametersSet);
                        
            if (Objects.nonNull(observers) && observers.size() > 0) {
                try {
                    lock();
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
                } finally {
                    unlock();
                    run();
                }
            }
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void disconnect() throws IOException {
        if (unlocked()) {
            try {
                lock();

                if (Objects.nonNull(client)) {
                    if (client.isConnected()) {
                        try {
                            client.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                client = null;
            } finally {
                if (Objects.nonNull(executor)) {
                    executor.shutdown();
                    while (!executor.isTerminated());
                }
                unlock();
            }
        }
    }

    @Override
    public void addStreamObserver(StreamObserver streamObserver) {
        if (unlocked()) {
            if (Objects.nonNull(streamObserver)) {
                observers.add(streamObserver);
            }
        }
    }

    @Override
    public void removeStreamObserver(StreamObserver streamObserver) {
        if (unlocked()) {
            if (Objects.nonNull(streamObserver)) {
                observers.remove(streamObserver);
            }
        }
    }

    @Override
    public void removeStreamObservers() {
        if (unlocked()) {
            observers.clear();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    private boolean unlocked() {
        return isLocked == false;
    }

    private void lock() {
        isLocked = true;
    }

    private void unlock() {
        isLocked = false;
    }
    
    private void paremetersSet(){
        this.isParametersSet = true;
    }
    
    private void parametersNotSet(){
        this.isParametersSet = false;
    }

    @Override
    public void run() {
        if (client.isConnected()) {
            BufferedReader inFromServer = null;
            try {
                inFromServer = new BufferedReader(new InputStreamReader(client
                        .getInputStream()));
                executor = Executors.newFixedThreadPool(5);
                while (true) {
                    for (StreamObserver obs : observers) {
                        obs.setStream(inFromServer.readLine());
                        executor.execute(obs);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (Objects.nonNull(inFromServer)) {
                        inFromServer.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SocketClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void setServerParameters(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
        paremetersSet();
    }
}
