package com.github.mkopylec.charon.configuration;

public class PathRewriteConfigurer {

    private PathRewriteConfiguration pathRewriteConfiguration;

    private PathRewriteConfigurer() {
        pathRewriteConfiguration = new PathRewriteConfiguration();
    }

    public static PathRewriteConfigurer pathRewrite() {
        return new PathRewriteConfigurer();
    }

    public PathRewriteConfigurer incomingPathRegex(String incomingPathRegex) {
        pathRewriteConfiguration.setIncomingPathRegex(incomingPathRegex);
        return this;
    }

    public PathRewriteConfigurer outgoingPathTemplate(String outgoingPathTemplate) {
        pathRewriteConfiguration.setOutgoingPathTemplate(outgoingPathTemplate);
        return this;
    }

    PathRewriteConfiguration getConfiguration() {
        return pathRewriteConfiguration;
    }
}
