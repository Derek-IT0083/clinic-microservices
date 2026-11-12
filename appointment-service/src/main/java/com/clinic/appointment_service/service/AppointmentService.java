package com.clinic.appointment_service.service;

import com.clinic.appointment_service.entity.Appointment;
import com.clinic.appointment_service.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Appointment createAppointment(String idNumber, Long scheduleId) {
        // 1. 跨服務呼叫 patient-service 驗證病人是否存在
        String patientUrl = "http://localhost:8090/api/patients/id-number/" + idNumber;
        try {
            Object patientResponse = restTemplate.getForObject(patientUrl, Object.class);
            if (patientResponse == null) {
                throw new RuntimeException("找不到該病人資料，無法掛號");
            }
        } catch (Exception e) {
            throw new RuntimeException("驗證病人失敗或伺服器未開通: " + idNumber);
        }

        // 2. 跨服務呼叫 schedule-service 檢查排班是否存在並扣減名額
        String scheduleCheckUrl = "http://localhost:8089/api/internal/schedules/" + scheduleId;
        String scheduleDecrementUrl = "http://localhost:8089/api/internal/schedules/" + scheduleId + "/decrement";
        try {
            // 檢查排班是否存在
            Object scheduleResponse = restTemplate.getForObject(scheduleCheckUrl, Object.class);
            if (scheduleResponse == null) {
                throw new RuntimeException("找不到指定的排班時段");
            }

            // 執行扣減名額
            restTemplate.put(scheduleDecrementUrl, null);

        } catch (Exception e) {
            throw new RuntimeException("排班檢查或名額扣減失敗（可能已額滿或診次不存在）: " + e.getMessage());
        }

        // 3. 建立掛號紀錄
        Appointment appointment = new Appointment();
        appointment.setPatientIdNumber(idNumber);
        appointment.setScheduleId(scheduleId);
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setStatus("BOOKED");

        return appointmentRepository.save(appointment);
    }

    // 4. 依病人身分證查詢掛號清單
    public List<Appointment> getAppointmentsByPatient(String idNumber) {
        return appointmentRepository.findByPatientIdNumber(idNumber);
    }

    // 5. 依排班 ID 查詢掛號清單
    public List<Appointment> getAppointmentsBySchedule(Long scheduleId) {
        return appointmentRepository.findByScheduleId(scheduleId);
    }

    // 6. 取消掛號
    @Transactional
    public boolean cancelAppointment(Long appointmentId) {
        // 1. 尋找該筆掛號紀錄
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            throw new RuntimeException("找不到此掛號紀錄");
        }

        // 2. 檢查是否已經取消過
        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException("此掛號已經是取消狀態");
        }

        // 3. 跨服務呼叫 schedule-service 將排班名額加回來
        Long scheduleId = appointment.getScheduleId();
        String incrementUrl = "http://localhost:8089/api/internal/schedules/" + scheduleId + "/increment";
        try {
            restTemplate.put(incrementUrl, null);
        } catch (Exception e) {
            throw new RuntimeException("通知排班服務加回名額失敗: " + e.getMessage());
        }

        // 4. 更新掛號狀態為 CANCELLED
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
        
        return true;
    }
}