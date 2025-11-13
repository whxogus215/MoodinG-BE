package com.likelion.mooding.common.compress;

import com.github.luben.zstd.Zstd;
import io.netty.handler.codec.http.HttpMethod;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZstdCompressFilter implements Filter {

    private static final String ZSTD_CODE = "zstd";
    private final Logger logger = LoggerFactory.getLogger(ZstdCompressFilter.class);

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String httpMethod = httpRequest.getMethod();
        if (!HttpMethod.GET.toString().equals(httpMethod)) {
            chain.doFilter(request, response);
            return ;
        }

        final String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/api/feedback/status/")) {
            chain.doFilter(request, response);
            return ;
        }

        final String header = httpRequest.getHeader("Accept-Encoding");
        if (header != null && header.contains(ZSTD_CODE)) {
            CompressServletResponseWrapper responseWrapper = new CompressServletResponseWrapper(httpResponse);
            chain.doFilter(request, responseWrapper);

            byte[] originContent = responseWrapper.getByteContent();

            try {
                // 응답 데이터를 꺼내서 Zstd 압축
                byte[] compressContent = Zstd.compress(originContent);
                response.getOutputStream().write(compressContent);
                httpResponse.setHeader("Content-Encoding", ZSTD_CODE);
            } catch (Exception e) {
                logger.error("Zstd 압축 실패, 원본 데이터를 전송합니다.", e);
                response.getOutputStream().write(originContent);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
