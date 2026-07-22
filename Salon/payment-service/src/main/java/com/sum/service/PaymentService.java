package com.sum.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.sum.domain.PaymentMethod;
import com.sum.model.PaymentOrder;
import com.sum.payload.dto.BookingDTO;
import com.sum.payload.dto.UserDTO;
import com.sum.payload.response.PaymentLinkResponse;

public interface PaymentService {
PaymentLinkResponse createOrder(UserDTO user,
                                BookingDTO booking,
                                PaymentMethod paymentMethod) throws RazorpayException, StripeException;
PaymentOrder getPaymentOrderById(Long Id) throws Exception;

PaymentOrder getPaymentOrderByPaymentId(String paymentId);

PaymentLink createRazorPayPaymentLink(UserDTO user,
                                      Long amount,
                                      Long orderId) throws RazorpayException;
String createStripePaymentLink(UserDTO user, Long amount,Long orderId) throws StripeException;

Boolean proceedPayment(PaymentOrder paymentOrder,String paymentId,
                       String paymentLinkId) throws RazorpayException;
}
