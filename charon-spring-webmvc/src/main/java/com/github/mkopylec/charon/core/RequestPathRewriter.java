package com.github.mkopylec.charon.core;

public interface RequestPathRewriter {

    String rewrite(String incomingRequestPath);
}
