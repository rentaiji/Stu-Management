package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Result;
import org.example.entity.*;
import org.example.mapper.*;
import org.example.service.EduResearchApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EduResearchApplicationServiceImpl extends ServiceImpl<EduResearchApplicationMapper, EduResearchApplication> implements EduResearchApplicationService {

    @Autowired
    private EduResearchApplicationMapper applicationMapper;
    
    @Autowired
    private EduResearchSciPaperMapper sciPaperMapper;
    
    @Autowired
    private EduResearchExcellentPaperMapper excellentPaperMapper;
    
    @Autowired
    private EduResearchCcfPaperMapper ccfPaperMapper;
    
    @Autowired
    private EduResearchAwardMapper awardMapper;
    
    @Autowired
    private EduResearchProjectMapper projectMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> submitApplication(Long teacherId, Integer researchType, Map<String, Object> detailData) {
        try {
            // 创建申请主记录
            EduResearchApplication application = new EduResearchApplication();
            application.setTeacherId(teacherId);
            application.setResearchType(researchType);
            application.setStatus("0"); // 待审核
            
            // 根据不同类型设置标题
            String title = "";
            switch (researchType) {
                case 1: // SCI论文
                    title = (String) detailData.get("paperTitle");
                    break;
                case 2: // 卓越期刊论文
                    title = (String) detailData.get("paperTitle");
                    break;
                case 3: // CCF会议论文
                    title = (String) detailData.get("paperTitle");
                    break;
                case 4: // 科研奖励
                    title = (String) detailData.get("awardName");
                    break;
                case 5: // 科研项目
                    title = (String) detailData.get("projectName");
                    break;
            }
            application.setTitle(title);
            
            boolean saved = this.save(application);
            if (!saved) {
                return Result.error("提交失败");
            }
            
            Long applicationId = application.getApplicationId();
            
            // 根据类型保存详情
            switch (researchType) {
                case 1: // SCI论文
                    EduResearchSciPaper sciPaper = new EduResearchSciPaper();
                    sciPaper.setApplicationId(applicationId);
                    sciPaper.setPaperTitle((String) detailData.get("paperTitle"));
                    sciPaper.setJournalName((String) detailData.get("journalName"));
                    sciPaper.setAuthorInfo((String) detailData.get("authorInfo"));
                    sciPaper.setCorrespondingAuthor((String) detailData.get("correspondingAuthor"));
                    sciPaper.setSciZone((String) detailData.get("sciZone"));
                    sciPaper.setIsRecommendedJournal((String) detailData.get("isRecommendedJournal"));
                    sciPaper.setCcfZone((String) detailData.get("ccfZone"));
                    sciPaper.setIsExcellentJournal((String) detailData.get("isExcellentJournal"));
                    sciPaper.setIsQingdaoFirstUnit((String) detailData.get("isQingdaoFirstUnit"));
                    sciPaper.setIsHighlyCited((String) detailData.get("isHighlyCited"));
                    sciPaper.setIsEsiPaper((String) detailData.get("isEsiPaper"));
                    sciPaper.setAwardLevel((String) detailData.get("awardLevel"));
                    sciPaper.setPaperAffiliation((String) detailData.get("paperAffiliation"));
                    sciPaper.setPoints(Double.parseDouble(detailData.get("points").toString()));
                    sciPaper.setRemark((String) detailData.get("remark"));
                    sciPaperMapper.insert(sciPaper);
                    break;
                    
                case 2: // 卓越期刊论文
                    EduResearchExcellentPaper excellentPaper = new EduResearchExcellentPaper();
                    excellentPaper.setApplicationId(applicationId);
                    excellentPaper.setPaperTitle((String) detailData.get("paperTitle"));
                    excellentPaper.setJournalName((String) detailData.get("journalName"));
                    excellentPaper.setAuthorInfo((String) detailData.get("authorInfo"));
                    excellentPaper.setCorrespondingAuthor((String) detailData.get("correspondingAuthor"));
                    excellentPaper.setJournalType((String) detailData.get("journalType"));
                    excellentPaper.setIsQingdaoFirstUnit((String) detailData.get("isQingdaoFirstUnit"));
                    excellentPaper.setPaperAffiliation((String) detailData.get("paperAffiliation"));
                    excellentPaper.setPoints(Double.parseDouble(detailData.get("points").toString()));
                    excellentPaper.setRemark((String) detailData.get("remark"));
                    excellentPaperMapper.insert(excellentPaper);
                    break;
                    
                case 3: // CCF会议论文
                    EduResearchCcfPaper ccfPaper = new EduResearchCcfPaper();
                    ccfPaper.setApplicationId(applicationId);
                    ccfPaper.setPaperTitle((String) detailData.get("paperTitle"));
                    ccfPaper.setConferenceName((String) detailData.get("conferenceName"));
                    ccfPaper.setFirstAuthor((String) detailData.get("firstAuthor"));
                    ccfPaper.setCorrespondingAuthor((String) detailData.get("correspondingAuthor"));
                    ccfPaper.setConferenceType((String) detailData.get("conferenceType"));
                    ccfPaper.setIsQingdaoFirstUnit((String) detailData.get("isQingdaoFirstUnit"));
                    ccfPaper.setPaperAffiliation((String) detailData.get("paperAffiliation"));
                    ccfPaper.setPoints(Double.parseDouble(detailData.get("points").toString()));
                    ccfPaper.setRemark((String) detailData.get("remark"));
                    ccfPaperMapper.insert(ccfPaper);
                    break;
                    
                case 4: // 科研奖励
                    EduResearchAward award = new EduResearchAward();
                    award.setApplicationId(applicationId);
                    award.setAwardName((String) detailData.get("awardName"));
                    award.setAchievementName((String) detailData.get("achievementName"));
                    award.setAwardLevel((String) detailData.get("awardLevel"));
                    if (detailData.get("awardDate") != null) {
                        award.setAwardDate(java.time.LocalDate.parse(detailData.get("awardDate").toString()));
                    }
                    award.setAllWinners((String) detailData.get("allWinners"));
                    award.setIssuingAuthority((String) detailData.get("issuingAuthority"));
                    award.setAwardGrade((String) detailData.get("awardGrade"));
                    if (detailData.get("unitRanking") != null) {
                        award.setUnitRanking(Integer.parseInt(detailData.get("unitRanking").toString()));
                    }
                    award.setCertificateNo((String) detailData.get("certificateNo"));
                    award.setMainAchievement((String) detailData.get("mainAchievement"));
                    award.setRemark((String) detailData.get("remark"));
                    awardMapper.insert(award);
                    break;
                    
                case 5: // 科研项目
                    EduResearchProject project = new EduResearchProject();
                    project.setApplicationId(applicationId);
                    project.setProjectNo((String) detailData.get("projectNo"));
                    project.setProjectName((String) detailData.get("projectName"));
                    project.setProjectSource((String) detailData.get("projectSource"));
                    project.setLeader((String) detailData.get("leader"));
                    if (detailData.get("signYear") != null) {
                        project.setSignYear(Integer.parseInt(detailData.get("signYear").toString()));
                    }
                    if (detailData.get("contractStartDate") != null) {
                        project.setContractStartDate(java.time.LocalDate.parse(detailData.get("contractStartDate").toString()));
                    }
                    if (detailData.get("contractEndDate") != null) {
                        project.setContractEndDate(java.time.LocalDate.parse(detailData.get("contractEndDate").toString()));
                    }
                    if (detailData.get("contractAmount") != null) {
                        project.setContractAmount(Double.parseDouble(detailData.get("contractAmount").toString()));
                    }
                    if (detailData.get("receivedAmount") != null) {
                        project.setReceivedAmount(Double.parseDouble(detailData.get("receivedAmount").toString()));
                    }
                    project.setRemark((String) detailData.get("remark"));
                    projectMapper.insert(project);
                    break;
            }
            
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<?> getMyApplications(Long teacherId) {
        LambdaQueryWrapper<EduResearchApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearchApplication::getTeacherId, teacherId)
               .orderByDesc(EduResearchApplication::getCreateTime);
        
        List<EduResearchApplication> applications = this.list(wrapper);
        
        List<Map<String, Object>> result = applications.stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("applicationId", app.getApplicationId());
            map.put("researchType", app.getResearchType());
            map.put("title", app.getTitle());
            map.put("status", app.getStatus());
            map.put("createTime", app.getCreateTime());
            map.put("auditRemark", app.getAuditRemark());
            
            // 添加类型名称
            String typeName = "";
            switch (app.getResearchType()) {
                case 1: typeName = "SCI论文"; break;
                case 2: typeName = "卓越期刊论文"; break;
                case 3: typeName = "CCF会议论文"; break;
                case 4: typeName = "科研奖励"; break;
                case 5: typeName = "科研项目"; break;
            }
            map.put("typeName", typeName);
            
            return map;
        }).collect(java.util.stream.Collectors.toList());
        
        return Result.success(result);
    }
    
    @Override
    public Result<?> getPendingAudit() {
        LambdaQueryWrapper<EduResearchApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearchApplication::getStatus, "0") // 待审核
               .orderByDesc(EduResearchApplication::getCreateTime);
        
        List<EduResearchApplication> applications = this.list(wrapper);
        
        List<Map<String, Object>> result = applications.stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("applicationId", app.getApplicationId());
            map.put("researchType", app.getResearchType());
            map.put("title", app.getTitle());
            map.put("status", app.getStatus());
            map.put("createTime", app.getCreateTime());
            
            // 添加类型名称
            String typeName = "";
            switch (app.getResearchType()) {
                case 1: typeName = "SCI论文"; break;
                case 2: typeName = "卓越期刊论文"; break;
                case 3: typeName = "CCF会议论文"; break;
                case 4: typeName = "科研奖励"; break;
                case 5: typeName = "科研项目"; break;
            }
            map.put("typeName", typeName);
            
            // 获取申请人信息
            // 这里可以根据实际需求获取教师姓名
            map.put("teacherId", app.getTeacherId());
            
            return map;
        }).collect(java.util.stream.Collectors.toList());
        
        return Result.success(result);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> auditApplication(Long applicationId, String status, Long auditBy, String auditRemark) {
        try {
            EduResearchApplication application = this.getById(applicationId);
            if (application == null) {
                return Result.error("申请记录不存在");
            }
            
            application.setStatus(status); // 1-通过, 2-拒绝
            application.setAuditBy(auditBy);
            application.setAuditTime(LocalDateTime.now());
            application.setAuditRemark(auditRemark);
            
            boolean updated = this.updateById(application);
            return updated ? Result.success(true) : Result.error("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("审核失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<?> getApplicationDetail(Long applicationId) {
        EduResearchApplication application = this.getById(applicationId);
        if (application == null) {
            return Result.error("申请记录不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("application", application);
        
        // 根据类型获取详情
        switch (application.getResearchType()) {
            case 1: // SCI论文
                LambdaQueryWrapper<EduResearchSciPaper> sciWrapper = new LambdaQueryWrapper<>();
                sciWrapper.eq(EduResearchSciPaper::getApplicationId, applicationId);
                EduResearchSciPaper sciPaper = sciPaperMapper.selectOne(sciWrapper);
                result.put("detail", sciPaper);
                break;
                
            case 2: // 卓越期刊论文
                LambdaQueryWrapper<EduResearchExcellentPaper> excelWrapper = new LambdaQueryWrapper<>();
                excelWrapper.eq(EduResearchExcellentPaper::getApplicationId, applicationId);
                EduResearchExcellentPaper excelPaper = excellentPaperMapper.selectOne(excelWrapper);
                result.put("detail", excelPaper);
                break;
                
            case 3: // CCF会议论文
                LambdaQueryWrapper<EduResearchCcfPaper> ccfWrapper = new LambdaQueryWrapper<>();
                ccfWrapper.eq(EduResearchCcfPaper::getApplicationId, applicationId);
                EduResearchCcfPaper ccfPaper = ccfPaperMapper.selectOne(ccfWrapper);
                result.put("detail", ccfPaper);
                break;
                
            case 4: // 科研奖励
                LambdaQueryWrapper<EduResearchAward> awardWrapper = new LambdaQueryWrapper<>();
                awardWrapper.eq(EduResearchAward::getApplicationId, applicationId);
                EduResearchAward award = awardMapper.selectOne(awardWrapper);
                result.put("detail", award);
                break;
                
            case 5: // 科研项目
                LambdaQueryWrapper<EduResearchProject> projectWrapper = new LambdaQueryWrapper<>();
                projectWrapper.eq(EduResearchProject::getApplicationId, applicationId);
                EduResearchProject project = projectMapper.selectOne(projectWrapper);
                result.put("detail", project);
                break;
        }
        
        return Result.success(result);
    }
}
