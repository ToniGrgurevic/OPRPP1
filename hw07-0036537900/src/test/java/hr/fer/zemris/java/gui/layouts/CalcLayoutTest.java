package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class CalcLayoutTest {

    @Test
    public void testPositions() throws InterruptedException {
        DemoFrame1 frame = new DemoFrame1();


        JLabel label = new JLabel("naknadno");

        var cp= frame.getContentPane();



         assertThrows(CalcLayoutException.class, ()-> {
                     cp.add(l("red_manji_od0"), new RCPosition(0, 1));
                 });
         assertThrows(CalcLayoutException.class, ()-> {
                        cp.add(l("red_veciod5"), new RCPosition(6, 1));
                    });
        assertThrows(CalcLayoutException.class, ()-> {
            cp.add(l("stup_manji_od0"), new RCPosition(1, -1));
        });
        assertThrows(CalcLayoutException.class, ()-> {
            cp.add(l("stup_veciod7"), new RCPosition(1, 8));
        });

        assertThrows(CalcLayoutException.class, ()-> {
            cp.add(l("vec_zauzeto"), new RCPosition(1, 1));
        });

        assertThrows(CalcLayoutException.class, ()-> {
            cp.add(l("nedopusteno Mjesto"), "1,4");
        });

        assertThrows(NullPointerException.class, ()-> {
            cp.add(null, new RCPosition(1, 7));
        });
        assertThrows(NullPointerException.class, ()-> {
            cp.add(l("null"), null);
        });

        assertThrows(IllegalArgumentException.class, ()-> {
            cp.add(l("krivi_argument"), "1,ć4");
        });


        SwingUtilities.invokeLater(()->{
            frame.setVisible(true);
        });
    }


    public JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }
}