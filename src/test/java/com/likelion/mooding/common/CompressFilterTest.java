package com.likelion.mooding.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import com.jayway.jsonpath.JsonPath;
import com.likelion.mooding.feedback.application.FakeFeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestFeedbackServiceConfig.class)
class CompressFilterTest {

    private static final String SESSION_KEY = "guestId";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("피드백 응답에 Zstd 압축이 적용되면, 인코딩 헤더가 추가되며, 디코딩 시 원본 데이터와 동일하다.")
    void Zstd_압축이_성공적으로_적용() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/feedback/1")
                                                                .sessionAttr(SESSION_KEY, uuid)
                                                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                .header("Accept-Encoding", "zstd"));

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk())
                                                 .andExpect(header().string("Content-Encoding", "zstd"))
                                                 .andReturn();

        final byte[] compressedBytes = mvcResult.getResponse().getContentAsByteArray();
        final byte[] decompressedBytes = Zstd.decompress(compressedBytes);
        String decompressedJsonResponse = new String(decompressedBytes, StandardCharsets.UTF_8);
        String responseValue = JsonPath.read(decompressedJsonResponse, "$.result");

        assertThat(responseValue).isEqualTo(FakeFeedbackChatCompletionService.createResponse().feedback());
    }

    @Test
    @DisplayName("Accept-Encoding에 zstd가 없을 경우, Zstd 압축을 하지 않는다.")
    void Zstd_압축이_적용되지_않음() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when & then
        mockMvc.perform(get("/api/feedback/1")
                            .sessionAttr(SESSION_KEY, uuid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(header().doesNotExist("Content-Encoding"))
               .andDo(print());
    }

    @Test
    @DisplayName("필터 URL 패턴에서 제외되는 경로로 요청 시, Zstd 압축을 하지 않는다.")
    void Zstd_압축이_적용되지_않음2() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when & then
        mockMvc.perform(get("/api/feedback/status/1")
                            .sessionAttr(SESSION_KEY, uuid)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Accept-Encoding", "zstd"))
               .andExpect(header().doesNotExist("Content-Encoding"))
               .andDo(print());
    }

    @Test
    @DisplayName("필터 URL 패턴에 포함되지 않는 경로로 요청 시, Zstd 압축을 하지 않는다.")
    void Zstd_압축이_적용되지_않음3() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when & then
        mockMvc.perform(post("/api/feedback")
                            .sessionAttr(SESSION_KEY, uuid)
                            .content(objectMapper.writeValueAsString(new FeedbackCreateRequest("Test Request")))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Accept-Encoding", "zstd"))
               .andExpect(header().doesNotExist("Content-Encoding"))
               .andDo(print());
    }
}
