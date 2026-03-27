-- 重置 MySQL root 密码为 'root'
-- 使用方法：以管理员身份运行 PowerShell，执行：
-- mysql -u root < "C:\Users\j1506\IdeaProjects\STU_Manage\reset_mysql_password.sql"

ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS stu_manage 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;
