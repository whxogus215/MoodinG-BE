package com.likelion.mooding.common.compress;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;

public class CompressServletResponseWrapper extends HttpServletResponseWrapper {

    ByteArrayOutputStream output;
    FilterServletOutputStream servletOutput;

    public CompressServletResponseWrapper(final HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (servletOutput == null) {
            servletOutput = new FilterServletOutputStream(output);
        }
        return servletOutput;
    }

    public byte[] getByteContent() {
        return output.toByteArray();
    }
}
