package com.ifood.controllers;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AboutControllerTest {

    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AboutController()).build();

    @Test
    public void testApplicationHealthCheck() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().is(200))
                .andExpect(content().string("Service is running."));
    }

}

