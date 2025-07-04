package com.learning.auth.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;

public class ServletInputStreamWrapper extends ServletInputStream {

    private final ByteArrayInputStream byteArrayInputStream;
    private int currentIndex = 0;

    public ServletInputStreamWrapper(ByteArrayInputStream byteArrayInputStream) {
        this.byteArrayInputStream = byteArrayInputStream;
    }

    @Override
    public int read() {
        return byteArrayInputStream.read();
    }

    @Override
    public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {

    }


}
