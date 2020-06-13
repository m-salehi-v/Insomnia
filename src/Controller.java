import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Controller {
    private InsomniaView view;
    private HttpManager model;
    private RightPanel rightPanel;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;

    public Controller(InsomniaView view, HttpManager model) {
        this.view = view;
        this.model = model;

        rightPanel = view.getRightPanel();
        middlePanel = view.getMiddlePanel();
        leftPanel = view.getLeftPanel();
        leftPanel.requestsListModelInit(model.getRequests());
        leftPanel.addCreateNewRequestHandler(new NewRequestHandler());
        leftPanel.addListSelectionHandler(new RequestsListSelectionHandler());
        middlePanel.addSendRequestActionHandler(new SendRequestHandler());
    }

    private class RequestsListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList<Request> requestJList = (JList<Request>) e.getSource();
            Request selectedRequest = requestJList.getSelectedValue();
            updateFrame(selectedRequest);
        }
    }

    private class NewRequestHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Request request = new Request(leftPanel.getNewRequestNameTextField().getText(),
                    (String) leftPanel.getNewRequestMethodsComboBox().getSelectedItem());
            model.setCurrentRequest(request);
            model.saveRequest(request);
            leftPanel.addRequest(request);

        }
    }

    private class SendRequestHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Request selectedRequest = leftPanel.getRequestsList().getSelectedValue();
            if (selectedRequest != null) {
                selectedRequest.setUrl(middlePanel.getUrlField().getText());
                selectedRequest.setMethod((String) middlePanel.getMethod().getSelectedItem());
                selectedRequest.getHeaders().clear();
                for (JPanel headersPanel : middlePanel.getHeaders()) {
                    JCheckBox checkBox = (JCheckBox) ((JPanel) headersPanel.getComponent(2)).getComponent(0);
                    JTextField nameField = (JTextField) ((JPanel) headersPanel.getComponent(1)).getComponent(0);
                    JTextField valueField = (JTextField) ((JPanel) headersPanel.getComponent(1)).getComponent(2);
                    if (checkBox.isEnabled()) {
                        if (!nameField.getText().equals("Header") || !valueField.getText().equals("Value")) {
                            selectedRequest.getHeaders().put(nameField.getText(), valueField.getText());
                        }
                    }
                }
                String selectedMessageBody = ((JLabel) middlePanel.getTabs().getTabComponentAt(0)).getText();
                selectedRequest.getFormData().clear();
                if (selectedMessageBody.equals("Form Data")) {
                    for (JPanel formDataPanel : middlePanel.getData()) {
                        JCheckBox checkBox = (JCheckBox) ((JPanel) formDataPanel.getComponent(2)).getComponent(0);
                        JTextField nameField = (JTextField) ((JPanel) formDataPanel.getComponent(1)).getComponent(0);
                        JTextField valueField = (JTextField) ((JPanel) formDataPanel.getComponent(1)).getComponent(2);
                        if (checkBox.isEnabled()) {
                            if (!nameField.getText().equals("Name") || !valueField.getText().equals("Value")) {
                                selectedRequest.getFormData().put(nameField.getText(), valueField.getText());
                            }
                        }
                    }
                } else if (selectedMessageBody.equals("JSON")) {
                    selectedRequest.setJson(((JTextArea) ((JScrollPane) middlePanel.getJson().getComponent(0)).getViewport().getView()).getText());
                    selectedRequest.setContentType("application/json");
                } else if (selectedMessageBody.equals("Binary")) {
                    if (!middlePanel.getBinaryPath().getText().equals("No File Selected")){
                        selectedRequest.getFormData().put("file", middlePanel.getBinaryPath().getText());
                        selectedRequest.setContentType("application/octet-stream");
                    }
                }
                selectedRequest.getQueries().clear();
                for (JPanel queryPanel : middlePanel.getQueries()) {
                    JCheckBox checkBox = (JCheckBox) ((JPanel) queryPanel.getComponent(2)).getComponent(0);
                    JTextField nameField = (JTextField) ((JPanel) queryPanel.getComponent(1)).getComponent(0);
                    JTextField valueField = (JTextField) ((JPanel) queryPanel.getComponent(1)).getComponent(2);
                    if (checkBox.isEnabled()) {
                        if (!nameField.getText().equals("Name") || !valueField.getText().equals("Value")) {
                            selectedRequest.getQueries().put(nameField.getText(), valueField.getText());
                        }
                    }
                }
                if (view.isFollowRedirect())
                    selectedRequest.setFollowRedirect(true);
                else
                    selectedRequest.setFollowRedirect(false);
                model.setCurrentRequest(selectedRequest);
                try {
                    model.requestProcessing(selectedRequest.build());
                } catch (IOException | ProtocolException | URISyntaxException ex) {
                    selectedRequest.setResponseSize("0");
                    selectedRequest.setResponseStatusMessage("Error");
                    selectedRequest.setResponseTime(0);
                    selectedRequest.setResponseBody("");
                    selectedRequest.setResponseHeaders(null);
                    rightPanel.revalidate();
                    ex.printStackTrace();
                }
                model.updateRequests();
                updateFrame(selectedRequest);
            }
        }
    }

    private void updateFrame(Request selectedRequest) {
        middlePanel.setSelectedMethod(selectedRequest.getMethod());
        middlePanel.setUrlText(selectedRequest.getUrl());
        middlePanel.removeAllItems();
        for (String key : selectedRequest.getHeaders().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getHeaders(), 1, false, key, selectedRequest.getHeaders().get(key));
            middlePanel.addHeaderQueryForm(middlePanel.getHeaders(), item, 1);
        }
        for (String name : selectedRequest.getFormData().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getData(), 3, false, name, selectedRequest.getFormData().get(name));
            middlePanel.addHeaderQueryForm(middlePanel.getData(), item, 3);
        }
        for (String name : selectedRequest.getQueries().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getQueries(), 2, false, name, selectedRequest.getQueries().get(name));
            middlePanel.addHeaderQueryForm(middlePanel.getQueries(), item, 2);
        }
        if (selectedRequest.getJson().length() != 0) {
            ((JTextArea) ((JScrollPane) middlePanel.getJson().getComponent(0)).getViewport().getView()).setText(selectedRequest.getJson());
        }
        if (selectedRequest.isHaveResponse()) {
            rightPanel.getTopPanel().removeAll();
            rightPanel.addStatusLine(selectedRequest.getResponseCode(), selectedRequest.getResponseStatusMessage());
            rightPanel.addTimeLabel(selectedRequest.getResponseTime());
            rightPanel.addSizeLabel(selectedRequest.getResponseSize());

            rightPanel.getHeader().removeAll();
            try {
                for (Header header : selectedRequest.getResponseHeaders())
                    rightPanel.addHeader(header.getName(), header.getValue());
            }catch (NullPointerException e){ }
            ((JTextArea)((JScrollPane)rightPanel.getPreview().getComponent(0)).getViewport().getView()).setText("");
            ((JTextArea) ((JScrollPane) rightPanel.getPreview().getComponent(0)).getViewport().getView()).setText(selectedRequest.getResponseBody());
        }
        rightPanel.revalidate();
        middlePanel.revalidate();
        leftPanel.revalidate();
    }
}
