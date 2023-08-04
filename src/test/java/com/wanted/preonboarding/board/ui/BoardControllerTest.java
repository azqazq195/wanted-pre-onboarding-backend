package com.wanted.preonboarding.board.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.wanted.preonboarding._common.RestControllerTest;
import com.wanted.preonboarding.board.application.BoardService;
import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.application.dto.BoardResponse;
import com.wanted.preonboarding.fixtures.BoardFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
public class BoardControllerTest extends RestControllerTest {
    @MockBean
    private BoardService boardService;

    @Test
    public void create() throws Exception {
        // given
        String url = "/board";
        BoardRequest boardRequest = BoardFixtures.createBoardRequest();
        BoardResponse boardResponse = BoardFixtures.createBoardResponse();
        MockHttpServletRequestBuilder requestBuilder = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequest));

        when(boardService.create(boardRequest)).thenReturn(boardResponse);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(
                        document(
                                "board/create",
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 시간")
                                )
                        )
                )
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        BoardResponse result = objectMapper.readValue(responseContent, BoardResponse.class);

        assertEquals(boardResponse, result);
    }

    @Test
    public void update() throws Exception {
        // given
        String url = "/board/{id}";
        Long id = 1L;
        BoardRequest boardRequest = BoardFixtures.createBoardRequest();
        BoardResponse boardResponse = BoardFixtures.createBoardResponse();
        MockHttpServletRequestBuilder requestBuilder = put(url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequest));

        when(boardService.update(id, boardRequest)).thenReturn(boardResponse);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "board/update",
                                pathParameters(
                                        parameterWithName("id").description("게시글 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 시간")
                                )
                        )
                )
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        BoardResponse result = objectMapper.readValue(responseContent, BoardResponse.class);

        assertEquals(boardResponse, result);
    }

    @Test
    public void delete() throws Exception {
        // given
        String url = "/board/{id}";
        Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders.delete(url, id);

        doNothing().when(boardService).delete(id);

        // when, then
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "board/delete",
                                pathParameters(
                                        parameterWithName("id").description("게시글 ID")
                                )
                        )
                )
                .andReturn();
    }

    @Test
    public void retrieve() throws Exception {
        // given
        String url = "/board/{id}";
        Long id = 1L;
        BoardResponse boardResponse = BoardFixtures.createBoardResponse();
        MockHttpServletRequestBuilder requestBuilder = get(url, id);

        when(boardService.retrieve(id)).thenReturn(boardResponse);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "board/retrieve",
                                pathParameters(
                                        parameterWithName("id").description("게시글 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("수정 시간")
                                )
                        )
                )
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        BoardResponse result = objectMapper.readValue(responseContent, BoardResponse.class);

        assertEquals(boardResponse, result);
    }

    @Test
    public void retrieveAll() throws Exception {
        // given
        String url = "/board";
        Pageable pageable = PageRequest.of(0, 10);
        List<BoardResponse> boardList = Arrays.asList(BoardFixtures.createBoardResponse(), BoardFixtures.createBoardResponse());
        Page<BoardResponse> boardPage = new PageImpl<>(boardList, pageable, boardList.size());
        MockHttpServletRequestBuilder requestBuilder = get(url)
                .param("size", String.valueOf(pageable.getPageSize()))
                .param("page", String.valueOf(pageable.getPageNumber()));

        when(boardService.retrieveAll(pageable)).thenReturn(boardPage);

        // when
        MvcResult mvcResult = mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "board/retrieveAll",
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                ),
                                relaxedResponseFields(
                                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        fieldWithPath("content[].title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("content[].content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("content[].createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                        fieldWithPath("content[].modifiedAt").type(JsonFieldType.STRING).description("수정 시간")
                                )
                        )
                )
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseContent);

        assertEquals(boardPage.getContent().size(), responseJson.get("content").size());
        for (int i = 0; i < boardPage.getContent().size(); i++) {
            BoardResponse expectedBoard = boardPage.getContent().get(i);
            JsonNode actualBoard = responseJson.get("content").get(i);
            assertEquals(expectedBoard.id(), actualBoard.get("id").asLong());
            assertEquals(expectedBoard.title(), actualBoard.get("title").asText());
            assertEquals(expectedBoard.content(), actualBoard.get("content").asText());
            assertEquals(expectedBoard.createdAt(), LocalDateTime.parse(actualBoard.get("createdAt").asText()));
            assertEquals(expectedBoard.modifiedAt(), LocalDateTime.parse(actualBoard.get("modifiedAt").asText()));
        }
    }
}
