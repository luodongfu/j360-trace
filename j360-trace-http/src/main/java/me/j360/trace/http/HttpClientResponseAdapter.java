package me.j360.trace.http;


import me.j360.trace.collector.core.ClientResponseAdapter;
import me.j360.trace.collector.core.KeyValueAnnotation;
import me.j360.trace.collector.core.TraceKeys;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class HttpClientResponseAdapter implements ClientResponseAdapter {


    private final HttpResponse response;

    public HttpClientResponseAdapter(HttpResponse response) {
        this.response = response;
    }

    @Override
    public Collection<KeyValueAnnotation> responseAnnotations() {
        int httpStatus = response.getHttpStatusCode();

        if ((httpStatus < 200) || (httpStatus > 299)) {
            KeyValueAnnotation statusAnnotation = KeyValueAnnotation.create(TraceKeys.HTTP_STATUS_CODE, String.valueOf(httpStatus));
            return Arrays.asList(statusAnnotation);
        }
        return Collections.EMPTY_LIST;
    }
}
