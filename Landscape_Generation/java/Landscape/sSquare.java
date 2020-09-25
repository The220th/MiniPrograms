package Landscape;

import Landscape.sPoint;
/**
 * Должно гарантироваться, что изменить квадрат нельзя
 */
public class sSquare
{
    private sPoint left_up;
    private short a;

    /**
     * Конструирует квадрат
     * 
     * @param Left_up - верхний левый угол
     * @param l - сторона квадрата - 1
     */
    public sSquare(sPoint Left_up, short l)
    {
        this.left_up = Left_up;
        a = l;
    }

    public sPoint getLU()
    {
        return left_up;
    }

    public sPoint getLD()
    {
        return new sPoint(this.left_up.getX(), (short)(this.left_up.getY()+a));
    }

    public sPoint getRU()
    {
        return new sPoint((short)(this.left_up.getX()+a), this.left_up.getY());
    }

    public sPoint getRD()
    {
        return new sPoint((short)(this.left_up.getX()+a), (short)(this.left_up.getY()+a));
    }

    public sPoint getC()
    {
        return this.left_up.addXY((short)(a/2), (short)(a/2));
    }

    public String toString()
    {
        return "Square: lu = " + this.left_up.toString() + ", l = " + a;
    }
}