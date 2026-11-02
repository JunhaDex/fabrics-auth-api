package com.fabrics.auth.user

import com.fabrics.auth.TestcontainersConfiguration
import com.fabrics.auth.security.AuthUser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.Instant

@Import(TestcontainersConfiguration::class)
@SpringBootTest
@AutoConfigureMockMvc
class MeControllerTests(
	@Autowired private val mvc: MockMvc,
	@Autowired private val service: SocialLoginService,
) {
	@Test
	fun `api without login returns 401`() {
		mvc.get("/api/me").andExpect { status { isUnauthorized() } }
	}

	@Test
	fun `page without login redirects to google`() {
		mvc.get("/") { accept = MediaType.TEXT_HTML }.andExpect {
			status { is3xxRedirection() }
			redirectedUrl("/oauth2/authorization/google")
		}
	}

	@Test
	fun `me returns logged in user`() {
		val profile = SocialLoginService.Profile("me@example.com", "me", null)
		val userId = service.login(Provider.GOOGLE, "sub-me", profile)
		val idToken = OidcIdToken("token", Instant.now(), Instant.now().plusSeconds(60), mapOf("sub" to "sub-me"))
		val principal = AuthUser(userId, DefaultOidcUser(emptyList(), idToken))

		mvc.get("/api/me") {
			with(authentication(OAuth2AuthenticationToken(principal, principal.authorities, "google")))
		}.andExpect {
			status { isOk() }
			jsonPath("$.id") { value(userId.toString()) }
			jsonPath("$.email") { value("me@example.com") }
			jsonPath("$.nickname") { value("me") }
		}
	}
}
