# Jasi Modern - 现代化补丁工具

基于 Jasi Patcher v4.6 技术的现代化 Android 应用重构项目。

## 项目概述

Jasi Modern 是对经典 Android 应用破解工具 Jasi Patcher v4.6 的全面重构，采用现代化技术栈和架构设计。

### 原版信息

- **原版**: Jasi Patcher v4.6
- **作者**: Jaspreet Singh (Jasi2169)
- **发布日期**: 2019年1月15日
- **状态**: 已停止维护 7+ 年
- **包名**: com.android.vendinf
- **核心类**: zone.jasi2169.patcher.JasiPatch

## 技术栈对比

| 组件 | Jasi v4.6 (原版) | Jasi Modern (新) |
|------|------------------|------------------|
| 语言 | Java | Kotlin |
| SDK min | 14 (Android 4.0) | 24 (Android 7.0) |
| SDK target | 27 (Android 8.1) | 34 (Android 14) |
| UI框架 | XML Layout | ViewBinding + Material Design 3 |
| 架构 | Activity + Service | MVVM + LiveData |
| 异步 | AsyncTask | Kotlin Coroutines |
| Xposed | 原生支持 | LSPosed 兼容 |

## 核心功能

### 1. 计费服务模拟 (Billing Service)
- 完全模拟 Google Play In-App Billing API
- 支持 v3 及以上版本
- 所有方法返回成功状态码 0
- 支持购买、消耗、查询历史

### 2. 许可证服务伪造 (Licensing Service)
- 模拟 Google Play License 验证
- 生成伪造的许可证令牌
- 5年有效期（可扩展）
- Base64 编码签名

### 3. Xposed 模块支持
- 系统级 API Hook
- Root 检测绕过
- 设备 ID 伪造
- SSL 证书固定绕过
- LSPosed 兼容

### 4. 广告屏蔽
- 内置 Hosts 文件（~47,400 条规则）
- 按类别分类：广告、社交媒体、追踪器
- 支持备份和恢复

### 5. 补丁系统
- 动态 DEX 加载 (DexClassLoader)
- 支持从 SD 卡加载补丁
- 补丁版本管理
- 缓存优化

## 项目结构

```
jasi-modern/
├── app/
│   ├── src/main/
│   │   ├── java/zone/jasimodern/
│   │   │   ├── JasiModernApp.kt       # 应用入口
│   │   │   ├── ui/
│   │   │   │   ├── MainActivity.kt    # 主界面
│   │   │   │   └── fragment/
│   │   │   │       └── SettingsFragment.kt
│   │   │   ├── service/
│   │   │   │   ├── BillingService.kt  # 计费服务
│   │   │   │   ├── LicensingService.kt # 许可证服务
│   │   │   │   ├── PatchManager.kt    # 补丁管理器
│   │   │   │   └── HostsManager.kt    # Hosts管理
│   │   │   ├── xposed/
│   │   │   │   └── XposedModule.kt    # Xposed模块
│   │   │   └── data/
│   │   │       └── PurchaseDatabase.kt # 购买记录
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   └── values/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── assets/
│   └── hosts                          # 广告屏蔽规则
├── .github/workflows/
│   └── build.yml                      # CI/CD
├── build.gradle.kts
├── settings.gradle
└── README.md
```

## 编译构建

### 环境要求
- JDK 17+
- Android SDK 34
- Gradle 8.2

### 构建命令

```bash
# 编译 Release APK
./gradlew assembleRelease

# 安装到设备
./gradlew installRelease

# 清理构建
./gradlew clean
```

### Gradle Wrapper (Termux 环境)

```bash
# 在 Termux 中构建
cd /data/data/com.termux/files/home/jasi-modern
bash build.sh
```

## 安全增强

1. **TLS 1.3 支持** - 所有网络通信使用最新加密协议
2. **Android Keystore** - 敏感数据加密存储
3. **字符串混淆** - 基于调用栈的动态解密
4. **反调试检测** - 检测调试器连接

## 法律声明

**本工具仅供教育和研究用途**

### 合法用途
- 安全研究和教育
- 测试自己开发的应用
- 学习 Android 安全机制
- 逆向工程技术学习

### 禁止用途
- 分发破解应用
- 绕过付费功能
- 侵犯他人知识产权
- 任何违反服务条款的行为

用户需遵守当地法律法规。

## 参考资料

- [原项目分析](https://github.com/len0409/jasi-patcher-analysis)
- [LSPosed 框架](https://github.com/LSPosed/LSPosed)
- [AndroidX 迁移指南](https://developer.android.com/jetpack/androidx/migrate)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**版本**: 1.0.0  
**构建日期**: 2026-09-27  
**分析者**: Hermes AI Agent