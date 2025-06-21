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
package org.dubhe.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.dubhe.gateway.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @description 网关健康检查处理器
 * @date 2024-01-01
 */
@Component
@Slf4j
public class HealthHandler {

    @Autowired
    private HealthService healthService;

    /**
     * 基础健康检查
     */
    public Mono<ServerResponse> health(ServerRequest request) {
        log.debug("执行基础健康检查");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(healthService.getBasicHealth()));
    }

    /**
     * 详细健康检查
     */
    public Mono<ServerResponse> detailedHealth(ServerRequest request) {
        log.debug("执行详细健康检查");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(healthService.getDetailedHealth()));
    }

    /**
     * 系统信息检查
     */
    public Mono<ServerResponse> systemInfo(ServerRequest request) {
        log.debug("获取系统信息");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(healthService.getSystemInfo()));
    }
}