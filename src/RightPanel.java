import javax.swing.*;
import java.awt.*;

/**
 * this class represents right panel of the application that holds
 * response details.
 *
 * @author Mohammad Salehi Vaziri
 */
public class RightPanel extends JPanel {
    private JPanel topPanel;
    private JTabbedPane tabs;
    private JPanel header;
    private JPanel preview;

    /**
     * Instantiates a new Right panel.
     */
    public RightPanel() {
        super();
        setLayout(new BorderLayout());

        topPanelInit();
        add(topPanel, BorderLayout.NORTH);

        tabsInit();
        add(tabs, BorderLayout.CENTER);
    }

    public JPanel getPreview() {
        return preview;
    }

    //initializes top panel that holds three labels to show response status
    private void topPanelInit() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        addStatusLine(0, "Error");
        addTimeLabel(0);
        addSizeLabel(0);
    }
    public void addStatusLine(int code, String message){
        topPanel.removeAll();
        topPanel.add(createStatusLabel(code, message));
    }
    public void addTimeLabel(double time){
        topPanel.add(createTimeLabel(time));
    }
    public void addSizeLabel(double size){
        topPanel.add(createSizeLabel(size));
    }
    //initializes tabs that are header and preview
    private void tabsInit(){
        tabs = new JTabbedPane();

        headerInit();

        preview = new JPanel();
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(46, 47, 43));
        textArea.setLineWrap(true);
        preview.add(new JScrollPane(textArea));
        preview.setBackground(new Color(46, 47, 43));

        tabs.add(preview, "Preview");
        tabs.add(new JScrollPane(header), "Header");
        tabs.setFont(new Font(null, Font.PLAIN, 13));
        tabs.setForegroundAt(0, new Color(187, 187, 187));
        tabs.setForegroundAt(1, new Color(187, 187, 187));
    }

    //creates the status label that consists of a code and message
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

    //creates time label
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

    //creates size label
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

    //initializes header tab that has a number of name and values implemented by
    //JTextArea and a copy button
    private void headerInit(){
        header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(new Color(46, 47, 43));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton button = new JButton("Copy to Clipboard");

        header.add(button);

    }

    //creating a new name and value and putting them in a panel to be added to
    //header tab.
    public void addHeader(String key, String value){

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
        textArea1.setEditable(false);

        JTextArea textArea2 = new JTextArea();
        textArea2.setText(value);
        textArea2.setForeground(Color.GRAY);
        textArea2.setBackground(new Color(46, 47, 43));
        textArea2.setLineWrap(true);
        textArea2.setEditable(false);

        textAreas.add(textArea1);
        textAreas.add(Box.createRigidArea(new Dimension(20, 0)));
        textAreas.add(textArea2);

        panel.add(textAreas, BorderLayout.NORTH);

        header.add(panel);
    }
}
