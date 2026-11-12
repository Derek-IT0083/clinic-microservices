package com.clinic.appointment_service.repository;

import com.clinic.appointment_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. 根據病人身分證字號查詢所有掛號紀錄
    List<Appointment> findByPatientIdNumber(String patientIdNumber);

    // 2. 根據排班 ID 查詢該診次的所有掛號紀錄
    List<Appointment> findByScheduleId(Long scheduleId);
}