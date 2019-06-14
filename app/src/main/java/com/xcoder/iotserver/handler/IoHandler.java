package com.xcoder.iotserver.handler;

import android.util.Log;
import com.xcoder.iotserver.utensil.Io;
import com.xcoder.iotserver.utensil.X;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Io handler
 *
 * @author Chuck Lee
 * @date 2019-06-14
 */
public class IoHandler implements IIoHandler<InputStream, OutputStream> {

    public static final String CHARSET_IN = "utf-8";

    public static final String CHARSET_OUT = "utf-8";

    @Override
    public void handle(InputStream is, OutputStream os) {
        try {
            String request = new String(Io.read(is), CHARSET_IN);
            Log.d("Request", request);

            String[] lines = X.splitNrOrRn(request);
            for (String line : lines) {
                if (!line.contains(":")) {

                    continue;
                }

            }

//            String p = s.substring(s.indexOf("GET") + 3, s.indexOf("HTTP")).trim();
//            p = p.substring(1);
//            this.play(p);

            StringBuilder htmlBuilder = new StringBuilder(128);
            htmlBuilder.append("HTTP/1.1 200 OK\n");
            htmlBuilder.append("Content-Type: text/html; charset=utf-8\n");
            htmlBuilder.append("\n");
            htmlBuilder.append("<h1>");
            htmlBuilder.append("Success");
            htmlBuilder.append("</h1>");

            String output = htmlBuilder.toString();

            os.write(output.getBytes(CHARSET_OUT));


        } catch (Throwable t) {
            Log.e("", "", t);
        } finally {
            Io.flushableFlush(os);
            Io.closeableClose(is, os);
        }
    }
}
