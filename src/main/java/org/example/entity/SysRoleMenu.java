package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("role_id")
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;
}
