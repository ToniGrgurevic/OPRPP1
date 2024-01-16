package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Implementation of Swing layout menager for Calculator aplication
 */
public class CalcLayout  implements LayoutManager2 {

    private Component[] components;

    private int margin;

    public CalcLayout(int margin) {
        this.margin = margin;
        components = new Component[35];
    }

    public CalcLayout() {
        this(0);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(comp  == null || constraints==null)
            throw new NullPointerException();
        if(! (constraints instanceof String)
            && ! (constraints instanceof RCPosition)){
            throw new IllegalArgumentException("Krivo ogranicenje!");
        }

        RCPosition position;
        if(constraints instanceof String)
            position = RCPosition.parse((String)constraints);
        else
            position =(RCPosition) constraints;


        if(position.getRow() == 1 &&
                ( position.getColumn() > 1 && position.getColumn() < 6 ) )
            throw new CalcLayoutException("pozicija " + position + " nije dopuštena");
        if( position.getRow() < 1 || position.getRow() > 5
                || position.getColumn() < 1 || position.getColumn() > 7)
            throw new CalcLayoutException("pozicija " + position + " je izvan mreže aplikacije");
        if(components[(position.getRow() -1 )*7 + position.getColumn() -1 ] != null)
            throw new CalcLayoutException("pozicija " + position + " je već zauzeta");

        int pozicija = (position.getRow() -1) *7 + position.getColumn() -1;
        components[pozicija ] =  comp;

    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        Dimension dimenzije = calculateCellWantedSize(parent, Component::getMaximumSize);
        var border = parent.getInsets();
        int width = dimenzije.width*7 + 6*margin + border.left + border.right;
        int height = dimenzije.height*5 + 4*margin + border.top + border.bottom;

        return new Dimension(width,height);


    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw  new UnsupportedOperationException();

    }

    @Override
    public void removeLayoutComponent(Component comp) {
        int index =Arrays.binarySearch(components, comp);
        if (index >= 0){
            components[index] = null;
        }

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {

        Dimension dimenzije = calculateCellWantedSize(parent, Component::getPreferredSize);
        var border = parent.getInsets();
        int width = dimenzije.width*7 + 6*margin + border.left + border.right;
        int height = dimenzije.height*5 + 4*margin + border.top + border.bottom;

        return new Dimension(width,height);


    }



    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension dimenzije = calculateCellWantedSize(parent, Component::getMinimumSize);
        var border = parent.getInsets();
        int width = dimenzije.width*7 + 6*margin + border.left + border.right;
        int height = dimenzije.height*5 + 4*margin + border.top + border.bottom;

        return new Dimension(width,height);


    }

    @Override
    public void layoutContainer(Container parent) {

        var border = parent.getInsets();

        int x  = border.left;
        int y = border.top;

        int[] cellHeight = new int[5];
        int[] cellWidth = new int[7];

        razdjeli(cellWidth, parent.getWidth() - border.left - border.right-margin*6);
        razdjeli(cellHeight, parent.getHeight() - border.top - border.bottom - margin*4);

        for (int row = 0;row<5;row++){
            x = border.left;

            for (int colum = 0 ; colum<7 ; colum++){

                var component = components[row * 7 + colum];
                if(component != null){
                    if(row == 0 && colum ==0 ){
                        int widht = 4*margin;
                        for(int i= 0 ;i<5;i++){
                            widht += cellWidth[i];
                        }

                        component.setBounds(0,0,widht,cellHeight[row]);
                    }else
                        component.setBounds(x,y , cellWidth[colum] , cellHeight[row]);
                }
                x+= margin + cellWidth[colum];
            }
            y += margin + cellHeight[row];
        }


    }

    private void razdjeli(int[] array, int size){

        double middle  = size * 1.0 / array.length;
        int numberOfSmaller = size - (int) middle * array.length;
        numberOfSmaller = array.length - numberOfSmaller;

        for(int i=0; i< array.length ; i++){
            int randomInt = (int) ( Math.random() * (array.length -i)  );
            if(randomInt < numberOfSmaller){
                numberOfSmaller--;
                array[i] = (int) middle + 1;
            }else
                array[i] = (int) middle;
        }
    }

    Dimension calculateCellWantedSize(Container parent, Function<Component,Dimension> getter){
        double maxRowSize = 0,maxColumnSize = 0;

        for (int i= 0;i< components.length;i++){
            var component = components[i];
            if(component == null ) continue;

            Dimension boxSize = getter.apply(component);
            if (boxSize == null) continue;
            //for cell width
            if(i == 0 ){
                if( (boxSize.getWidth()  - 4*margin)/ 5 > maxRowSize){
                    maxRowSize =(boxSize.getWidth()  - 4*margin  ) / 5;
                }
            }else{
                if(boxSize.getWidth() > maxRowSize)
                    maxRowSize = boxSize.getWidth();
            }

            // for cell height
            if(boxSize.getHeight() > maxColumnSize)
                maxColumnSize = boxSize.getHeight();

        }
        return new Dimension((int)maxRowSize , (int) maxColumnSize);
    }
}
