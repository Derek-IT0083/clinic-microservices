package com.clinic.patient_service.repository;

import com.clinic.patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // 支援透過身分證字號查詢病人
    Optional<Patient> findByIdNumber(String idNumber);
}