package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("real_name")
    private String realName;

    @TableField("password")
    private String password;

    @TableField("user_type")
    private String userType;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("gender")
    private String gender;

    @TableField("birth_date")
    private java.time.LocalDate birthDate;

    @TableField("id_card")
    private String idCard;

    @TableField("nation")
    private String nation;

    @TableField("political_status")
    private Integer politicalStatus;

    @TableField("native_place")
    private String nativePlace;

    @TableField("home_address")
    private String homeAddress;

    @TableField("avatar")
    private String avatar;

    @TableField("dept_id")
    private Long deptId;

    @TableField("login_ip")
    private String loginIp;

    @TableField("login_date")
    private LocalDateTime loginDate;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("remark")
    private String remark;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
