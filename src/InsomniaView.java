import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class InsomniaView extends JFrame {

    private JMenuBar menuBar;
    private LeftPanel leftPanel;
    private MiddlePanel middlePanel;
    private RightPanel rightPanel;

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
        JMenuItem exit = new JMenuItem("Exit", 'E');
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        applicationMenu.add(options);
        applicationMenu.add(exit);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        JMenuItem toggleFullScreen = new JMenuItem("Toggle Full Screen", 'F');
        toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        JMenuItem toggleSidebar = new JMenuItem("Toggle Sidebar", 'S');
        toggleSidebar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, KeyEvent.CTRL_DOWN_MASK));
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

        MenuBarInit();

        leftPanel = new LeftPanel();
        middlePanel = new MiddlePanel();
        rightPanel = new RightPanel();

        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, rightPanel);
        splitPane1.setDividerLocation(265);
        splitPane2.setDividerLocation(770);
        splitPane1.setDividerSize(3);
        splitPane2.setDividerSize(3);

        add(splitPane2, BorderLayout.CENTER);
        setVisible(true);
    }
}
