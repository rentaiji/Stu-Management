# 教师工作台 - 快速开始

## 🚀 5分钟快速部署

### 第1步: 执行数据库脚本

```bash
# 在MySQL中执行
mysql -u root -p stu_manage < src/main/resources/sql/create_teacher_workbench_tables.sql
mysql -u root -p stu_manage < src/main/resources/sql/add_teacher_menu_permissions.sql
```

### 第2步: 启动后端服务

```bash
cd C:\Users\j1506\IdeaProjects\STU_Manage
mvn spring-boot:run
```

### 第3步: 访问教师工作台

1. 打开浏览器访问: http://localhost:8080/index.html
2. 使用测试账号登录:
   - **账号**: `T001`
   - **密码**: `123456`
3. 登录后自动跳转到教师工作台

---

## 📋 功能概览

| 模块 | 权限要求 | 说明 |
|------|---------|------|
| 🏠 首页 | 所有教师 | 工作台数据统计 |
| 📚 我的课程 | academic:course:list | 查看授课列表 |
| 📝 成绩管理 | academic:score:input | 录入和管理成绩 |
| 👥 学生名单 | academic:course:list | 查看选课学生 |
| 🔬 科研档案 | teacher:research:* | 管理科研成果 |
| 📅 个人课表 | teacher:schedule:view | 查看授课安排 |
| 📢 发布通知 | teacher:notice:send | 发送通知(仅系主任/辅导员) |
| 💰 财务查询 | teacher:finance:view | 查询课时费和经费 |

---

## 🔑 测试账号

| 账号 | 角色 | 权限说明 |
|------|------|---------|
| T001 | 普通教师 | 教学+科研+课表+财务 |
| admin | 超级管理员 | 所有权限 |

**注意**: 如需测试系主任权限,请在数据库中修改T001的角色为5(系主任)。

```sql
-- 修改为系主任
UPDATE sys_user_role SET role_id = 5 WHERE user_id = 2;

-- 改回普通教师
UPDATE sys_user_role SET role_id = 4 WHERE user_id = 2;
```

---

## 📖 详细文档

- **[功能说明](./教师工作台功能说明.md)** - 完整的功能介绍和使用指南
- **[权限配置](./教师工作台权限配置说明.md)** - 权限体系详解和配置方法
- **[部署指南](./教师工作台部署指南.md)** - 详细的部署步骤和问题排查
- **[开发总结](./教师工作台开发完成总结.md)** - 技术实现和后续规划

---

## ⚡ 常见问题

### Q1: 为什么看不到某些菜单?
**A**: 您的角色可能没有该功能权限。请联系管理员分配权限或查看[权限配置说明](./教师工作台权限配置说明.md)。

### Q2: 如何为新教师分配权限?
**A**: 
1. 在"管理控制台 → 教师管理"中创建教师账号
2. 点击教师查看详情
3. 选择对应角色(普通教师/系主任/辅导员)
4. 保存后重新登录生效

### Q3: 部分功能显示模拟数据?
**A**: 是的,以下功能后端接口尚未实现,使用前端模拟数据:
- 个人课表
- 发布通知
- 财务查询

如需正式使用,请参考[部署指南](./教师工作台部署指南.md)中的"后续开发指引"补充后端接口。

---

## 🛠️ 技术栈

- **前端**: Vue.js 2.7 + Element UI 2.15
- **后端**: Spring Boot + MyBatis Plus
- **数据库**: MySQL 8.0+
- **认证**: JWT Token

---

## 📞 获取帮助

遇到问题? 

1. 查看[部署指南](./教师工作台部署指南.md)的"常见问题排查"章节
2. 检查浏览器Console和后端日志
3. 联系技术支持团队

---

**祝您使用愉快!** 🎉
