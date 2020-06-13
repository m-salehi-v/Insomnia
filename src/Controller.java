import org.apache.hc.core5.http.Header;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is in charge of connecting logic and UI of the program.
 * it contains some action handlers and some methods to update UI.
 *
 * @author Mohammad Salehi Vaziri
 */
public class Controller {
    private InsomniaView view;
    private HttpManager model;
    private RightPanel rightPanel;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;

    /**
     * Instantiates a new Controller.
     *
     * @param view  the view(UI)
     * @param model the model(httpManager)
     */
    public Controller(InsomniaView view, HttpManager model) {
        this.view = view;
        this.model = model;

        rightPanel = view.getRightPanel();
        middlePanel = view.getMiddlePanel();
        leftPanel = view.getLeftPanel();
        //adding action handlers to view items
        leftPanel.requestsListModelInit(model.getRequests());
        leftPanel.addCreateNewRequestHandler(new NewRequestHandler());
        leftPanel.addListSelectionHandler(new RequestsListSelectionHandler());
        middlePanel.addSendRequestActionHandler(new SendRequestHandler());
    }

    //this method shows details of a request such as headers, queries, response, ...
    //that is selected from the requests list on the left.
    private class RequestsListSelectionHandler implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList<Request> requestJList = (JList<Request>) e.getSource();
            Request selectedRequest = requestJList.getSelectedValue();
            updateView(selectedRequest);
        }
    }

    //adding a new request to the list of saved requests
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

    //sending a request is handled here and it is implemented by swing worker
    //so it doesn't freeze while waiting for a response
    private class SendRequestHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SendRequest(view, model).execute();
        }
    }

    //update view according to properties of the selected request.
    private void updateView(Request selectedRequest) {
        update(selectedRequest, middlePanel, rightPanel, leftPanel);
    }

    /**
     * update all three panels' items according to the selected request.
     *
     * @param selectedRequest the selected request
     * @param middlePanel     the middle panel
     * @param rightPanel      the right panel
     * @param leftPanel       the left panel
     */
    static void update(Request selectedRequest, MiddlePanel middlePanel, RightPanel rightPanel, LeftPanel leftPanel) {
        middlePanel.setSelectedMethod(selectedRequest.getMethod());
        middlePanel.setUrlText(selectedRequest.getUrl());
        middlePanel.removeAllItems();
        //unchecked ones are the ones which their checkbox was not selected
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
            rightPanel.addStatusLine(selectedRequest.getResponse().getCode(), selectedRequest.getResponse().getStatusMessage());
            rightPanel.addTimeLabel(selectedRequest.getResponse().getTime());
            rightPanel.addSizeLabel(selectedRequest.getResponse().getSize());

            rightPanel.getHeader().removeAll();
            try {
                StringBuilder text = new StringBuilder();
                for (Header header : selectedRequest.getResponse().getHeaders()) {
                    rightPanel.addHeader(header.getName(), header.getValue());
                    text.append(header.getName()).append(": ").append(header.getValue()).append("\n");
                }
                rightPanel.addCopyButton();
                rightPanel.getCpyButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        StringSelection stringSelection = new StringSelection(text.toString());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                    }
                });
            }catch (NullPointerException e){ }
            ((JTextArea)((JScrollPane) rightPanel.getRaw().getComponent(0)).getViewport().getView()).setText("");
            ((JTextArea) ((JScrollPane) rightPanel.getRaw().getComponent(0)).getViewport().getView()).setText(selectedRequest.getResponse().getBody());

            rightPanel.setEditorPaneURL(selectedRequest.getUrl() + selectedRequest.getQuery());
        }
        rightPanel.revalidate();
        middlePanel.revalidate();
        leftPanel.revalidate();
    }
}
