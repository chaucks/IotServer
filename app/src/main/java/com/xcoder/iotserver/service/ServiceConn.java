package com.xcoder.iotserver.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Service connection
 *
 * @author Chuck lee
 * @date 2019-06-11
 */
public class ServiceConn implements ServiceConnection {

    /**
     * Class name
     */
    private String className;

    /**
     * flags
     */
    private int flags;

    /**
     * Class
     */
    private Class clazz;

    public ServiceConn(String className, int flags) {
        this.className = className;
        this.flags = flags;
    }

    public ServiceConn(final Class clazz, int flags) {
        this(clazz.getName(), flags);
        this.clazz = clazz;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 绑定ServiceConnection
     *
     * @param context context
     */
    public final void bindService(final Context context) {
        final Intent intent = new Intent();
        intent.setClassName(context, this.className);
        context.bindService(intent, this, this.flags);
    }
}
