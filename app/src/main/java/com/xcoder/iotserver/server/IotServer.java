package com.xcoder.iotserver.server;

import android.util.Log;
import com.xcoder.iotserver.utensil.Io;
import com.xcoder.iotserver.utensil.X;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Iot server
 *
 * @author Chuck Lee
 * @date 2019-05-31
 */
public class IotServer extends Thread {

    public static final String CHARSET_IN = "utf-8";

    public static final String CHARSET_OUT = "utf-8";

    private volatile boolean running;

    private IHandler iHandler;

    public IotServer() {
        super.setPriority(MAX_PRIORITY);
        super.setDaemon(true);
        this.running = true;
    }

    public IotServer(IHandler<String, String> iHandler) {
        this();
        X.objectNotNull(iHandler);
        this.iHandler = iHandler;
    }

    @Override
    public void run() {
        ServerSocket ss;
        try {
            ss = new ServerSocket(8090);
        } catch (IOException e) {
            Log.e("", "", e);
            return;
        }
        for (; this.running; ) {
            Socket s = null;
            InputStream is = null;
            ByteArrayOutputStream bos = null;
            OutputStream os = null;
            try {
                s = ss.accept();
                is = s.getInputStream();

                String request = new String(Io.read(is), CHARSET_IN);
                Log.d("Request", request);

                Object object = this.iHandler.handle(request);

                StringBuilder htmlBuilder = new StringBuilder(128);
                htmlBuilder.append("HTTP/1.1 200 OK\n");
                htmlBuilder.append("Content-Type: text/html; charset=utf-8\n");
                htmlBuilder.append("\n");
                htmlBuilder.append("<h1>");
                htmlBuilder.append(object);
                htmlBuilder.append("</h1>");

                String output = htmlBuilder.toString();

                os = s.getOutputStream();
                os.write(output.getBytes(CHARSET_OUT));
                os.flush();
            } catch (Throwable t) {
                Log.e("", "", t);
            } finally {
                Io.closeableClose(os, bos, is, s);
            }
        }

        try {
            ss.close();
        } catch (Throwable t) {
            Log.e("", "", t);
        }
    }

    @Override
    public void interrupt() {
        try {
            this.running = false;
            super.interrupt();
        } catch (Throwable t) {
            Log.e("IotServer.interrupt", t.getMessage(), t);
        }
    }
}
