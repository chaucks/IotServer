package com.xcoder.iotserver.switcher;

/**
 * Iot device controller switch interface
 *
 * @author Chuck Lee
 * @date 2019-06-03
 */
public interface ISwitcher {
    /**
     * MediaPlayer play wav DTMF signal
     * & Rf 433mzh send close
     *
     * @param cmd which command
     */
    void play(String cmd);

    /**
     * 释放资源
     */
    void release();
}
