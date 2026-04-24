-- 为普通教师添加科研管理权限

-- 1. 添加科研管理菜单权限
INSERT INTO sys_menu (menu_name, parent_id, path, component, perms, icon, order_num, menu_type) 
VALUES ('科研成果查看', 0, '/research/view', NULL, 'research:view', 'document', 1, '1');

INSERT INTO sys_menu (menu_name, parent_id, path, component, perms, icon, order_num, menu_type) 
VALUES ('科研成果新增', 0, '/research/add', NULL, 'research:add', 'plus', 2, '1');

INSERT INTO sys_menu (menu_name, parent_id, path, component, perms, icon, order_num, menu_type) 
VALUES ('科研成果编辑', 0, '/research/edit', NULL, 'research:edit', 'edit', 3, '1');

-- 2. 获取刚插入的菜单ID（需要根据实际情况调整）
-- 假设教师角色ID为2，需要查询实际的菜单ID后关联
-- 这里使用子查询自动关联

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 4, menu_id FROM sys_menu WHERE perms IN ('research:view', 'research:add', 'research:edit');

-- 注意：如果教师角色ID不是2，请修改上面的role_id值
-- 可以通过以下SQL查询教师角色ID：
-- SELECT role_id, role_name FROM sys_role WHERE role_name LIKE '%教师%';
