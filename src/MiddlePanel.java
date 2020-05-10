import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MiddlePanel extends JPanel {
    private JPanel topPanel;
    private JTabbedPane tabs;
    private JPanel header;

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
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JScrollPane headerScrollable = new JScrollPane(header);

        tabs.add(null, "Body");
        tabs.add(null, "Auth");
        tabs.add(null, "Query");
        tabs.add(headerScrollable, "Header");

        tabs.setFont(new Font(null, Font.PLAIN, 13));

        tabs.setForegroundAt(0, new Color(187, 187, 187));
        tabs.setForegroundAt(1, new Color(187, 187, 187));
        tabs.setForegroundAt(2, new Color(187, 187, 187));
        tabs.setForegroundAt(3, new Color(187, 187, 187));

        addHeader(headers);
    }

    private JPanel createHeader(ArrayList<JPanel> headers, int type) {

        JPanel panel = new JPanel();
        panel.setBackground(new Color(46, 47, 43));
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(2000, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel textFields = new JPanel();
        textFields.setBackground(new Color(46, 47, 43));
        textFields.setLayout(new BoxLayout(textFields, BoxLayout.X_AXIS));

        ImageIcon headerIcon;
        JLabel label = new JLabel();
        if (type == 1)
            headerIcon = new ImageIcon(this.getClass().getResource("res/header.png"));
        else {
            headerIcon = new ImageIcon(this.getClass().getResource("res/settingIcon.png"));
            label.addMouseListener(new deleteAllHandler(headers));
        }
        label.setIcon(headerIcon);
        panel.add(label, BorderLayout.WEST);

        JTextField textField1 = new JTextField();
        if (type == 1) {
            textField1.setText("Header");
            textField1.addFocusListener(new PromptTextHandler());
        } else {
            textField1 = new JTextField("New Header");
            textField1.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    addHeader(headers);
                }
            });
        }
        textField1.setForeground(Color.GRAY);
        textField1.setMaximumSize(new Dimension(2000, 25));
        textField1.setBackground(new Color(46, 47, 43));
        textField1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(107, 107, 107)));

        JTextField textField2 = new JTextField();
        if (type == 1) {
            textField2.setText("Value");
            textField2.addFocusListener(new PromptTextHandler());
        } else {
            textField2.setText("New Value");
            textField2.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    addHeader(headers);
                }
            });
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
        if (type == 1) {
            buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            JCheckBox checkBox = new JCheckBox();
            checkBox.addActionListener(new CheckBoxActionHandler(textField1, textField2));

            ImageIcon delIcon = new ImageIcon(this.getClass().getResource("res/delete.png"));
            JButton deleteB = new JButton(delIcon);
            deleteB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    headers.remove(panel);
                    updateHeader(headers);
                    updateUI();
                }
            });
            deleteB.setToolTipText("Delete this item");
            buttons.add(checkBox);
            buttons.add(deleteB);
        } else {
            buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
        }
        panel.add(textFields, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        return panel;
    }

    private void addHeader(ArrayList<JPanel> headers) {
        headers.add(createHeader(headers, 1));
        updateHeader(headers);
        updateUI();
    }

    private void updateHeader(ArrayList<JPanel> headers) {
        header.removeAll();
        for (JPanel jPanel : headers) {
            header.add(jPanel);
        }
        header.add(createHeader(headers, 2));
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

        public deleteAllHandler(ArrayList<JPanel> headers) {
            this.headers = headers;
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
                    updateHeader(headers);
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
