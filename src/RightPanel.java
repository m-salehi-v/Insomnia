import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {
    private JPanel topPanel;

    public RightPanel() {
        super();
        setLayout(new BorderLayout());

        topPanelInit();
        add(topPanel, BorderLayout.NORTH);
    }


    private void topPanelInit() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 9));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        topPanel.add(createStatusLabel(0, "Error"));
        topPanel.add(createTimeLabel(0));
        topPanel.add(createSizeLabel(0));
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
}
