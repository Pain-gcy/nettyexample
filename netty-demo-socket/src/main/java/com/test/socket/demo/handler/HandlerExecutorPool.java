package com.test.socket.demo.handler;

import java.util.concurrent.*;

/**
 * @author guochunyuan
 * @create on  2018-09-18 9:41
 */
public class HandlerExecutorPool {
    private ExecutorService executor;

    public HandlerExecutorPool(int maxSize, int queueSize) {
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }

}
