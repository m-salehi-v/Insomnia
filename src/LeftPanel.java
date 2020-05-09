import javax.swing.*;
import java.awt.*;

public class LeftPanel extends JPanel {
    private JLabel logo;
    private DefaultListModel<Request> requestsListModel;

    public LeftPanel() {
        super();
        setBackground(new Color(46, 47, 43));
        setLayout(new BorderLayout());

        logoInit();
        requestsListInit();

        addRequest(new Request("My Request1", "GET"));
        addRequest(new Request("My Request2", "POST"));
        addRequest(new Request("My Request3", "PUT"));
        addRequest(new Request("My Request4", "PATCH"));
        addRequest(new Request("My Request5", "DELETE"));

        add(logo, BorderLayout.NORTH);
    }

    private void logoInit() {

        logo = new JLabel("   Insomnia");
        logo.setOpaque(true);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Calibri", Font.PLAIN, 20));
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        logo.setBackground(new Color(105, 94, 184));
        logo.setPreferredSize(new Dimension(logo.getPreferredSize().width, logo.getPreferredSize().height + 18));
    }

    private void requestsListInit(){
        requestsListModel = new DefaultListModel<>();
        JList<Request> requestsList = new JList<>(requestsListModel);
        requestsList.setCellRenderer(new RequestRenderer());
        requestsList.setBackground(new Color(46, 47, 43));
        JScrollPane spRequestList = new JScrollPane(requestsList);
        spRequestList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(spRequestList, BorderLayout.CENTER);
    }

    private void addRequest(Request request){
        requestsListModel.addElement(request);
    }

    private class RequestRenderer extends JLabel implements ListCellRenderer<Request> {

        public RequestRenderer() {
            //this line of code is out of method below because every time that method is called size of label increases
            setPreferredSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height + 35));
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends Request> list, Request value, int index, boolean isSelected, boolean cellHasFocus) {
            String type = value.getType();
            switch (type) {
                case "GET":
                    type = "<font style=\"color : rgb(171, 158, 221)\">&ensp; GET &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "POST":
                    type = "<font style=\"color : rgb(132, 186, 77)\">&ensp; POST &nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "PUT":
                    type = "<font style=\"color : rgb(223, 160, 81)\">&ensp; PUT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "PATCH":
                    type = "<font style=\"color : rgb(193, 174, 61)\">&ensp; PTCH &nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                default:
                    type = "<font style=\"color : rgb(226, 139, 138)\">&ensp; DEL &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
            }

            if (isSelected){
                setBackground(new Color(55, 56, 52));
                setText("<html>" + type + "<font style=\"color : rgb(255, 255, 255)\">" + value.getName() + "</font></html>");
            }
            else {
                setBackground(new Color(46, 47, 43));
                setText("<html>" + type + "<font style=\"color : rgb(166, 163, 161)\">" + value.getName() + "</font></html>");
            }
            setOpaque(true);

            return this;
        }
    }
}

