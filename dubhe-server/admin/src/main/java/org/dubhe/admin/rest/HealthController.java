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
package org.dubhe.admin.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dubhe.admin.service.HealthService;
import org.dubhe.biz.base.constant.Permissions;
import org.dubhe.biz.base.vo.DataResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description 健康检查控制器
 * @date 2024-01-01
 */
@Api(tags = "系统：健康检查")
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @Autowired
    private HealthService healthService;

    @ApiOperation("基础健康检查")
    @GetMapping
    public DataResponseBody<Map<String, Object>> health() {
        log.debug("执行基础健康检查");
        return new DataResponseBody<>(healthService.getBasicHealth());
    }

    @ApiOperation("详细健康检查")
    @GetMapping("/detailed")
    @PreAuthorize(Permissions.SYSTEM_LOG)
    public DataResponseBody<Map<String, Object>> detailedHealth() {
        log.debug("执行详细健康检查");
        return new DataResponseBody<>(healthService.getDetailedHealth());
    }

    @ApiOperation("数据库连接检查")
    @GetMapping("/database")
    @PreAuthorize(Permissions.SYSTEM_LOG)
    public DataResponseBody<Map<String, Object>> databaseHealth() {
        log.debug("执行数据库连接检查");
        return new DataResponseBody<>(healthService.getDatabaseHealth());
    }

    @ApiOperation("Redis连接检查")
    @GetMapping("/redis")
    @PreAuthorize(Permissions.SYSTEM_LOG)
    public DataResponseBody<Map<String, Object>> redisHealth() {
        log.debug("执行Redis连接检查");
        return new DataResponseBody<>(healthService.getRedisHealth());
    }

    @ApiOperation("系统信息检查")
    @GetMapping("/system")
    @PreAuthorize(Permissions.SYSTEM_LOG)
    public DataResponseBody<Map<String, Object>> systemInfo() {
        log.debug("获取系统信息");
        return new DataResponseBody<>(healthService.getSystemInfo());
    }
}