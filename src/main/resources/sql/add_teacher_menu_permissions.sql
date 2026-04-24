-- 为教师工作台新增菜单权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, perms, icon, menu_type, status) VALUES
(45, '个人课表', 40, 2, 'schedule', 'teacher:schedule:view', 'el-icon-time', 'C', '0'),
(46, '发布通知', 40, 3, 'notice', 'teacher:notice:send', 'el-icon-bell', 'C', '0'),
(47, '财务查询', 40, 4, 'finance', 'teacher:finance:view', 'el-icon-wallet', 'C', '0');

-- 更新普通教师权限（增加课表和财务查看）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4, 45), (4, 47);

-- 更新系主任权限（增加全部新功能）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5, 45), (5, 46), (5, 47);

-- 辅导员也可以发布通知
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(6, 46);
