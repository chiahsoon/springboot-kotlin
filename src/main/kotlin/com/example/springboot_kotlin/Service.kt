import com.example.springboot_kotlin.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

// Service
@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    fun createUser(name: String): User {
        val user = User(name = name)
        return userRepository.save(user)
    }

    fun getUser(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun updateUser(id: Long, name: String): User? {
        return userRepository.findByIdOrNull(id)?.also { user ->
            user.name = name
            userRepository.save(user)
        }
    }

    fun deleteUser(id: Long): Boolean {
        return userRepository.findByIdOrNull(id)?.let { user ->
            userRepository.delete(user)
            true
        } ?: false
    }

    fun searchUsersByName(name: String): List<User> {
        return userRepository.findByNameContainingIgnoreCase(name)
    }
}

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createPayment(fromUserId: Long, toUserId: Long, amount: BigDecimal): Payment {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Amount must be positive")
        }

        val fromUser = userRepository.findByIdOrNull(fromUserId)
            ?: throw IllegalArgumentException("From user not found")

        val toUser = userRepository.findByIdOrNull(toUserId)
            ?: throw IllegalArgumentException("To user not found")

        if (fromUser.id == toUser.id) {
            throw IllegalArgumentException("Cannot make payment to self")
        }

        val payment = Payment(
            fromUser = fromUser,
            toUser = toUser,
            amount = amount
        )

        return paymentRepository.save(payment)
    }

    fun getPaymentsByUser(userId: Long): UserPaymentsSummary {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw IllegalArgumentException("User not found")

        val sentPayments = paymentRepository.findByFromUser(user)
        val receivedPayments = paymentRepository.findByToUser(user)

        return UserPaymentsSummary(
            userId = userId,
            totalSent = sentPayments.sumOf { it.amount },
            totalReceived = receivedPayments.sumOf { it.amount },
            sentPayments = sentPayments,
            receivedPayments = receivedPayments
        )
    }

    fun getAllPayments(): List<Payment> = paymentRepository.findAll()
}