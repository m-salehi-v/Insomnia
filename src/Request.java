import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.*;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

/**
 * This class holds all information about a request and its
 * response when it is received. it also has one more task that is
 * building request with given features and pass it to HttpManager Class.
 *
 * @author Mohammad Salehi Vaziri
 */
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

    /**
     * Instantiates a new Request.
     *
     * @param name   the name of the request
     * @param method the method of the request
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Request(String name, String method) throws IllegalArgumentException {
        this.name = name;
        this.method = method;
        initWithDefaults();
    }

    /**
     * Sets have response.
     *
     * @param haveResponse the have response
     */
    public void setHaveResponse(boolean haveResponse) {
        this.haveResponse = haveResponse;
    }

    /**
     * Is have response boolean.
     *
     * @return the boolean
     */
    public boolean isHaveResponse() {
        return haveResponse;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Is show response headers boolean.
     *
     * @return the boolean
     */
    public boolean isShowResponseHeaders() {
        return showResponseHeaders;
    }

    /**
     * Gets query.
     *
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Is follow redirect boolean.
     *
     * @return the boolean
     */
    public boolean isFollowRedirect() {
        return followRedirect;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets form data.
     *
     * @return the form data
     */
    public LinkedHashMap<String, String> getFormData() {
        return formData;
    }

    /**
     * Gets queries.
     *
     * @return the queries
     */
    public LinkedHashMap<String, String> getQueries() {
        return queries;
    }

    /**
     * Gets json.
     *
     * @return the json
     */
    public String getJson() {
        return json;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Sets follow redirect.
     *
     * @param followRedirect the follow redirect
     */
    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    /**
     * Sets content type.
     *
     * @param contentType the content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets json.
     *
     * @param json the json
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * Gets unchecked headers.
     *
     * @return the unchecked headers
     */
    public LinkedHashMap<String, String> getUncheckedHeaders() {
        return uncheckedHeaders;
    }

    /**
     * Gets unchecked form data.
     *
     * @return the unchecked form data
     */
    public LinkedHashMap<String, String> getUncheckedFormData() {
        return uncheckedFormData;
    }

    /**
     * Gets unchecked queries.
     *
     * @return the unchecked queries
     */
    public LinkedHashMap<String, String> getUncheckedQueries() {
        return uncheckedQueries;
    }

    /**
     * Gets response.
     *
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    //this method initializes most of the fields with default values
    //so if user didn't change them there would be no problem.
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

    /**
     * this method builds the request with features indicated by fields as
     * a HttpUriRequestBase instance and returns it.
     *
     * @return the HttpUriRequestBase instance
     * @throws URISyntaxException the uri syntax exception
     */
    public HttpUriRequestBase build() throws URISyntaxException {
        HttpUriRequestBase request = null;
        //forming query
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

//    /**
//     * Create random file name string.
//     *
//     * @return the string
//     */
//    public String createRandomFileName(){
//        String tmp = "" + System.nanoTime() + ".png";
//        response.setImageOutputName(tmp);
//        return tmp;
//    }

    @Override
    public String toString() {
        return "url: " + url + " | method: " + method +
                " | headers: " + headers +
                " | body: " + formData;
    }
}
