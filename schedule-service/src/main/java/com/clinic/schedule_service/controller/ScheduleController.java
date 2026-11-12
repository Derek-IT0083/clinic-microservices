package com.clinic.schedule_service.controller;

import com.clinic.schedule_service.entity.DoctorSchedule;
import com.clinic.schedule_service.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private DoctorScheduleRepository doctorscheduleRepository;

    // 1. 新增排班
    // @PostMapping
    // public DoctorSchedule createSchedule(@RequestBody DoctorSchedule schedule) {
    //     return scheduleRepository.save(schedule);
    // }

    // 2. 查詢指定日期的排班
    // @GetMapping
    // public List<DoctorSchedule> getSchedulesByDate(@RequestParam("date") LocalDate date) {
    //     return scheduleRepository.findByWorkDate(date);
    // }
    
    //2次更新
    // 1. 新增排班（含防重複排呆邏輯）
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody DoctorSchedule schedule) {
        // 檢查該醫生在同日同時段是否已經有排班
        Optional<DoctorSchedule> existingSchedule = doctorscheduleRepository.findByDoctorIdAndDoctorNameAndWorkDateAndShiftType(            
            schedule.getDoctorName(),
            schedule.getDoctorId(), 
            schedule.getWorkDate(), 
            schedule.getShiftType()
        );

        if (existingSchedule.isPresent()) {
            // 如果已存在，回傳 HTTP 400 Bad Request 與中文錯誤訊息
            return ResponseEntity.badRequest().body("錯誤：該醫生在此日期與時段已有排班記錄，不可重複建立！");
        }

        // 若無重複則順利儲存並回傳 HTTP 200 OK 與儲存後的物件
        DoctorSchedule savedSchedule = doctorscheduleRepository.save(schedule);
        return ResponseEntity.ok(savedSchedule);
    }

    // 2. 查詢指定日期的排班
    @GetMapping
    public List<DoctorSchedule> getSchedulesByDate(@RequestParam("date") LocalDate date) {
        return doctorscheduleRepository.findByWorkDate(date);
    }

    // 3. 依據科別與日期範圍搜尋班表（進階查詢）
    @GetMapping("/search")
    public List<DoctorSchedule> searchSchedules(
            @RequestParam("doctorName") String doctorName,
            @RequestParam("department") String department,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        
        return doctorscheduleRepository.findByDepartmentAndDoctorNameAndWorkDateBetween(department, doctorName, startDate, endDate);
    }
}