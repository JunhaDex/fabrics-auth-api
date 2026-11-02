package com.fabrics.auth.security

import com.fabrics.auth.user.Provider
import com.fabrics.auth.user.SocialLoginService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

@Component
class OidcLoginUserService(
	private val socialLogin: SocialLoginService,
) : OAuth2UserService<OidcUserRequest, OidcUser> {
	private val delegate = OidcUserService()

	override fun loadUser(request: OidcUserRequest): OidcUser {
		val oidcUser = delegate.loadUser(request)
		val provider = Provider.valueOf(request.clientRegistration.registrationId.uppercase())
		val profile = SocialLoginService.Profile(oidcUser.email, oidcUser.fullName, oidcUser.picture)
		return AuthUser(socialLogin.login(provider, oidcUser.subject!!, profile), oidcUser)
	}
}
