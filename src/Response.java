import org.apache.hc.core5.http.Header;

import java.io.Serializable;

public class Response implements Serializable {

    private org.apache.hc.core5.http.Header[] headers;
    private double time;
    private int size;
    private String body;
    private int code;
    private String statusMessage;
    private String contentType;
    private String imageOutputName;

    public Response() {
        headers = null;
        imageOutputName = "";
    }

    public Header[] getHeaders() {
        return headers;
    }

    public String getImageOutputName() {
        return imageOutputName;
    }

    public double getTime() {
        return time;
    }

    public int getSize() {
        return size;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setImageOutputName(String imageOutputName) {
        this.imageOutputName = imageOutputName;
    }
}
