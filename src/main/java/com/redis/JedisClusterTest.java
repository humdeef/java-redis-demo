package com.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/15.
 */
public class JedisClusterTest {
    public static void main(String[] args) throws IOException {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(2);

        HostAndPort hp0 = new HostAndPort("192.168.25.156", 7000);
        HostAndPort hp1 = new HostAndPort("192.168.25.156", 7001);
        HostAndPort hp2 = new HostAndPort("192.168.25.156", 7002);
        HostAndPort hp3 = new HostAndPort("192.168.25.158", 7000);
        HostAndPort hp4 = new HostAndPort("192.168.25.158", 7001);
        HostAndPort hp5 = new HostAndPort("192.168.25.158", 7002);

        Set<HostAndPort> hps = new HashSet<HostAndPort>();
        hps.add(hp0);
        hps.add(hp1);
        hps.add(hp2);
        hps.add(hp3);
        hps.add(hp4);
        hps.add(hp5);

        // 超时，最大的转发数，最大链接数，最小链接数都会影响到集群
        JedisCluster jedisCluster = new JedisCluster(hps, 5000, 10, config);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            jedisCluster.set("mn" + i, "n" + i);
        }
        long end = System.currentTimeMillis();

        System.out.println("Simple  @ Sharding Set : " + (end - start) / 10000);

        for (int i = 0; i < 2; i++) {
            System.out.println("第"+(i+1)+"个 value is: "+jedisCluster.get("mn" + i));
        }

//        jedisCluster.close();

    }
}
