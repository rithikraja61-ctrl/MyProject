package com.sum.service;

import com.sum.domain.BookingStatus;
import com.sum.dto.BookingRequest;
import com.sum.dto.SalonDTO;
import com.sum.dto.ServiceDTO;
import com.sum.dto.UserDTO;
import com.sum.model.Booking;
import com.sum.model.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest bookingRequest, UserDTO userDTO,
                          SalonDTO salon,
                          Set<ServiceDTO> serviceDTOSet) throws Exception;

    List<Booking> getBookingsByCustomer(Long customerId);
    List<Booking> getBookingsBySalon(Long salonId);
    Booking getBookingById(Long bookingId) throws Exception;
    Booking updateBooking(Long bookingId, BookingStatus status) throws Exception;
    List<Booking> getBookingsByData(LocalDate date,Long salonId);
    SalonReport getSalonReport(Long salonId);
}
