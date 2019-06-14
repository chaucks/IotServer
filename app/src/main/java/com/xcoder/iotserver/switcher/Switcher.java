package com.xcoder.iotserver.switcher;

import android.media.MediaPlayer;
import android.util.Log;
import com.xcoder.iotserver.utensil.X;

import java.io.File;

/**
 * Switch of iot control
 *
 * @author Chuck Lee
 * @date 2019-06-03
 */
public class Switcher implements ISwitcher {

    private final MediaPlayer mediaPlayer;

    public Switcher() {
        this.mediaPlayer = new MediaPlayer();
    }

    @Override
    public void play(final String path) throws Throwable {
        X.objectsNotNull(this.mediaPlayer, path);
        synchronized (this.mediaPlayer) {
            String filePath = X.appendExternalStorageDirectory(File.separator, path, ".wav");
            Log.v("File path:", filePath);
            this.mediaPlayer.reset();
            this.mediaPlayer.setDataSource(filePath);
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
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
}
