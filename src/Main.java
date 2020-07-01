import View.loginFrame;
import com.alee.laf.WebLookAndFeel;
import javax.swing.*;
import java.awt.*;

import static Utils.Tools.InitGlobalFont;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater (() -> {
            // Install WebLaF as application L&F
            WebLookAndFeel.install ();
            InitGlobalFont(new Font("微软雅黑", Font.BOLD, 12));  //统一设置字体
            loginFrame.LoginFrame();
        });
    }
}
