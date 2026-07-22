package com.sum.mapper;

import com.sum.dto.BookingDTO;
import com.sum.model.Booking;

public class BookingMapper {
    public static BookingDTO toDTO(Booking booking)
    {
        BookingDTO bookingDTO =new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setServiceIds(booking.getServiceIds());
        return bookingDTO;
    }
}
