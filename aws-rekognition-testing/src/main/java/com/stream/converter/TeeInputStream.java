package com.stream.converter;
import java.io.*;

public class TeeInputStream extends InputStream {
    private InputStream in;
    private OutputStream out;

    public TeeInputStream(InputStream in, OutputStream branch) {
        this.in=in;
        this.out=branch;
    }
    public int read() throws IOException {
        int read = in.read();
        if (read != -1) out.write(read);
        return read;
    }
    public void close() throws IOException {
        in.close();
        out.close();
    }
}