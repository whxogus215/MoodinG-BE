package com.likelion.mooding.common.compress;

import com.github.luben.zstd.ZstdOutputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class StreamingCompressServletResponseWrapper extends HttpServletResponseWrapper {

    private final HttpServletResponse originalResponse;
    private ZstdOutputStream zstdOutputStream;

    private ServletOutputStream servletOutputStream;
    private PrintWriter writer;

    public StreamingCompressServletResponseWrapper(final HttpServletResponse response) throws IOException {
        super(response);
        originalResponse = response;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }
        if (servletOutputStream == null) {
            servletOutputStream = new StreamingServletOutputStream(getZstdOutputStream());
        }
        return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (servletOutputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(getZstdOutputStream(), StandardCharsets.UTF_8));
        }
        return writer;
    }

    public void finish() throws IOException {
        if (writer != null) {
            writer.close();
        } else if (servletOutputStream != null) {
            servletOutputStream.close();
        } else if (zstdOutputStream != null) {
            zstdOutputStream.close();
        }
    }

    public ZstdOutputStream getZstdOutputStream() throws IOException {
        if (zstdOutputStream == null) {
            zstdOutputStream = new ZstdOutputStream(originalResponse.getOutputStream());
        }
        return zstdOutputStream;
    }
}
