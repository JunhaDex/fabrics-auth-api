package com.fabrics.auth.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID>

interface SocialAccountRepository : JpaRepository<SocialAccount, Long> {
	fun findByProviderAndProviderUserId(provider: Provider, providerUserId: String): SocialAccount?
}
