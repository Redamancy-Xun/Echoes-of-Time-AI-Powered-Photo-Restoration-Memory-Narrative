# 光影溯洄 - 基于GFPGAN的照片修复与故事重现 ✨📸🕰️

光影溯洄是一个融合现代AI技术与情感回忆的创新平台，致力于通过先进的图像处理和人工智能技术，让老照片重焕生机，同时帮助用户挖掘和分享照片背后的珍贵故事。 🧩❤️ 

## 项目概述 📋

本项目采用前后端分离的架构，前端使用HTML、CSS和JavaScript构建直观易用的用户界面，后端采用Java语言，利用GFPGAN模型实现照片修复功能，并结合讯飞星火大模型挖掘照片背后的故事。 🌐🔧

## 功能介绍 🚀

### 1. 老照片扫描与智能修复与美化 🖼️✨

用户可通过手机摄像头或本地相册上传存在褪色、破损、模糊等问题的老照片。系统利用GFPGAN模型对照片进行智能分析和修复，包括：
- 褪色还原 🌈
- 破损修补 🩹
- 模糊清晰化 🔍
- 肤色调整 🎨
- 背景优化 🌆
- 等等... ...

在修复过程中，系统会保留老照片特有的年代感和韵味，使其既焕然一新又不失历史质感。 🕰️🌟

### 2. 故事回忆与分享 📖👨👩👧👦
在照片修复过程中，系统引导用户回忆并记录照片背后的故事。用户可以：
- 添加文字描述 📝
- 选择合适标签 🧩

系统利用讯飞星火模型（图像识别与图像生成）分析照片内容和用户输入的信息，生成富有故事性的文字和图片集，并将其与修复后的照片绑定，形成一个完整的照片故事集。用户可以将这些故事集分享给家人和朋友，共同重温珍贵的过往时光。 💌👨👩👧👦

### 3. 登录注册 🔐
提供用户注册和登录功能，支持多种认证方式，确保用户数据的安全性和隐私保护。 🔒

## 项目结构 🏗️🛠️

```
├── frontend/                 # 前端代码 🌐
│   ├── css/                  # css文件夹 🎨
│   ├── js/                   # js文件夹 🌐
│   ├── index.html            # 首页视图 👀
│   └── login-register.html   # 登录注册页视图 👀
├── backend/                  # 后端代码 🛠️
│   └── java/                 # Java核心业务逻辑 ☕
├── 08.讯飞星火多模DEMO/       # 讯飞星火模型调用demo 📚
└── imageGeneration_Demo      # 讯飞星火模型模型调用demo 📚
```

## 安装与部署 🚀

### 方法一（推荐 🌟）：直接访问网站 🌐

可以直接访问：[光影溯洄 - 基于GFPGAN的照片修复与故事重现](https://chinese.redamancyxun.fun/echo/index.html)

### 方法二：本地部署 🔧

#### 克隆仓库 ⚙️

需要从GitHub克隆项目到本地，如：

```text
git clone https://github.com/Redamancy-Xun/Echoes-of-Time-AI-Powered-Photo-Restoration-Memory-Narrative.git
```

#### 数据库配置 🗄️

MySQL 数据库 🐬

1. 创建 MySQL 数据库 🐬
2. 根据 Java 对应源码配置数据表信息 📜
3. 配置数据库连接信息 `backend/Echoes-of-Time-AI-Powered-Photo-Restoration-Memory-Narrative/src/main/resources/application.yml` ⚙️

Redis 数据库 🟥

1. 创建 Redis 数据库 🟥
2. 配置数据库连接信息 `backend/Echoes-of-Time-AI-Powered-Photo-Restoration-Memory-Narrative/src/main/resources/application.yml` ⚙️

#### AI模型配置 🧠

1. 申请 Replicate API Token 🔑

2. 申请讯飞大模型 API Key 🗝️

3. 配置环境变量 🌍

   ```bash
   export REPLICATE_API_TOKEN=your_token_here
   export XUNFEI_API_KEY=your_api_key_here
   export XUNFEI_API_SECRET=your_api_secret_here
   ```

4. 修改对应 Java 源码

#### 前端环境搭建 🛠️

此网站为纯原生 HTML+CSS+JS 项目，故没有特殊的构建工具与部署指令，直接使用浏览器打开 index.html 文件即可（需要修改对应的请求 ip 为 localhost）

#### 后端环境搭建 🛠️

1. 配置本机信息 ⚙️

2. Java服务部署 ☕

   ```shell
   cd backend/Echoes-of-Time-AI-Powered-Photo-Restoration-Memory-Narrative
   mvn clean install
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

## 资源引用

本项目引用了下述资源：

1. [GFPGAN开源模型](https://github.com/TencentARC/GFPGAN)
2. [讯飞星火模型](https://www.xfyun.cn/)
3. 讯飞API - Java调用demo（08.讯飞星火多模DEMO、imageGeneration_Demo）

## 贡献指南 🤝

1.  Fork仓库 🍴
2.  创建你的特性分支 (`git checkout -b feature/AmazingFeature`) 🌱
3.  提交你的更改 (`git commit -m 'Add some AmazingFeature'`) 💬
4.  将你的更改推送到分支 (`git push origin feature/AmazingFeature`) 🚀
5.  打开 Pull Request 👐

## 许可证 📄

本项目采用 [MIT License](LICENSE)。 📜

## 联系我们 📞

如果您有任何问题或建议，请通过以下方式联系我们：
- 邮箱: 10235101427@stu.ecnu.edu.cn 📧
- 问题追踪: [GitHub Issues](https://github.com/Redamancy-Xun/Echoes-of-Time-AI-Powered-Photo-Restoration-Memory-Narrative/issues) 🐛

感谢您使用光影溯洄！我们期待您的反馈和建议。 🙏❤️
    
