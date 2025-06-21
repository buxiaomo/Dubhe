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
package org.dubhe.gateway.config;

import org.dubhe.gateway.handler.HealthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @description 健康检查路由配置
 * @date 2024-01-01
 */
@Configuration
public class HealthRouterConfig {

    /**
     * 健康检查路由配置
     * @param healthHandler 健康检查处理器
     * @return 路由函数
     */
    @Bean
    public RouterFunction<ServerResponse> healthRoutes(HealthHandler healthHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/health")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
                        healthHandler::health)
                .andRoute(RequestPredicates.GET("/health/detailed")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
                        healthHandler::detailedHealth)
                .andRoute(RequestPredicates.GET("/health/system")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), 
                        healthHandler::systemInfo);
    }
}