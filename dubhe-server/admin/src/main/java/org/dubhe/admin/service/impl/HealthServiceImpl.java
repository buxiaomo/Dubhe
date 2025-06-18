/**
 * Copyright 2020 Tianshu AI Platform. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================================================
 */
package org.dubhe.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dubhe.admin.service.HealthService;
import org.dubhe.biz.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 健康检查服务实现类
 * @date 2024-01-01
 */
@Service
@Slf4j
public class HealthServiceImpl implements HealthService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisUtils redisUtils;

    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private static final String STATUS_UNKNOWN = "UNKNOWN";

    @Override
    public Map<String, Object> getBasicHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", STATUS_UP);
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("application", "Dubhe Admin Service");
        health.put("version", "1.0.0");
        return health;
    }

    @Override
    public Map<String, Object> getDetailedHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // 基础信息
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("application", "Dubhe Admin Service");
        health.put("version", "1.0.0");
        
        // 各组件状态
        Map<String, Object> components = new HashMap<>();
        components.put("database", getDatabaseHealth());
        components.put("redis", getRedisHealth());
        components.put("system", getSystemInfo());
        
        health.put("components", components);
        
        // 整体状态判断
        boolean allHealthy = components.values().stream()
                .allMatch(component -> {
                    if (component instanceof Map) {
                        return STATUS_UP.equals(((Map<?, ?>) component).get("status"));
                    }
                    return false;
                });
        
        health.put("status", allHealthy ? STATUS_UP : STATUS_DOWN);
        
        return health;
    }

    @Override
    public Map<String, Object> getDatabaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                dbHealth.put("status", STATUS_UP);
                dbHealth.put("database", connection.getMetaData().getDatabaseProductName());
                dbHealth.put("version", connection.getMetaData().getDatabaseProductVersion());
                dbHealth.put("url", connection.getMetaData().getURL());
            } else {
                dbHealth.put("status", STATUS_DOWN);
                dbHealth.put("error", "数据库连接无效");
            }
        } catch (SQLException e) {
            log.error("数据库健康检查失败", e);
            dbHealth.put("status", STATUS_DOWN);
            dbHealth.put("error", e.getMessage());
        }
        
        dbHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return dbHealth;
    }

    @Override
    public Map<String, Object> getRedisHealth() {
        Map<String, Object> redisHealth = new HashMap<>();
        
        try {
            // 测试Redis连接
            String testKey = "health_check_" + System.currentTimeMillis();
            String testValue = "test";
            
            redisUtils.set(testKey, testValue, 10);
            String retrievedValue = (String) redisUtils.get(testKey);
            redisUtils.del(testKey);
            
            if (testValue.equals(retrievedValue)) {
                redisHealth.put("status", STATUS_UP);
                redisHealth.put("message", "Redis连接正常");
            } else {
                redisHealth.put("status", STATUS_DOWN);
                redisHealth.put("error", "Redis读写测试失败");
            }
        } catch (Exception e) {
            log.error("Redis健康检查失败", e);
            redisHealth.put("status", STATUS_DOWN);
            redisHealth.put("error", e.getMessage());
        }
        
        redisHealth.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return redisHealth;
    }

    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        try {
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            
            // JVM信息
            Map<String, Object> jvmInfo = new HashMap<>();
            jvmInfo.put("name", runtimeBean.getVmName());
            jvmInfo.put("version", runtimeBean.getVmVersion());
            jvmInfo.put("vendor", runtimeBean.getVmVendor());
            jvmInfo.put("uptime", runtimeBean.getUptime());
            
            // 内存信息
            Map<String, Object> memoryInfo = new HashMap<>();
            memoryInfo.put("heapUsed", memoryBean.getHeapMemoryUsage().getUsed());
            memoryInfo.put("heapMax", memoryBean.getHeapMemoryUsage().getMax());
            memoryInfo.put("heapCommitted", memoryBean.getHeapMemoryUsage().getCommitted());
            memoryInfo.put("nonHeapUsed", memoryBean.getNonHeapMemoryUsage().getUsed());
            memoryInfo.put("nonHeapMax", memoryBean.getNonHeapMemoryUsage().getMax());
            
            // 系统属性
            Map<String, Object> systemProps = new HashMap<>();
            systemProps.put("osName", System.getProperty("os.name"));
            systemProps.put("osVersion", System.getProperty("os.version"));
            systemProps.put("osArch", System.getProperty("os.arch"));
            systemProps.put("javaVersion", System.getProperty("java.version"));
            systemProps.put("javaVendor", System.getProperty("java.vendor"));
            
            systemInfo.put("status", STATUS_UP);
            systemInfo.put("jvm", jvmInfo);
            systemInfo.put("memory", memoryInfo);
            systemInfo.put("system", systemProps);
            
        } catch (Exception e) {
            log.error("获取系统信息失败", e);
            systemInfo.put("status", STATUS_UNKNOWN);
            systemInfo.put("error", e.getMessage());
        }
        
        systemInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return systemInfo;
    }
}