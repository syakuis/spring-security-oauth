package io.github.syakuis.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ResourceServerApplicationTest {

    @Autowired
    private MessageSourceAccessor i18n;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void hello() {
        String name = i18n.getMessage("author.name");
        Assertions.assertEquals("Seokkyun. Choi.", name);
        log.debug(name);
    }

    @Test
    void objectMapper() throws Exception {
        String json = "[1,2,3,4]";
        objectMapper.readValue(json, ArrayList.class);
        Assertions.assertEquals(
            Arrays.asList(1, 2, 3, 4),
            objectMapper.readValue(json, ArrayList.class));
    }
}
