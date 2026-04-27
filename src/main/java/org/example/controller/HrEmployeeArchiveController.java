package org.example.controller;

import com.alibaba.excel.EasyExcel;
import org.example.common.Result;
import org.example.entity.HrEmployeeArchive;
import org.example.service.HrEmployeeArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/hr/archive")
public class HrEmployeeArchiveController {

    @Autowired
    private HrEmployeeArchiveService archiveService;

    /**
     * 根据用户ID获取档案
     */
    @GetMapping("/user/{userId}")
    public Result<HrEmployeeArchive> getByUserId(@PathVariable Long userId) {
        HrEmployeeArchive archive = archiveService.getByUserId(userId);
        return Result.success(archive);
    }

    /**
     * 保存或更新档案
     */
    @PostMapping
    public Result<Boolean> saveOrUpdate(@RequestBody HrEmployeeArchive archive) {
        boolean success = archiveService.saveOrUpdateArchive(archive);
        return success ? Result.success(true) : Result.error("保存失败");
    }

    /**
     * 删除档案（级联删除子表）
     */
    @DeleteMapping("/{archiveId}")
    public Result<Boolean> delete(@PathVariable Long archiveId) {
        boolean success = archiveService.removeById(archiveId);
        return success ? Result.success(true) : Result.error("删除失败");
    }
    
    /**
     * 导出所有档案
     */
    @GetMapping("/export")
    public void export(@RequestParam(required = false) String token, HttpServletResponse response) throws IOException {
        // 简单验证token
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("未授权");
            return;
        }
        
        List<HrEmployeeArchive> list = archiveService.list();
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("人事档案", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        EasyExcel.write(response.getOutputStream(), HrEmployeeArchive.class)
                .sheet("人事档案")
                .doWrite(list);
    }
}
