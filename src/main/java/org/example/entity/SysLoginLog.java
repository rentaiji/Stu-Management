package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_login_log")
public class SysLoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @TableField("user_id")
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("real_name")
    private String realName;

    @TableField("user_type")
    private String userType;

    @TableField("login_time")
    private LocalDateTime loginTime;

    @TableField("ip_address")
    private String ipAddress;

    @TableField("user_agent")
    private String userAgent;

    @TableField("login_status")
    private String loginStatus;

    @TableField("fail_reason")
    private String failReason;

    @TableField("operation")
    private String operation;

    @TableField("create_time")
    private LocalDateTime createTime;
}
