import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
    private CloseableHttpResponse response;
    private double responseTime;

    public Request(String name, String method) throws IllegalArgumentException{
        this.name = name;
        this.method = method;
        initWithDefaults();
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

    public CloseableHttpResponse getResponse() {
        return response;
    }

    public void setResponse(CloseableHttpResponse response) {
        this.response = response;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public double getResponseTime() {
        return responseTime;
    }

    private void analyzeArgs(String[] args) throws IllegalArgumentException{
        url = args[0];
        for (int i = 1; i < args.length; i++){
            if (args[i].startsWith("-")){
                String s = args[i].substring(1);
                switch (s) {
                    case "M":
                    case "-method":
                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                            method = args[i + 1];
                        else
                            method = "GET";
                        break;
                    case "i":
                        showResponseHeaders = true;
                        break;
                    case "h":
                    case "-help":
                        showHelp = true;
                        break;
                    case "f":
                        followRedirect = true;
                        break;
                    case "O":
                    case "-output":
                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                            outputName = args[i + 1];
                        else {
                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                            outputName = "output_" + LocalDateTime.now().format(dateFormat);
                        }
                        break;
                    case "H":
                    case "-headers":
                        if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                            analyzeHeaders(args[i+1]);
                        else
                            throw new IllegalArgumentException("header requires parameter");
                        break;
                    case "S":
                    case "-save":
                        saveRequest = true;
                        break;
                    case "d":
                    case "-data":
                        if (contentType.length() == 0) {
                            if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                                analyzeData(args[i + 1]);
                            else
                                throw new IllegalArgumentException("--data(-d) requires parameter");
                            contentType = "multipart/form-data";
                        } else
                            throw new IllegalArgumentException("you can only use one type of message body");
                        break;
                    case "-upload":
                        if(contentType.length() == 0) {
                            if ((i + 1) < args.length && !args[i + 1].startsWith("-")) {
                                formData.put("file", args[i + 1]);
                                contentType = "application/octet-stream";
                            }
                            else
                                throw new IllegalArgumentException("--upload requires parameter");
                        } else
                            throw new IllegalArgumentException("you can only use one type of message body");
                        break;
                    case "-urlencoded":
                        if (contentType.length() == 0) {
                            if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                                analyzeData(args[i + 1]);
                            else
                                throw new IllegalArgumentException("--urlencoded requires parameter");
                            contentType = "application/x-www-form-urlencoded";
                        }else
                            throw new IllegalArgumentException("you can only use one type of message body");
                        break;
                    case "json":
                        if (contentType.length() == 0) {
                            if ((i + 1) < args.length && !args[i + 1].startsWith("-"))
                                analyzeJson(args[i + 1]);
                            else
                                throw new IllegalArgumentException("-json requires parameter");
                            contentType = "application/json";
                        }else
                            throw new IllegalArgumentException("you can only use one type of message body");
                        break;
                    default:
                        throw new IllegalArgumentException("This argument is invalid -> " + method);
                }
            }
        }
    }

    private void initWithDefaults(){
        url = "";
        headers = new LinkedHashMap<>();
        formData = new LinkedHashMap<>();
        queries = new LinkedHashMap<>();
//        method = "GET";
        showResponseHeaders = false;
        showHelp = false;
        followRedirect = false;
        outputName = "";
        saveRequest = false;
        contentType = "";
        json = "";
        response = null;
    }
    private void analyzeHeaders(String input){
        String[] hs = input.split(";");
        for (String header: hs){
            String[] keyAndValue = header.split(":", 2);
            if (keyAndValue.length == 1)
                headers.put(keyAndValue[0], "");
            else
                headers.put(keyAndValue[0], keyAndValue[1]);
        }
    }
    private void analyzeData(String input){
        String[] d = input.split("&");
        for (String data: d){
            String[] nameAndValue = data.split("=", 2);
            if (nameAndValue.length == 2)
                formData.put(nameAndValue[0], nameAndValue[1]);
        }
    }

    private void analyzeJson(String input){
        input = input.substring(1, input.length() - 2);
        String[] d = input.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i < d.length; i++){
            String[] nameAndValue = d[i].split(":", 2);
            if (nameAndValue.length == 2){
                stringBuilder.append("\"").append(nameAndValue[0]).append("\"")
                        .append(":").append("\"").append(nameAndValue[1]).append("\"");
                if (i != d.length - 1)
                    stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        json = stringBuilder.toString();
    }

    public HttpUriRequestBase build(String[] args){
        if (!args[0].equals("fire") && !args[0].equals("list"))
            analyzeArgs(args);
        HttpUriRequestBase request;
        switch (method){
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                request = new HttpPost(url);
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            case "PATCH":
                request = new HttpPatch(url);
                break;
            case "PUT":
                request = new HttpPut(url);
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
            if (!contentType.equals("multipart/form-data"))
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
