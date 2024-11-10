package com.example.springboot_kotlin

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

// Data classes for API
data class CreatePaymentRequest(
    val fromUserId: Long,
    val toUserId: Long,
    val amount: BigDecimal
)

data class UserPaymentsSummary(
    val userId: Long,
    val totalSent: BigDecimal,
    val totalReceived: BigDecimal,
    val sentPayments: List<Payment>,
    val receivedPayments: List<Payment>
)

data class CreateUserRequest(
    @field:NotBlank(message = "Name is required")
    val name: String
)

data class UpdateUserRequest(
    @field:NotBlank(message = "Name is required")
    val name: String
)

data class UserResponse(
    val id: Long,
    val name: String
)