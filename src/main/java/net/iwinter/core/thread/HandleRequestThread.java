package net.iwinter.core.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/19 10:39
 */
public class HandleRequestThread implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        new Thread(r).start();
    }
}
