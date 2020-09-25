package Landscape;

/**
 * Должно гарантироваться, что изменить точку нельзя
 */
public class sPoint
{
    private short x;
    private short y;
    public sPoint(short X, short Y)
    {
        this.x = X;
        this.y = Y;
    }

    public short getX()
    {
        return this.x;
    }
    public short getY()
    {
        return this.y;
    }

    public sPoint addX(short addedX)
    {
        return new sPoint((short)(this.x + addedX), this.y);
    }

    public sPoint addY(short addedY)
    {
        return new sPoint(this.x, (short)(this.y + addedY));
    }

    public sPoint subX(short addedX)
    {
        return new sPoint((short)(this.x - addedX), this.y);
    }

    public sPoint subY(short addedY)
    {
        return new sPoint(this.x, (short)(this.y - addedY));
    }

    public sPoint addXY(short addedX, short addedY)
    {
        return new sPoint((short)(this.x + addedX), (short)(this.y + addedY));
    }

    static sPoint make(short X, short Y)
    {
        return new sPoint(X, Y);
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }
}