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
package org.dubhe.admin.service;

import java.util.Map;

/**
 * @description 健康检查服务接口
 * @date 2024-01-01
 */
public interface HealthService {

    /**
     * 获取基础健康状态
     * @return 基础健康信息
     */
    Map<String, Object> getBasicHealth();

    /**
     * 获取详细健康状态
     * @return 详细健康信息
     */
    Map<String, Object> getDetailedHealth();

    /**
     * 获取数据库连接状态
     * @return 数据库健康信息
     */
    Map<String, Object> getDatabaseHealth();

    /**
     * 获取Redis连接状态
     * @return Redis健康信息
     */
    Map<String, Object> getRedisHealth();

    /**
     * 获取系统信息
     * @return 系统信息
     */
    Map<String, Object> getSystemInfo();
}