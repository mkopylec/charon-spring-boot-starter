package com.github.mkopylec.charon.specification

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ['server.servlet.context-path: /context'])
class NonEmptyContextPathProxyingRequestSpec extends ProxyingRequestSpec {
}
