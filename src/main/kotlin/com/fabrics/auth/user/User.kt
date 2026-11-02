package com.fabrics.auth.user

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class User(
	var email: String?,
	var nickname: String?,
	var profileImageUrl: String?,
) {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	var id: UUID? = null

	@Enumerated(EnumType.STRING)
	var status: UserStatus = UserStatus.ACTIVE

	val createdAt: Instant = Instant.now()
}

enum class UserStatus { ACTIVE, SUSPENDED, WITHDRAWING }
