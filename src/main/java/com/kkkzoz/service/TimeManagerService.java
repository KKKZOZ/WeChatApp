package com.kkkzoz.service;


import com.kkkzoz.domain.entity.Segment;
import com.kkkzoz.domain.entity.Solution;
import com.kkkzoz.domain.entity.User;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.mapper.UserMapper;
import com.kkkzoz.repository.SolutionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TimeManagerService {


    private final SolutionRepository solutionRepository;

    private final UserMapper userMapper;

    private static final int minMinutes = 30;

    private static final int maxMinutes = 120;


    //查询对应教练的solution
    public List<Solution> getSolutionList(int category, String teacherId, int weekOfYear) {
        return solutionRepository
                .findByCategoryAndTeacherIdAndWeekOfYear(category, teacherId, weekOfYear);
    }

    public List<Solution> getSolutionList(String teacherId, int weekOfYear) {
        return solutionRepository
                .findByTeacherIdAndWeekOfYear(teacherId, weekOfYear);

    }

    public ResponseVO addSolution(Solution solution) {
        solutionRepository.save(solution);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO reserveSolution(String studentId, String solutionId, int segmentId) {
        Solution solution = solutionRepository.findById(solutionId).get();

        User user = userMapper.selectById(studentId);
        String studentName = user.getName();

        Segment segment = solution.getSegments().get(segmentId - 1);
        log.info("segment: {}", segment);
        //如果还能预约
        if (segment.getOccupy() < segment.getMode()) {
            segment.setOccupy(segment.getOccupy() + 1);
            List<String> members = segment.getMembers();
            //如果未出现在预约名单中，才向名单中注册此人
            if (!members.contains(studentName)) {
                members.add(studentName);
            } else {
                return new ResponseVO(ResultCode.RESERVE_ERROR);
            }
            solutionRepository.save(solution);
            return new ResponseVO(ResultCode.SUCCESS);
        }
        if (segment.getOccupy() == segment.getMode()) {
            return new ResponseVO(ResultCode.RESERVE_FAILED);
        }
        return null;
    }

    public ResponseVO cancelReservedSolution(String studentId, String solutionId, int segmentId) {
        Solution solution = solutionRepository.findById(solutionId).get();

        Segment segment = solution.getSegments().get(segmentId - 1);
        log.info("segment: {}", segment);
        User user = userMapper.selectById(studentId);
        String studentName = user.getName();
        //如果此人在预约名单中，则取消预约
        if (segment.getMembers().contains(studentName)) {
            segment.setOccupy(segment.getOccupy() - 1);
            segment.getMembers().remove(studentName);
        } else {
            return new ResponseVO(ResultCode.RESERVE_CANCEL_ERROR);
        }
        solutionRepository.save(solution);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO cancelSolution(String solutionId) {
        solutionRepository.deleteById(solutionId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<Solution> designSolution(
            String teacherId,
            String localDate,
            String licenseNumber,
            String teacherName,
            int weekday,
            int startTime,
            int endTime,
            int mode,
            int category,int weekOfYear) {
        //TODO: 根据日期，星期，时间，模式，类别，教师id，生成一个方案


        List<Solution> solutions = new ArrayList<>();


        int totalTime = endTime - startTime;


        for (int i = 1; i <= totalTime; i++) {
            Solution solution = new Solution();
            solution.setTeacherId(teacherId);
            solution.setCategory(category);
            log.info("localDate: {}", localDate);
            solution.setLocalDate(LocalDate.parse(localDate));
            solution.setTeacherName(teacherName);
            solution.setWeekday(weekday);
            solution.setLicenseNumber(licenseNumber);
            solution.setWeekOfYear(weekOfYear);
            solution.setSegments(new ArrayList<Segment>());

            if (totalTime % (mode * i) != 0) {
                continue;
            }
            int averageTime = totalTime / mode / i;

            if (averageTime > maxMinutes) {
                continue;
            }

            //结束条件
            if (averageTime < minMinutes) {
                break;
            }
            for (int j = 1; j <= i; j++) {
                //j为第几段
                Segment segment = new Segment();
                segment.setSegmentId(j);
                segment.setMode(mode);
                segment.setOccupy(0);
                segment.setMembers(new ArrayList<String>());
                segment.setPractice(averageTime);
                segment.setStartTime(startTime + mode * averageTime * (j - 1));
                segment.setEndTime(startTime + mode * averageTime * j);

                //保存加入数组

                solution.getSegments().add(segment);
            }

            solutions.add(solution);

        }

        return solutions;

    }

    public ResponseVO saveSolution(Solution solution) {
        solutionRepository.save(solution);
        return new ResponseVO(ResultCode.SUCCESS);
    }
}
