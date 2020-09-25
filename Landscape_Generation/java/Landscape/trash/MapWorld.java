package Landscape;

import java.lang.*;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

public class MapWorld
{
    public final static int maxH = 9;

    public final static int maxWet = 9;

    private int[][] land;

    private int[][] wet;

    private int beginLand;

    private int beginWet;

    private float defect;

    private MapWorld(){};
    
    private boolean done = false;


    public static void main(String[] args)
    {
        MapWorld mw = new MapWorld(3, 0, 0, 0);
        mw.buildMap();
        int[][] a = mw.getLand();
        PrintMap(a);
    }

    /**
     * Конструрирует объект типа MapWorld
     * 
     * @param size2Pow - величина мира. Сюда вносится степень двойки. Скажем, нужен мир на 256x256, тогда size2Pow = 8
     * @param commonH - какая высота будет приоритетная. Например, если нужно побольше воды, то commonH = 0
     * @param commonWet - какая влажность будет приоритетная. Например, если нужно больше засушливости, то commonWet = 0
     * @param defect - погрешность. Указывается в процнтах, чем больше, тем больше может быть артефактов на карте, но если правильно подобрать то будет выглядить реалистичнее. Если никакие погрешности не нужны, то ставить 0
     */
    public MapWorld(int size2Pow, int commonH, int commonWet, float defect)
    {
        int size = 1;
        beginLand = commonH;
        beginWet = commonWet;
        this.defect = defect;

        for(int i = 0; i < size2Pow; i++)
            size <<= 1;
        land = new int[size+1][size+1];
        for(int[] item : land)
            Arrays.fill(item, -1);
        wet = new int[size+1][size+1];
        for(int[] item : wet)
            Arrays.fill(item, -1);
    }

    public void buildMap()
    {
        squarePhase(land, 0, 0, land.length-1, 0);
        squarePhase(wet, 0, 0, wet.length-1, 1);
        done = true;
    }

    private void squarePhase(int[][] a, int leftUpX, int leftUpY, int l, int mode)
    {
        System.out.println("leftUpX = " + leftUpX + ", leftUpY = " + leftUpY + ", l = " + l);
        Random r = new Random();
        if(l == 1)
            return;
        if(a[leftUpY][leftUpX] == -1)
            a[leftUpY][leftUpX] = r.nextInt((mode==0?maxH:maxWet) + 1);
        if(a[leftUpY][leftUpX+l] == -1)
            a[leftUpY][leftUpX+l] = r.nextInt((mode==0?maxH:maxWet) + 1);
        if(a[leftUpY+l][leftUpX+l] == -1)
            a[leftUpY+l][leftUpX+l] = r.nextInt((mode==0?maxH:maxWet) + 1);
        if(a[leftUpY+l][leftUpX] == -1)
            a[leftUpY+l][leftUpX] = r.nextInt((mode==0?maxH:maxWet) + 1);
        if(a[(leftUpY+l)/2][(leftUpX+l)/2] == -1)
        {
            a[(leftUpY+l)/2][(leftUpX+l)/2] = rndCenter(a[leftUpY][leftUpX], a[leftUpY][leftUpX+l], a[leftUpY+l][leftUpX+l], a[leftUpY+l][leftUpX], mode);
        }
        if(mode == 0)
        {
            System.out.println("Before diamond");
            PrintMap(a);
        }
        diamondPhase(a, leftUpX + l/2, leftUpY + l/2, l/2, mode);
        if(mode == 0)
        {
            System.out.println("After diamond");
            PrintMap(a);
        }

        /*diamondPhase(a, leftUpX + l/4, leftUpY + l/4, l/4, mode);
        diamondPhase(a, (leftUpX+l)/2 + l/4, leftUpY + l/4,l/4, mode);
        diamondPhase(a, leftUpX + l/4, (leftUpY)/2 + l/4, l/4, mode);
        diamondPhase(a, (leftUpX+l)/2 + l/4, (leftUpY+l)/2 + l/4, l/4, mode);*/

        squarePhase(a, leftUpX, leftUpY, l/2, mode);
        squarePhase(a, (leftUpX+l)/2, leftUpY, l/2, mode);
        squarePhase(a, leftUpX, (leftUpY)/2, l/2, mode);
        squarePhase(a, (leftUpX+l)/2, (leftUpY+l)/2, l/2, mode);
    }

    private void diamondPhase(int[][] a, int centerX, int centerY, int halfL, int mode)
    {
        if(halfL == 0)
            return;
        System.out.println("x: " + centerX + ", y: " + centerY + ", halfL = " + halfL);
        int buffN, buffW, buffS, buffE;
        
        buffW = define4DiamondPhase(a, centerY, centerX-halfL*2, halfL, mode);
        buffE = define4DiamondPhase(a, centerY, centerX+halfL*2, halfL, mode);
        buffS = define4DiamondPhase(a, centerY+halfL*2, centerX, halfL, mode);
        buffN = define4DiamondPhase(a, centerY-halfL*2, centerX, halfL, mode);

        if(a[centerY][centerX-halfL] == -1)
            a[centerY][centerX-halfL] = rndCenter(a[centerY][centerX], buffW, a[centerY-halfL][centerX-halfL], a[centerY+halfL][centerX-halfL], mode);
        if(a[centerY][centerX+halfL] == -1)
            a[centerY][centerX+halfL] = rndCenter(a[centerY][centerX], buffE, a[centerY-halfL][centerX+halfL], a[centerY+halfL][centerX+halfL], mode);
        if(a[centerY+halfL][centerX] == -1)
            a[centerY+halfL][centerX] = rndCenter(a[centerY][centerX], buffS, a[centerY+halfL][centerX-halfL], a[centerY+halfL][centerX+halfL], mode);
        if(a[centerY-halfL][centerX] == -1)
            a[centerY-halfL][centerX] = rndCenter(a[centerY][centerX], buffN, a[centerY-halfL][centerX-halfL], a[centerY-halfL][centerX+halfL], mode);
    }

    private int define4DiamondPhase(int[][] a, int centerPointX, int centerPointY, int l, int mode)
    {
        if(centerPointX < 0 || centerPointX >= a.length || centerPointY < 0 || centerPointY >= a.length)
            return mode==0?beginLand:beginWet;
        else if(a[centerPointY][centerPointX] == -1)
        {
            a[centerPointY][centerPointX] = rndCenter(a[centerPointY-l][centerPointX-l], a[centerPointY-l][centerPointX+l], a[centerPointY+l][centerPointX+l], a[centerPointY+l][centerPointX-l], mode);
            return a[centerPointY][centerPointX];
        }
        else
            return a[centerPointY][centerPointX];
    }

    private int rndCenter(int x1, int x2, int x3, int x4, int mode) throws IllegalArgumentException
    {
        if(x1 < 0 || x2 < 0 || x3 < 0 || x4 < 0)
            throw new IllegalArgumentException("x1 = " + x1 + ", x2 = " + x2 + ", x3 = " + x3 + ", x4 = " + x4 + " must be more than zero or equals");
        int res, buff;
        Random r = new Random();
        res = (x1+x2+x3+x4) / 4;

        buff = r.nextInt((int)( res * defect + 0.4)+1);
        res = r.nextBoolean()?res-buff:res+buff;
        if(res > (mode==0?maxH:maxWet))
            res = mode==0?maxH:maxWet;
        if(res < 0)
            res = 0;
        return res;
    }

    public int[][] getLand()
    {
        if(done)
            return land;
        else
            return null;
    }

    public int[][] getWet()
    {
        if(done)
            return wet;
        else
            return null;
    }

    public static void PrintMap(int[][] a)
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