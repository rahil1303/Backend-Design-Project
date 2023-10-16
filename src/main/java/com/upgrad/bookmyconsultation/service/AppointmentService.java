package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.exception.SlotUnavailableException;
import com.upgrad.bookmyconsultation.repository.AppointmentRepository;
import com.upgrad.bookmyconsultation.repository.UserRepository;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public String bookAppointment(Appointment appointment) throws SlotUnavailableException, InvalidInputException {
        ValidationUtils.validate(appointment);
        Optional<Appointment> existingAppointment = Optional.ofNullable(appointmentRepository.findByDoctorIdAndTimeSlotAndAppointmentDate(appointment.getDoctorId(), appointment.getTimeSlot(), appointment.getAppointmentDate()));
        if (existingAppointment.isPresent()) {
            throw new SlotUnavailableException("The slot is already booked.");
        }
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return savedAppointment.getAppointmentId();
    }

    public Appointment getAppointment(String appointmentId) {
		return appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceUnAvailableException("Appointment with ID: " + appointmentId + " not found"));
	}	

    public List<Appointment> getAppointmentsForUser(String userId) {
        return appointmentRepository.findByUserId(userId);
    }
}
