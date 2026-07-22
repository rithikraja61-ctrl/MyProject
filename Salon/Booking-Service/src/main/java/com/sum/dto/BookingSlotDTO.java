package com.sum.dto;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
@Data
public class BookingSlotDTO
{
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
