package com.fabrics.auth.user

import com.fabrics.auth.security.AuthUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MeController(private val users: UserRepository) {
	@GetMapping("/api/me")
	fun me(@AuthenticationPrincipal principal: AuthUser): MeResponse {
		val user = users.findById(principal.userId).orElseThrow()
		return MeResponse(user.id!!, user.email, user.nickname, user.profileImageUrl)
	}

	data class MeResponse(val id: UUID, val email: String?, val nickname: String?, val profileImageUrl: String?)
}
