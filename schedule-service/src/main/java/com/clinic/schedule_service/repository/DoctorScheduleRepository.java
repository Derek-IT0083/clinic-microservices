package com.clinic.schedule_service.repository;

import com.clinic.schedule_service.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    // 依據日期查詢當天的所有排班
    // List<DoctorSchedule> findByWorkDate(LocalDate workDate);
    //2次更新
    // 1. 依據日期查詢班表
    List<DoctorSchedule> findByWorkDate(LocalDate workDate);

    // 2. 依據科別與日期區間查詢（常用於前端掛號頁面篩選）
    List<DoctorSchedule> findByDepartmentAndDoctorNameAndWorkDateBetween(String department, String doctorName, LocalDate startDate, LocalDate endDate);

    // 3. 用於檢查重複排班：查詢特定醫生、特定日期與特定時段是否已有班
    Optional<DoctorSchedule> findByDoctorIdAndDoctorNameAndWorkDateAndShiftType(String doctorId, String doctorName, LocalDate workDate, String shiftType);
}