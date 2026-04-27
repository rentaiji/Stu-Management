package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("hr_employee_archive")
public class HrEmployeeArchive implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "archive_id", type = IdType.AUTO)
    private Long archiveId;

    @TableField("user_id")
    private Long userId;

    @TableField("archive_status")
    private String archiveStatus; // 档案状态:未建档/已建档

    // 基本信息
    @TableField("name_pinyin")
    private String namePinyin;
    @TableField("nation")
    private String nation;
    @TableField("age")
    private Integer age;
    @TableField("birth_date_archive")
    private LocalDate birthDateArchive;
    @TableField("native_place")
    private String nativePlace;
    @TableField("nationality_region")
    private String nationalityRegion;
    @TableField("marital_status")
    private String maritalStatus;
    @TableField("political_status")
    private String politicalStatus;
    @TableField("join_party_date")
    private LocalDate joinPartyDate;

    // 工作信息
    @TableField("join_work_date")
    private LocalDate joinWorkDate;
    @TableField("join_teaching_date")
    private LocalDate joinTeachingDate;
    @TableField("join_school_date")
    private LocalDate joinSchoolDate;
    @TableField("expected_retire_date")
    private LocalDate expectedRetireDate;
    @TableField("department")
    private String department;
    @TableField("current_status")
    private String currentStatus;
    @TableField("personal_status")
    private String personalStatus;

    // 学历信息
    @TableField("highest_degree")
    private String highestDegree;
    @TableField("highest_degree_school")
    private String highestDegreeSchool;
    @TableField("highest_degree_major")
    private String highestDegreeMajor;
    @TableField("highest_degree_start")
    private LocalDate highestDegreeStart;
    @TableField("highest_degree_end")
    private LocalDate highestDegreeEnd;
    @TableField("first_degree")
    private String firstDegree;
    @TableField("first_degree_school")
    private String firstDegreeSchool;
    @TableField("first_degree_major")
    private String firstDegreeMajor;
    @TableField("first_degree_end")
    private LocalDate firstDegreeEnd;
    @TableField("discipline_category")
    private String disciplineCategory;

    // 职称岗位
    @TableField("professional_title")
    private String professionalTitle;
    @TableField("title_appointment_date")
    private LocalDate titleAppointmentDate;
    @TableField("professional_title_level")
    private String professionalTitleLevel;
    @TableField("position")
    private String position;
    @TableField("position_level")
    private String positionLevel;
    @TableField("position_appointment_date")
    private LocalDate positionAppointmentDate;
    @TableField("is_management_post")
    private Integer isManagementPost;
    @TableField("is_professional_post")
    private Integer isProfessionalPost;
    @TableField("is_worker_post")
    private Integer isWorkerPost;
    @TableField("professional_post_category")
    private String professionalPostCategory;

    // 分类标签
    @TableField("employee_category")
    private String employeeCategory;
    @TableField("employee_source")
    private String employeeSource;
    @TableField("job_type")
    private String jobType;

    // 兼职与双肩挑
    @TableField("concurrent_post")
    private String concurrentPost;
    @TableField("concurrent_post_level")
    private String concurrentPostLevel;
    @TableField("concurrent_post_date")
    private LocalDate concurrentPostDate;
    @TableField("is_double_shoulder")
    private Integer isDoubleShoulder;
    @TableField("double_shoulder_dept")
    private String doubleShoulderDept;
    @TableField("is_industry_talent")
    private Integer isIndustryTalent;

    // 党政职务
    @TableField("party_position")
    private String partyPosition;
    @TableField("party_position_level")
    private String partyPositionLevel;
    @TableField("party_position_date")
    private LocalDate partyPositionDate;

    // 教师资格与证书
    @TableField("teacher_cert_no")
    private String teacherCertNo;
    @TableField("teacher_cert_date")
    private LocalDate teacherCertDate;
    @TableField("is_counselor")
    private String isCounselor;
    @TableField("is_psychology_teacher")
    private Integer isPsychologyTeacher;
    @TableField("has_psychology_cert")
    private Integer hasPsychologyCert;
    @TableField("is_dual_qualified")
    private Integer isDualQualified;
    @TableField("is_undergrad_teacher")
    private Integer isUndergradTeacher;

    // 人才与特聘
    @TableField("talent_title")
    private String talentTitle;
    @TableField("talent_appointment_date")
    private LocalDate talentAppointmentDate;
    @TableField("first_employment_date")
    private LocalDate firstEmploymentDate;
    @TableField("regularization_date")
    private LocalDate regularizationDate;
    @TableField("introduction_level")
    private String introductionLevel;
    @TableField("special_appointment_level")
    private String specialAppointmentLevel;
    @TableField("special_contract_date")
    private LocalDate specialContractDate;
    @TableField("special_contract_file")
    private String specialContractFile;

    // 联系方式
    @TableField("phone")
    private String phone;
    @TableField("email")
    private String email;
    @TableField("address")
    private String address;
    @TableField("emergency_contact")
    private String emergencyContact;
    @TableField("emergency_contact_phone")
    private String emergencyContactPhone;

    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
