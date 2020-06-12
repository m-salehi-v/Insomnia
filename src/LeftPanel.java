import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * it represents the left panel of the application that contains insomnia logo
 * and a button to add a new request and a list of saved requests.
 *
 * @author Mohammad Salehi vaziri
 */
public class LeftPanel extends JPanel {
    private JLabel logo;
    //saved requests will be hold in this list
    private DefaultListModel<Request> requestsListModel;
    private JList<Request> requestsList;
    private JComboBox<String> newRequestMethodsComboBox;
    private JTextField newRequestNameTextField;
    private JButton createNewRequestButton;

    /**
     * Instantiates a new Left panel.
     */
    public LeftPanel() {
        super();
        setBackground(new Color(46, 47, 43));
        setLayout(new BorderLayout());

        requestsListModel = new DefaultListModel<>();
        createNewRequestButton = new JButton("Create");
        logoInit();
        requestsListInit();

        add(logo, BorderLayout.NORTH);
    }

    public JList<Request> getRequestsList() {
        return requestsList;
    }

    public JComboBox<String> getNewRequestMethodsComboBox() {
        return newRequestMethodsComboBox;
    }

    public JTextField getNewRequestNameTextField() {
        return newRequestNameTextField;
    }

    public JButton getCreateNewRequestButton() {
        return createNewRequestButton;
    }

    public void requestsListModelInit(ArrayList<Request> requests){
        for (Request request :requests)
            requestsListModel.addElement(request);
        revalidate();
        repaint();
    }

    //initializes logo's JLabel
    private void logoInit() {

        logo = new JLabel("   Insomnia");
        logo.setOpaque(true);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Calibri", Font.PLAIN, 20));
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        logo.setBackground(new Color(105, 94, 184));
        logo.setPreferredSize(new Dimension(logo.getPreferredSize().width, logo.getPreferredSize().height + 18));
    }

    //forms "add new request" button and saved requests list
    private void requestsListInit(){
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(new Color(46, 47, 43));
        panel.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        JButton newRequestB = new JButton(new ImageIcon(this.getClass().getResource("res/newRequest.png")));
        newRequestB.setFocusable(false);
        newRequestB.setToolTipText("New Request");
        newRequestB.setBorder(BorderFactory.createEmptyBorder());
        newRequestB.setBackground(new Color(46, 47, 43));
        newRequestB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewRequestFrame();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(46, 47, 43));
        buttonPanel.add(newRequestB);

        requestsList = new JList<>(requestsListModel);
        requestsList.setCellRenderer(new RequestRenderer());
        requestsList.setBackground(new Color(46, 47, 43));
        JScrollPane spRequestList = new JScrollPane(requestsList);
        spRequestList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(spRequestList, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    //adds the given request to saved requests list
    public void addRequest(Request request){
        requestsListModel.addElement(request);
    }

    //shows a frame to get a name and method and add a new saved request with
    //the given name and method
    private void showNewRequestFrame(){
        JFrame frame = new JFrame("New Request");
        frame.setSize(800,180);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon("src/res/Insomnia.png").getImage());
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        frame.setContentPane(panel);

        JLabel label = new JLabel("Name");
        newRequestNameTextField = new JTextField("My Request");
        String[] methods = {"GET", "POST", "PUT", "PATCH", "DELETE"};
        newRequestMethodsComboBox = new JComboBox<>(methods);
        newRequestMethodsComboBox.setPreferredSize(new Dimension(newRequestMethodsComboBox.getPreferredSize().width + 20, newRequestMethodsComboBox.getPreferredSize().height));
//        createNewRequestButton = new JButton("Create");
        createNewRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                addRequest(new Request(newRequestName.getText(), (String) newRequestMethods.getSelectedItem()));
                frame.dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createNewRequestButton);
        panel.add(label, BorderLayout.NORTH);
        panel.add(newRequestNameTextField, BorderLayout.CENTER);
        panel.add(newRequestMethodsComboBox, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void addListSelectionHandler(ListSelectionListener listener){
        requestsList.addListSelectionListener(listener);
    }

    public void addCreateNewRequestHandler(ActionListener listener){
        createNewRequestButton.addActionListener(listener);
    }
    //it shows each request as a JLabel
    private class RequestRenderer extends JLabel implements ListCellRenderer<Request> {

        public RequestRenderer() {
            //this line of code is out of method below because every time that method is called size of label increases
            setPreferredSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height + 35));
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends Request> list, Request value, int index, boolean isSelected, boolean cellHasFocus) {
            String method = value.getMethod();
            switch (method) {
                case "GET":
                    method = "<font style=\"color : rgb(171, 158, 221)\">&ensp; GET &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "POST":
                    method = "<font style=\"color : rgb(132, 186, 77)\">&ensp; POST &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "PUT":
                    method = "<font style=\"color : rgb(223, 160, 81)\">&ensp; PUT &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                case "PATCH":
                    method = "<font style=\"color : rgb(193, 174, 61)\">&ensp; PTCH &nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
                default:
                    method = "<font style=\"color : rgb(226, 139, 138)\">&ensp; DEL &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
                    break;
            }

            if (isSelected){
                setBackground(new Color(55, 56, 52));
                setText("<html>" + method + "<font style=\"color : rgb(255, 255, 255)\">" + value.getName() + "</font></html>");
            }
            else {
                setBackground(new Color(46, 47, 43));
                setText("<html>" + method + "<font style=\"color : rgb(166, 163, 161)\">" + value.getName() + "</font></html>");
            }
            setOpaque(true);

            return this;
        }
    }
}

