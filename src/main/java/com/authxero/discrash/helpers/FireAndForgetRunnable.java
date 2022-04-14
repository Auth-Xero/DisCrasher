package com.authxero.discrash.helpers;

public class FireAndForgetRunnable implements Runnable {

    private RunnableFunction runnableFunction;

    public FireAndForgetRunnable(RunnableFunction rf){
        this.runnableFunction = rf;
    }

    @Override
    public void run() {
        try {
            runnableFunction.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
