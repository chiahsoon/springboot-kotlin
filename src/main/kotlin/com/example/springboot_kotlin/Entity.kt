package com.example.springboot_kotlin

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.math.BigDecimal

@Entity
@Table(name = "users")
class User(
    @jakarta.persistence.Id @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @OneToMany(mappedBy = "fromUser")
    val paymentsSent: List<Payment> = mutableListOf(),

    @OneToMany(mappedBy = "toUser")
    val paymentsReceived: List<Payment> = mutableListOf()
)

@Entity
@Table(name = "payments")
class Payment(
    @jakarta.persistence.Id @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    val fromUser: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    val toUser: User,

    @Column(nullable = false)
    val amount: BigDecimal
)