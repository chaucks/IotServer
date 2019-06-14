package com.xcoder.iotserver.handler;

import java.net.Socket;

/**
 * Socket handler
 *
 * @author Chuck Lee
 * @date 2019-06-14
 */
public interface ISocketHandler {
    /**
     * Handle socket
     *
     * @param socket socket
     */
    void handle(Socket socket);
}
