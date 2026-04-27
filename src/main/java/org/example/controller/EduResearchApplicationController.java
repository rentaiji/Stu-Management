package org.example.controller;

import org.example.common.Result;
import org.example.service.EduResearchApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
     * 获取已审批列表（教务端）
     */
    @GetMapping("/approved-list")
    public Result<?> getApprovedList(HttpServletRequest request) {
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
        
        return researchApplicationService.getApprovedList();
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

    /**
     * 上传附件（支持PDF）
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Long teacherId = (Long) request.getAttribute("userId");
        if (teacherId == null) {
            return Result.error("用户未登录");
        }
        
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            return Result.error("只支持PDF文件");
        }
        
        try {
            // 创建上传目录
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "research";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            String filePath = uploadDir + File.separator + fileName;
            
            // 保存文件
            file.transferTo(new File(filePath));
            
            // 返回相对路径
            String relativePath = "research/" + fileName;
            return Result.success(relativePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 更新科研申请（用于被打回后重新提交）
     */
    @PutMapping("/update")
    public Result<Boolean> updateApplication(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long teacherId = (Long) request.getAttribute("userId");
        if (teacherId == null) {
            return Result.error("用户未登录");
        }
        
        Long applicationId = params.get("applicationId") != null ? 
            Long.valueOf(params.get("applicationId").toString()) : null;
        if (applicationId == null) {
            return Result.error("申请ID不能为空");
        }
        
        Integer researchType = params.get("researchType") != null ? 
            Integer.valueOf(params.get("researchType").toString()) : null;
        
        @SuppressWarnings("unchecked")
        Map<String, Object> detailData = (Map<String, Object>) params.get("detailData");
        
        return researchApplicationService.updateApplication(applicationId, teacherId, researchType, detailData);
    }

    /**
     * 撤销申请（仅待审核状态可撤销）
     */
    @PostMapping("/withdraw/{applicationId}")
    public Result<Boolean> withdrawApplication(@PathVariable Long applicationId, HttpServletRequest request) {
        Long teacherId = (Long) request.getAttribute("userId");
        if (teacherId == null) {
            return Result.error("用户未登录");
        }
        
        return researchApplicationService.withdrawApplication(applicationId, teacherId);
    }
}
