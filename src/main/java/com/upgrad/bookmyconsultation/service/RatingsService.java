package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Doctor;
import com.upgrad.bookmyconsultation.entity.Rating;
import com.upgrad.bookmyconsultation.repository.DoctorRepository;
import com.upgrad.bookmyconsultation.repository.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingsService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public void submitRatings(Rating rating) {
        // Set a UUID for the rating
        rating.setId(UUID.randomUUID().toString());

        // Save the rating to the database
        ratingsRepository.save(rating);

        // Get the doctor id from the rating object
        String doctorId = rating.getDoctorId();

        // Find that specific doctor using doctor id
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor != null) {
            // Modify the average rating for that specific doctor by including the new rating
            List<Rating> allRatingsForDoctor = ratingsRepository.findByDoctorId(doctorId);
            double averageRating = allRatingsForDoctor.stream()
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);

            doctor.setRating(averageRating);

            // Save the doctor object to the database
            doctorRepository.save(doctor);
        }
    }
}
