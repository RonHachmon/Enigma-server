package app.mini_apps.agent.utils.threads;

import java.util.concurrent.ThreadFactory;

public class DaemonThread implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}
