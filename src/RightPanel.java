import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
    private JPanel raw;
    private JPanel preview;
    private JEditorPane editorPane;
    private JButton cpyButton;

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

    public JPanel getRaw() {
        return raw;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public JPanel getHeader() {
        return header;
    }

    public void setEditorPaneURL(String url){
        try {
            editorPane.setPage(url);
        } catch (IOException e) {
            editorPane.setContentType("text/html");
            editorPane.setText("<html><h1>Could not load</h1></html>");
        }
    }

    public JButton getCpyButton() {
        return cpyButton;
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
        topPanel.add(createStatusLabel(code, message));
    }
    public void addTimeLabel(double time){
        topPanel.add(createTimeLabel(time));
    }
    public void addSizeLabel(int size){
        topPanel.add(createSizeLabel(size));
    }

    public JEditorPane getEditorPane() {
        return editorPane;
    }

    //initializes tabs that are header and preview
    private void tabsInit(){
        tabs = new JTabbedPane();

        headerInit();

        rawInit();
        previewInit();
        JLabel viewLabel = new JLabel("Raw");
        viewLabel.addMouseListener(new SelectTabHandler());
        tabs.add(raw, "Raw");
        tabs.setTabComponentAt(0, viewLabel);
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
        } else if (statusCode/100 == 2){
            statusLabel.setText(statusCode + " " + statusMessage);
            statusLabel.setBackground(new Color(55, 171, 46));
        } else if (statusCode/100 == 3){
            statusLabel.setText(statusCode + " " + statusMessage);
            statusLabel.setBackground(new Color(105, 94, 184));
        }else if (statusCode/100 == 4){
            statusLabel.setText(statusCode + " " + statusMessage);
            statusLabel.setBackground(new Color(219, 151, 13));
        }else if (statusCode/100 == 5){
            statusLabel.setText(statusCode + " " + statusMessage);
            statusLabel.setBackground(new Color(227, 77, 77));
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
    private JLabel createSizeLabel(int size){

        JLabel statusLabel = new JLabel();
        if(size < 1000)
            statusLabel.setText("SIZE " + size + " B");
        else
            statusLabel.setText("SIZE " + size/1000.0 + " KB");
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
    }

    public void addCopyButton(){
        cpyButton = new JButton("Copy to Clipboard");
        header.add(cpyButton);
    }

    private void rawInit(){
        raw = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(46, 47, 43));
        textArea.setLineWrap(true);
        raw.add(new JScrollPane(textArea), BorderLayout.CENTER);
        raw.setBackground(new Color(46, 47, 43));
    }

    private void previewInit(){
        preview = new JPanel(new BorderLayout());
        preview.setBackground(new Color(46, 47, 43));
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setBackground(new Color(225, 225, 225));
        preview.add(new JScrollPane(editorPane), BorderLayout.CENTER);
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
    private class SelectTabHandler extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem1 = new JMenuItem("Raw Data");
            JMenuItem menuItem2 = new JMenuItem("Visual Preview");
            menuItem1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel rawLabel = new JLabel("Raw");
                    rawLabel.addMouseListener(new SelectTabHandler());
                    tabs.setTabComponentAt(0, rawLabel);
                    tabs.setComponentAt(0, raw);
                    tabs.setSelectedIndex(0);
                    revalidate();
                }
            });
            menuItem2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JLabel previewLabel = new JLabel("Preview");
                    previewLabel.addMouseListener(new SelectTabHandler());
                    tabs.setTabComponentAt(0, previewLabel);
                    tabs.setComponentAt(0, preview);
                    tabs.setSelectedIndex(0);
                    revalidate();
                }
            });
            popupMenu.add(menuItem1);
            popupMenu.add(menuItem2);
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON1) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY() + 5);
            }
        }

    }
}
