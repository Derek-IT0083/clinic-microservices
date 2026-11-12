package com.clinic.schedule_service.controller;

import com.clinic.schedule_service.entity.DoctorSchedule;
import com.clinic.schedule_service.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/schedules")
public class ScheduleInternalController {
    
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    // 1. 查詢排班資訊
    @GetMapping("/{id}")
    public ResponseEntity<DoctorSchedule> getDoctorScheduleById(@PathVariable Long id) {
        return doctorScheduleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. 扣減排班名額
    @PutMapping("/{id}/decrement")
    public ResponseEntity<String> decrementSlot(@PathVariable Long id) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(id).orElse(null);
        if (doctorSchedule == null) {
            return ResponseEntity.badRequest().body("找不到此排班");
        }
        
        if (doctorSchedule.getAvailableSlots() <= 0) {
            return ResponseEntity.badRequest().body("此時段已額滿");
        }

        doctorSchedule.setAvailableSlots(doctorSchedule.getAvailableSlots() - 1);
        doctorScheduleRepository.save(doctorSchedule);
        return ResponseEntity.ok("名額扣減成功");
    }

    // 3. 回復/加回排班名額（取消掛號時使用）
    @PutMapping("/{id}/increment")
    public ResponseEntity<String> incrementSlot(@PathVariable Long id) {
        DoctorSchedule doctorSchedule = doctorScheduleRepository.findById(id).orElse(null);
        if (doctorSchedule == null) {
            return ResponseEntity.badRequest().body("找不到此排班");
        }

        doctorSchedule.setAvailableSlots(doctorSchedule.getAvailableSlots() + 1);
        doctorScheduleRepository.save(doctorSchedule);
        return ResponseEntity.ok("名額已成功加回");
    }

}
