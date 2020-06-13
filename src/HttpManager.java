import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.*;
import java.util.ArrayList;

public class HttpManager {
    private ArrayList<Request> requests;
    private Request currentRequest;

    public HttpManager() {
        requests = new ArrayList<>();
        requestsInit();
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
    }


    private void requestsInit(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Saved Requests/requests.bin")))){
            requests = (ArrayList)ois.readObject();
        } catch (EOFException e){
        } catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }
    public void requestProcessing(HttpUriRequestBase request) throws IOException, ProtocolException, HttpHostConnectException {


            CloseableHttpClient client;
            if (currentRequest.isFollowRedirect())
                client = HttpClientBuilder.create().build();
            else
                client = HttpClientBuilder.create().disableRedirectHandling().build();
            long start = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(request);
            long time = System.currentTimeMillis() - start;
            currentRequest.setHaveResponse(true);
            currentRequest.setResponseTime(time/1000.0);
            if (currentRequest.isShowHelp())
                printHelp();
            StringBuilder output = new StringBuilder();
            output.append(response.getVersion()).append(" ")
                    .append(response.getCode()).append(" ")
                    .append(response.getReasonPhrase()).append("\n");
            if (currentRequest.isShowResponseHeaders()) {
                for (Header header : response.getHeaders())
                    output.append(header).append("\n");
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            currentRequest.setResponseBody(responseBody);
            output.append("\n").append(responseBody);
            if (currentRequest.getOutputName().length() != 0){
                File file = new File("Saved Responses/" + currentRequest.getOutputName());
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                outputStream.write(responseBody.getBytes());
                outputStream.flush();
                outputStream.close();
                System.out.println("response saved to " + file.getAbsolutePath());
            }
            System.out.println(output.toString());
            currentRequest.setResponseCode(response.getCode());
            currentRequest.setResponseHeaders(response.getHeaders());
            Header header = response.getHeader("Content-Length");
            if (header == null)
                currentRequest.setResponseSize("?");
            else
                currentRequest.setResponseSize(header.getValue());
            currentRequest.setResponseStatusMessage(response.getReasonPhrase());

    }

    public void saveRequest(Request request){
        requests.add(request);
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Saved Requests/requests.bin")))){
            oos.writeObject(requests);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void printHelp(){
        System.out.println("-M\n--method       Set request method with the parameter followed by this argument");
        System.out.println("-H\n--headers      Set request headers in this format \"key1:value1;key2:value2;...\"");
        System.out.println("-i             to show response headers");
        System.out.println("-f             to automatically follow redirects");
        System.out.println("-O\n--output       Save response body in a file named by the parameter after this argument");
        System.out.println("-S\n--save         Save current request");
        System.out.println("-d\n--data         Send multipart form data in this format: \"name1=value1&name2=value2&...\"");
        System.out.println("--upload       send file: --upload \"file path\"");
        System.out.println("--urlencoded   send urlencoded body in this format: \"name1=value1&name2=value2&...\"");
        System.out.println("-json          send json body in this format: \"{name1:value1,name2:value2,...}\"");
        System.out.println("fire           used to send saved requests: fire 1 3");
        System.out.println("list           shows all saved requests");
        System.out.println();
    }
    public void updateRequests(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Saved Requests/requests.bin")))){
            oos.writeObject(requests);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
