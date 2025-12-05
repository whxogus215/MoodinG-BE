package com.likelion.mooding.common;

import com.likelion.mooding.feedback.application.FakeFeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.FakeFeedbackService;
import com.likelion.mooding.feedback.application.FeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.FeedbackService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFeedbackServiceConfig {

    @Bean
    @Primary
    public FeedbackChatCompletionService feedbackChatCompletionService() {
        return new FakeFeedbackChatCompletionService();
    }

    @Bean
    @Primary
    public FeedbackService feedbackService() {
        return new FakeFeedbackService();
    }
}
