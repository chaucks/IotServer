package com.xcoder.iotserver.server;

import android.util.Log;
import com.xcoder.iotserver.utensil.X;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static final int BUFFER_SIZE = 1024;

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
            BufferedReader br = null;
            OutputStream os = null;
            try {
                s = ss.accept();
                br = new BufferedReader(new InputStreamReader(s.getInputStream(), CHARSET_IN));
                StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
                for (String line = br.readLine(); null != line; line = br.readLine()) {
                    if ("".equals(line)) {
                        Log.d("line", line);
                        break;
                    }
                    buffer.append(line);
                }
                String request = buffer.toString();
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
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e("", "", e);
                    }
                }
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.e("", "", e);
                    }
                }
                if (null != s) {
                    try {
                        s.close();
                    } catch (IOException e) {
                        Log.e("", "", e);
                    }
                }
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
