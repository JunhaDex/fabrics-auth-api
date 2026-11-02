package com.fabrics.auth.user

import com.fabrics.auth.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class SocialLoginServiceTests(
	@Autowired private val service: SocialLoginService,
	@Autowired private val users: UserRepository,
) {
	private val profile = SocialLoginService.Profile("a@example.com", "nick", "https://example.com/a.png")

	@Test
	fun `first login creates user with provider profile`() {
		val id = service.login(Provider.GOOGLE, "sub-new", profile)

		val user = users.findById(id).orElseThrow()
		assertEquals("a@example.com", user.email)
		assertEquals("nick", user.nickname)
		assertEquals("https://example.com/a.png", user.profileImageUrl)
		assertEquals(UserStatus.ACTIVE, user.status)
	}

	@Test
	fun `same provider account returns same user`() {
		val first = service.login(Provider.GOOGLE, "sub-repeat", profile)
		val second = service.login(Provider.GOOGLE, "sub-repeat", profile.copy(nickname = "changed"))

		assertEquals(first, second)
		assertEquals("nick", users.findById(first).orElseThrow().nickname)
	}

	@Test
	fun `same email with different provider id creates separate user`() {
		val first = service.login(Provider.GOOGLE, "sub-x", profile)
		val second = service.login(Provider.GOOGLE, "sub-y", profile)

		assertNotEquals(first, second)
	}

	@Test
	fun `missing email is allowed`() {
		val id = service.login(Provider.GOOGLE, "sub-no-email", profile.copy(email = null))

		assertEquals(null, users.findById(id).orElseThrow().email)
	}
}
