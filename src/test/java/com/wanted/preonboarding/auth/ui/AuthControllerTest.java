package com.wanted.preonboarding.auth.ui;

import com.wanted.preonboarding._common.RestControllerTest;
import com.wanted.preonboarding.auth.application.AuthService;
import com.wanted.preonboarding.auth.application.dto.SignInRequest;
import com.wanted.preonboarding.auth.application.dto.SignUpRequest;
import com.wanted.preonboarding.auth.application.dto.TokenRequest;
import com.wanted.preonboarding.auth.application.dto.TokenResponse;
import com.wanted.preonboarding.fixtures.AuthFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends RestControllerTest {
    @MockBean
    private AuthService authService;

    @Test
    public void signIn() throws Exception {
        // given
        String url = "/auth/sign-in";
        SignInRequest signInRequest = AuthFixtures.createSignInRequest();
        TokenResponse tokenResponse = AuthFixtures.createTokenResponse();
        MockHttpServletRequestBuilder requestBuilder = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest));

        when(authService.signIn(signInRequest)).thenReturn(tokenResponse);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                url.substring(1),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("재발행 토큰")
                                )
                        )
                )
                .andReturn();

        // then
        String responseContent = mvcResult.getResponse().getContentAsString();
        TokenResponse result = objectMapper.readValue(responseContent, TokenResponse.class);

        assertEquals(tokenResponse, result);
    }

    @Test
    public void signOut() throws Exception {
        // given
        String url = "/auth/sign-out";
        MockHttpServletRequestBuilder requestBuilder = delete(url)
                .header("Authorization", "accessToken");

        doNothing().when(authService).signOut(any());

        // when, then
        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                url.substring(1),
                                requestHeaders(
                                        headerWithName("Authorization").description("JWT 토큰")
                                )
                        )
                )
                .andReturn();
    }

    @Test
    public void signUp() throws Exception {
        // given
        String url = "/auth/sign-up";
        SignUpRequest signUpRequest = AuthFixtures.createSignUpRequest();
        MockHttpServletRequestBuilder requestBuilder = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest));

        doNothing().when(authService).signUp(signUpRequest);

        // when, then
        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(
                        document(
                                url.substring(1),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        )
                )
                .andReturn();
    }

    @Test
    public void refresh() throws Exception {
        // given
        String url = "/auth/refresh";
        TokenRequest tokenRequest = AuthFixtures.createTokenRequest();
        TokenResponse tokenResponse = AuthFixtures.createTokenResponse();
        MockHttpServletRequestBuilder requestBuilder = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest));

        when(authService.refreshToken(tokenRequest)).thenReturn(tokenResponse);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                url.substring(1),
                                requestFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("재발행 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT 토큰"),
                                        fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("재발행 토큰")
                                )
                        )
                )
                .andReturn();

        // then
        String responseContent = mvcResult.getResponse().getContentAsString();
        TokenResponse result = objectMapper.readValue(responseContent, TokenResponse.class);

        assertEquals(tokenResponse, result);
    }
}
