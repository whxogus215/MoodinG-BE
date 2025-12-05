package com.likelion.mooding.feedback.application;

import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackResultResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackStatusResponse;

public class FakeFeedbackService implements FeedbackService {

    @Override
    public Long createFeedback(final Guest guest, final FeedbackCreateRequest request) {
        return 0L;
    }

    @Override
    public FeedbackStatusResponse getFeedbackStatus(final Guest guest, final Long id) {
        return null;
    }

    @Override
    public FeedbackResultResponse getFeedback(final Guest guest, final Long id) {
        return new FeedbackResultResponse(FakeFeedbackChatCompletionService.createResponse().feedback());
    }
}
