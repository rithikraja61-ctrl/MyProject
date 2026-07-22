package com.sum.model;

import lombok.Data;

@Data
public class SalonReport {
    private Long salonId;
    private String salonName;
    private Integer totalEarnings;
    private Integer totalBookings;
    private Integer cancelledBookings;
    private Double totalRefund;

}
