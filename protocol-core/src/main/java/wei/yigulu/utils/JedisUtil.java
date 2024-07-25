package wei.yigulu.utils;

import redis.clients.jedis.Jedis;

import static wei.yigulu.utils.ConfigRead.configMap;

public class JedisUtil {

    public static Jedis jedis;
    private int count=1;
    public JedisUtil(){
        getJedis();
    }

    public void getJedis() {
        jedis = new Jedis(configMap.get("redisHost").toString(), Integer.parseInt(configMap.get("redisPort").toString()));
        jedis.auth(configMap.get("redisPass").toString());
    }

    public void setValue(String json_string) {
        jedis.set(String.valueOf(count),json_string);
        count++;
    }

}
