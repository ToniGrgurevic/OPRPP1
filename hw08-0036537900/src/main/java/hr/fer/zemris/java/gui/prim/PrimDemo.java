package hr.fer.zemris.java.gui.prim;


import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * program koji se pokreće iz naredbenog retka bez ikakvih argumenata. Po
 * pokretanju otvara prozor u kojem se prikazuju dvije liste (jednako visoke, jednako široke, rastegnute preko
 * čitave površine prozora izuzev donjeg ruba). U donjem rubu nalazi se  gumb "sljedeći".
 */
public class PrimDemo  extends JFrame {



    PrimDemo() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PrimDemo");
        setLocation(20, 20);
        setSize(500, 500);
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimListModel model = new PrimListModel();

        JList<Integer> list1 = new JList<>( model);
        JList<Integer> list2 = new JList<>( model);



        JPanel bottomPanel = new JPanel(new GridLayout(1, 0));

        JButton dodaj = new JButton("sljedeci");
        bottomPanel.add(dodaj);


        Random rand = new Random();
        dodaj.addActionListener(e -> {
            model.next();
        });

        JPanel central = new JPanel(new GridLayout(1, 0));
        central.add(new JScrollPane(list1));
        central.add(new JScrollPane(list2));
        cp.add(central, BorderLayout.CENTER);
        cp.add(bottomPanel, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new PrimDemo();
            frame.pack();
            frame.setVisible(true);
        });
    }
}
