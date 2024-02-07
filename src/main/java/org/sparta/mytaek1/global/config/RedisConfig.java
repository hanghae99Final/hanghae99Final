package org.sparta.mytaek1.global.config;

import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisConfig {
    @Getter
    private static final RedissonClient redissonClient = createRedissonClient();

    private static RedissonClient createRedissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

}