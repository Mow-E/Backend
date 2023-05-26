package se.mow_e.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.mow_e.models.LoginRequest;

@TestConfiguration
public class RequestTokenConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    public String authRequest(String username, String password,
                              HttpMethod method, String url, int status) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .request(method, url)
                        .content(objectMapper.writeValueAsString(new LoginRequest(username, password)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    public String request(String token, HttpMethod method, String url, int status) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .request(method, url)
                        .header("Authorization", "Bearer " + token)
                        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        return mvcResult.getResponse().getContentAsString();
    }

    public MockHttpServletResponse requestImage(String token, String url) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .request(HttpMethod.GET, url)
                        .header("Authorization", "Bearer " + token).accept(MediaType.IMAGE_JPEG_VALUE)
                        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();

        return mvcResult.getResponse();
    }

    public String getToken(String response) {
        return JsonPath.read(response, "$.token");
    }

    public String getToken(String username, String password,
                           HttpMethod method, String url, int status) throws Exception {
        return JsonPath.read(authRequest(username, password, method, url, status), "$.token");
    }

}
