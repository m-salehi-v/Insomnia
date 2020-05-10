import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MiddlePanel extends JPanel {
    private JPanel topPanel;
    private JTabbedPane tabs;
    private JPanel header;
    private JPanel query;

    public MiddlePanel() {
        super();
        setLayout(new BorderLayout());

        topPanelInit();
        add(topPanel, BorderLayout.NORTH);

        tabsInit();
        add(tabs, BorderLayout.CENTER);
    }

    private void topPanelInit() {
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));

        String[] methods = {"GET", "POST", "PUT", "PATCH", "DELETE"};
        JComboBox<String> method = new JComboBox<>(methods);
        method.setPreferredSize(new Dimension(method.getPreferredSize().width + 25, method.getPreferredSize().height));

        JTextField urlField = new JTextField("Enter URL here");
        urlField.setFont(new Font("Arial", Font.PLAIN, 12));
        urlField.setForeground(new Color(187, 187, 187));
        urlField.addFocusListener(new PromptTextHandler());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        JButton send = new JButton("Send");
        send.setPreferredSize(new Dimension(send.getPreferredSize().width, send.getPreferredSize().height));
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(save.getPreferredSize().width, save.getPreferredSize().height));
        buttonsPanel.add(send);
        buttonsPanel.add(save);

        topPanel.add(method, BorderLayout.WEST);
        topPanel.add(urlField, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
    }

    private void tabsInit() {
        tabs = new JTabbedPane();

        ArrayList<JPanel> headers = new ArrayList<>();
        header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(46, 47, 43));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JScrollPane headerScrollable = new JScrollPane(header);

        ArrayList<JPanel> queries = new ArrayList<>();
        query = new JPanel();
        query.setLayout(new BoxLayout(query, BoxLayout.Y_AXIS));
        query.setBackground(new Color(46, 47, 43));
        query.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JScrollPane queryScrollable = new JScrollPane(query);

        tabs.add(null, "Body");
        tabs.add(null, "Auth");
        tabs.add(queryScrollable, "Query");
        tabs.add(headerScrollable, "Header");

        tabs.setFont(new Font(null, Font.PLAIN, 13));

        tabs.setForegroundAt(0, new Color(187, 187, 187));
        tabs.setForegroundAt(1, new Color(187, 187, 187));
        tabs.setForegroundAt(2, new Color(187, 187, 187));
        tabs.setForegroundAt(3, new Color(187, 187, 187));

        addHeaderOrQuery(headers, 1);
        addHeaderOrQuery(queries, 2);
    }

    private JPanel createItem(ArrayList<JPanel> items, int type, boolean newItem) { //0 for new 1 for header 2 for query

        JPanel panel = new JPanel();
        panel.setBackground(new Color(46, 47, 43));
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(2000, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        JPanel textFields = new JPanel();
        textFields.setBackground(new Color(46, 47, 43));
        textFields.setLayout(new BoxLayout(textFields, BoxLayout.X_AXIS));

        ImageIcon headerIcon;
        JLabel label = new JLabel();
        if (newItem) {
            headerIcon = new ImageIcon(this.getClass().getResource("res/settingIcon.png"));
            label.addMouseListener(new deleteAllHandler(items, type));
        }
        else{
            headerIcon = new ImageIcon(this.getClass().getResource("res/header.png"));
        }
        label.setIcon(headerIcon);
        panel.add(label, BorderLayout.WEST);

        JTextField textField1 = new JTextField();
        if (newItem){
            if (type == 1)
                textField1 = new JTextField("New Header");
            else
                textField1 = new JTextField("New Name");
            textField1.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    addHeaderOrQuery(items, type);
                }
            });
        }
        else if (type == 1) {
            textField1.setText("Header");
            textField1.addFocusListener(new PromptTextHandler());
        } else {
            textField1.setText("Name");
            textField1.addFocusListener(new PromptTextHandler());
        }
        textField1.setForeground(Color.GRAY);
        textField1.setMaximumSize(new Dimension(2000, 25));
        textField1.setBackground(new Color(46, 47, 43));
        textField1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(107, 107, 107)));

        JTextField textField2 = new JTextField();
        if (newItem){
            textField2.setText("New Value");
            textField2.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    addHeaderOrQuery(items, type);
                }
            });
        } else {
            textField2.setText("Value");
            textField2.addFocusListener(new PromptTextHandler());
        }
        textField2.setForeground(Color.GRAY);
        textField2.setMaximumSize(new Dimension(2000, 25));
        textField2.setBackground(new Color(46, 47, 43));
        textField2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(107, 107, 107)));

        textFields.add(textField1);
        textFields.add(Box.createRigidArea(new Dimension(20, 0)));
        textFields.add(textField2);

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(46, 47, 43));
        if (newItem)
            buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        else  {
            buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            JCheckBox checkBox = new JCheckBox();
            checkBox.addActionListener(new CheckBoxActionHandler(textField1, textField2));

            ImageIcon delIcon = new ImageIcon(this.getClass().getResource("res/delete.png"));
            JButton deleteB = new JButton(delIcon);
            deleteB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    items.remove(panel);
                    updateHeaderAndQuery(items, type);
                    updateUI();
                }
            });
            deleteB.setToolTipText("Delete this item");
            buttons.add(checkBox);
            buttons.add(deleteB);
        }
        panel.add(textFields, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void addHeaderOrQuery(ArrayList<JPanel> items, int type) {
            items.add(createItem(items, type, false));
            updateHeaderAndQuery(items, type);
            updateUI();
    }

    private void updateHeaderAndQuery(ArrayList<JPanel> items, int type) {
        if (type == 1) {
            header.removeAll();
            for (JPanel jPanel : items) {
                header.add(jPanel);
            }
            header.add(createItem(items, 1, true));
        } else {
            query.removeAll();
            for (JPanel jPanel : items) {
                query.add(jPanel);
            }
            query.add(createItem(items,2, true));
        }
    }

    private static class PromptTextHandler implements FocusListener {
        private String prevText;

        @Override
        public void focusGained(FocusEvent e) {
            JTextField field = (JTextField) e.getSource();
            prevText = field.getText();
            if (field.getText().equals("Enter URL here") || field.getText().equals("Header") || field.getText().equals("Value"))
                field.setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {
            JTextField field = (JTextField) e.getSource();
            if (field.getText().equals("")) {
                if (prevText.equals("Enter URL here"))
                    field.setText("Enter URL here");
                else if (prevText.equals("Header"))
                    field.setText("Header");
                else
                    field.setText("Value");
            }
        }
    }

    private static class CheckBoxActionHandler implements ActionListener {

        JTextField textField1;
        JTextField textField2;

        CheckBoxActionHandler(JTextField textField1, JTextField textField2) {
            this.textField1 = textField1;
            this.textField2 = textField2;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox) e.getSource();
            if (cb.isSelected()) {
                textField1.setForeground(new Color(76, 76, 76));
                textField2.setForeground(new Color(76, 76, 76));
                textField1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(76, 76, 76)));
                textField2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(76, 76, 76)));

            } else {
                textField1.setForeground(Color.GRAY);
                textField2.setForeground(Color.GRAY);
                textField1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(107, 107, 107)));
                textField2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(107, 107, 107)));
            }
        }
    }

    private class deleteAllHandler extends MouseAdapter{
        ArrayList<JPanel> headers;
        int type;

        public deleteAllHandler(ArrayList<JPanel> headers, int type) {
            this.headers = headers;
            this.type = type;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("delete all");
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (headers.size() > 0) {
                        headers.subList(0, headers.size()).clear();
                    }
                    updateHeaderAndQuery(headers, type);
                    updateUI();
                }
            });
            popupMenu.add(menuItem);
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON1) {
                popupMenu.show(e.getComponent(),
                        e.getX(), e.getY() + 5);
            }
        }
    }
}
