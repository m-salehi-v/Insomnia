import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

public class Request implements Serializable{
    private String name;
    private String url;
    private String method;
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, String> formData;
    private LinkedHashMap<String, String> queries;
    private boolean showResponseHeaders;
    private boolean followRedirect;
    private String contentType;
    private String json;
    private org.apache.hc.core5.http.Header[] responseHeaders;
    private double responseTime;
    private String responseSize;
    private String responseBody;
    private int responseCode;
    private String responseStatusMessage;
    private boolean haveResponse;

    public Request(String name, String method) throws IllegalArgumentException{
        this.name = name;
        this.method = method;
        initWithDefaults();
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setHaveResponse(boolean haveResponse) {
        this.haveResponse = haveResponse;
    }

    public boolean isHaveResponse() {
        return haveResponse;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseHeaders(org.apache.hc.core5.http.Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseStatusMessage() {
        return responseStatusMessage;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseStatusMessage(String responseStatusMessage) {
        this.responseStatusMessage = responseStatusMessage;
    }

    public String getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(String responseSize) {
        this.responseSize = responseSize;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public boolean isShowResponseHeaders() {
        return showResponseHeaders;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public String getUrl() {
        return url;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getFormData() {
        return formData;
    }

    public LinkedHashMap<String, String> getQueries() {
        return queries;
    }

    public String getJson() {
        return json;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private void initWithDefaults(){
        url = "";
        headers = new LinkedHashMap<>();
        formData = new LinkedHashMap<>();
        queries = new LinkedHashMap<>();
        showResponseHeaders = true;
        followRedirect = false;
        contentType = "";
        json = "";
        responseHeaders = null;
    }

    public HttpUriRequestBase build() throws URISyntaxException {
        HttpUriRequestBase request = null;
        URIBuilder builder = null;

            builder = new URIBuilder(url);
            for(String name: queries.keySet())
                builder.setParameter(name, queries.get(name));
        switch (method){
            case "GET":
                request = new HttpGet(builder.build());
                break;
            case "POST":
                request = new HttpPost(builder.build());
                break;
            case "DELETE":
                request = new HttpDelete(builder.build());
                break;
            case "PATCH":
                request = new HttpPatch(builder.build());
                break;
            case "PUT":
                request = new HttpPut(builder.build());
                break;
            default:
                throw new IllegalArgumentException("This method is invalid -> " + method);
        }

        for (String key: headers.keySet())
            request.addHeader(key, headers.get(key));
        if(formData.size() != 0) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (String key : formData.keySet()) {
                if (key.contains("file")) {
                    File file = new File(formData.get(key));
                    entityBuilder.addBinaryBody(key, file, ContentType.DEFAULT_BINARY, file.getName());
                } else
                    entityBuilder.addTextBody(key, formData.get(key));
            }
            HttpEntity entity = entityBuilder.build();
            if (contentType.equals("application/octet-stream"))
                 request.addHeader("Content-Type", contentType);
            request.setEntity(entity);
        } else if (json.length() != 0){
            request.setEntity(new StringEntity(json));
            request.addHeader("Content-Type", contentType);
        }
        return request;
    }



    @Override
    public String toString() {
        return "url: " + url + " | method: " + method +
                " | headers: " + headers +
                " | body: " + formData;
    }
}
