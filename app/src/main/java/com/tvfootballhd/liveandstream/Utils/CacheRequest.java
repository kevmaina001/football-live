package com.tvfootballhd.liveandstream.Utils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

public class CacheRequest extends Request<NetworkResponse> {
    private final Response.ErrorListener mErrorListener;
    private final Response.Listener<NetworkResponse> mListener;

    public CacheRequest(int i, String str, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(i, str, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    /* access modifiers changed from: protected */
    public Response<NetworkResponse> parseNetworkResponse(NetworkResponse networkResponse) {
        Cache.Entry parseCacheHeaders = HttpHeaderParser.parseCacheHeaders(networkResponse);
        if (parseCacheHeaders == null) {
            parseCacheHeaders = new Cache.Entry();
        }
        long currentTimeMillis = System.currentTimeMillis();
        parseCacheHeaders.data = networkResponse.data;
        parseCacheHeaders.softTtl = 180000 + currentTimeMillis;
        parseCacheHeaders.ttl = currentTimeMillis + 86400000;
        String str = networkResponse.headers.get("Date");
        if (str != null) {
            parseCacheHeaders.serverDate = HttpHeaderParser.parseDateAsEpoch(str);
        }
        String str2 = networkResponse.headers.get("Last-Modified");
        if (str2 != null) {
            parseCacheHeaders.lastModified = HttpHeaderParser.parseDateAsEpoch(str2);
        }
        parseCacheHeaders.responseHeaders = networkResponse.headers;
        return Response.success(networkResponse, parseCacheHeaders);
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(NetworkResponse networkResponse) {
        this.mListener.onResponse(networkResponse);
    }

    /* access modifiers changed from: protected */
    public VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    public void deliverError(VolleyError volleyError) {
        this.mErrorListener.onErrorResponse(volleyError);
    }
}
