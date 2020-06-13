import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * it represents the main frame of the application that consists of
 * a menu bar and three panels.
 *
 * @author Mohammad Salehi Vaziri
 */
public class InsomniaView extends JFrame {

    private JMenuBar menuBar;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;
    private RightPanel rightPanel;
    //to know if size of the frame is full screen or not
    private boolean isFullScreen;
    //to show app must be hidden on system tray or not
    private boolean hideInSystemTray;
    private boolean followRedirect;
    private boolean toggleSideBar;

    /**
     * Instantiates a new Insomnia view.
     */
    public InsomniaView() {
        super("Insomnia");
        initFrame();
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public JSplitPane getSplitPane1() {
        return splitPane1;
    }

    public JSplitPane getSplitPane2() {
        return splitPane2;
    }

    //initializes menu bar of the application and menu item's functionality
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
        exit.addActionListener(new ExitMenuHandler(this));
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        JMenuItem toggleFullScreen = new JMenuItem("Toggle Full Screen", 'F');
        toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        toggleFullScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isFullScreen) {
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
                if (toggleSideBar) {
                    leftPanel.setVisible(false);
                    toggleSideBar = false;
                }
                else {
                    leftPanel.setVisible(true);
                    splitPane1.setDividerLocation(265);
                    toggleSideBar = true;
                }
            }
        });
        viewMenu.add(toggleFullScreen);
        viewMenu.add(toggleSidebar);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem about = new JMenuItem("About", 'A');
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Developed by Mohammad Salehi Vaziri\n" +
                        "ID: 9831037\nemail: mohammadsalehivaziri@gmail.com\n", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JMenuItem help = new JMenuItem("Help", 'H');
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "\n-M\n--method       Set request method with the parameter followed by this argument\n"
                                + "-H\n--headers       Set request headers in this format \"key1:value1;key2:value2;...\"\n"
                                + "-i                  to show response headers\n"
                                + "-f                  to automatically follow redirects\n"
                                + "-O\n--output        Save response body in a file named by the parameter after this argument\n"
                                + "-S\n--save          Save current request\n"
                                + "-d\n--data          Send multipart form data in this format: \"name1=value1&name2=value2&...\"\n"
                                + "--upload            send file: --upload \"file path\"\n"
                                + "--urlencoded        send urlencoded body in this format: \"name1=value1&name2=value2&...\"\n"
                                + "-json               send json body in this format: \"{name1:value1,name2:value2,...}\"\n"
                                + "fire                used to send saved requests: fire 1 3\n"
                                + "list                shows all saved requests", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(about);
        helpMenu.add(help);

        menuBar.add(applicationMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    //forms the main frame of the application using JSplitPane and three panels
    //from LeftPanel, MiddlePanel and RightPanel classes.
    private void initFrame() {
        Settings settings;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("settings")));){
            settings = (Settings) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            settings = new Settings(1175, 600, 265, 770, false,
                    false, false, false);
        }
        setSize( settings.getFrameWidth(), settings.getFrameHeight());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(this.getClass().getResource("res/Insomnia.png")).getImage());
        followRedirect = settings.isFollowRedirect();
        hideInSystemTray = settings.isHideInSystemTray();
        toggleSideBar = settings.isToggleSideBar();
        isFullScreen = settings.isFullScreen();

        MenuBarInit();

        leftPanel = new LeftPanel();
        middlePanel = new MiddlePanel();
        rightPanel = new RightPanel();

        splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, rightPanel);
        splitPane1.setDividerLocation(settings.getSeparator1Location());
        splitPane2.setDividerLocation(settings.getSeparator2Location());
        splitPane1.setDividerSize(2);
        splitPane2.setDividerSize(2);

        add(splitPane2, BorderLayout.CENTER);
    }

    //puts the program in system tray
    private void systemTray() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("res/insomnia.png"));
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
            exit.addActionListener(new ExitMenuHandler(this));
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

    //shows options frame with two check boxes.
    private void showOptionsFrame() {
        JFrame frame = new JFrame("Options");
        frame.setLocationRelativeTo(null);
        frame.setSize(300, 150);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon(this.getClass().getResource("res/Insomnia.png")).getImage());

        JPanel framePanel = new JPanel(new BorderLayout(10, 10));
        framePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JCheckBox checkBox1 = new JCheckBox("Follow Redirect");
        JCheckBox checkBox2 = new JCheckBox("Hide in System Tray");
        if (hideInSystemTray)
            checkBox2.setSelected(true);
        else
            checkBox2.setSelected(false);
        if (followRedirect)
            checkBox1.setSelected(true);
        else
            checkBox1.setSelected(false);
        checkBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                followRedirect = checkBox.isSelected();
            }
        });
        checkBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                hideInSystemTray = checkBox.isSelected();
            }
        });
        framePanel.add(checkBox1, BorderLayout.NORTH);
        framePanel.add(checkBox2, BorderLayout.CENTER);
        frame.setContentPane(framePanel);
        frame.setVisible(true);
    }
    static class Settings implements Serializable {
        private int frameHeight;
        private int frameWidth;
        private int separator1Location;
        private int separator2Location;
        private boolean followRedirect;
        private boolean hideInSystemTray;
        private boolean toggleSideBar;
        private boolean fullScreen;

        public Settings(int frameHeight, int frameWidth, int separator1Location, int separator2Location,
                        boolean followRedirect, boolean hideInSystemTray, boolean toggleSideBar, boolean fullScreen) {
            this.frameHeight = frameHeight;
            this.frameWidth = frameWidth;
            this.separator1Location = separator1Location;
            this.separator2Location = separator2Location;
            this.followRedirect = followRedirect;
            this.hideInSystemTray = hideInSystemTray;
            this.toggleSideBar = toggleSideBar;
            this.fullScreen = fullScreen;
        }

        public int getFrameHeight() {
            return frameHeight;
        }

        public int getFrameWidth() {
            return frameWidth;
        }

        public int getSeparator1Location() {
            return separator1Location;
        }

        public int getSeparator2Location() {
            return separator2Location;
        }

        public boolean isFollowRedirect() {
            return followRedirect;
        }

        public boolean isHideInSystemTray() {
            return hideInSystemTray;
        }

        public boolean isToggleSideBar() {
            return toggleSideBar;
        }

        public boolean isFullScreen() {
            return fullScreen;
        }
    }
    private class ExitMenuHandler implements ActionListener{
        private InsomniaView insomniaView;

        public ExitMenuHandler(InsomniaView insomniaView) {
            this.insomniaView = insomniaView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (hideInSystemTray && isVisible()) {
                setVisible(false);
                systemTray();
            } else {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("settings")));){
                    oos.writeObject(new Settings(insomniaView.getHeight(), insomniaView.getWidth(), splitPane1.getDividerLocation(),
                            splitPane2.getDividerLocation(), followRedirect, hideInSystemTray, toggleSideBar, isFullScreen));
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        }
    }
}
