package com.sum.service.impl;

import com.sum.domain.BookingStatus;
import com.sum.dto.BookingRequest;
import com.sum.dto.SalonDTO;
import com.sum.dto.ServiceDTO;
import com.sum.dto.UserDTO;
import com.sum.model.Booking;
import com.sum.model.SalonReport;
import com.sum.repository.BookingRepository;
import com.sum.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BookingRepository bookingRepository;
    @Override
    public Booking createBooking(BookingRequest booking,
                                 UserDTO userDTO,
                                 SalonDTO salon,
                                 Set<ServiceDTO> serviceDTOSet) throws Exception {
        int totalDuration = serviceDTOSet.stream().mapToInt(ServiceDTO::getDuration).sum();
        LocalDateTime bookingStartTime=booking.getStartTime();
        LocalDateTime bookingEndTime=bookingStartTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable=isTimeSlotAvailable(salon,bookingStartTime,bookingEndTime);
        int totalPrice = serviceDTOSet.stream().mapToInt(ServiceDTO::getPrice).sum();
        Set<Long> idList=serviceDTOSet.stream().map(ServiceDTO::getId).collect(Collectors.toSet());
        Booking newBooking=new Booking();
        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setServiceIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setTotalPrice(totalPrice);
       return bookingRepository.save(newBooking);

    }
     public Boolean isTimeSlotAvailable(SalonDTO salonDTO,LocalDateTime bookingStartTime,
                                        LocalDateTime bookingEndTime) throws Exception {
         List<Booking> existingBookings = getBookingsByCustomer(salonDTO.getId());
         LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
         LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingStartTime.toLocalDate());
         
         logger.info("DEBUG: salonOpenTime = {}", salonOpenTime);
         logger.info("DEBUG: salonCloseTime (before adjustment) = {}", salonCloseTime);
         logger.info("DEBUG: bookingStartTime = {}", bookingStartTime);
         logger.info("DEBUG: bookingEndTime = {}", bookingEndTime);
         
         // If close time is before open time, salon is open overnight (e.g., 22:00 to 10:00)
         // In this case, close time is on the next day
         if (salonCloseTime.isBefore(salonOpenTime)) {
             logger.info("DEBUG: Detected overnight salon hours, adding 1 day to closeTime");
             salonCloseTime = salonCloseTime.plusDays(1);
         }
         
         logger.info("DEBUG: salonCloseTime (after adjustment) = {}", salonCloseTime);
         logger.info("DEBUG: bookingStartTime.isBefore(salonOpenTime) = {}", bookingStartTime.isBefore(salonOpenTime));
         logger.info("DEBUG: bookingEndTime.isAfter(salonCloseTime) = {}", bookingEndTime.isAfter(salonCloseTime));
         
         if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
             throw new Exception("Booking time must be within salon working hours");
         }
         for (Booking existingBooking : existingBookings) {
             LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
             LocalDateTime existingBookingEndTime = existingBooking.getEndTime();
             if (bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)) {
                 throw new Exception("Booking time slot is not available");
             }
             if(bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)) {
                 throw new Exception("Booking time slot is not available");
             }
         }
         return true;
     }
    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new Exception("Booking not exist with id" + bookingId);
        }
        return booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) throws Exception {
        Booking booking=getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByData(LocalDate date, Long salonId) {
        List<Booking> allBookings=getBookingsBySalon(salonId);
        if(date==null)
        {
            return allBookings;
        }
       return allBookings.stream().filter(booking ->isSameDate(booking.getStartTime(),date)|| isSameDate(booking.getEndTime(),date)).collect(Collectors.toList());

    }
    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);


    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings=getBookingsBySalon(salonId);
        int totalEarnings=bookings.stream().mapToInt(Booking::getTotalPrice).sum();
        Integer totalBookings=bookings.size();
        List<Booking> cancelledBookings=bookings.stream().filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED)).toList();
        Double totalRefund=cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();
         SalonReport report=new SalonReport();
         report.setSalonId(salonId);
         report.setTotalEarnings(totalEarnings);
         report.setTotalBookings(totalBookings);
         report.setCancelledBookings(cancelledBookings.size());
         report.setTotalRefund(totalRefund);
         report.setTotalBookings(totalBookings);
         return report;


    }
}
