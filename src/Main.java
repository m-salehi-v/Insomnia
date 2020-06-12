import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

/**
 * the class where main method is located.
 *
 * @author Mohammad Salehi Vaziri
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args not used
     * @throws ClassNotFoundException          the class not found exception
     * @throws UnsupportedLookAndFeelException the unsupported look and feel exception
     * @throws InstantiationException          the instantiation exception
     * @throws IllegalAccessException          the illegal access exception
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        //setting flatDark look and feel
        try {
            FlatDarkLaf.install();
        } catch (Exception e) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        InsomniaView insomniaView = new InsomniaView();
        HttpManager manager = new HttpManager();
        Controller controller = new Controller(insomniaView, manager);
        insomniaView.setVisible(true);

    }

}
