package com.fabrics.auth.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SocialLoginService(
	private val users: UserRepository,
	private val socialAccounts: SocialAccountRepository,
) {
	/**
	 * 제공자 계정에 연결된 회원 ID를 반환한다. 연결된 회원이 없으면 받은 프로필로 새 회원을 만든다.
	 * 이메일이 같은 기존 회원이 있어도 연결하지 않는다.
	 */
	@Transactional
	fun login(provider: Provider, providerUserId: String, profile: Profile): UUID {
		socialAccounts.findByProviderAndProviderUserId(provider, providerUserId)
			?.let { return it.user.id!! }
		val user = users.save(User(profile.email, profile.nickname, profile.imageUrl))
		socialAccounts.save(SocialAccount(user, provider, providerUserId))
		return user.id!!
	}

	data class Profile(val email: String?, val nickname: String?, val imageUrl: String?)
}
