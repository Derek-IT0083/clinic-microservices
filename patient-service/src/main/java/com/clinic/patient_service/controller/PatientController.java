package com.clinic.patient_service.controller;

import com.clinic.patient_service.entity.Patient;
import com.clinic.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // 1. 查詢所有病人
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // 2. 新增病人資料
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    // 3. 根據身分證字號查詢病人
    @GetMapping("/id-number/{idNumber}")
    public Patient getPatientByIdNumber(@PathVariable String idNumber) {
        return patientRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new RuntimeException("找不到該身分證字號的病人: " + idNumber));
    }
}