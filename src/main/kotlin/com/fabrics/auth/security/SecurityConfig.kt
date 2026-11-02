package com.fabrics.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher

@Configuration
class SecurityConfig {
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http {
			authorizeHttpRequests {
				authorize(anyRequest, authenticated)
			}
			// TODO: 앱·웹 교환 토큰 흐름이 생기면 성공 후 이동 대상을 그 흐름으로 바꾼다
			oauth2Login {
				defaultSuccessUrl("/api/me", true)
			}
			exceptionHandling {
				defaultAuthenticationEntryPointFor(
					HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
					PathPatternRequestMatcher.withDefaults().matcher("/api/**"),
				)
			}
		}
		return http.build()
	}
}
