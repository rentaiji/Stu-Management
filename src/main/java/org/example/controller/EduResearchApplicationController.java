package org.example.controller;

import org.example.common.Result;
import org.example.service.EduResearchApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/edu/research")
public class EduResearchApplicationController {

    @Autowired
    private EduResearchApplicationService researchApplicationService;

    /**
     * 提交科研申请（教师端）
     */
    @PostMapping("/submit")
    public Result<Boolean> submitApplication(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long teacherId = (Long) request.getAttribute("userId");
        if (teacherId == null) {
            return Result.error("用户未登录");
        }
        
        Integer researchType = params.get("researchType") != null ? 
            Integer.valueOf(params.get("researchType").toString()) : null;
        
        @SuppressWarnings("unchecked")
        Map<String, Object> detailData = (Map<String, Object>) params.get("detailData");
        
        return researchApplicationService.submitApplication(teacherId, researchType, detailData);
    }

    /**
     * 获取我的申请列表（教师端）
     */
    @GetMapping("/my-applications")
    public Result<?> getMyApplications(HttpServletRequest request) {
        Long teacherId = (Long) request.getAttribute("userId");
        if (teacherId == null) {
            return Result.error("用户未登录");
        }
        
        return researchApplicationService.getMyApplications(teacherId);
    }

    /**
     * 获取申请详情
     */
    @GetMapping("/detail/{applicationId}")
    public Result<?> getApplicationDetail(@PathVariable Long applicationId) {
        return researchApplicationService.getApplicationDetail(applicationId);
    }

    /**
     * 获取待审核列表（教务端）
     */
    @GetMapping("/pending-audit")
    public Result<?> getPendingAudit(HttpServletRequest request) {
        // 权限检查：只有有发布课程权限的教务老师可以查看
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权访问");
        }
        
        return researchApplicationService.getPendingAudit();
    }

    /**
     * 审核申请（教务端）
     */
    @PostMapping("/audit")
    public Result<Boolean> auditApplication(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        // 权限检查：只有有发布课程权限的教务老师可以审核
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权操作");
        }
        
        Long applicationId = Long.valueOf(params.get("applicationId").toString());
        String status = params.get("status").toString(); // 1-通过, 2-拒绝
        String auditRemark = params.get("auditRemark") != null ? params.get("auditRemark").toString() : null;
        
        return researchApplicationService.auditApplication(applicationId, status, userId, auditRemark);
    }
}
