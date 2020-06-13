import org.apache.hc.core5.http.Header;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            updateView(selectedRequest);
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
            new SendRequest(view, model).execute();
        }
    }

    private void updateView(Request selectedRequest) {
        update(selectedRequest, middlePanel, rightPanel, leftPanel);
    }

    static void update(Request selectedRequest, MiddlePanel middlePanel, RightPanel rightPanel, LeftPanel leftPanel) {
        middlePanel.setSelectedMethod(selectedRequest.getMethod());
        middlePanel.setUrlText(selectedRequest.getUrl());
        middlePanel.removeAllItems();
        for (String key : selectedRequest.getHeaders().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getHeaders(), 1, false, key, selectedRequest.getHeaders().get(key));
            middlePanel.addHeaderQueryForm(middlePanel.getHeaders(), item, 1);
        }
        for (String key : selectedRequest.getUncheckedHeaders().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getHeaders(), 1, false, key, selectedRequest.getUncheckedHeaders().get(key));
            ((JCheckBox) ((JPanel) item.getComponent(2)).getComponent(0)).setSelected(false);
            middlePanel.addHeaderQueryForm(middlePanel.getHeaders(), item, 1);
        }
        for (String name : selectedRequest.getFormData().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getData(), 3, false, name, selectedRequest.getFormData().get(name));
            middlePanel.addHeaderQueryForm(middlePanel.getData(), item, 3);
        }
        for (String key : selectedRequest.getUncheckedFormData().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getHeaders(), 3, false, key, selectedRequest.getUncheckedFormData().get(key));
            ((JCheckBox) ((JPanel) item.getComponent(2)).getComponent(0)).setSelected(false);
            middlePanel.addHeaderQueryForm(middlePanel.getData(), item, 3);
        }
        for (String name : selectedRequest.getQueries().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getQueries(), 2, false, name, selectedRequest.getQueries().get(name));
            middlePanel.addHeaderQueryForm(middlePanel.getQueries(), item, 2);
        }
        for (String name : selectedRequest.getUncheckedQueries().keySet()) {
            JPanel item = middlePanel.createItem(middlePanel.getQueries(), 2, false, name, selectedRequest.getUncheckedQueries().get(name));
            ((JCheckBox) ((JPanel) item.getComponent(2)).getComponent(0)).setSelected(false);
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
            ((JTextArea)((JScrollPane) rightPanel.getPreview().getComponent(0)).getViewport().getView()).setText("");
            ((JTextArea) ((JScrollPane) rightPanel.getPreview().getComponent(0)).getViewport().getView()).setText(selectedRequest.getResponseBody());
        }
        rightPanel.revalidate();
        middlePanel.revalidate();
        leftPanel.revalidate();
    }
}
