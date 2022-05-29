package com.kkkzoz.service;


import com.kkkzoz.domain.entity.Segment;
import com.kkkzoz.domain.entity.Solution;
import com.kkkzoz.global.ResponseVO;
import com.kkkzoz.global.ResultCode;
import com.kkkzoz.repository.SolutionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TimeManagerService {


    private final SolutionRepository solutionRepository;

    private static final int minMinutes = 30;


    //查询对应教练的solution
    public List<Solution> getSolutionList(int category, int teacherId,int weekOfYear) {
        return solutionRepository
                .findByCategoryAndTeacherIdAndWeekOfYear(category, teacherId,weekOfYear);
    }

    public List<Solution> getSolutionList(int teacherId,int weekOfYear) {
        return solutionRepository
                .findByTeacherIdAndWeekOfYear(teacherId,weekOfYear);

    }

    public ResponseVO addSolution(Solution solution) {
        solutionRepository.save(solution);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO reserveSolution(Long studentId, String solutionId, int segmentId) {
        Solution solution = solutionRepository.findById(solutionId).get();

        Segment segment = solution.getSegments().get(segmentId - 1);
        log.info("segment: {}", segment);
        //如果还能预约
        if (segment.getOccupy() < segment.getMode()) {
            segment.setOccupy(segment.getOccupy() + 1);
            List<Long> members = segment.getMembers();
            //如果未出现在预约名单中，才向名单中注册此人
            if (!members.contains(studentId)) {
                members.add(studentId);
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

    public ResponseVO cancelReservedSolution(Long studentId, String solutionId, int segmentId) {
        Solution solution = solutionRepository.findById(solutionId).get();

        Segment segment = solution.getSegments().get(segmentId - 1);
        log.info("segment: {}", segment);
        //如果此人在预约名单中，则取消预约
        if (segment.getMembers().contains(studentId)) {
            segment.setOccupy(segment.getOccupy() - 1);
            segment.getMembers().remove(studentId);
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
            Long teacherId,
            String localDate,
            int weekday,
            int startTime,
            int endTime,
            int mode,
            int category) {
        //TODO: 根据日期，星期，时间，模式，类别，教师id，生成一个方案


        List<Solution> solutions = new ArrayList<>();


        int totalTime = endTime - startTime;


        for (int i = 1; i <= totalTime; i++) {
            Solution solution = new Solution();
            solution.setTeacherId(Math.toIntExact(teacherId));
            solution.setCategory(category);
            solution.setLocalDate(localDate);
            solution.setWeekday(weekday);
            solution.setSegments(new ArrayList<Segment>());


            int averageTime = totalTime / mode / i;
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
                segment.setMembers(new ArrayList<Long>());
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
