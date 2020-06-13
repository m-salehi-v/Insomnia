import org.apache.hc.core5.http.Header;

import java.io.Serializable;

/**
 * this class only holds essential info about a response nothing else.
 *
 * @author Mohammad Salehi Vaziri
 */
public class Response implements Serializable {

    private org.apache.hc.core5.http.Header[] headers;
    private double time;
    private int size;
    private String body;
    private int code;
    private String statusMessage;
    private String contentType;
    private String imageOutputName;

    /**
     * Instantiates a new Response.
     */
    public Response() {
        headers = null;
        imageOutputName = "";
    }

    /**
     * Get headers header [ ].
     *
     * @return the header [ ]
     */
    public Header[] getHeaders() {
        return headers;
    }

    /**
     * Gets image output name.
     *
     * @return the image output name
     */
    public String getImageOutputName() {
        return imageOutputName;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public double getTime() {
        return time;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets status message.
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
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
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Sets status message.
     *
     * @param statusMessage the status message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
     * Sets image output name.
     *
     * @param imageOutputName the image output name
     */
    public void setImageOutputName(String imageOutputName) {
        this.imageOutputName = imageOutputName;
    }
}
