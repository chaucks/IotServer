package com.xcoder.iotserver.server;

import android.util.Log;
import com.xcoder.iotserver.handler.SocketHandler;
import com.xcoder.iotserver.utensil.Io;
import com.xcoder.iotserver.utensil.X;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Iot server
 *
 * @author Chuck Lee
 * @date 2019-05-31
 */
public class IotServer extends Thread {

    private ServerSocket serverSocket;

    private volatile boolean running;

    public IotServer() {
        super.setPriority(MAX_PRIORITY);
        super.setDaemon(true);
        this.running = true;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(8090);
        } catch (IOException e) {
            Log.e("IotServer.run", "new ServerSocket", e);
            return;
        }

        SocketHandler socketHandler = new SocketHandler();
        for (Socket socket; this.running; ) {
            try {
                socket = this.serverSocket.accept();
            } catch (Throwable t) {
                Log.e("ServerSocket", "accept", t);
                continue;
            }
            socketHandler.handle(socket);
        }
//        Io.closeableClose(this.serverSocket);
    }

    @Override
    public void interrupt() {
        this.running = false;
        Io.closeableClose(this.serverSocket);
        X.interrupt(this);
    }
}
