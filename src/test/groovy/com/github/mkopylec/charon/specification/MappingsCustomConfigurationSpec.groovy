package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.MappingsRepository
import org.springframework.beans.factory.annotation.Autowired

class MappingsCustomConfigurationSpec extends BasicSpec {

    @Autowired
    private MappingsRepository mappingsRepository

    def "Should get mapping custom configuration properties"() {
        given:
        def mapping = mappingsRepository.getMappings().find { mapping -> mapping.name == 'proxy 1' }

        when:
        def customConfiguration = mapping.customConfiguration

        then:
        customConfiguration.boolean
        customConfiguration.string == 'custom string'
        customConfiguration.integer == 666
    }
}
