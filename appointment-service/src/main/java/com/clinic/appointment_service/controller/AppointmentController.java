package com.clinic.appointment_service.controller;

import com.clinic.appointment_service.dto.ApiResponse;
import com.clinic.appointment_service.entity.Appointment;
import com.clinic.appointment_service.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 1. 建立掛號 (會自動跨服務檢查病人 整合 ApiResponse)
    // POST http://localhost:8091/api/appointments?idNumber=C123456789&scheduleId=2
    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> createAppointment(
            @RequestParam String idNumber, 
            @RequestParam Long scheduleId) {
        Appointment appointment = appointmentService.createAppointment(idNumber, scheduleId);
        return ResponseEntity.ok(ApiResponse.success("掛號成功", appointment));
    }

    // 2. 查詢某病人的所有掛號紀錄
    // 例如: GET http://localhost:8091/api/appointments/patient/A123456789
    @GetMapping("/patient/{idNumber}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable String idNumber) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(idNumber);
        return ResponseEntity.ok(appointments);
    }

    // 3. 查詢某診次（排班）的所有掛號紀錄
    // 例如: GET http://localhost:8091/api/appointments/schedule/1
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Appointment>> getAppointmentsBySchedule(@PathVariable Long scheduleId) {
        List<Appointment> appointments = appointmentService.getAppointmentsBySchedule(scheduleId);
        return ResponseEntity.ok(appointments);
    }

    // 4. 取消掛號
    // 例如: PUT http://localhost:8091/api/appointments/1/cancel
     @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("掛號取消成功，名額已釋放", null));
    }   
}