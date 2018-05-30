package com.github.mkopylec.charon.specification

import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ['server.servlet.context-path: /'])
class EmptyContextPathProxyingRequestSpec extends ProxyingRequestSpec {
}
