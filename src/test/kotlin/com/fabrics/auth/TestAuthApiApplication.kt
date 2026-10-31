package com.fabrics.auth

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AuthApiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
