import javax.swing.*;
import java.awt.*;

public class LeftPanel extends JPanel {
    private JLabel logo;

    public LeftPanel() {
        super();
        setBackground(Color.darkGray);
        setLayout(new BorderLayout());
        logoInit();
        add(logo, BorderLayout.NORTH);
    }

    private void logoInit() {

        logo = new JLabel("   Insomnia");
        logo.setOpaque(true);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Calibri", Font.PLAIN, 20));
        logo.setHorizontalAlignment(SwingConstants.LEFT);
        logo.setBackground(new Color(122, 104, 186));
        logo.setPreferredSize(new Dimension(logo.getPreferredSize().width, logo.getPreferredSize().height + 18));
    }
}

