# common-module

一个基于 Spring Boot 3.x 的企业级公共业务能力沉淀项目，提供开箱即用的微服务基础模块和通用能力支持。

---

## 📖 项目简介

`common-module` 是一个多模块的 Java 企业级应用框架，旨在为企业级应用提供可复用的公共业务模块和基础能力。项目采用模块化设计，包含了常见的**用户管理、认证授权、安全控制、支付处理、文件存储、对象存储、AI集成、消息通信**等业务场景，开发者可以直接使用或在此基础上快速扩展，避免重复造轮子。

### 核心价值

- 🎯 **开箱即用**：提供完整的业务模块实现，快速集成到新项目
- 🔧 **模块化设计**：按需引入，灵活组合，降低耦合度
- 🚀 **微服务友好**：作为微服务项目的底座，方便扩展和拆分
- 📦 **统一标准**：统一的基础能力实现，提升代码质量和可维护性
- 🤖 **AI 就绪**：内置 Spring AI 和 LangChain4j 集成，支持智能对话和语音合成

---

## 🏗 项目架构

```
common-module/
├── common/                      # 核心通用模块
├── security/                    # 安全认证核心模块
├── security-jwt/                # JWT 令牌支持
├── security-oauth2-server/      # OAuth2 授权服务器
├── security-oauth2-message-resource/  # OAuth2 资源服务器
├── security-apikeys/            # API Key 认证
├── security-wechat-qrcode-login/ # 微信扫码登录
├── security-mqtt/               # MQTT 安全认证
├── user-service/                # 用户服务模块
├── oss/                         # 对象存储服务（多平台支持）
├── filestore/                   # 文件存储管理
├── payment/                     # 支付核心模块
├── payment-alipay/              # 支付宝支付集成
├── ai-chat/                     # AI 聊天集成（Spring AI）
├── intelligent-chat/            # 智能聊天服务
├── echovoid-tts/                  # 语音合成服务
├── langchain4j-samples/         # LangChain4j 示例
├── echovoid-chat-common/          # 聊天通用模块
├── order-service/               # 订单服务示例
├── product-service/             # 产品服务示例
├── common-gateway/              # API 网关
├── multi-db-samples/            # 多数据源示例
├── httpclient-call/             # HTTP 客户端调用封装
├── mcp-weather/                 # MCP 天气服务示例
├── domain-server/               # 域名服务
└── starter/                     # 启动器模块（整合多个模块）
```

---

## 🛠 技术栈

### 核心框架

| 技术 | 版本       | 说明 |
|------|----------|------|
| Spring Boot | 3.4.4    | 核心框架 |
| Spring Cloud | 2024.0.1 | 微服务框架 |
| Spring AI | 1.0.0    | AI 集成框架 |
| MyBatis Plus | 3.5.10.1 | ORM 框架 |
| Gradle | 8.14.3   | 构建工具 |

### 数据库与存储

- **PostgreSQL**：主要数据库支持
- **Redis**：缓存和会话存储
- **MyBatis Plus**：ORM 持久层框架
- **多数据源支持**：ShardingSphere 示例

### 安全认证

- **JWT**（JSON Web Token）：无状态认证
- **OAuth2**：标准授权协议（服务器端 + 资源服务器）
- **API Key**：API 密钥认证
- **微信扫码登录**：第三方登录集成
- **MQTT 安全**：物联网安全认证

### 对象存储支持

- **阿里云 OSS**
- **七牛云**
- **MinIO**（自建对象存储）
- **AWS S3**
- **本地文件系统**

### AI 能力

- **Spring AI**：OpenAI、Anthropic 等模型集成
- **LangChain4j**：AI 应用开发框架
- **语音合成**：TTS 服务
- **智能对话**：聊天记忆和上下文管理

### 其他特性

- **邮件服务**：支持 SMTP 邮件发送
- **短信服务**：集成云通信短信 SDK
- **二维码生成**：ZXing 支持
- **Prometheus**：指标监控
- **HTTP 客户端**：Retrofit2 封装

---

## 📦 核心模块说明

### 1. common（核心通用模块）

提供通用的工具类、DTO、异常处理、验证器等基础设施。

**主要功能：**
- 统一响应格式（`RestData`）
- 分页支持（`PageDTO`、`CursorPageDTO`）
- 全局异常处理（`GlobalExceptionHandler`）
- JSON 工具类（`JsonUtils`）
- 邮件发送服务（`EmailSender`）
- 短信发送服务（`SmsSender`）
- 二维码生成工具
- RSA 加密工具
- IP 查询 API
- 请求追踪过滤器

### 2. security（安全认证核心）

提供基于 Spring Security 的安全认证框架。

**主要功能：**
- JWT 认证过滤器
- 统一登录服务（`LoginService`）
- 安全配置聚合器
- 基于角色的访问控制（RBAC）
- JSON 格式的认证响应处理
- 支持自定义安全配置提供者

### 3. security-jwt（JWT 支持）

JWT 令牌的创建、解析和刷新功能。

**主要功能：**
- Token 创建和解析
- Access Token 和 Refresh Token 支持
- 自定义令牌属性
- 令牌过期管理

### 4. security-oauth2-server（OAuth2 授权服务器）

基于 Spring Security OAuth2 Authorization Server 的实现。

**主要功能：**
- 授权码模式
- 客户端凭证模式
- 资源所有者密码凭证模式
- 客户端注册管理
- 授权码和令牌存储（支持 Redis/数据库）

### 5. oss（对象存储服务）

支持多平台对象存储的统一抽象。

**支持的平台：**
- 阿里云 OSS
- 七牛云
- MinIO
- AWS S3

**主要功能：**
- 文件上传
- 文件下载
- 文件删除
- 预签名 URL 生成
- 文件元数据管理

### 6. filestore（文件存储管理）

完整的文件管理系统，支持文件权限、分享等功能。

**主要功能：**
- 文件上传（支持分片上传）
- 文件下载（支持权限控制）
- 文件元数据管理
- 文件分享（临时链接）
- 文件访问控制（ACL）
- 支持多种存储后端（OSS、本地文件系统）

### 7. payment（支付核心模块）

支付业务的抽象和统一接口。

**主要功能：**
- 支付订单管理
- 支付回调处理
- 支付状态查询
- 退款处理

### 8. payment-alipay（支付宝支付）

支付宝支付的具体实现。

**主要功能：**
- 支付宝 SDK 集成
- 支付下单
- 支付回调
- 订单查询
- 退款处理

### 9. ai-chat（AI 聊天集成）

基于 Spring AI 的智能对话服务。

**主要功能：**
- 多模型支持（OpenAI、Claude 等）
- 对话记忆管理（数据库持久化）
- 聊天历史查询
- 流式响应支持
- 工具调用（Function Calling）

### 10. intelligent-chat（智能聊天服务）

支持多个 AI 服务商的智能聊天实现。

**支持的服务商：**
- 阿里云 DashScope
- OpenAI
- 其他兼容服务商

### 11. echovoid-tts（语音合成）

文本转语音（TTS）服务。

### 12. langchain4j-samples（LangChain4j 示例）

LangChain4j 框架的使用示例和最佳实践。

### 13. user-service（用户服务）

用户管理服务模块。

**主要功能：**
- 用户注册/登录
- 用户信息管理
- 角色权限管理
- MyBatis Plus 数据访问

### 14. common-gateway（API 网关）

基于 Spring Cloud Gateway 的 API 网关。

**主要功能：**
- 路由转发
- JWT 认证
- 请求过滤
- 负载均衡

### 15. starter（启动器模块）

整合了多个核心模块的启动器，可直接运行。

**集成的模块：**
- common
- security
- security-wechat-qrcode-login
- product-service
- order-service
- oss
- filestore
- payment
- Prometheus 监控

---

## 🚀 快速开始

### 环境要求

- **JDK**: 21+
- **Gradle**: 8.0+
- **PostgreSQL**: 12+
- **Redis**: 6.0+（可选，用于缓存和会话）

### 克隆项目

```bash
git clone https://github.com/penncollins317/common-module.git
cd common-module
```

### 构建项目

```bash
# 构建所有模块
./gradlew build

# 跳过测试构建
./gradlew build -x test

# 构建并安装到本地仓库
./gradlew build publishToMavenLocal
```

### 运行示例

启动 `starter` 模块作为完整示例：

```bash
cd starter
./gradlew bootRun
```

### 配置数据库

1. 创建 PostgreSQL 数据库
2. 执行对应模块的 SQL 脚本（位于 `postgresql/` 目录）
3. 配置数据库连接信息（见配置说明）

---

## ⚙️ 配置说明

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/echovoid_db
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
```

### JWT 配置

```yaml
echovoid:
  jwt:
    secret: your-jwt-secret-key
    issuer: http://your-domain.com
    expire: 2592000  # 令牌过期时间（秒）
```

### OSS 配置示例（七牛云）

```yaml
echovoid:
  oss:
    type: qiniu
    access-key: your-access-key
    secret-key: your-secret-key
    endpoint: your-endpoint.com
    bucket-name: your-bucket
    secret: false  # 是否使用私有存储
```

### 安全配置

```yaml
echovoid:
  security:
    wechat:
      biz:
        appid: your-wechat-appid
        app-secret: your-wechat-secret
        token: your-wechat-token
        aes-key: your-aes-key
```

### 邮件配置

```yaml
spring:
  mail:
    host: smtp.qq.com
    port: 465
    username: your-email@qq.com
    password: your-smtp-password
    properties:
      mail:
        smtp:
          ssl:
            enable: true
          auth: true
```

---

## 📚 模块使用指南

### 引入依赖

在项目的 `build.gradle` 中添加需要的模块依赖：

```gradle
dependencies {
    // 引入核心通用模块
    implementation project(':common')
    
    // 引入安全认证模块
    implementation project(':security')
    implementation project(':security-jwt')
    
    // 引入用户服务
    implementation project(':user-service')
    
    // 引入对象存储
    implementation project(':oss')
    
    // 引入支付模块
    implementation project(':payment')
    implementation project(':payment-alipay')
    
    // 引入 AI 聊天
    implementation project(':ai-chat')
}
```

### 使用安全认证

```java
@RestController
@RequestMapping("/api")
public class MyController {
    
    @GetMapping("/profile")
    public RestData<User> getProfile(Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        // 使用 userId 获取用户信息
        return RestData.ok(userService.getById(userId));
    }
    
    @HasRole("ADMIN")
    @GetMapping("/admin")
    public RestData<String> adminOnly() {
        return RestData.ok("Admin only");
    }
}
```

### 使用对象存储

```java
@Autowired
private OssService ossService;

public void uploadFile(MultipartFile file) {
    FileUploadDTO uploadDTO = FileUploadDTO.builder()
        .file(file)
        .bucket("my-bucket")
        .build();
    
    FileMetaDTO result = ossService.upload(uploadDTO);
    String fileUrl = result.getUrl();
}
```

### 使用 AI 聊天

```java
@Autowired
private ChatClient chatClient;

public String chat(String message) {
    return chatClient.call(message);
}
```

---

## 🔧 开发指南

### 项目结构规范

- 每个模块应包含自动配置类（`*AutoConfig.java`）
- 使用 `@ComponentScan` 扫描本模块组件
- 配置文件位于 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

### 添加新模块

1. 在 `settings.gradle` 中添加模块
2. 在 `build.gradle` 中配置模块依赖
3. 创建模块的自动配置类
4. 编写单元测试

### 代码规范

- 使用 Lombok 简化代码
- 统一异常处理（使用 `ServiceException`）
- 统一响应格式（使用 `RestData`）
- 添加必要的注释和文档

---

## 📝 模块依赖关系

```
common (核心)
  ├── security (依赖 common)
  │   ├── security-jwt (被 security 依赖)
  │   ├── security-oauth2-server (依赖 security)
  │   ├── security-apikeys (依赖 security)
  │   ├── security-wechat-qrcode-login (依赖 security)
  │   └── security-mqtt (依赖 security)
  ├── user-service (依赖 common)
  ├── oss (依赖 common, security)
  ├── filestore (依赖 common, security, user-service, oss)
  ├── payment (依赖 common)
  ├── payment-alipay (依赖 payment, security)
  ├── ai-chat (依赖 common, security, echovoid-chat-common)
  └── starter (整合多个模块)
```

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 贡献规范

- 代码符合项目规范
- 添加必要的测试用例
- 更新相关文档
- 提交信息清晰明了

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

## 👥 作者

- **Penn Collins** - *项目维护者* - penncollins317@gmail.com

---

## 🙏 致谢

感谢所有为本项目做出贡献的开发者和开源社区！

---

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue: [GitHub Issues](https://github.com/penncollins317/common-module/issues)
- 邮箱: penncollins317@gmail.com

---

**⭐ 如果这个项目对你有帮助，请给个 Star！**
