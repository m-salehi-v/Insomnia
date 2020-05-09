import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MiddlePanel extends JPanel{
    private JPanel topPanel;
    private JTabbedPane tabs;
    public MiddlePanel() {
        super();
        setBackground(new Color(46, 47, 43));
        setLayout(new BorderLayout());

        topPanelInit();
        add(topPanel, BorderLayout.NORTH);

        tabsInit();
        add(tabs, BorderLayout.CENTER);
    }

    private void topPanelInit(){
        topPanel = new JPanel();
        topPanel.setBackground(new Color(255,255,255));
        topPanel.setLayout(new BorderLayout(0,0));

        String[] methods = {"GET", "POST", "PUT", "PATCH", "DELETE"};
        JComboBox<String> method = new JComboBox<>(methods);
        method.setPreferredSize(new Dimension(method.getPreferredSize().width + 5, method.getPreferredSize().height));
        method.setBackground(Color.WHITE);

        JTextField urlField = new JTextField("Enter URL here");
        urlField.setFont(new Font("Arial", Font.PLAIN, 12));
        urlField.setForeground(Color.DARK_GRAY);
        urlField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (urlField.getText().equals("Enter URL here"))
                    urlField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (urlField.getText().equals(""))
                    urlField.setText("Enter URL here");
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(255,255,255));
        JButton send = new JButton("Send");
        send.setBackground(Color.WHITE);
        send.setPreferredSize(new Dimension(send.getPreferredSize().width, send.getPreferredSize().height - 6));
        JButton save = new JButton("Save");
        save.setPreferredSize(new Dimension(save.getPreferredSize().width, save.getPreferredSize().height - 6));
        save.setBackground(Color.WHITE);
        buttonsPanel.add(send);
        buttonsPanel.add(save);

        topPanel.add(method, BorderLayout.WEST);
        topPanel.add(urlField, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
    }

    private void tabsInit(){
        tabs = new JTabbedPane();
    }
}
