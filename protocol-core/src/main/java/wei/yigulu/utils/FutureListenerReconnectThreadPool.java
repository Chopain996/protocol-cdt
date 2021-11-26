package wei.yigulu.utils;

import wei.yigulu.netty.BaseProtocolBuilder;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Netty future的侦听器连接线程池（定时任务池）
 *
 * @author xiuwei
 * @date 2021/11/22
 */
public class FutureListenerReconnectThreadPool {

    private static class LazyHolder {
        private static final FutureListenerReconnectThreadPool INSTANCE = new FutureListenerReconnectThreadPool();
    }

    private FutureListenerReconnectThreadPool() {
    }

    private Map<BaseProtocolBuilder, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    public static final FutureListenerReconnectThreadPool getInstance() {
        return LazyHolder.INSTANCE;
    }

    ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);


    public ScheduledFuture submitReconnectJob(BaseProtocolBuilder protocolBuilder, Runnable command) {
        return submitReconnectJob(protocolBuilder, command, 5);
    }

    public ScheduledFuture submitReconnectJob(BaseProtocolBuilder protocolBuilder, Runnable command, int delaySecond) {
        synchronized (protocolBuilder) {
            protocolBuilder.getLog().info("{},添加延时重连任务",protocolBuilder.getBuilderId());
            if (this.scheduledFutureMap.containsKey(protocolBuilder)) {
                ScheduledFuture f = this.scheduledFutureMap.get(protocolBuilder);
                //线程池内有客户端对应的定时任务线程
                if (!f.isDone() || !f.isCancelled()) {
                    //如果之前提交的定时任务未执行完毕
                    f.cancel(true);
                }
            }
            this.scheduledFutureMap.put(protocolBuilder, pool.schedule(command, delaySecond, TimeUnit.SECONDS));
        }
        return this.scheduledFutureMap.get(protocolBuilder);
    }
}