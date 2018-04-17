package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.MappingsRepository
import com.github.mkopylec.charon.configuration.MappingProperties
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

class MappingsMetadataSpec extends BasicSpec {

    @Autowired
    private MappingsRepository mappingsRepository

    @DirtiesContext
    def "Should get metadata informations in mapping"() {
        when:
        MappingProperties mapping = mappingsRepository.getMappings().stream().filter({ m -> m.name.equals('proxy 1')}).findFirst().get();

        then:
        Assert.assertEquals(mapping.getMetadata().get('custom-str'), "server.org")
        Assert.assertEquals(mapping.getMetadata().get('custom-int'), "42")
        Assert.assertEquals(mapping.getMetadata().get('custom-boolean'), "true")
    }
}
