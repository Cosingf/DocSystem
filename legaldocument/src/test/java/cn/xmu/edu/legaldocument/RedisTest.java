package cn.xmu.edu.legaldocument;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LegaldocumentApplication.class)
//@Sql("/init-schema.sql")
public class RedisTest {
    @Autowired
    private JedisPool jedisPool;


    @Test
    public void testRedisConnection() {
        Jedis jedis=jedisPool.getResource();
        jedis.flushDB();

        // get set
        jedis.set("hello", "world");
        System.out.println(jedis.get("hello"));
        jedis.rename("hello", "newhello");
        System.out.println(jedis.get("newhello"));
        jedis.setex("hello2", 1800, "world");
    }
}
