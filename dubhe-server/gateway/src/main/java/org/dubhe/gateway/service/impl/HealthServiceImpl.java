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
package org.dubhe.gateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dubhe.gateway.service.HealthService;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 网关健康检查服务实现类
 * @date 2024-01-01
 */
@Service
@Slf4j
public class HealthServiceImpl implements HealthService {

    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private static final String STATUS_UNKNOWN = "UNKNOWN";

    @Override
    public Map<String, Object> getBasicHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", STATUS_UP);
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("application", "Dubhe Gateway Service");
        health.put("version", "1.0.0");
        health.put("port", System.getProperty("server.port", "8800"));
        return health;
    }

    @Override
    public Map<String, Object> getDetailedHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // 基础信息
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("application", "Dubhe Gateway Service");
        health.put("version", "1.0.0");
        health.put("port", System.getProperty("server.port", "8800"));
        
        // 各组件状态
        Map<String, Object> components = new HashMap<>();
        components.put("system", getSystemInfo());
        components.put("gateway", getGatewayInfo());
        
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

    /**
     * 获取网关特定信息
     * @return 网关信息
     */
    private Map<String, Object> getGatewayInfo() {
        Map<String, Object> gatewayInfo = new HashMap<>();
        
        try {
            gatewayInfo.put("status", STATUS_UP);
            gatewayInfo.put("type", "Spring Cloud Gateway");
            gatewayInfo.put("reactive", true);
            gatewayInfo.put("webflux", true);
            
            // 网关配置信息
            Map<String, Object> configInfo = new HashMap<>();
            configInfo.put("profiles", System.getProperty("spring.profiles.active", "unknown"));
            configInfo.put("port", System.getProperty("server.port", "8800"));
            
            gatewayInfo.put("config", configInfo);
            
        } catch (Exception e) {
            log.error("获取网关信息失败", e);
            gatewayInfo.put("status", STATUS_UNKNOWN);
            gatewayInfo.put("error", e.getMessage());
        }
        
        gatewayInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return gatewayInfo;
    }
}