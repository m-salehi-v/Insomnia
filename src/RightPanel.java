import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel topPanel;
    private JTabbedPane tabs;
    private JPanel header;
    private JPanel preview;

    public RightPanel() {
        super();
        setLayout(new BorderLayout());

        topPanelInit();
        add(topPanel, BorderLayout.NORTH);

        tabsInit();
        add(tabs, BorderLayout.CENTER);
    }


    private void topPanelInit() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        topPanel.add(createStatusLabel(0, "Error"));
        topPanel.add(createTimeLabel(0));
        topPanel.add(createSizeLabel(0));
    }

    private void tabsInit(){
        tabs = new JTabbedPane();

        headerInit();

        preview = new JPanel();
        preview.setBackground(new Color(46, 47, 43));

        tabs.add(preview, "Preview");
        tabs.add(new JScrollPane(header), "Header");
        tabs.setFont(new Font(null, Font.PLAIN, 13));
        tabs.setForegroundAt(0, new Color(187, 187, 187));
        tabs.setForegroundAt(1, new Color(187, 187, 187));
    }

    private JLabel createStatusLabel(int statusCode, String statusMessage) {
        JLabel statusLabel = new JLabel();
        if (statusMessage.equals("Error")) {
            statusLabel.setText("Error");
            statusLabel.setBackground(new Color(199, 40, 40));
        } else {
            statusLabel.setText(statusCode + " " + statusMessage);
            statusLabel.setBackground(new Color(55, 171, 46));
        }
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font(null, Font.BOLD, 12));
        statusLabel.setOpaque(true);
        return statusLabel;
    }

    private JLabel createTimeLabel(double time) {
        JLabel statusLabel = new JLabel();
        statusLabel.setText("TIME " + time + " s");
        statusLabel.setBackground(new Color(188, 191, 187));
        statusLabel.setForeground(Color.darkGray);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font(null, Font.PLAIN, 12));
        statusLabel.setOpaque(true);
        return statusLabel;
    }

    private JLabel createSizeLabel(double size){

        JLabel statusLabel = new JLabel();
        statusLabel.setText("SIZE " + size + " KB");
        statusLabel.setBackground(new Color(188, 191, 187));
        statusLabel.setForeground(Color.darkGray);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font(null, Font.PLAIN, 12));
        statusLabel.setOpaque(true);
        return statusLabel;
    }

    private void headerInit(){
        header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(46, 47, 43));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton button = new JButton("Copy to Clipboard");

        addHeader("NAME", "VALUE");
        addHeader("Date", "Mon, 11 May 2020 19:24:00 GMT");
        addHeader("Alt-Svc" , "h3-27=\":443\"; ma=2592000,h3-25=\":443\"; ma=2592000,h3-Q050=\":443\";" +
                " ma=2592000,h3-Q049=\":443\"; ma=2592000,h3-Q048=\":443\"; ma=2592000,h3-Q046=\":443" +
                "\"; ma=2592000,h3-Q043=\":443\"; ma=2592000,quic=\":443\"; ma=2592000; v=\"46,43\"");
        addHeader("Set-Cookie", "1P_JAR=2020-05-11-19; expires=Wed, 10-Jun-2020 19:24:00 GMT; path=/; domain=.google.com; Secure");
        header.add(button);

    }
    private void addHeader(String key, String value){

        JPanel panel = new JPanel();
        panel.setBackground(new Color(46, 47, 43));
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(380, 500));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel textAreas = new JPanel();
        textAreas.setBackground(new Color(46, 47, 43));
        textAreas.setLayout(new BoxLayout(textAreas, BoxLayout.X_AXIS));

        JTextArea textArea1 = new JTextArea();
        textArea1.setText(key);
        textArea1.setForeground(Color.GRAY);
        textArea1.setBackground(new Color(46, 47, 43));
        textArea1.setLineWrap(true);

        JTextArea textArea2 = new JTextArea();
        textArea2.setText(value);
        textArea2.setForeground(Color.GRAY);
        textArea2.setBackground(new Color(46, 47, 43));
        textArea2.setLineWrap(true);

        textAreas.add(textArea1);
        textAreas.add(Box.createRigidArea(new Dimension(20, 0)));
        textAreas.add(textArea2);

        panel.add(textAreas, BorderLayout.NORTH);

        header.add(panel);
    }
}
