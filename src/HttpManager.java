import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ProtocolException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * In this class the whole process of receiving response of a request
 * is done and fields of the Request which hold info about response
 * gets initialized.
 *
 * @author Mohammad Salehi Vaziri
 */
public class HttpManager {
    private ArrayList<Request> requests;
    private Request currentRequest;

    /**
     * Instantiates a new Http manager.
     */
    public HttpManager() {
        requests = new ArrayList<>();
        requestsInit();
    }

    /**
     * Gets requests.
     *
     * @return the requests
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * Sets current request.
     *
     * @param currentRequest the current request
     */
    public void setCurrentRequest(Request currentRequest) {
        this.currentRequest = currentRequest;
    }

    //initializes saved requests by reading them from a file in "Saved Requests" directory
    private void requestsInit() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("Saved Requests/requests.bin")))) {
            requests = (ArrayList) ois.readObject();
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * the request that has been initialized with user inputs is passed to this method
     * and its response is received here.
     *
     * @param request the request
     * @throws IOException       the io exception
     * @throws ProtocolException the protocol exception
     */
    public void requestProcessing(HttpUriRequestBase request) throws IOException, ProtocolException {


        CloseableHttpClient client;
        //follow redirect or not
        if (currentRequest.isFollowRedirect())
            client = HttpClientBuilder.create().build();
        else
            client = HttpClientBuilder.create().disableRedirectHandling().build();
        long start = System.currentTimeMillis();
        CloseableHttpResponse response = client.execute(request);
        long time = System.currentTimeMillis() - start; //time took for response to be received
        currentRequest.setHaveResponse(true);
        currentRequest.getResponse().setTime(time / 1000.0);

        StringBuilder output = new StringBuilder();
        output.append(response.getVersion()).append(" ")
                .append(response.getCode()).append(" ")
                .append(response.getReasonPhrase()).append("\n");
        for (Header header : response.getHeaders())
            output.append(header).append("\n");
        currentRequest.getResponse().setContentType(response.getHeader("Content-Type").getValue());
        String responseBody;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(response.getEntity() == null)
            responseBody = "";
        else {
            //reading response body
            InputStream in = response.getEntity().getContent();
            int r;
            byte[] buffer = new byte[4096];
            while ((r = in.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, r);
            }
            baos.flush();
            responseBody = baos.toString();
            currentRequest.getResponse().setSize(baos.toByteArray().length);
            in.close();
            baos.close();
        }
        //if body is an image
//        if (response.getHeader("Content-Type").getValue().contains("image/png")) {
//            File file = new File("Saved Images/" + currentRequest.createRandomFileName());
//            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
//            os.write(baos.toByteArray());
//            baos.close();
//            os.close();
//        }
        if (response.getHeader("Content-Type").getValue().contains("application/json"))
            currentRequest.getResponse().setBody(new JSONObject(responseBody).toString(2));
        else
            currentRequest.getResponse().setBody(responseBody);
        output.append("\n").append(responseBody);
        System.out.println(output.toString());
        currentRequest.getResponse().setCode(response.getCode());
        currentRequest.getResponse().setHeaders(response.getHeaders());
        currentRequest.getResponse().setStatusMessage(response.getReasonPhrase());

    }

    /**
     * add a new Request to requests' list
     *
     * @param request the request
     */
    public void saveRequest(Request request) {
        requests.add(request);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Saved Requests/requests.bin")))) {
            oos.writeObject(requests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * save updated condition of the requests in saved requests file
     */
    public void updateRequests() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("Saved Requests/requests.bin")))) {
            oos.writeObject(requests);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
