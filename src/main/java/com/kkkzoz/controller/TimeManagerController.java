package com.kkkzoz.controller;

import com.kkkzoz.domain.entity.Solution;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.service.TimeManagerService;
import com.kkkzoz.service.UserService;
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
        //TODO:
        Long teacherId = Long.parseLong(params.get("teacherId"));
        String localDate = params.get("localDate");
        int weekday = Integer.parseInt(params.get("weekday"));
        int startTime = Integer.parseInt(params.get("startTime"));
        int endTime = Integer.parseInt(params.get("endTime"));
        int mode = Integer.parseInt(params.get("mode"));
        int category = Integer.parseInt(params.get("category"));
        return timeManagerService.designSolution(teacherId, localDate, weekday, startTime, endTime, mode, category);
    }


    @PostMapping("/solution")
    @ApiOperation(value = "教练段选择可使用的时间段分配方案后保存到后端")
    public ResponseVO saveSolution(@RequestBody Solution solution) {
        return timeManagerService.saveSolution(solution);
    }


    @GetMapping("/solution")
    @ApiOperation(value = "请求刷新数据")
    public List<Solution> getSolutionList(
            @RequestParam("userId") Long userId,
            @RequestParam("weekOfYear") int weekOfYear) {
        String role = userService.getUserRole(userId);
        if (role.equals("student")) {
            int category = userService.getCategory(userId);
            int teacherId = Math.toIntExact(userService.getTeacherIdByUserId(userId));
            return timeManagerService.getSolutionList(category, teacherId,weekOfYear);
        } else {
            return timeManagerService.getSolutionList(Math.toIntExact(userId),weekOfYear);
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
        Long studentId = Long.parseLong(params.get("studentId"));
        String solutionId = params.get("solutionId");
        int segmentId = Integer.parseInt(params.get("segmentId"));
        return timeManagerService.reserveSolution(studentId, solutionId, segmentId);
    }

    @DeleteMapping("/solution/reserve")
    @ApiOperation(value = "学生取消预约")
    public ResponseVO cancelReservedSolution(@RequestBody Map<String, String> params) {
        Long studentId = Long.parseLong(params.get("studentId"));
        String solutionId = params.get("solutionId");
        int segmentId = Integer.parseInt(params.get("segmentId"));
        return timeManagerService.cancelReservedSolution(studentId, solutionId, segmentId);
    }


}
