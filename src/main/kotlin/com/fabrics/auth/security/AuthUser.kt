package com.fabrics.auth.security

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.util.UUID

/** 로그인한 fabrics 회원. [getName]은 제공자 ID가 아니라 회원 ID다. */
class AuthUser(val userId: UUID, oidcUser: OidcUser) :
	DefaultOidcUser(oidcUser.authorities, oidcUser.idToken, oidcUser.userInfo) {
	override fun getName(): String = userId.toString()
}
