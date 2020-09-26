package Landscape;

import java.lang.*;
import java.util.*;

import Landscape.sPoint;
import Landscape.sSquare;
import Landscape.sDiamond;

public class MapGenerator
{
    private short[][] worldMap;
    private short maxH;
    private short outMap;
    private float defect;
    public static void main(String[] args)
    {
        MapGenerator mg = new MapGenerator((short)6, (short)15, (short)0, 0.1f);
        short[][] a = mg.genMap();
        PrintMap(a);
    }

    /**
     * Конструрирует объект
     * 
     * @param size2Pow - размер карты будет 2^(size2Pow) + 1
     * @param MaxH - максимальная высота на карте
     * @param OutMap - высота за картой. Например, если нужно побольше воды, то OutMap = 0
     * @param Defect - Дописать!!!!!!!!!!!!
     */
    public MapGenerator(short size2Pow, short MaxH, short OutMap, float Defect)
    {
        if(size2Pow < 2 || MaxH < 0 ||  OutMap < 0 || OutMap > MaxH /*|| Float.compare(Defect, 1) > 0 || Float.compare(Defect, 0) < 0*/)
            throw new IllegalArgumentException();
        
        int size = 1;
        for(int i = 0; i < size2Pow; i++)
            size <<= 1;
        size++; // size = 2^(size2Pow)+1

        worldMap = new short[size][size];
        for(short[] item : worldMap) // Заполнить всё -1
            for(short li = 0; li < item.length; li++)
                item[li] = -1;

        this.maxH = MaxH;
        this.outMap = OutMap;
        this.defect = Defect;
    }
    
    /**
     * x
     * |
     * |
     * v  y---->
     *   0 1 2 3 4 5 ...
     * 0 * * * * * * ...
     * 1 * * * * * * ...
     * 2 * * * * * * ...
     * 3 * * * * * * ...
     * 4 * * * * * * ...
     * 5 * * * * * * ...
     *  ... ... ...  ...
     * genMap[y][x]
     * @return карта
     */
    public short[][] genMap()
    {
        short l = (short)(worldMap.length - 1);
        sSquare s;
        sDiamond d;

        Random r = new Random();
        set(new sPoint((short)0, (short)0), (short)r.nextInt(maxH + 1));
        set(new sPoint(l, (short)0), (short)r.nextInt(maxH + 1));
        set(new sPoint((short)0, l), (short)r.nextInt(maxH + 1));
        set(new sPoint(l, l), (short)r.nextInt(maxH + 1));

        do
        {
            s = new sSquare(new sPoint((short)0, (short)0), l);
            d = new sDiamond(new sPoint((short)(l/2), (short)0), (short)(l/2));
            //PrintMap(worldMap);
            do
            {
                //System.out.println(s);
                squarePhase(s, l);
                s = nextSquare(s, l);
                //PrintMap(worldMap);
            }while(s != null);

            do
            {
                //System.out.println(d);
                diamondPhase(d, l);
                d = nextDiamond(d, (short)(l/2));
                //PrintMap(worldMap);
            }while(d != null);
            l = (short)(l/2);
        }while(l > 1);
        return worldMap;
    }

    private void squarePhase(sSquare s, short l)
    {
        set(s.getC(), genCenter( get(s.getLU()), get(s.getLD()), get(s.getRU()), get(s.getRD()), l) );
    }

    private void diamondPhase(sDiamond d, short l)
    {
        set(d.getC(), genCenter( get(d.getU()), get(d.getD()), get(d.getL()), get(d.getR()), l) );
    }

    private short get(sPoint p)
    {
        if(checkOutSide(p))
            return outMap;
        return worldMap[p.getY()][p.getX()];
    }

    private void set(sPoint p, short value)
    {
        worldMap[p.getY()][p.getX()] = value;
    }

    private short getSize()
    {
        return (short)worldMap.length;
    }

    private sSquare nextSquare(sSquare current, short l)
    {
        sSquare res;
        short l1 = (short)(l+1);
        sPoint buff = current.getLU();
        if(checkOutSide(buff.addX(l1)))
            if(checkOutSide(buff.addY(l1)))
                res = null;
            else
                res = new sSquare(new sPoint((short)0, (short)(buff.getY() + l) ), l);
        else
            res = new sSquare(buff.addX(l), l);
        return res;
    }

    private sDiamond nextDiamond(sDiamond current, short l)
    {   //IwantToGoNah$i
        sDiamond res;
        short l2 = (short)(l*2);
        sPoint buff = current.getC();
        if(checkOutSide(buff.addX((short)(l2)))) // Вылетело за карту направо
            if( !checkOutSide( buff.addX(l2).subX(l) ) ) // Это была верхняя или нижняя сторона
                if(!checkOutSide(buff.addY((short)1))) //Ещё есть место
                    res = new sDiamond(new sPoint((short)0, (short)(buff.getY() + l)), l);
                else
                    res = null;
            else // Это был ромб на второнах слева или справа
                res = new sDiamond(new sPoint(l, (short)(buff.getY() + l)), l);
        else // Ещё есть место
            res = new sDiamond(buff.addX(l2), l);
        return res;
    }

    /**
     * Проверка выхода за карту
     * @param p
     * @return вернёт true, если выйдет за карту, false - если внутри карты
     */
    private boolean checkOutSide(sPoint p)
    {
        boolean res = false;
        if(p.getX() < 0 || p.getX() >= worldMap.length || p.getY() < 0 || p.getY() >= worldMap.length)
            res = true;
        return res;
    }

    private short genCenter(short x1, short x2, short x3, short x4, short l) throws IllegalArgumentException
    {
        if(x1 < 0 || x2 < 0 || x3 < 0 || x4 < 0)
            throw new IllegalArgumentException("x1 = " + x1 + ", x2 = " + x2 + ", x3 = " + x3 + ", x4 = " + x4 + " must be more than zero or equals");
        short res, buff;
        Random r = new Random();
        res = (short)((x1+x2+x3+x4) / 4);

        buff = (short)((l/worldMap.length)*res*defect*r.nextFloat() + 0.5);
        buff *= r.nextBoolean()?1:-1;
        res += buff;
        if(res > maxH) res = maxH;
        if(res < 0) res = 0;
        return res;
    }

    public static void PrintMap(short[][] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            for(int j = 0; j < a[i].length; j++)
            if(a[i][j] != -1)
                System.out.printf("%2d ", a[i][j]);
            else
                System.out.printf("%2c ", 'o');
            System.out.println();
        }
        System.out.println();
    }
}