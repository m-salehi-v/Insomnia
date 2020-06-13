import org.apache.hc.core5.http.ProtocolException;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class SendRequest extends SwingWorker {
    private InsomniaView view;
    private HttpManager model;
    private RightPanel rightPanel;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;

    public SendRequest(InsomniaView view, HttpManager model) {
        this.view = view;
        this.model = model;

        rightPanel = view.getRightPanel();
        middlePanel = view.getMiddlePanel();
        leftPanel = view.getLeftPanel();
    }
    @Override
    protected Object doInBackground() throws Exception {
        Request selectedRequest = leftPanel.getRequestsList().getSelectedValue();
        if (selectedRequest != null) {
            selectedRequest.setUrl(middlePanel.getUrlField().getText());
            selectedRequest.setMethod((String) middlePanel.getMethod().getSelectedItem());
            selectedRequest.getHeaders().clear();
            selectedRequest.getUncheckedHeaders().clear();
            for (JPanel headersPanel : middlePanel.getHeaders()) {
                JCheckBox checkBox = (JCheckBox) ((JPanel) headersPanel.getComponent(2)).getComponent(0);
                JTextField nameField = (JTextField) ((JPanel) headersPanel.getComponent(1)).getComponent(0);
                JTextField valueField = (JTextField) ((JPanel) headersPanel.getComponent(1)).getComponent(2);
                if (checkBox.isSelected()) {
                    if (!nameField.getText().equals("Header") || !valueField.getText().equals("Value")) {
                        selectedRequest.getHeaders().put(nameField.getText(), valueField.getText());
                    }
                }else
                    selectedRequest.getUncheckedHeaders().put(nameField.getText(), valueField.getText());
            }
            String selectedMessageBody = ((JLabel) middlePanel.getTabs().getTabComponentAt(0)).getText();
            selectedRequest.getFormData().clear();
            selectedRequest.getUncheckedFormData().clear();
            if (selectedMessageBody.equals("Form Data")) {
                for (JPanel formDataPanel : middlePanel.getData()) {
                    JCheckBox checkBox = (JCheckBox) ((JPanel) formDataPanel.getComponent(2)).getComponent(0);
                    JTextField nameField = (JTextField) ((JPanel) formDataPanel.getComponent(1)).getComponent(0);
                    JTextField valueField = (JTextField) ((JPanel) formDataPanel.getComponent(1)).getComponent(2);
                    if (checkBox.isSelected()) {
                        if (!nameField.getText().equals("Name") || !valueField.getText().equals("Value")) {
                            selectedRequest.getFormData().put(nameField.getText(), valueField.getText());
                            selectedRequest.setContentType("multipart/form-data");
                        }
                    }else
                        selectedRequest.getUncheckedFormData().put(nameField.getText(), valueField.getText());
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
            selectedRequest.getUncheckedQueries().clear();
            for (JPanel queryPanel : middlePanel.getQueries()) {
                JCheckBox checkBox = (JCheckBox) ((JPanel) queryPanel.getComponent(2)).getComponent(0);
                JTextField nameField = (JTextField) ((JPanel) queryPanel.getComponent(1)).getComponent(0);
                JTextField valueField = (JTextField) ((JPanel) queryPanel.getComponent(1)).getComponent(2);
                if (checkBox.isSelected()) {
                    if (!nameField.getText().equals("Name") || !valueField.getText().equals("Value")) {
                        selectedRequest.getQueries().put(nameField.getText(), valueField.getText());
                    }
                }else
                    selectedRequest.getUncheckedQueries().put(nameField.getText(), valueField.getText());
            }
            if (view.isFollowRedirect())
                selectedRequest.setFollowRedirect(true);
            else
                selectedRequest.setFollowRedirect(false);
            model.setCurrentRequest(selectedRequest);
            try {
                model.requestProcessing(selectedRequest.build());
            } catch (IOException | ProtocolException | URISyntaxException ex) {
                selectedRequest.getResponse().setSize(0);
                selectedRequest.getResponse().setStatusMessage("Error");
                selectedRequest.getResponse().setTime(0);
                selectedRequest.getResponse().setBody("Error: " + ex.getMessage());
                selectedRequest.getResponse().setHeaders(null);
                rightPanel.revalidate();
                ex.printStackTrace();
            }
            model.updateRequests();
            updateView(selectedRequest);
        }
        return null;
    }
    private void updateView(Request selectedRequest) {
        Controller.update(selectedRequest, middlePanel, rightPanel, leftPanel);
    }
}
