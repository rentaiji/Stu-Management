-- ============================================
-- 教师工作台部署验证脚本
-- ============================================

-- 1. 检查新表是否创建成功
SELECT '=== 检查新数据表 ===' AS '';
SHOW TABLES LIKE 'edu_notice';
SHOW TABLES LIKE 'edu_teacher_fund';
SHOW TABLES LIKE 'edu_teaching_fee';

-- 2. 检查菜单权限是否添加
SELECT '=== 检查新增菜单 ===' AS '';
SELECT menu_id, menu_name, perms, icon 
FROM sys_menu 
WHERE menu_id IN (45, 46, 47);

-- 预期结果:
-- 45 | 个人课表 | teacher:schedule:view | el-icon-time
-- 46 | 发布通知 | teacher:notice:send | el-icon-bell
-- 47 | 财务查询 | teacher:finance:view | el-icon-wallet

-- 3. 检查角色权限分配
SELECT '=== 检查角色权限分配 ===' AS '';
SELECT 
    r.role_id,
    r.role_name,
    GROUP_CONCAT(m.menu_name ORDER BY m.menu_id SEPARATOR ', ') AS menus
FROM sys_role r
JOIN sys_role_menu rm ON r.role_id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE m.menu_id IN (45, 46, 47)
GROUP BY r.role_id, r.role_name;

-- 预期结果:
-- 普通教师(role_id=4): 个人课表, 财务查询
-- 系主任(role_id=5): 个人课表, 发布通知, 财务查询
-- 辅导员(role_id=6): 发布通知

-- 4. 检查测试账号
SELECT '=== 检查测试账号 T001 ===' AS '';
SELECT 
    u.user_id,
    u.user_name,
    u.real_name,
    u.user_type,
    r.role_id,
    r.role_name
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.user_name = 'T001';

-- 预期结果: user_id=2, role_id=4, role_name=普通教师

-- 5. 检查测试数据
SELECT '=== 检查测试数据 ===' AS '';

-- 通知数据
SELECT COUNT(*) AS notice_count FROM edu_notice;
-- 预期: 2条

-- 项目经费数据
SELECT COUNT(*) AS fund_count FROM edu_teacher_fund;
-- 预期: 2条

-- 课时费数据
SELECT COUNT(*) AS fee_count FROM edu_teaching_fee;
-- 预期: 2条

-- 6. 查看具体测试数据
SELECT '=== 通知测试数据 ===' AS '';
SELECT notice_id, title, target_desc, read_count 
FROM edu_notice;

SELECT '=== 项目经费测试数据 ===' AS '';
SELECT fund_id, project_name, fund_source, total_amount, balance 
FROM edu_teacher_fund;

SELECT '=== 课时费测试数据 ===' AS '';
SELECT fee_id, course_name, hours, amount, status 
FROM edu_teaching_fee;

-- 7. 验证权限查询接口所需的数据
SELECT '=== 验证权限查询完整性 ===' AS '';

-- 检查T001的完整权限链
SELECT 
    '用户' AS level,
    u.user_id,
    u.user_name,
    NULL AS role_id,
    NULL AS menu_id,
    NULL AS menu_name
FROM sys_user u
WHERE u.user_name = 'T001'

UNION ALL

SELECT 
    '角色' AS level,
    u.user_id,
    u.user_name,
    r.role_id,
    NULL AS menu_id,
    NULL AS menu_name
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role r ON ur.role_id = r.role_id
WHERE u.user_name = 'T001'

UNION ALL

SELECT 
    '菜单' AS level,
    u.user_id,
    u.user_name,
    r.role_id,
    m.menu_id,
    m.menu_name
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role r ON ur.role_id = r.role_id
JOIN sys_role_menu rm ON r.role_id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.user_name = 'T001'
ORDER BY level, menu_id;

-- 8. 生成权限报告
SELECT '=== T001(普通教师)权限报告 ===' AS '';
SELECT 
    m.menu_id,
    m.menu_name,
    m.perms,
    m.icon,
    CASE 
        WHEN m.menu_type = 'M' THEN '目录'
        WHEN m.menu_type = 'C' THEN '菜单'
        WHEN m.menu_type = 'F' THEN '按钮'
    END AS type
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role_menu rm ON ur.role_id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE u.user_name = 'T001'
  AND m.deleted = 0
ORDER BY m.menu_id;

-- ============================================
-- 验证完成提示
-- ============================================
SELECT '
============================================
✅ 验证完成! 

如果所有查询都返回预期结果,说明部署成功。

下一步:
1. 启动Spring Boot应用
2. 使用 T001/123456 登录
3. 访问教师工作台页面
============================================
' AS '';
