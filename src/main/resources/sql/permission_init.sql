DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    menu_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    perms VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    menu_type CHAR(1) DEFAULT 'M' COMMENT '菜单类型 M-目录 C-菜单 F-按钮',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    visible CHAR(1) DEFAULT '0' COMMENT '显示状态 0-显示 1-隐藏',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 初始化菜单数据
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, perms, icon, menu_type, status) VALUES
(1, '系统管理', 0, 1, 'system', NULL, 'el-icon-s-tools', 'M', '0'),
(2, '用户管理', 1, 1, 'user', 'system:user:list', 'el-icon-user', 'C', '0'),
(3, '新增用户', 2, 1, '', 'system:user:add', '#', 'F', '0'),
(4, '编辑用户', 2, 2, '', 'system:user:edit', '#', 'F', '0'),
(5, '删除用户', 2, 3, '', 'system:user:delete', '#', 'F', '0'),
(6, '角色管理', 1, 2, 'role', 'system:role:list', 'el-icon-s-custom', 'C', '0'),
(7, '新增角色', 6, 1, '', 'system:role:add', '#', 'F', '0'),
(8, '编辑角色', 6, 2, '', 'system:role:edit', '#', 'F', '0'),
(9, '删除角色', 6, 3, '', 'system:role:delete', '#', 'F', '0'),
(10, '分配权限', 6, 4, '', 'system:role:assign', '#', 'F', '0'),
(11, '部门管理', 1, 3, 'dept', 'system:dept:list', 'el-icon-office-building', 'C', '0'),
(12, '新增部门', 11, 1, '', 'system:dept:add', '#', 'F', '0'),
(13, '编辑部门', 11, 2, '', 'system:dept:edit', '#', 'F', '0'),
(14, '删除部门', 11, 3, '', 'system:dept:delete', '#', 'F', '0'),

(20, '教学管理', 0, 2, 'academic', NULL, 'el-icon-reading', 'M', '0'),
(21, '课程管理', 20, 1, 'course', 'academic:course:list', 'el-icon-collection', 'C', '0'),
(22, '新增课程', 21, 1, '', 'academic:course:add', '#', 'F', '0'),
(23, '编辑课程', 21, 2, '', 'academic:course:edit', '#', 'F', '0'),
(24, '删除课程', 21, 3, '', 'academic:course:delete', '#', 'F', '0'),
(25, '成绩管理', 20, 2, 'score', 'academic:score:list', 'el-icon-edit', 'C', '0'),
(26, '录入成绩', 25, 1, '', 'academic:score:input', '#', 'F', '0'),
(27, '审核成绩', 25, 2, '', 'academic:score:audit', '#', 'F', '0'),
(28, '发布成绩', 25, 3, '', 'academic:score:publish', '#', 'F', '0'),
(29, '选课管理', 20, 3, 'enroll', 'academic:enroll:list', 'el-icon-circle-check', 'C', '0'),
(30, '退课处理', 29, 1, '', 'academic:enroll:cancel', '#', 'F', '0'),

(40, '教师发展', 0, 3, 'teacher', NULL, 'el-icon-user-solid', 'M', '0'),
(41, '科研管理', 40, 1, 'research', 'teacher:research:list', 'el-icon-document', 'C', '0'),
(42, '新增成果', 41, 1, '', 'teacher:research:add', '#', 'F', '0'),
(43, '编辑成果', 41, 2, '', 'teacher:research:edit', '#', 'F', '0'),
(44, '删除成果', 41, 3, '', 'teacher:research:delete', '#', 'F', '0');

-- 初始化角色菜单关联（超级管理员拥有所有权限）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE deleted = 0;

-- 学校教务管理员权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(2, 20), (2, 21), (2, 25), (2, 26), (2, 27), (2, 28), (2, 29), (2, 30),
(2, 40), (2, 41);

-- 院系教务秘书权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(3, 2), (3, 3), (3, 4), (3, 5),
(3, 20), (3, 21), (3, 22), (3, 23), (3, 24),
(3, 25), (3, 26), (3, 27), (3, 28),
(3, 29), (3, 30);

-- 普通教师权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4, 21),
(4, 25), (4, 26),
(4, 41), (4, 42), (4, 43), (4, 44);

-- 系主任权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5, 2), (5, 3), (5, 4),
(5, 20), (5, 21), (5, 22), (5, 23), (5, 24),
(5, 25), (5, 26), (5, 27), (5, 28),
(5, 29), (5, 30),
(5, 40), (5, 41), (5, 42), (5, 43), (5, 44);

-- 辅导员权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(6, 2), (6, 3), (6, 4),
(6, 25);

-- 学生权限（仅查看）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(7, 21),
(7, 25);
