package com.jcaa.appcrudusers.entrypoint.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class AppExecutors {
    private final Executor diskIO;
    private final Executor mainThread;

    public AppExecutors(final Executor diskIO, final Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public AppExecutors() {
        this(Executors.newFixedThreadPool(3), new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static final class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(final Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
