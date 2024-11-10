package com.example.springboot_kotlin

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<User>
}

// Extended Repository
@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByFromUser(user: User): List<Payment>
    fun findByToUser(user: User): List<Payment>
}