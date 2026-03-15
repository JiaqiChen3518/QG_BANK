# QG银行管理系统

## 项目简介

QG银行管理系统是一个基于Java Web技术开发的银行系统，提供用户账户管理、资金交易、交易记录查询等功能，同时支持管理员对用户和交易记录的管理。

## 技术栈

- **后端**：Java Servlet、MySQL、Druid连接池、Fastjson
- **前端**：Vue.js、Axios、HTML5、CSS3
- **构建工具**：Maven
- **服务器**：Tomcat

## 功能特性

### 用户功能
- 🔐 用户登录与注册
- 💳 存款、取款、转账操作
- 📊 个人交易记录查询
- 👤 个人信息修改
- 💹 理财产品购买（活期/定期）
- 🚪 安全退出

### 管理员功能
- 👥 用户管理（查看所有用户信息）
- 📋 交易记录管理（查看所有交易记录）
- 💰 理财产品管理（查看所有购买记录）

## 项目结构

```
qg_bank/
├── src/
│   ├── main/
│   │   ├── java/com/qg/bank/
│   │   │   ├── dao/                    # 数据访问层
│   │   │   │   ├── UserDao.java
│   │   │   │   ├── UserDaoImpl.java
│   │   │   │   ├── TransactionRecordDao.java
│   │   │   │   ├── TransactionRecordDaoImpl.java
│   │   │   │   ├── FinantialProductDao.java
│   │   │   │   └── FinantialProductDaoImpl.java
│   │   │   ├── filter/                 # 过滤器层
│   │   │   │   └── LoginFilter.java    # 登录验证过滤器
│   │   │   ├── pojo/                   # 实体类
│   │   │   │   ├── User.java
│   │   │   │   ├── TransactionRecord.java
│   │   │   │   ├── FinancialProduct.java
│   │   │   │   └── Result.java         # 统一返回结果封装
│   │   │   ├── service/                # 业务逻辑层
│   │   │   │   ├── UserService.java
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── TransactionService.java
│   │   │   │   ├── TransactionServiceImpl.java
│   │   │   │   ├── FinancialService.java
│   │   │   │   └── FinancialServiceImpl.java
│   │   │   ├── servlet/                # 控制器层
│   │   │   │   ├── LoginServlet.java
│   │   │   │   ├── RegisterServlet.java
│   │   │   │   ├── SelfInfoServlet.java
│   │   │   │   ├── DispositServlet.java
│   │   │   │   ├── WithdrawServlet.java
│   │   │   │   ├── TransferServlet.java
│   │   │   │   ├── TransactionRecordServlet.java
│   │   │   │   ├── AdminServlet.java
│   │   │   │   ├── UpdateUserServlet.java
│   │   │   │   └── FinancialServlet.java
│   │   │   └── util/                   # 工具类
│   │   │       └── DBUtil.java         # 数据库连接工具
│   │   ├── resources/                  # 配置文件
│   │   │   └── db.properties           # 数据库配置文件
│   │   └── webapp/                     # 前端资源
│   │       ├── css/
│   │       │   └── style.css           # 页面样式
│   │       ├── js/
│   │       │   ├── vue.js
│   │       │   └── axios-0.18.0.js
│   │       ├── login.html              # 登录页面
│   │       ├── register.html           # 注册页面
│   │       ├── main.html               # 用户主页面
│   │       └── admin.html              # 管理员页面
│   └── test/                           # 测试代码
├── target/                             # 构建输出目录
└── pom.xml                             # Maven配置文件
```

## 核心功能模块

### 1. 用户认证模块
- 登录验证
- 注册功能
- 验证码生成与验证
- 权限控制（普通用户/管理员）
- **登录过滤器**：统一处理登录验证逻辑

### 2. 账户管理模块
- 个人信息查询与修改
- 余额查询

### 3. 交易模块
- 存款操作
- 取款操作
- 转账操作
- 交易记录生成

### 4. 理财模块
- 活期理财产品购买
- 定期理财产品购买
- 理财记录查询

### 5. 记录查询模块
- 个人交易记录查询
- 管理员查看所有交易记录
- 管理员查看所有理财记录

## 项目优化记录

### 1. 统一返回结果封装
将原来分散的boolean返回值和实体类返回值统一封装为`Result`类，包含：
- `success`: 操作是否成功
- `message`: 提示信息
- `data`: 返回数据

这样前端可以统一处理响应，大大提高了代码的可读性和维护性。

### 2. 登录验证提取到Filter
将原来分散在各个Servlet中的登录判断逻辑提取到`LoginFilter`中，实现：
- 代码复用，避免重复编写登录判断
- 统一处理，便于维护
- 简化Servlet代码，使其专注于业务逻辑

### 3. 数据库配置分离
将数据库连接信息从代码中分离到`db.properties`配置文件中：
- 便于环境切换（开发/测试/生产）
- 提高配置管理的灵活性
- 增强安全性（避免硬编码敏感信息）

### 4. 异常处理优化
- DAO层抛出具体异常

- Service层捕获异常并封装到Result中

- 便于日志记录和问题排查

- 前端可以根据错误类型给出友好提示

  

## 创作日记与感想

### 项目初期的困惑

一开始做这个项目时，我是先分包并且根据三层架构创建了一些类，写具体的方法。但是后面才意识到，我一开始并不了解三层架构，没有分清每一层的职责。

起初我并没有意识到要写接口，只知道一个劲的写方法，后面才知道接口的重要性。接口定义了规范，让代码更加灵活和可扩展，也便于后续的维护和重构。

常常会忍不住把一些判断逻辑写到了Servlet中，导致Controller层变得臃肿。现在回头看，Service层应该承担更多的业务逻辑处理，而Servlet只负责接收请求和返回响应。

### 返回值的设计演变

在一开始很多方法的返回值类型都是boolean或者一些实体类，没有意识到可以用一个结果类来封装。这导致代码参差不齐，可读性并不高。

后来引入了`Result`类统一封装返回结果，代码变得清晰多了。前端也能根据统一的结构处理响应，大大提高了开发效率。

### 异常处理的领悟

也是做了这个项目之后，我才知道异常有什么用。一开始仅仅是看到能抛出就抛出，不能抛出就捕获（包起来不报红就行🌞🌞）。

后面才知道，其实是可以在DAO层抛出相应的异常，在Service层中捕获，并且作为结果一起返回。这些异常同时也可以作为日志记录起来，方便查看和维护。

### 关于CSS样式

这个项目的CSS样式大多是借助AI写的，自己写的实在没法看😳😳。通过这个项目我也意识到了前端样式的重要性，一个好的界面能大大提升用户体验。

### 收获与成长

通过这个项目，我深刻理解了：
1. **三层架构的重要性**：明确各层职责，代码结构更清晰
2. **接口的价值**：接口定义了规范，让代码更加灵活和可扩展，也便于后续的维护和重构
3. **统一规范的价值**：无论是返回值还是异常处理，统一规范都能提高代码质量
4. **配置化的好处**：将配置与代码分离，提高灵活性和可维护性
5. **Filter的应用场景**：给大部分servlet省下了很多代码，如request编码字符集、response响应数据格式、是否登录等等
6. **代码复用的意义**：减少重复代码，提高开发效率

这是一个不断学习和改进的过程，虽然代码还有很多可以优化的地方，但每一步的改进都让我对Java Web开发有了更深的理解。

## 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- Tomcat 7+

## 安装部署

### 1. 数据库配置
1. 创建数据库：`CREATE DATABASE qg_bank;`
2. 配置数据库连接信息：修改 `src/main/resources/db.properties` 文件

```properties
driverClassName=com.mysql.cj.jdbc.Driver
jdbcUrl=jdbc:mysql://localhost:3306/qg_bank?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
username=your_username
password=your_password
```

### 2. 项目构建
```bash
# 进入项目根目录
cd qg_bank
# 构建项目
mvn clean package
```

### 3. 部署到Tomcat
1. 将 `target/qg_bank.war` 文件复制到Tomcat的 `webapps` 目录
2. 启动Tomcat服务器
3. 访问 `http://localhost:8080/qg_bank`

## 使用说明

### 用户登录
1. 访问登录页面：`http://localhost:8080/qg_bank/index.html`
2. 输入用户名和密码
3. 输入验证码
4. 点击登录按钮

### 用户注册
1. 在登录页面点击"注册一个账号"链接
2. 填写注册信息
3. 点击注册按钮

### 用户操作
登录后进入主页面，可以进行以下操作：
- **存款**：点击"存款"标签，输入存款金额，点击确认存款
- **取款**：点击"取款"标签，输入取款金额，点击确认取款
- **转账**：点击"转账"标签，输入转账金额和目标用户，点击确认转账
- **查看交易记录**：点击"交易记录"标签
- **购买理财产品**：点击"理财"标签，选择产品类型和金额
- **修改个人信息**：点击"修改信息"按钮，在弹出的对话框中修改信息

### 管理员操作
使用管理员账号登录后进入管理员页面，可以：
- 查看所有用户信息：点击"用户管理"标签
- 查看所有交易记录：点击"交易记录管理"标签
- 查看所有理财记录：点击"理财产品管理"标签

## 项目特点

1. **前后端分离**：前端使用Vue.js构建，后端使用Java Servlet提供API
2. **三层架构**：清晰的分层设计，职责明确
3. **统一返回格式**：使用Result类封装返回结果，便于前端处理
4. **登录过滤器**：统一处理登录验证，代码复用
5. **安全性**：使用验证码防止恶意登录，密码加密存储
6. **模块化开发**：代码结构清晰，易于维护和扩展
7. **事务处理**：确保交易操作的原子性
8. **连接池优化**：使用Druid连接池提高数据库性能

## 注意事项

- 确保数据库服务正常运行
- 确保Tomcat服务器配置正确
- 首次使用时需要创建管理员账号
- 转账操作时请确保目标用户存在
- 理财购买时请确保余额充足

## 默认账号

- 管理员账号：admin / admin123
- 普通用户：自行注册

---

*本项目为学习项目，持续优化中...*
