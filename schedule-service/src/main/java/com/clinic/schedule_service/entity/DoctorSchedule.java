package com.clinic.schedule_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedules")
@Data
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doctorId;     // 醫生編號
    private String department;   // 科別 (例如: 內科, 小兒科)
    private String doctorName;   // 醫生姓名
    private LocalDate workDate;  // 看診日期
    
    @Column(nullable = true)
    private LocalTime startTime;
    
    @Column(nullable = true)
    private LocalTime endTime;
    
    private String shiftType;    // 診別 (MORNING: 早診, AFTERNOON: 午診, EVENING: 晚診)
    private Integer maxPatients; // 最大可預約人數
    
    // 在 schedule-service 的 DoctorSchedule.java 中確保有類似欄位
    private Integer availableSlots; // 剩餘可掛號名額

    // 新增排班時，若沒帶剩餘名額，預設等於最大可預約人數
    @PrePersist
    public void prePersist() {
        if (availableSlots == null) {
            availableSlots = maxPatients;
        }
    }

}
