import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private InsomniaView view;
    private HttpManager model;

    public Controller(InsomniaView view, HttpManager model) {
        this.view = view;
        this.model = model;

        view.getLeftPanel().requestsListModelInit(model.getRequests());
        view.getLeftPanel().addCreateNewRequestHandler(new NewRequestHandler());

    }

    private class RequestsListSelectionHandler implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {

        }
    }

    private class NewRequestHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Request request = new Request(view.getLeftPanel().getNewRequestNameTextField().getText(),
                    (String) view.getLeftPanel().getNewRequestMethodsComboBox().getSelectedItem());
            model.setCurrentRequest(request);
            model.saveRequest(request);
            view.getLeftPanel().addRequest(request);

        }
    }
}
