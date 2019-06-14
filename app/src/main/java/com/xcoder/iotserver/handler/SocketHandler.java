package com.xcoder.iotserver.handler;

import com.xcoder.iotserver.utensil.Io;
import com.xcoder.iotserver.utensil.X;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket handler
 *
 * @author Chuck lee
 * @date 2019-06-14
 */
public class SocketHandler implements ISocketHandler {

    @Override
    public void handle(Socket socket) {
        X.THREAD_POOL.execute(() -> {
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                new IoHandler().handle(is, os);
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                Io.closeableClose(socket);
            }
        });
    }
}
