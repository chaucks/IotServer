package com.xcoder.iotserver.utensil;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * IO
 *
 * @author Chuck Lee
 * @date 2019-06-11
 */
public class Io {

    private static final long DEFAULT_EXPIRE = Long.MAX_VALUE;

    private static final long DEFAULT_TIMEOUT = 150L;

    private static final int DEFAULT_LENGTH = 102400;

    /**
     * Read byte array buffer
     *
     * @author Chuck Lee
     * @date 2019-06-22
     */
    @FunctionalInterface
    public interface Ir {
        /**
         * Io read
         *
         * @param buffer buffer
         * @throws IOException IOException
         */
        void read(byte[] buffer) throws IOException;
    }

    /**
     * Read input stream and write output stream
     *
     * @param is InputStream
     * @param os OutputStream
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    public static void i2o(final InputStream is, final OutputStream os) throws IOException, InterruptedException {
        i2o(is, os, DEFAULT_EXPIRE, DEFAULT_TIMEOUT, DEFAULT_LENGTH);
    }

    /**
     * Read input stream and write output stream
     *
     * @param is      InputStream
     * @param os      OutputStream
     * @param expire  expire
     * @param timeout timeout
     * @param length  length
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    public static void i2o(final InputStream is, final OutputStream os, final long expire
            , final long timeout, final int length) throws IOException, InterruptedException {
        final int available = available(is, expire, timeout);
        read(is, available, length, os::write);
        flushableFlush(os);
    }

    /**
     * Read input stream get byte array
     *
     * @param is InputStream
     * @return byte array
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    public static byte[] read(final InputStream is) throws IOException, InterruptedException {
        int available = available(is, DEFAULT_EXPIRE, DEFAULT_TIMEOUT);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            read(is, available, available, bos::write);
            return bos.toByteArray();
        } finally {
            closeableClose(bos);
        }
    }

    /**
     * Read input stream
     *
     * @param is     is
     * @param length length
     * @param ir     ir
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    public static void read(final InputStream is, final int available, int length, final Ir ir) throws IOException {
        if (length > available) {
            length = available;
        }
        for (int i = 0, j = available / length; i < j; i++) {
            byte[] buffer = new byte[length];
            is.read(buffer);
            ir.read(buffer);
        }

        int remain = available % length;
        if (0 < remain) {
            byte[] buffer = new byte[remain];
            ir.read(buffer);
        }
    }

    /**
     * Get InputStream available.
     *
     * @param is      InputStream
     * @param expire  expire
     * @param timeout timeout
     * @return available
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    public static int available(final InputStream is, final long expire, final long timeout) throws IOException, InterruptedException {
        int available = is.available();
        for (long t0 = System.currentTimeMillis(); 1 > available; available = is.available()) {
            if (expire < System.currentTimeMillis() - t0) {
                throw new RuntimeException("Waiting available time out......");
            }
            Thread.sleep(timeout);
        }
        return available;
    }

    /**
     * Flushable array flush
     *
     * @param flushableArray flushableArray
     */
    public static void flushableFlush(Flushable... flushableArray) {
        for (Flushable flushable : flushableArray) {
            if (null == flushable) {
                continue;
            }
            try {
                flushable.flush();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Closeable array close
     *
     * @param closeables closeables
     */
    public static void closeableClose(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (null == closeable) {
                continue;
            }
            try {
                closeable.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Socket array close
     *
     * @param sockets sockets
     */
    public static void socketClose(Socket... sockets) {
        for (Socket socket : sockets) {
            if (null == socket) {
                continue;
            }
            if (socket.isClosed()) {
                continue;
            }
            try {
                socket.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * ServerSocket array close
     *
     * @param serverSockets serverSockets
     */
    public static void serverSocketClose(ServerSocket... serverSockets) {
        for (ServerSocket serverSocket : serverSockets) {
            if (null == serverSocket) {
                continue;
            }
            if (serverSocket.isClosed()) {
                continue;
            }
            try {
                serverSocket.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
