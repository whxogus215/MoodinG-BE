package com.likelion.mooding.feedback.application;

import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackResultResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackStatusResponse;

public interface FeedbackService {

    Long createFeedback(Guest guest, FeedbackCreateRequest request);

    FeedbackStatusResponse getFeedbackStatus(Guest guest, Long id);

    FeedbackResultResponse getFeedback(Guest guest, Long id);
}
