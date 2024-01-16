package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;

public class BarChartDemo extends JFrame {




        public BarChartDemo(String path) {

            var cp = getContentPane();
            cp.setLayout(new BorderLayout());

            JLabel label = new JLabel("Bar Chart : " + path);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            cp.add(label, BorderLayout.NORTH);
            BarChart barChartData = BarChart.parse(path);
            setTitle("Bar Chart Example");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            BarChartComponent component = new BarChartComponent(barChartData);
            cp.add(component, BorderLayout.CENTER);

        }



        public static void main(String[] args) {

            String path  = "";
            if(args.length == 0){
                path = "src/main/resources/barData";
            }else if(args.length != 1) {
                System.out.println("Invalid number of arguments");
                System.exit(1);
            }else {
                path = args[0];
            }

            String filePath = path;
            SwingUtilities.invokeLater(() -> {
                BarChartDemo example = new BarChartDemo(filePath);
                example.setVisible(true);
            });
        }




}
