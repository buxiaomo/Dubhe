# Gateway 健康检查功能

## 概述

本模块为 Dubhe Gateway 服务提供了完整的健康检查功能，支持基础健康状态、详细健康信息和系统信息查询。

## 功能特性

- **基础健康检查**: 提供服务基本状态信息
- **详细健康检查**: 提供系统组件详细状态
- **系统信息查询**: 提供JVM、内存、系统属性等信息
- **响应式支持**: 基于Spring WebFlux的非阻塞实现
- **标准化接口**: 遵循Spring Boot Actuator规范

## API 端点

### 1. 基础健康检查

```
GET /health
```

**响应示例:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00",
  "application": "Dubhe Gateway Service",
  "version": "1.0.0",
  "port": "8800"
}
```

### 2. 详细健康检查

```
GET /health/detailed
```

**响应示例:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00",
  "application": "Dubhe Gateway Service",
  "version": "1.0.0",
  "port": "8800",
  "components": {
    "system": {
      "status": "UP",
      "jvm": {...},
      "memory": {...},
      "system": {...}
    },
    "gateway": {
      "status": "UP",
      "type": "Spring Cloud Gateway",
      "reactive": true,
      "webflux": true
    }
  }
}
```

### 3. 系统信息查询

```
GET /health/system
```

**响应示例:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T10:00:00",
  "jvm": {
    "name": "OpenJDK 64-Bit Server VM",
    "version": "11.0.16+8",
    "vendor": "Eclipse Adoptium",
    "uptime": 123456
  },
  "memory": {
    "heapUsed": 134217728,
    "heapMax": 1073741824,
    "heapCommitted": 268435456,
    "nonHeapUsed": 67108864,
    "nonHeapMax": -1
  },
  "system": {
    "osName": "Mac OS X",
    "osVersion": "13.0",
    "osArch": "x86_64",
    "javaVersion": "11.0.16",
    "javaVendor": "Eclipse Adoptium"
  }
}
```

## Spring Boot Actuator 端点

除了自定义健康检查端点外，还可以使用标准的 Actuator 端点：

```
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

## 配置说明

### application.yml 配置

```yaml
# 管理端点配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
      base-path: /actuator
  endpoint:
    health:
      show-details: when-authorized
      show-components: always
  health:
    defaults:
      enabled: true

# 自定义健康检查配置
dubhe:
  gateway:
    health:
      enabled: true
      detailed-enabled: true
      system-info-enabled: true
```

## 部署和监控

### Docker 健康检查

在 Dockerfile 中添加健康检查：

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8800/health || exit 1
```

### Kubernetes 健康检查

在 Kubernetes 部署文件中配置：

```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
      - name: gateway
        livenessProbe:
          httpGet:
            path: /health
            port: 8800
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8800
          initialDelaySeconds: 5
          periodSeconds: 5
```

### 监控集成

可以将健康检查端点集成到监控系统中：

- **Prometheus**: 通过 `/actuator/metrics` 端点
- **Grafana**: 创建健康状态仪表板
- **Alertmanager**: 配置健康状态告警

## 故障排查

### 常见问题

1. **端点无法访问**
   - 检查端口配置是否正确
   - 确认防火墙设置
   - 验证路由配置

2. **健康检查返回DOWN状态**
   - 查看详细健康检查信息
   - 检查系统资源使用情况
   - 查看应用日志

3. **响应时间过长**
   - 检查系统负载
   - 优化健康检查逻辑
   - 调整超时配置

### 日志配置

```yaml
logging:
  level:
    org.dubhe.gateway: DEBUG
    org.springframework.cloud.gateway: INFO
```

## 扩展开发

### 添加自定义健康指标

1. 实现 `HealthIndicator` 接口
2. 注册为 Spring Bean
3. 在详细健康检查中集成

### 集成外部依赖检查

- 数据库连接检查
- Redis 连接检查
- 外部服务可用性检查
- 消息队列连接检查

## 安全考虑

- 生产环境建议限制详细信息的访问权限
- 可以通过网关路由规则控制访问
- 考虑添加认证和授权机制