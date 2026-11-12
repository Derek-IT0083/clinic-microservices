package com.clinic.schedule_service.controller;

import com.clinic.schedule_service.entity.DoctorSchedule;
import com.clinic.schedule_service.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

@Controller
public class DoctorScheduleGraphController {
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @QueryMapping
    public List<DoctorSchedule> searchDoctorSchedules(
            @Argument String department,
            @Argument String doctorName,
            @Argument String startDate,
            @Argument String endDate) {
        
        // 將前端傳入的 String 日期轉為 LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        // 呼叫我們之前調校好的 Repository 方法
        return doctorScheduleRepository.findByDepartmentAndDoctorNameAndWorkDateBetween(
            department, doctorName, start, end
        );
    }

    // 1. 新增排班 (Create)
    @MutationMapping
    public DoctorSchedule createSchedule(
            @Argument String doctorId,
            @Argument String department,
            @Argument String doctorName,
            @Argument String workDate,
            @Argument String shiftType,
            @Argument String startTime,
            @Argument String endTime,
            @Argument Integer maxPatients) {

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctorId);
        schedule.setDepartment(department);
        schedule.setDoctorName(doctorName);
        schedule.setWorkDate(LocalDate.parse(workDate));
        schedule.setShiftType(shiftType);
        schedule.setStartTime(LocalTime.parse(startTime));
        schedule.setEndTime(LocalTime.parse(endTime));
        schedule.setMaxPatients(maxPatients);

        return doctorScheduleRepository.save(schedule);
    }

    // 2. 修改排班 (Update)
    @MutationMapping
    public DoctorSchedule updateSchedule(
            @Argument Long id,
            @Argument String doctorId,
            @Argument String department,
            @Argument String doctorName,
            @Argument String workDate,
            @Argument String shiftType,
            @Argument String startTime,
            @Argument String endTime,
            @Argument Integer maxPatients) {

        DoctorSchedule schedule = doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + id + " 的排班資料"));

        schedule.setDoctorId(doctorId);
        schedule.setDepartment(department);
        schedule.setDoctorName(doctorName);
        schedule.setWorkDate(LocalDate.parse(workDate));
        schedule.setShiftType(shiftType);
        schedule.setStartTime(LocalTime.parse(startTime));
        schedule.setEndTime(LocalTime.parse(endTime));
        schedule.setMaxPatients(maxPatients);

        return doctorScheduleRepository.save(schedule);
    }

    // 3. 刪除排班 (Delete)
    @MutationMapping
    public Boolean deleteSchedule(@Argument Long id) {
        if (doctorScheduleRepository.existsById(id)) {
            doctorScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
