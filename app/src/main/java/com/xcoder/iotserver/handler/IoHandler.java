package com.xcoder.iotserver.handler;

import android.util.Log;

import com.xcoder.iotserver.controller.Rf433Controller;
import com.xcoder.iotserver.utensil.Io;
import com.xcoder.iotserver.utensil.X;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Io handler
 *
 * @author Chuck Lee
 * @date 2019-06-14
 */
public class IoHandler implements IIoHandler<InputStream, OutputStream> {

    public static final String CHARSET_IN = "utf-8";

    public static final String CHARSET_OUT = "utf-8";

    public static final String ERROR_404_HTML = "HTTP/1.1 404 ERROR\n"
            .concat("Content-Type: text/html; charset=utf-8\n")
            .concat("\n").concat("<h1>Error</h1>");

    public static final String OK_200_HTML = "HTTP/1.1 200 OK\n"
            .concat("Content-Type: text/html; charset=utf-8\n")
            .concat("\n").concat("<h1>Success</h1>");

    @Override
    public void handle(InputStream is, OutputStream os) {
        try {
            String request = new String(Io.read(is), CHARSET_IN);
            Log.d("Request", request);

            Map<String, String> requestMap = getRequestMap(request);

            new Rf433Controller().control(requestMap);

            os.write(OK_200_HTML.getBytes(CHARSET_OUT));
        } catch (Throwable t) {
            Log.e("IoHandler", "handle", t);
            try {
                os.write(ERROR_404_HTML.getBytes(CHARSET_OUT));
            } catch (Throwable tt) {
                tt.printStackTrace();
            }
        } finally {
            Io.flushableFlush(os);
            Io.closeableClose(is, os);
        }
    }

    /**
     * Request 解析
     *
     * @param request request
     * @return request map
     */
    public static Map<String, String> getRequestMap(String request) {
        Map<String, String> requestMap = new HashMap<>(16);
        String[] lines = X.splitNrOrRn(request);
        String line0 = lines[0];

        // Parse request uri
        String[] strings = line0.split(" ");
        requestMap.put("Method", strings[0]);
        requestMap.put("Uri", strings[1]);
        requestMap.put("Version", strings[2]);

        for (String line : lines) {
            int indexOfColon = line.indexOf(":");
            if (-1 < indexOfColon) {
                String value = line.substring(1 + indexOfColon);
                String key = line.substring(0, indexOfColon);
                requestMap.put(key.trim(), value.trim());
            }
        }
        return requestMap;
    }


}
