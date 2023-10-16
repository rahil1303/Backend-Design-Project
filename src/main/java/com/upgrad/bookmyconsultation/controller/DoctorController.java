package com.upgrad.bookmyconsultation.controller;

import com.upgrad.bookmyconsultation.model.TimeSlot;
import com.upgrad.bookmyconsultation.entity.Doctor;
import com.upgrad.bookmyconsultation.enums.Speciality;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.service.DoctorService;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private DoctorService service;

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorDetails(@PathVariable String id) {
        logger.info("Fetching details for doctor with ID: {}", id);
        return ResponseEntity.ok(service.getDoctor(id));
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(@RequestParam(value = "speciality", required = false) String speciality) {
        logger.info("Fetching all doctors with speciality: {}", speciality);
        return ResponseEntity.ok(service.getAllDoctorsWithFilters(speciality));
    }

    @PostMapping
    public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) throws InvalidInputException {
        logger.info("Registering new doctor with name: {} {}", doctor.getFirstName(), doctor.getLastName());
        Doctor registeredDoctor = service.register(doctor);
        return new ResponseEntity<>(registeredDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/speciality")
    public ResponseEntity<List<String>> getSpeciality() {
        return ResponseEntity.ok(Stream.of(Speciality.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{doctorId}/timeSlots")
    public ResponseEntity<TimeSlot> getTimeSlots(@RequestParam(value = "date", required = false) String date,
                                                 @PathVariable String doctorId) {
        if (!ValidationUtils.isValid(date)) {
            logger.error("Invalid date provided: {}", date);
            throw new InvalidParameterException("Not a valid date");
        }

        if (!service.doesDoctorExist(doctorId)) {
            logger.error("Invalid doctor ID provided: {}", doctorId);
            throw new InvalidParameterException("Not a valid doctor id");
        }

        logger.info("Fetching time slots for doctor with ID: {} on date: {}", doctorId, date);
        return ResponseEntity.ok(service.getTimeSlots(doctorId, date));
    }
}
