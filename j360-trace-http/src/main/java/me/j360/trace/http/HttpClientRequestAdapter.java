package me.j360.trace.http;

import com.sun.istack.internal.Nullable;
import me.j360.trace.collector.core.*;
import me.j360.trace.collector.core.module.Endpoint;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

public class HttpClientRequestAdapter implements ClientRequestAdapter {

    private final HttpClientRequest request;
    private final SpanNameProvider spanNameProvider;

    public HttpClientRequestAdapter(HttpClientRequest request, SpanNameProvider spanNameProvider) {
        this.request = request;
        this.spanNameProvider = spanNameProvider;
    }


    @Override
    public String getSpanName() {
        return spanNameProvider.spanName(request);
    }

    @Override
    public void addSpanIdToRequest(@Nullable SpanId spanId) {
        if (spanId == null) {
            request.addHeader(BraveHttpHeaders.Sampled.getName(), "0");
        } else {
            request.addHeader(BraveHttpHeaders.Sampled.getName(), "1");
            request.addHeader(BraveHttpHeaders.TraceId.getName(), IdConversion.convertToString(spanId.traceId));
            request.addHeader(BraveHttpHeaders.SpanId.getName(), IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                request.addHeader(BraveHttpHeaders.ParentSpanId.getName(), IdConversion.convertToString(spanId.parentId));
            }
        }
    }


    @Override
    public Collection<KeyValueAnnotation> requestAnnotations() {
        URI uri = request.getUri();
        KeyValueAnnotation annotation = KeyValueAnnotation.create(TraceKeys.HTTP_URL, uri.toString());
        return Arrays.asList(annotation);
    }

    @Override
    public Endpoint serverAddress() {
        return null;
    }
}
