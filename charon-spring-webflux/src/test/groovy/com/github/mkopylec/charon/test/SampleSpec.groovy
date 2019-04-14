package com.github.mkopylec.charon.test

import com.github.mkopylec.charon.test.specification.BasicSpec

class SampleSpec extends BasicSpec {

    def "Should"() {
        when:
        def x = true
        println restTemplate.getForObject("/dupa", String);
        then:
        x
    }
}
