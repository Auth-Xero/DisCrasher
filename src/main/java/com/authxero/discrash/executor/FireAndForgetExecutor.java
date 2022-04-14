package com.authxero.discrash.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//This is probably unnecessary but what you gonna do
public class FireAndForgetExecutor {

    private static final Executor executor = Executors.newFixedThreadPool(5);
    public static FireAndForgetExecutor instance = null;

    public FireAndForgetExecutor() {
        instance = this;
    }

    public void exec(Runnable cmd) {
        executor.execute(cmd);
    }
}
