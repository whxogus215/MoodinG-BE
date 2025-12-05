package com.likelion.mooding.feedback.application;

import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import reactor.core.publisher.Mono;

public class FakeFeedbackChatCompletionService implements FeedbackChatCompletionService {

    @Override
    public Mono<FeedbackCreateResponse> completeChat(final FeedbackCreateRequest request) {
        return null;
    }

    public static FeedbackCreateResponse createResponse() {
        return new FeedbackCreateResponse("Hello World");
    }
}
