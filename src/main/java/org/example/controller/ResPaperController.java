package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.Result;
import org.example.entity.ResPaper;
import org.example.service.ResPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/res/paper")
public class ResPaperController {

    @Autowired
    private ResPaperService paperService;

    /**
     * 教师提交论文申报
     */
    @PostMapping("/submit")
    public Result<Boolean> submit(@RequestBody ResPaper paper) {
        paper.setStatus("待审核");
        paper.setSubmitTime(LocalDateTime.now());
        paper.setCreateTime(LocalDateTime.now());
        boolean success = paperService.save(paper);
        return success ? Result.success(true) : Result.error("提交失败");
    }

    /**
     * 查询我的申报（教师端）
     */
    @GetMapping("/my-applications")
    public Result<List<ResPaper>> getMyApplications(@RequestParam Long teacherId) {
        LambdaQueryWrapper<ResPaper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResPaper::getTeacherId, teacherId)
               .orderByDesc(ResPaper::getCreateTime);
        List<ResPaper> list = paperService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 查询待审批列表（教务管理员）
     */
    @GetMapping("/pending")
    public Result<List<ResPaper>> getPendingList() {
        LambdaQueryWrapper<ResPaper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResPaper::getStatus, "待审核")
               .orderByAsc(ResPaper::getSubmitTime);
        List<ResPaper> list = paperService.list(wrapper);
        return Result.success(list);
    }

    /**
     * 审批通过
     */
    @PutMapping("/approve/{paperId}")
    public Result<Boolean> approve(@PathVariable Long paperId, @RequestParam Long auditorId) {
        ResPaper paper = paperService.getById(paperId);
        if (paper != null) {
            paper.setStatus("已通过");
            paper.setAuditTime(LocalDateTime.now());
            paper.setAuditorId(auditorId);
            paper.setUpdateTime(LocalDateTime.now());
            boolean success = paperService.updateById(paper);
            return success ? Result.success(true) : Result.error("审批失败");
        }
        return Result.error("记录不存在");
    }

    /**
     * 审批驳回
     */
    @PutMapping("/reject/{paperId}")
    public Result<Boolean> reject(@PathVariable Long paperId, 
                                   @RequestParam String auditRemark,
                                   @RequestParam Long auditorId) {
        ResPaper paper = paperService.getById(paperId);
        if (paper != null) {
            paper.setStatus("已驳回");
            paper.setAuditRemark(auditRemark);
            paper.setAuditTime(LocalDateTime.now());
            paper.setAuditorId(auditorId);
            paper.setUpdateTime(LocalDateTime.now());
            boolean success = paperService.updateById(paper);
            return success ? Result.success(true) : Result.error("驳回失败");
        }
        return Result.error("记录不存在");
    }

    /**
     * 获取详情
     */
    @GetMapping("/{paperId}")
    public Result<ResPaper> getDetail(@PathVariable Long paperId) {
        ResPaper paper = paperService.getById(paperId);
        return Result.success(paper);
    }
}
