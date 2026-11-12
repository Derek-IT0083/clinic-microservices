package com.clinic.patient_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idNumber; // 身分證字號 / 居留證號

    @Column(nullable = false)
    private String name;     // 姓名

    @Column(nullable = false)
    private String phone;    // 聯絡電話

    private LocalDate birthDate; // 生日

    private String gender;       // 性別
}