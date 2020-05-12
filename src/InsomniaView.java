import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class InsomniaView extends JFrame {

    private JMenuBar menuBar;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;
    private RightPanel rightPanel;
    private boolean isFullScreen;
    private boolean hideInSystemTray;

    public InsomniaView() {
        super("Insomnia");
        initFrame();
    }


    private void MenuBarInit() {
        menuBar = new JMenuBar();
        JMenu applicationMenu = new JMenu("Application");
        applicationMenu.setMnemonic('A');
        JMenuItem options = new JMenuItem("Options", 'O');
        options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK));
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOptionsFrame();
            }
        });
        JMenuItem exit = new JMenuItem("Exit", 'E');
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        applicationMenu.add(options);
        applicationMenu.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hideInSystemTray) {
                    setVisible(false);
                    systemTray();
                }
                else
                    System.exit(0);
            }
        });
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        JMenuItem toggleFullScreen = new JMenuItem("Toggle Full Screen", 'F');
        toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        toggleFullScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isFullScreen){
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                    isFullScreen = true;
                } else {
                    setExtendedState(JFrame.NORMAL);
                    isFullScreen = false;
                }
            }
        });
        JMenuItem toggleSidebar = new JMenuItem("Toggle Sidebar", 'S');
        toggleSidebar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, KeyEvent.CTRL_DOWN_MASK));
        toggleSidebar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (leftPanel.isVisible())
                    leftPanel.setVisible(false);
                else {
                    leftPanel.setVisible(true);
                    splitPane1.setDividerLocation(265);
                }
            }
        });
        viewMenu.add(toggleFullScreen);
        viewMenu.add(toggleSidebar);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem about = new JMenuItem("About", 'A');
        JMenuItem help = new JMenuItem("Help", 'H');
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(about);
        helpMenu.add(help);

        menuBar.add(applicationMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }


    private void initFrame(){
        setSize(1175, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("src/res/Insomnia.png").getImage());

        MenuBarInit();

        leftPanel = new LeftPanel();
        middlePanel = new MiddlePanel();
        rightPanel = new RightPanel();

        splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, rightPanel);
        splitPane1.setDividerLocation(265);
        splitPane2.setDividerLocation(770);
        splitPane1.setDividerSize(2);
        splitPane2.setDividerSize(2);

        add(splitPane2, BorderLayout.CENTER);
        setVisible(true);
    }

    private void systemTray(){
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()){
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage("src/res/insomnia.png");
            PopupMenu popup = new PopupMenu();
            ActionListener openActionHandler = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                }
            };
            MenuItem open = new MenuItem("Open");
            open.addActionListener(openActionHandler);
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            popup.add(open);
            popup.add(exit);

            trayIcon = new TrayIcon(image, "Insomnia", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(openActionHandler);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    private void showOptionsFrame(){
        JFrame frame = new JFrame("Options");
        frame.setLocationRelativeTo(null);
        frame.setSize(300,150);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("src/res/Insomnia.png").getImage());

        JPanel framePanel = new JPanel(new BorderLayout(10,10));
        framePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JCheckBox checkBox1 = new JCheckBox("Follow Redirect");
        JCheckBox checkBox2 = new JCheckBox("Hide in System Tray");
        if (hideInSystemTray)
            checkBox2.setSelected(true);
        else
            checkBox2.setSelected(false);
        checkBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                hideInSystemTray = checkBox.isSelected();
            }
        });
        JRadioButton radioButton1 = new JRadioButton("light theme");
        JRadioButton radioButton2 = new JRadioButton("dark theme");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        JPanel panel = new JPanel();
        panel.add(radioButton1);
        panel.add(radioButton2);
        framePanel.add(checkBox1, BorderLayout.NORTH);
        framePanel.add(checkBox2, BorderLayout.CENTER);
        framePanel.add(panel, BorderLayout.SOUTH);
        frame.setContentPane(framePanel);
        frame.setVisible(true);
    }
}
