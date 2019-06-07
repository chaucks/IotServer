package com.xcoder.iotserver.switcher;

import android.app.Service;
import android.media.MediaPlayer;
import android.util.Log;

import com.xcoder.iotserver.server.IHandler;
import com.xcoder.iotserver.utensil.X;

import java.io.File;
import java.io.IOException;

/**
 * Switch of iot control
 *
 * @author Chuck Lee
 * @date 2019-06-03
 */
public class Switcher implements ISwitcher, IHandler {
    /**
     * Hold service instance
     */
    private Service service;

    private final MediaPlayer mediaPlayer;

    public Switcher(Service service) {
        X.objectNotNull(service);
        this.service = service;
        this.mediaPlayer = new MediaPlayer();
    }

    @Override
    public void play(final String path) {
        X.objectsNotNull(this.service, this.mediaPlayer, path);
        synchronized (this.mediaPlayer) {
            try {
                String filePath = X.appendExternalStorageDirectory(File.separator, path, ".wav");
                Log.v("File path:", filePath);
                this.mediaPlayer.reset();
                this.mediaPlayer.setDataSource(filePath);
                this.mediaPlayer.prepare();
                this.mediaPlayer.start();
            } catch (IOException e) {
                Log.e("Switcher.play", e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public void release() {
        try {
            if (null != this.mediaPlayer) {
                if (this.mediaPlayer.isPlaying()) {
                    this.mediaPlayer.stop();
                }
                this.mediaPlayer.release();
            }
        } catch (Throwable t) {
            Log.e("Switcher.release", t.getMessage(), t);
        }
    }

    @Override
    public Object handle(Object o) {
        String s = (String) o;
        String p = s.substring(s.indexOf("GET") + 3, s.indexOf("HTTP")).trim();
        p = p.substring(1);
        this.play(p);
        return "Success";
    }
}
