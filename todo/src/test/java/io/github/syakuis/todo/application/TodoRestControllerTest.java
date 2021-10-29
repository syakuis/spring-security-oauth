package io.github.syakuis.todo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi.
 * @since 2021-10-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TodoRestControllerTest {

    @Autowired
    private MockMvc mvc;
    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void init() {
        headers.setBearerAuth("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiJjN2I3ODJiNS1mOGI1LTExZWItOTcyZi0wMmEyMDg1MDZiZDYiLCJhZGRpdGlvbmFsSW5mb3JtYXRpb24iOm51bGwsInVzZXJfbmFtZSI6InRlc3QiLCJzY29wZSI6WyJyZWFkIl0sIm5hbWUiOiLthYzsiqTtirgiLCJleHAiOjE2MzUwNzQ3ODksImp0aSI6IjY2OTY0M2Q4LTdjNzUtNGFhOC1iZDc3LThiZGI0Yzg5NDhiNSIsImNsaWVudF9pZCI6IjVmNDg5NjA4MGVjZWU2Yzc4MTdjNjhhZmFmNjM0YjA0ZWM3NGU5Y2NjY2Q5YjY0YTY5ODQ1Mjg0ZWYzNmZmNGZkYjk3NDgwZGVhYWUxNTM1In0.PrREKEOSGo5O0k9qUjBh6rMiFz2ssOD72-0I5Zn_8JKhNbQ0X29mbpEaixz9l4bGIWamPkkWo50bTP909yhJzLlDXUn-teX5W2xOQH4QIw-xTQJZogXpcJ8eA2a_Bb0vHfr3rMlALqoXNkTve2h5l6UOIKLgF-1BwIRmIQfEYJL-Lupfd1lHExVdScHQtrjPAxtcI7LZRDhpDgae0CEr5pRZlue-2tcnCmyT3AUJ4souBOxqqCqdFptJOiUySTTnCaQGoNLT_siOIle_qLgQuXLYFRNZs-g18TYleyj4WnhOq3taAyBB5ECnvw4IzWS3CdnMu4kd4lLnRZt1Z3bPMg");
    }

    @Test
    void todo() throws Exception {
        mvc.perform(get("/todo/v1/todos/{id}", "1").headers(headers))
            .andExpect(status().isOk())
        ;
    }
}