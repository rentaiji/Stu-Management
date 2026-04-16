package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @TableField("menu_name")
    private String menuName;

    @TableField("parent_id")
    private Long parentId;

    @TableField("order_num")
    private Integer orderNum;

    @TableField("path")
    private String path;

    @TableField("component")
    private String component;

    @TableField("perms")
    private String perms;

    @TableField("icon")
    private String icon;

    @TableField("menu_type")
    private String menuType;

    @TableField("status")
    private String status;

    @TableField("visible")
    private String visible;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
