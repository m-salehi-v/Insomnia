import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
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

    public void run(String[] args) throws IllegalArgumentException{
        if (args.length != 0) {
            if (args[0].equals("list")) {
                int i = 1;
                for (Request Request : this.requests)
                    System.out.println(i++ + " . " + Request.toString());
            } else if (args[0].equals("fire")) {
                for (int i = 1; i < args.length; i++){
                    if (args[i].startsWith("-"))
                        break;
                    try {
                        int requestNum = Integer.parseInt(args[i]) - 1;
                        if (requestNum < requests.size() && requestNum >= 0) {
                            currentRequest = requests.get(requestNum);
                            requestProcessing(currentRequest.build(args));
                        }
                        else
                            System.out.println("invalid number -> " + (requestNum + 1));
                        System.out.println("----------------------------------------------------------------------------------------");
                    } catch (NumberFormatException e){
                        System.err.println("you should enter numbers after \"fire\" argument");
                    }
                }
            } else {
//                currentRequest = new Request();
                HttpUriRequestBase requestBase = currentRequest.build(args);
                if(currentRequest.isSaveRequest()){
                    saveRequest(currentRequest);
                }
                requestProcessing(requestBase);
            }
        } else
            System.err.println("no input");
    }

    private void requestsInit(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Saved Requests/requests.bin")))){
            requests = (ArrayList)ois.readObject();
        } catch (EOFException e){
        } catch (IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }
    public void requestProcessing(HttpUriRequestBase request){

        try {
            CloseableHttpClient client;
            if (currentRequest.isFollowRedirect())
                client = HttpClientBuilder.create().build();
            else
                client = HttpClientBuilder.create().disableRedirectHandling().build();
            CloseableHttpResponse response = client.execute(request);
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
            currentRequest.setResponse(response);
        } catch (IOException | ParseException e){
            System.err.println(e.getMessage());
        }
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
}
