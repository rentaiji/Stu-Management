package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCompetitionAward;
import org.example.service.EduCompetitionAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/competition")
public class EduCompetitionController {

    @Autowired
    private EduCompetitionAwardService competitionService;

    /**
     * 获取当前用户的获奖列表
     */
    @GetMapping("/list")
    public Result<List<EduCompetitionAward>> getList(@RequestParam Long studentId) {
        System.out.println("=== 查询学生竞赛列表 ===");
        System.out.println("studentId: " + studentId);
        
        LambdaQueryWrapper<EduCompetitionAward> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCompetitionAward::getUserId, studentId)
                .orderByDesc(EduCompetitionAward::getCreateTime);
        List<EduCompetitionAward> list = competitionService.list(wrapper);
        System.out.println("查询结果数量: " + (list != null ? list.size() : 0));
        
        return Result.success(list);
    }

    /**
     * 获取所有待审核列表（教务管理员）
     */
    @GetMapping("/pending-list")
    public Result<List<EduCompetitionAward>> getPendingList() {
        System.out.println("=== 查询待审批列表 ===");
        
        LambdaQueryWrapper<EduCompetitionAward> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCompetitionAward::getStatus, "待审核")
                .orderByDesc(EduCompetitionAward::getCreateTime);
        List<EduCompetitionAward> list = competitionService.list(wrapper);
        System.out.println("查询结果数量: " + (list != null ? list.size() : 0));
        
        return Result.success(list);
    }

    /**
     * 获取所有审批记录（教务管理员）
     */
    @GetMapping("/all-list")
    public Result<List<EduCompetitionAward>> getAllList(@RequestParam(required = false) String status) {
        LambdaQueryWrapper<EduCompetitionAward> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(EduCompetitionAward::getStatus, status);
        }
        wrapper.orderByDesc(EduCompetitionAward::getCreateTime);
        return Result.success(competitionService.list(wrapper));
    }

    /**
     * 审批通过
     */
    @PutMapping("/approve/{awardId}")
    public Result<Boolean> approve(@PathVariable Long awardId) {
        EduCompetitionAward award = competitionService.getById(awardId);
        if (award != null) {
            award.setStatus("已通过");
            award.setAuditTime(java.time.LocalDateTime.now());
            award.setUpdateTime(java.time.LocalDateTime.now());
            boolean success = competitionService.updateById(award);
            return success ? Result.success(true) : Result.error("审批失败");
        }
        return Result.error("记录不存在");
    }

    /**
     * 审批驳回
     */
    @PutMapping("/reject/{awardId}")
    public Result<Boolean> reject(@PathVariable Long awardId, @RequestParam String auditRemark) {
        EduCompetitionAward award = competitionService.getById(awardId);
        if (award != null) {
            award.setStatus("已驳回");
            award.setAuditRemark(auditRemark);
            award.setAuditTime(java.time.LocalDateTime.now());
            award.setUpdateTime(java.time.LocalDateTime.now());
            boolean success = competitionService.updateById(award);
            return success ? Result.success(true) : Result.error("驳回失败");
        }
        return Result.error("记录不存在");
    }

    /**
     * 保存获奖信息
     */
    @PostMapping
    public Result<Boolean> save(@RequestBody EduCompetitionAward award) {
        System.out.println("=== 竞赛申报保存请求 ===");
        System.out.println("userId: " + award.getUserId());
        System.out.println("competitionName: " + award.getCompetitionName());
        System.out.println("status: " + award.getStatus());
        
        // 强制设置 userId，防止前端未传递
        if (award.getUserId() == null) {
            System.out.println("错误：用户ID为空");
            return Result.error("用户ID不能为空");
        }
        // 设置默认值
        if (award.getDataSource() == null) {
            award.setDataSource("学生自填");
        }
        if (award.getStatus() == null) {
            award.setStatus("待审核");
        }
        
        System.out.println("准备保存到数据库...");
        boolean success = competitionService.save(award);
        System.out.println("保存结果: " + success);
        System.out.println("生成的awardId: " + award.getAwardId());
        
        return success ? Result.success(true) : Result.error("保存失败");
    }

    /**
     * 更新获奖信息
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody EduCompetitionAward award) {
        boolean success = competitionService.updateById(award);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    /**
     * 删除获奖信息
     */
    @DeleteMapping("/{awardId}")
    public Result<Boolean> delete(@PathVariable Long awardId) {
        boolean success = competitionService.removeById(awardId);
        return success ? Result.success(true) : Result.error("删除失败");
    }
}
