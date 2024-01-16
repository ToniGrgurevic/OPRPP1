package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;

/**
 * Komponenta na
 * svojoj površini stvara prikaz podataka koji su definirani primljenim objektom (tj. crta stupčasti dijagram)
 */
public class BarChartComponent extends JComponent {

    private BarChart barChartData;



    public BarChartComponent(BarChart barChartData) {
        this.barChartData = barChartData;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int charHeight = fontMetrics.getAscent();

        int labelYStart = 15;
        int labelXStart =10;
        int numbersYLen = String.valueOf(barChartData.getyMax()).length() * fontMetrics.stringWidth("0") ;
        int startX = numbersYLen + labelYStart + 15;
        int startY = getHeight() - 30 - labelXStart; //??

        int barWidth =( getWidth() - startX - 10) / barChartData.getValues().size() ;

        int distanceYDelta = ( startY - 10) / ( (barChartData.getyMax() - barChartData.getyMin()) / barChartData.getDeltaY() ) ;

        var values = barChartData.getValues();

        // Draw x and y axis


        // Draw y axis values
        for (int i = 0; i <= (barChartData.getyMax() - barChartData.getyMin()) / barChartData.getDeltaY() ; i++) {
            g2d.setColor(Color.GRAY);
            g2d.drawLine(startX - 5, startY - i * distanceYDelta, getWidth() - 5 , startY - i * distanceYDelta);
            g2d.setColor(Color.BLACK);
            int distanceYnumber = String.valueOf(barChartData.getyMin() + i * barChartData.getDeltaY()).length() * fontMetrics.stringWidth("0");
            g2d.drawString(String.valueOf(barChartData.getyMin() + i * barChartData.getDeltaY()), startX - (  distanceYnumber ) - 10, startY - i * distanceYDelta + charHeight/3);

        }

        g2d.setColor(Color.GRAY);
        //draw x axis values
        for (int i = 0; i < values.size(); i++) {
            g2d.drawLine(startX + i * (barWidth + 1), startY+5, startX + i * (barWidth + 1), 5);
        }


        // Draw bars based on the data
        for (int i = 0; i < barChartData.getValues().size(); i++) {
            int barHeight =(int) (values.get(i).getY() * 1.0/ barChartData.getDeltaY() * (distanceYDelta ));
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(startX + i * (barWidth + 1), startY - barHeight, barWidth, barHeight);

            String value = String.valueOf(values.get(i).getX());
            int charWidth = fontMetrics.stringWidth(value);
            g2d.setColor(Color.BLACK);
            g2d.drawString(value, startX + i * (barWidth + 1) + barWidth/2 - (charWidth /2) , startY + 15);
        }

        // Draw x and y axis and arowheads
        g2d.setColor(Color.BLACK);
        g2d.drawLine(startX, startY, startX, 5);
        g2d.drawLine(startX, startY, getWidth()-5, startY);
        drawArrowHead(g2d, getWidth()-5, startY, true);
        drawArrowHead(g2d, startX, 5, false);

        // Draw x and y axis labels
        g2d.setColor(Color.BLACK);
        int stringWidth = fontMetrics.stringWidth(barChartData.getxInfo());
        g2d.drawString(barChartData.getxInfo(), (getWidth())/2 - stringWidth/2, getHeight() - 10);

        g2d.rotate(Math.toRadians(-90), 15, getHeight()/2);
        g2d.drawString(barChartData.getyInfo(), 0, getHeight()/2);

    }



    private void drawArrowHead(Graphics2D g2d, int x, int y, boolean isXAxis) {
        int arrowSize = 5;

        if (isXAxis) {
            g2d.drawLine(x - arrowSize, y - arrowSize, x, y);
            g2d.drawLine(x - arrowSize, y + arrowSize, x, y);
        } else {
            g2d.drawLine(x - arrowSize, y + arrowSize, x, y);
            g2d.drawLine(x + arrowSize, y + arrowSize, x, y);
        }
    }

}
