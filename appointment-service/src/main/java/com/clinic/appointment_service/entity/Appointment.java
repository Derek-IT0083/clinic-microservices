package com.clinic.appointment_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientIdNumber; // 病人身分證字號

    @Column(nullable = false)
    private Long scheduleId;        // 排班 ID

    @Column(nullable = false)
    private LocalDateTime appointmentTime; // 掛號時間

    @Column(nullable = false)
    private String status;          // 狀態 (例如: BOOKED, CANCELLED)
}