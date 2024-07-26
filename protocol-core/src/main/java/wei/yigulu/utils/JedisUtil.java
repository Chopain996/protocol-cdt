package wei.yigulu.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.atomic.AtomicInteger;

public class JedisUtil {

    private static final JedisPool pool;
    private static AtomicInteger count =new AtomicInteger(0);
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50); // 最大连接数
        String host = ConfigRead.configMap.get("redisHost").toString();
        int port = Integer.parseInt(ConfigRead.configMap.get("redisPort").toString());
        String password = ConfigRead.configMap.get("redisPass").toString();
        // 创建pool时直接提供host, port和password
        pool = new JedisPool(poolConfig, host, port, 0, password);
    }

    public String setValue(String jsonString) {
        // 使用try-with-resources确保Jedis实例被正确关闭
        try (Jedis jedis = pool.getResource()) {
            // 使用Redis的 INCR 命令来自动管理键值
            String key = String.valueOf(count.getAndIncrement());
            jedis.set(key, jsonString);
            return key;
        }
    }
}
