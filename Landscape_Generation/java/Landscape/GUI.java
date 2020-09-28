package Landscape;

import java.lang.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import Landscape.MapGenerator;
import Landscape.TownGenerator;

public class GUI
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater( () ->
        {
            var frame = new sFrame();
            frame.setTitle("Landscape");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        );
    }
}

class sFrame extends JFrame
{
    public sFrame()
    {
        this.add(new JScrollPane(new sComponent(10, 2)));
        this.pack();
    }
}

class sComponent extends JComponent
{
    private int DEFAULT_WIDTH;
    private int DEFAULT_HEIGHT;
    private int sizeble;
    private final short maxH = 15;
    short[][] map;
    
    public sComponent(int pow2, int Sizeble)
    {
        sizeble = Sizeble;
        MapGenerator mg = new MapGenerator((short)(pow2), maxH, (short)2, 0.5f);
        map = mg.genMap();
        TownGenerator tg = (new TownGenerator(map, 7));
        tg.addTowns();
        /*Scanner in = new Scanner(System.in);
        map = new short[16][16];
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                map[i][j] = in.nextShort();*/
        
        DEFAULT_WIDTH = map.length*Sizeble;
        DEFAULT_HEIGHT = map.length*Sizeble;
    }

    public void paintComponent(Graphics gOld)
    {
        Graphics2D g = (Graphics2D)gOld;

        //var p = new Line2D.Float(5f, 5f, 2047f, 2047f);
        //g.draw(p);
        
        
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++)
                paintBlock(j, i, getBlock(map[i][j]), g);
    }

    public Color getBlock(short a)
    {
        if(a == 0)
            return new Color(0, 6, 92); // Глубокая водичка
        if(a == 1)
            return Color.BLUE; // Синяя водичка
        if(a == 2)
            return Color.CYAN; // Голубая водичка
        if(a == 3)
            return Color.YELLOW; // Пляжный песочек
        if(a == 4)
            return new Color(92, 91, 15); // Темный песочек
        if(a == 5)
            return new Color(112, 62, 9); // Земля коричневенькая
        if(a == 6)
            return Color.GREEN; // Зеленая трава
        if(a == 7 || a == 8)
            return new Color(86, 138, 10); // Не такая зеленая трава
        if(a == 9)
            return new Color(0, 61, 3); //Темненькая земля
        if(a == 10)
            return new Color(116, 138, 10); // Бесплодная земля
        if(a == 11)
            return new Color(63, 64, 57); // Скала
        if(a == 12)
            return new Color(79, 79, 76); // Скала повыше
        if(a == 13)
            return new Color(195, 197, 217); // Мокрый снег
        if(a == 14)
            return new Color(235, 235, 240); // Снег
        if(a == 15)
            return new Color(189, 193, 242); // Лёд

        if(a == 101)
            return new Color(26, 24, 22); // Город
        if(a == 102 )
            return new Color(255, 183, 0); // Центр города
        if(a == 103)
            return new Color(/*117, 93, 30*//*74, 74, 74*/184, 132, 0); // Дорога между городами
        if(a == 104)
            return new Color(56, 56, 56);
        return Color.BLACK;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void paintBlock(int X, int Y, Color c, Graphics2D g)
    {
        //Color buff;
        //buff = g.getColor();
        g.setPaint(c);
        g.fillRect(X*sizeble, Y*sizeble, sizeble, sizeble);
        //g.setPaint(buff);
    }
}