package me.js.rest.common.conifg;

import lombok.extern.slf4j.Slf4j;
import me.js.rest.events.dto.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Test
    public void test() {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Result result = new Result();
        result.setName("jongseon");
        vop.set("key", result);
    }
}