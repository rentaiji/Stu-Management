package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;
}
