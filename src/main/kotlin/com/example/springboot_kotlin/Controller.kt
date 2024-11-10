import com.example.springboot_kotlin.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// Controller
@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(private val paymentService: PaymentService) {

    @PostMapping
    fun createPayment(@RequestBody request: CreatePaymentRequest): Payment {
        return paymentService.createPayment(
            fromUserId = request.fromUserId,
            toUserId = request.toUserId,
            amount = request.amount
        )
    }

    @GetMapping("/user/{userId}")
    fun getUserPayments(@PathVariable userId: Long): UserPaymentsSummary {
        return paymentService.getPaymentsByUser(userId)
    }

    @GetMapping
    fun getAllPayments(): List<Payment> {
        return paymentService.getAllPayments()
    }
}

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody request: CreateUserRequest): UserResponse {
        val user = userService.createUser(request.name)
        return UserResponse(user.id, user.name)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUser(id)
        return user?.let {
            ResponseEntity.ok(UserResponse(it.id, it.name))
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllUsers(): List<UserResponse> {
        return userService.getAllUsers()
            .map { UserResponse(it.id, it.name) }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUser(id, request.name)
        return updatedUser?.let {
            ResponseEntity.ok(UserResponse(it.id, it.name))
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        return if (userService.deleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search")
    fun searchUsers(@RequestParam name: String): List<UserResponse> {
        return userService.searchUsersByName(name)
            .map { UserResponse(it.id, it.name) }
    }
}