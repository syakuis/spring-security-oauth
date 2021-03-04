package io.github.syakuis.authorization.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FormLoginAndHttpBasicAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void loginUserTest() throws Exception {
        this.mvc.perform(post("/login")
            .with(csrf())
            .param("username", "test")
            .param("password", "1234")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void loginUserAdmin() throws Exception {
        this.mvc.perform(post("/login")
            .with(csrf())
            .param("username", "admin")
            .param("password", "!@#$")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void loginHttpBasic() throws Exception {
        this.mvc.perform(get("/api/profile").with(httpBasic("admin", "!@#$")))
            .andExpect(MockMvcResultMatchers.content().string("ok"))
            .andExpect(status().isOk());
    }

}
