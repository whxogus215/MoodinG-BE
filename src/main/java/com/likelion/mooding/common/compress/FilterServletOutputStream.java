package com.likelion.mooding.common.compress;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilterServletOutputStream extends ServletOutputStream {

    private final DataOutputStream output;

    public FilterServletOutputStream(OutputStream output) {
        this.output = new DataOutputStream(output);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener listener) { // default implementaion ignored
    }

    @Override
    public void write(final int b) throws IOException {
        output.write(b);
    }
}
