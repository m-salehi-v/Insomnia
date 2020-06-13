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
    private boolean showHelp;
    private boolean followRedirect;
    private String outputName;
    private boolean saveRequest;
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

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public String getOutputName() {
        return outputName;
    }

    public boolean isSaveRequest() {
        return saveRequest;
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

//    private void analyzeArgs(String[] args) throws IllegalArgumentException{
//        url = args[0];
//        for (int i = 1; i < args.length; i++){
//            if (args[i].startsWith("-")){
//                String s = args[i].substring(1);
//                switch (s) {
//                    case "M":
//                    case "-method":
//                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
//                            method = args[i + 1];
//                        else
//                            method = "GET";
//                        break;
//                    case "i":
//                        showResponseHeaders = true;
//                        break;
//                    case "h":
//                    case "-help":
//                        showHelp = true;
//                        break;
//                    case "f":
//                        followRedirect = true;
//                        break;
//                    case "O":
//                    case "-output":
//                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
//                            outputName = args[i + 1];
//                        else {
//                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
//                            outputName = "output_" + LocalDateTime.now().format(dateFormat);
//                        }
//                        break;
//                    case "H":
//                    case "-headers":
//                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
//                            analyzeHeaders(args[i+1]);
////                        else
////                            throw new IllegalArgumentException("header requires parameter");
//                        break;
//                    case "S":
//                    case "-save":
//                        saveRequest = true;
//                        break;
//                    case "d":
//                    case "-data":
//                        if (contentType.length() == 0) {
//                            if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
//                                analyzeData(args[i + 1]);
//                                contentType = "multipart/form-data";
//                            }
////                            else
////                                throw new IllegalArgumentException("--data(-d) requires parameter");
//                        } else
//                            throw new IllegalArgumentException("you can only use one type of message body");
//                        break;
//                    case "-upload":
//                        if(contentType.length() == 0) {
//                            if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
//                                formData.put("file", args[i + 1]);
//                                contentType = "application/octet-stream";
//                            }
////                            else
////                                throw new IllegalArgumentException("--upload requires parameter");
//                        } else
//                            throw new IllegalArgumentException("you can only use one type of message body");
//                        break;
//                    case "-urlencoded":
//                        if (contentType.length() == 0) {
//                            if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
//                                analyzeData(args[i + 1]);
//                                contentType = "application/x-www-form-urlencoded";
//                            }
////                            else {
////                                throw new IllegalArgumentException("--urlencoded requires parameter");
////                            }
//                        }else
//                            throw new IllegalArgumentException("you can only use one type of message body");
//                        break;
//                    case "json":
//                        if (contentType.length() == 0) {
//                            if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
//                                analyzeJson(args[i + 1]);
//                                contentType = "application/json";
//                            }
////                            else {
////                                throw new IllegalArgumentException("-json requires parameter");
////                            }
//                        }else
//                            throw new IllegalArgumentException("you can only use one type of message body");
//                        break;
//                    default:
//                        throw new IllegalArgumentException("This argument is invalid -> " + method);
//                }
//            }
//        }
//    }

    private void initWithDefaults(){
        url = "";
        headers = new LinkedHashMap<>();
        formData = new LinkedHashMap<>();
        queries = new LinkedHashMap<>();
        showResponseHeaders = true;
        showHelp = false;
        followRedirect = false;
        outputName = "";
        saveRequest = false;
        contentType = "";
        json = "";
        responseHeaders = null;
    }
//    private void analyzeHeaders(String input){
//        String[] hs = input.split(";");
//        for (String header: hs){
//            String[] keyAndValue = header.split(":", 2);
//            if (keyAndValue.length == 1)
//                headers.put(keyAndValue[0], "");
//            else
//                headers.put(keyAndValue[0], keyAndValue[1]);
//        }
//    }
//    private void analyzeData(String input){
//        String[] d = input.split("&");
//        for (String data: d){
//            String[] nameAndValue = data.split("=", 2);
//            if (nameAndValue.length == 2)
//                formData.put(nameAndValue[0], nameAndValue[1]);
//        }
//    }
//
//    private void analyzeJson(String input){
//        input = input.substring(1, input.length() - 2);
//        String[] d = input.split(",");
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{");
//        for (int i = 0; i < d.length; i++){
//            String[] nameAndValue = d[i].split(":", 2);
//            if (nameAndValue.length == 2){
//                stringBuilder.append("\"").append(nameAndValue[0]).append("\"")
//                        .append(":").append("\"").append(nameAndValue[1]).append("\"");
//                if (i != d.length - 1)
//                    stringBuilder.append(",");
//            }
//        }
//        stringBuilder.append("}");
//        json = stringBuilder.toString();
//    }

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
