package wei.yigulu.utils;

import redis.clients.jedis.Jedis;

public class JedisUtil {

    public static Jedis jedis;
    private int count=1;
    public JedisUtil(){
        getJedis();
    }

    public void getJedis() {
        jedis = new Jedis("192.168.232.128", 6379);
        jedis.auth("123456");
    }

    public void setValue(String json_string) {
        jedis.set(String.valueOf(count),json_string);
        count++;
    }

}
