package com.ifood.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class AboutTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApplicationHealthCheck() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().is(200))
                .andExpect(content().string("Service is running."));
    }

}

