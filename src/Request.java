import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.*;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

public class Request implements Serializable {
    private String name;
    private String url;
    private String method;
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, String> formData;
    private LinkedHashMap<String, String> queries;
    private LinkedHashMap<String, String> uncheckedHeaders;
    private LinkedHashMap<String, String> uncheckedFormData;
    private LinkedHashMap<String, String> uncheckedQueries;
    private boolean showResponseHeaders;
    private boolean followRedirect;
    private String contentType;
    private String json;
    private boolean haveResponse;
    private String query;
    private Response response;

    public Request(String name, String method) throws IllegalArgumentException {
        this.name = name;
        this.method = method;
        initWithDefaults();
    }

    public void setHaveResponse(boolean haveResponse) {
        this.haveResponse = haveResponse;
    }

    public boolean isHaveResponse() {
        return haveResponse;
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

    public String getQuery() {
        return query;
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

    public String getContentType() {
        return contentType;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public LinkedHashMap<String, String> getUncheckedHeaders() {
        return uncheckedHeaders;
    }

    public LinkedHashMap<String, String> getUncheckedFormData() {
        return uncheckedFormData;
    }

    public LinkedHashMap<String, String> getUncheckedQueries() {
        return uncheckedQueries;
    }

    public Response getResponse() {
        return response;
    }

    private void initWithDefaults() {
        url = "";
        headers = new LinkedHashMap<>();
        formData = new LinkedHashMap<>();
        queries = new LinkedHashMap<>();
        uncheckedFormData = new LinkedHashMap<>();
        uncheckedHeaders = new LinkedHashMap<>();
        uncheckedQueries = new LinkedHashMap<>();
        showResponseHeaders = true;
        followRedirect = false;
        contentType = "";
        json = "";
        query = "";
        response = new Response();
    }

    public HttpUriRequestBase build() throws URISyntaxException {
        HttpUriRequestBase request = null;
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (String name : queries.keySet())
            builder.append(name).append("=").append(queries.get(name)).append("&");
        query =  builder.toString();
        switch (method) {
            case "GET":
                request = new HttpGet(url + query);
                break;
            case "POST":
                request = new HttpPost(url + query);
                break;
            case "DELETE":
                request = new HttpDelete(url + query);
                break;
            case "PATCH":
                request = new HttpPatch(url + query);
                break;
            case "PUT":
                request = new HttpPut(url + query);
                break;
            default:
                throw new IllegalArgumentException("This method is invalid -> " + method);
        }

        for (String key : headers.keySet())
            request.addHeader(key, headers.get(key));
        if (formData.size() != 0) {
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
        } else if (json.length() != 0) {
            request.setEntity(new StringEntity(json));
            request.addHeader("Content-Type", contentType);
        }
        return request;
    }

    public String createRandomFileName(){
        String tmp = "" + System.nanoTime() + ".png";
        response.setImageOutputName(tmp);
        return tmp;
    }

    @Override
    public String toString() {
        return "url: " + url + " | method: " + method +
                " | headers: " + headers +
                " | body: " + formData;
    }
}
