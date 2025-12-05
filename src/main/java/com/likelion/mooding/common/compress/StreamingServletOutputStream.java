package com.likelion.mooding.common.compress;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class StreamingServletOutputStream extends ServletOutputStream {

    private final OutputStream outputStream;

    public StreamingServletOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener listener) {
        // 논블로킹 방식이 아니므로 구현 X
    }

    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
