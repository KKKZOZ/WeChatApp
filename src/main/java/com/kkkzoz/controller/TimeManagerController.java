package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.Solution;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.service.TimeManagerService;
import com.kkkzoz.service.UserService;
import com.kkkzoz.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/timeManager")
@AllArgsConstructor
public class TimeManagerController {

    private final UserService userService;

    private final TimeManagerService timeManagerService;


    @PostMapping("/solution/design")
    @ApiOperation(value = "教练完成对时间的分配安排")
    public List<Solution> designSolution(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        String localDate = params.get("localDate");
        String licenseNumber = params.get("licenseNumber");
        String teacherName = params.get("teacherName");
        int weekday = Integer.parseInt(params.get("weekday"));
        int startTime = Integer.parseInt(params.get("startTime"));
        int endTime = Integer.parseInt(params.get("endTime"));
        int mode = Integer.parseInt(params.get("mode"));
        int category = Integer.parseInt(params.get("category"));
        return timeManagerService
                .designSolution(userId, localDate,
                        licenseNumber, teacherName,
                        weekday, startTime,
                        endTime, mode, category);
    }


    @PostMapping("/solution")
    @ApiOperation(value = "教练段选择可使用的时间段分配方案后保存到后端")
    public ResponseVO saveSolution(@RequestBody Solution solution) {
        log.info("saveSolution: {}", solution);
        return timeManagerService.saveSolution(solution);
    }


    @GetMapping("/solution")
    @ApiOperation(value = "请求刷新数据")
    public List<Solution> getSolutionList(
            @RequestParam("weekOfYear") int weekOfYear) {
        String userId = SecurityUtil.getUserId();
        String role = userService.getUserRole(userId);
        if (role.equals("student")) {
            int category = userService.getCategory(userId);
            String teacherId = userService.getTeacherIdByUserId(userId);
            return timeManagerService.getSolutionList(category, teacherId, weekOfYear);
        } else {
            return timeManagerService.getSolutionList(userId, weekOfYear);
        }

    }

    @DeleteMapping("/solution")
    @ApiOperation(value = "教练取消时间分配")
    public ResponseVO cancelSolution(@RequestBody Map<String, String> params) {
        String solutionId = params.get("solutionId");
        return timeManagerService.cancelSolution(solutionId);
    }


    @PostMapping("/solution/reserve")
    @ApiOperation(value = "学生预约时间段")
    public ResponseVO reserveSolution(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        String solutionId = params.get("solutionId");
        int segmentId = Integer.parseInt(params.get("segmentId"));
        return timeManagerService.reserveSolution(userId, solutionId, segmentId);
    }

    @DeleteMapping("/solution/reserve")
    @ApiOperation(value = "学生取消预约")
    public ResponseVO cancelReservedSolution(@RequestBody Map<String, String> params) {
        String userId = SecurityUtil.getUserId();
        String solutionId = params.get("solutionId");
        int segmentId = Integer.parseInt(params.get("segmentId"));
        return timeManagerService.cancelReservedSolution(userId, solutionId, segmentId);
    }


}
