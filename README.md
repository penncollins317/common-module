# Common-Module 🚀

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-9.x-blue)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于 **Spring Boot 4.x** 和 **Java 25** 构建的高性能、模块化企业级基础业务框架。它沉淀了通用的业务能力，提供开箱即用的微服务组件，旨在帮助开发者快速构建稳定、可扩展的现代应用程序。

---

## 🌟 核心特性

- 🛡️ **现代化安全架构**：集成 Spring Security 7.0+，支持 OAuth2 授权服务器与微信扫码登录。
- � **多功能文件存储**：统一支付与存储接口，支持阿里云 OSS、七牛云、MinIO 及 AWS S3。
- � **全能支付方案**：内置支付宝和微信支付能力，标准化的支付流程处理。
- 🤖 **AI 能力集成**：深度集成 LangChain4j，提供大模型对话与智能化方案示例。
- 🏗️ **模块化耦合**：精心设计的依赖关系，各模块可独立引用，按需组合。
- 💎 **高性能适配**：针对 Java 25 新特性优化，支持最前沿的技术栈。

---

## 🏗️ 项目架构

项目采用多模块 Gradle 结构，清晰划分业务边界：

```text
common-module/
├── common/                  # 核心通用包：工具类、响应封装、异常处理、JWT 支持
├── security/                # 认证授权核心：基于 Spring Security 的通用配置
├── security-oauth2-server/  # OAuth2 授权服务器：完善的令牌管理与授权流程
├── security-wechat-qrcode/  # 微信集成：支持微信扫码登录业务
├── user-service/            # 用户领域：用户管理、角色权限控制
├── filestore/               # 文件中心：屏蔽底层差异，支持国内外主流对象存储
├── payment/                 # 支付中台：统一支付接口抽象
│   ├── payment-alipay/      # 支付宝实现
│   └── payment-wxpay/       # 微信支付实现
├── langchain4j-samples/     # AI 实验室：LangChain4j 最佳实践示例
├── order-service/           # 示例业务：订单处理逻辑参考
├── product-service/         # 示例业务：产品管理系统参考
└── starter/                 # 聚合启动模块：开箱即用的全量能力集成容器
```

---

## 🛠️ 技术栈清单

### 核心框架
| 组件 | 版本 | 描述 |
| :--- | :--- | :--- |
| **Spring Boot** | `4.0.1` | 现代微服务基座 |
| **Java** | `25` | 长期支持版（前沿特性） |
| **Spring Authorization Server** | `1.5.5` | 标准 OAuth2 授权实现 |
| **MyBatis Plus** | `3.5.16` | 持久层增强工具 |
| **SpringDoc** | `3.0.1` | OpenAPI 3.0 接口文档 |

### 辅助支持
- **数据库**: PostgreSQL, Redis (基于 JDBC & MyBatis Plus)
- **云服务**: Aliyun OSS, Qiniu, AWS S3, MinIO
- **AI**: LangChain4j (`1.10.0`)
- **监控**: Micrometer + Prometheus

---

## 📦 重点模块深挖

### 1. `common` - 稳固的根基
提供所有模块共享的底层能力：
- **统一响应**: `RestData<T>` 规范 API 返回。
- **JWT 支持**: 集成 `nimbus-jose-jwt`，提供高性能 Token 处理。
- **全局异常**: 优雅的 `GlobalExceptionHandler` 捕获。
- **自动化工具**: 深度封装 JSON、RSA、验证器等常用工具。

### 2. `filestore` - 无缝存储切换
一个接口，对接所有：
- 支持**秒传**、**分片上传**。
- 兼容 Aliyun, Qiniu, MinIO, S3 和本地存储。
- 基于策略模式，配置文件即可切换后端。

### 3. `security` 家族 - 极致安全
- **security-oauth2-server**: 提供完整的授权码、密码、客户端凭证模式。
- **wechat-qrcode-login**: 解决复杂的微信公众号/小程序扫码登录流程。

### 4. `langchain4j-samples` - 拥抱 AI
- 集成 OpenAI、Google Custom Search。
- 展示如何将 RAG（检索增强生成）与 Spring 业务深度结合。

---

## 🚀 快速上手

### 环境准备
- **JDK 25**
- **Gradle 9.x**
- **Docker**

### 克隆与编译
```bash
git clone https://github.com/penncollins317/common-module.git
cd common-module

# 编译并发布到本地缓存
./gradlew publishToMavenLocal
```

### 运行 Starter 示例
`starter` 模块集成了所有核心功能，是最佳的学习起点：
```bash
# 修改 starter/src/main/resources/application.yml 中的数据库链接
./gradlew starter:bootRun
```

---

## � 开发规范

1. **自动配置**: 每个子模块应维护自己的 `AutoConfiguration`。
2. **零耦合原则**: 业务模块（如 `order-service`）尽量依赖 `common` 而非其他业务模块。
3. **Lombok**: 强制使用 Lombok 简化 POJO 代码。
4. **API 文档**: 编写 Controller 时请务必配套 `springdoc` 注解。

---

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源。

## 👥 联系作者

- **Penn Collins**
- **Email**: penncollins317@gmail.com
- **GitHub**: [penncollins317](https://github.com/penncollins317)
- **Gitee**: [penncollins317](https://gitee.com/zero6879)

---

**如果这个项目对你有帮助，请给个 ⭐ Star！**
