package com.sum.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.sum.domain.PaymentMethod;
import com.sum.model.PaymentOrder;
import com.sum.payload.dto.BookingDTO;
import com.sum.payload.dto.UserDTO;
import com.sum.payload.response.PaymentLinkResponse;
import com.sum.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor

public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO booking,
            @RequestParam PaymentMethod paymentMethod
            ) throws Exception {
        UserDTO user = new UserDTO();
        user.setFullName("Raja");
        user.setEmail("raja@gmail.com");
        user.setId(1L);

        PaymentLinkResponse response = paymentService.createOrder(user, booking, paymentMethod);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable Long paymentOrderId
    ) throws Exception {
        PaymentOrder response = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
     @RequestParam String paymentId,
     @RequestParam String paymentLinkId
    ) throws Exception {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentId);
        boolean response = paymentService.proceedPayment(paymentOrder,paymentId,paymentLinkId);
        return ResponseEntity.ok(response);
    }


}
