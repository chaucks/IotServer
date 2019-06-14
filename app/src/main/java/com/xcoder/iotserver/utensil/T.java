package com.xcoder.iotserver.utensil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class T {

    public static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();

    /**
     * Thread interrupt
     *
     * @param threads threads
     */
    public static void interrupt(Thread... threads) {
        for (Thread thread : threads) {
            if (null == thread) {
                continue;
            }
            try {
                thread.interrupt();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 线程池停机
     */
    public static void shutdown() {
        try {
            THREAD_POOL.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
