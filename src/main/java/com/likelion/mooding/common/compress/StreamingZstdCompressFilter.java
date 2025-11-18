package com.likelion.mooding.common.compress;

import io.netty.handler.codec.http.HttpMethod;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StreamingZstdCompressFilter implements Filter {

    private static final String ZSTD_CODE = "zstd";

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String httpMethod = httpRequest.getMethod();
        if (!HttpMethod.GET.toString().equals(httpMethod)) {
            chain.doFilter(request, response);
            return;
        }

        final String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/api/feedback/status/")) {
            chain.doFilter(request, response);
            return;
        }

        final String header = httpRequest.getHeader("Accept-Encoding");
        if (header != null && header.contains(ZSTD_CODE)) {
            httpResponse.setHeader("Content-Encoding", ZSTD_CODE);
            StreamingCompressServletResponseWrapper responseWrapper = new StreamingCompressServletResponseWrapper(httpResponse);

            try {
                chain.doFilter(request, responseWrapper);
            } finally {
                if (request.isAsyncStarted()) {
                    request.getAsyncContext().addListener(new FinishResponseListener(responseWrapper));
                } else {
                    responseWrapper.finish();
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class FinishResponseListener implements AsyncListener {

        private final StreamingCompressServletResponseWrapper wrapper;

        private FinishResponseListener(final StreamingCompressServletResponseWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            wrapper.finish(); // 비동기 작업 완료 시 스트림 닫기
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            wrapper.finish(); // 타임아웃 시 닫기
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            wrapper.finish(); // 에러 시 닫기
        }

        @Override
        public void onStartAsync(AsyncEvent event) {
            // 아무것도 안 함
        }
    }
}
