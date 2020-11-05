import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

//Хотел как лучше, а получились всё равно спагетти...
//https://www.youtube.com/watch?v=8yGZ28XlTew
class sComponent extends JComponent
{
    private static boolean DoScreenshot = true; // Если true, то сохранит "скриншот" графиков в файл out.png
    private static double A = 0; // Ваша левая границы
    private static double M = 0.5*Math.PI; // Точка разрыва
    private static double B = Math.PI; // Ваша правая границы
    private static int scaleX = 220; // Чтобы "приблизить" график по OX увеличьте число (окно увеличится тоже)
    private static int scaleY = 220; // Чтобы "приблизить" график по OY увеличьте число (окно увеличится тоже)

    private static boolean PRINTDEFAULT = true; // false = ваш исходный график не рисуется
    private static Color colorDefaulf = new Color(255, 170, 0); // RGB цвет исходного графика
    private static Color colorBackground = new Color(238, 238, 238); // RGB цвет фона
    private static Color colorAMB = new Color(20, 135, 40); // RGB цвет границ

    private static boolean PRINTSINCOS = true; // false = ряд Фурье, построенный по cos и sin, не рисуется
    private static int N_SINCOS = 30; // N для ряда Фурье по cos и sin. 1 <= k <= N
    private static Color colorSINCOS = new Color(0, 255, 4); // RGB цвет для ряда Фурье по cos и sin

    private static boolean PRINTCOS = true; // false = ряд Фурье, построенный только по cos, не рисуется
    private static int N_COS = 30; // N для ряда Фурье по cos. 1 <= k <= N
    private static Color colorCOS = new Color(255, 0, 0); // RGB цвет для ряда Фурье по cos

    private static boolean PRINTSIN = true; // false = ряд Фурье, построенный только по sin, не рисуется
    private static int N_SIN = 30; // N для ряда Фурье по sin. 1 <= k <= N
    private static Color colorSIN = new Color(13, 0, 255); // RGB цвет для ряда Фурье по sin
    //Теперь изменяйте функции f(x), Fsincos(x), Fcos(x)  и Fsin(x). Поаккуратнее с делением на нуль
    //Число pi - это Math.PI
    //Число Эйлера - это Math.E
    //cos - это Math.cos()
    //sin - это Math.sin()
    //Степень - это Math.pow(a, b) <-> a^b
    
    private double f(double x) //Исходная функция. Надо поменять на вашу
    {
        if(A < x && x < M)
            return x*x;
        else if(M < x && x < B)
            return 0;
        else
        {
            System.out.println("3A rPAHuceu!");
            return -1;
        }
    }

    private double Fsincos(double x) //ряд Фурье, построенный по cos и sin. Надо поменять на то, что получилось у вас
    {
        int k;
        double res = 0;

        res += 0.5*Math.PI*Math.PI*(1.0/12.0); //a0/2 ("а нулевое"/2)
        for (k = 1; k <= N_SINCOS; ++k) // 1 <= k <= N
        {
            res += ((Math.pow(-1, k))/(2*k*k))*Math.cos(2*k*x); //Часть с косинусом
            res += ((Math.pow(-1, k)-1)/(2*Math.PI*k*k*k) - (Math.PI*(Math.pow(-1, k)))/(4*k))*Math.sin(2*k*x); //Часть со синусом
        }
        return res;
    }
    private double Fcos(double x) //ряд Фурье, построенный только по cos. Тоже надо поменять на то, что получилось у вас
    {
        int k;
        double res = 0;

        res += 0.5*Math.PI*Math.PI*(1.0/12.0); //a0/2 ("а нулевое"/2)
        for(k = 1; k <= N_COS; ++k) // 1 <= k <= N
            res += ((Math.PI*Math.sin(0.5*k*Math.PI))/(2*k) + (2*Math.cos(0.5*k*Math.PI))/(k*k) - (4*Math.sin(0.5*k*Math.PI))/(Math.PI*k*k*k))*Math.cos(k*x); //Часть с косинусом
        return res;
    }

    private double Fsin(double x) //ряд Фурье, построенный только по sin. Меняйте на то, что получилось у вас
    {
        int k;
        double res = 0;

        for(k = 1; k <= N_SIN; k++) // 1 <= k <= N
            res += ((2*Math.sin(0.5*k*Math.PI))/(k*k) - (Math.PI*Math.cos(0.5*k*Math.PI))/(2*k) + (4*(Math.cos(0.5*k*Math.PI)-1))/(Math.PI*k*k*k))*Math.sin(k*x); //Часть со синусом
        return res;
    }


    private double dx = 0.0001;
    private int DEFAULT_WIDTH;
    private int DEFAULT_HEIGHT;
    private int CENTERX;
    private int CENTERY;
	
	private BufferedImage img;
	private boolean imgSave;

    public sComponent()
    {
        setComponentSize();
		if(DoScreenshot)
		{
			img = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
			//знаю, ((Graphics2D)g).drawImage(img, <args>) было бы быстрее...
			imgSave = false;
			Graphics2D g0 = img.createGraphics();
			
			double x, y;
			g0.setPaint(colorBackground);
			g0.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT); // BackGround
			g0.setPaint(new Color(0, 0, 0));

			g0.drawLine(CENTERX, 0, CENTERX, DEFAULT_HEIGHT); // OY
			g0.drawLine(0, CENTERY, DEFAULT_WIDTH, CENTERY); // OX

			g0.setPaint(colorAMB);
			g0.drawLine((int)(CENTERX+A*scaleX), 0, (int)(CENTERX+A*scaleX), DEFAULT_HEIGHT); // A
			g0.drawString("A", (int)(CENTERX+A*scaleX)+5, CENTERY-5);
			g0.drawLine((int)(CENTERX+M*scaleX), 0, (int)(CENTERX+M*scaleX), DEFAULT_HEIGHT); // M
			g0.drawString("M", (int)(CENTERX+M*scaleX)+5, CENTERY-5);
			g0.drawLine((int)(CENTERX+B*scaleX), 0, (int)(CENTERX+B*scaleX), DEFAULT_HEIGHT); // B
			g0.drawString("B", (int)(CENTERX+B*scaleX)+5, CENTERY-5);

			g0.setPaint(new Color(0, 0, 0));
			g0.drawRect(0, 0, 100, 70);
			g0.drawString("Made by The220th", 3, 85);
			g0.setPaint(colorDefaulf);
			g0.drawString("f(x)", 5, 15);
			g0.setPaint(colorSINCOS);
			g0.drawString("sin&cos, N = " + N_SINCOS, 5, 30);
			g0.setPaint(colorCOS);
			g0.drawString("cos, N = " + N_COS, 5, 45);
			g0.setPaint(colorSIN);
			g0.drawString("sin, N = " +  N_SIN, 5, 60);
		}
		else
			imgSave = true;
    }

    private void setComponentSize()
    {
        double min, max, x;
        int dw = 150, dh = 150;
        for(x = A + 0.00000000001, min = x, max = x; x < B; x+=dx)
        {
            if(f(x) > max)
                max = f(x);
            if(f(x) < min)
                min = f(x);
        }
        max*=scaleY;
        min*=scaleY;
        if(max > 0)
        {
            if(min < 0)
            {
                DEFAULT_HEIGHT = (int)((max-min+dh*2));
                CENTERY = (int)(max)+dh;
            }
            else
            {
                DEFAULT_HEIGHT = (int)((max+dh*2));
                CENTERY = (int)(max)+dh;
            }
        }
        else
        {
            DEFAULT_HEIGHT = (int)((Math.abs(min)+dh*2));
            CENTERY = dh;
        }
        if(A > 0)
        {
            DEFAULT_WIDTH = (int)((B*scaleX+dw*2));
            CENTERX = (int)(dw);
        }
        else
        {
            DEFAULT_WIDTH = (int)(((B - A)*scaleX + dw*2));
            CENTERX = (int)((Math.abs(A)*scaleX + dw));
        }
    }

    public void paintComponent(Graphics gOld)
    {
        Graphics2D g = (Graphics2D)gOld;
        
        double x, y;

        g.setPaint(colorBackground);
        g.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT); // BackGround
        g.setPaint(new Color(0, 0, 0));

        g.drawLine(CENTERX, 0, CENTERX, DEFAULT_HEIGHT); // OY
        g.drawLine(0, CENTERY, DEFAULT_WIDTH, CENTERY); // OX

        g.setPaint(colorAMB);
        g.drawLine((int)(CENTERX+A*scaleX), 0, (int)(CENTERX+A*scaleX), DEFAULT_HEIGHT); // A
        g.drawString("A", (int)(CENTERX+A*scaleX)+5, CENTERY-5);
        g.drawLine((int)(CENTERX+M*scaleX), 0, (int)(CENTERX+M*scaleX), DEFAULT_HEIGHT); // M
        g.drawString("M", (int)(CENTERX+M*scaleX)+5, CENTERY-5);
        g.drawLine((int)(CENTERX+B*scaleX), 0, (int)(CENTERX+B*scaleX), DEFAULT_HEIGHT); // B
        g.drawString("B", (int)(CENTERX+B*scaleX)+5, CENTERY-5);

        g.setPaint(new Color(0, 0, 0));
        g.drawRect(0, 0, 100, 70);
        g.drawString("Made by The220th", 3, 85);
        g.setPaint(colorDefaulf);
        g.drawString("f(x)", 5, 15);
        g.setPaint(colorSINCOS);
        g.drawString("sin&cos, N = " + N_SINCOS, 5, 30);
        g.setPaint(colorCOS);
        g.drawString("cos, N = " + N_COS, 5, 45);
        g.setPaint(colorSIN);
        g.drawString("sin, N = " +  N_SIN, 5, 60);

        if(PRINTDEFAULT)
        {
            for(x = A+0.00000000001; x < B; x+=dx)
            {
                y = f(x);
                paintPixel((int)(CENTERX+x*scaleX), (int)(CENTERY-y*scaleY), colorDefaulf, g);
            }
        }

        if(PRINTSINCOS)
        {
            for(x = A; x < B; x+=dx)
            {
                y = Fsincos(x);
                paintPixel((int)(CENTERX+x*scaleX), (int)(CENTERY-y*scaleY), colorSINCOS, g);
            }
        }

        if(PRINTCOS)
        {
            for(x = A; x < B; x+=dx)
            {
                y = Fcos(x);
                paintPixel((int)(CENTERX+x*scaleX), (int)(CENTERY-y*scaleY), colorCOS, g);
            }
        }

        if(PRINTSIN)
        {
            for(x = A; x < B; x+=dx)
            {
                y = Fsin(x);
                paintPixel((int)(CENTERX+x*scaleX), (int)(CENTERY-y*scaleY), colorSIN, g);
            }
        }
		
		if(imgSave == false)
		{
			try
			{
			File outputfile = new File("out.png");
			ImageIO.write(img, "png", outputfile);
			imgSave = true;
			}
			catch(IOException e)
			{
				System.out.println("Image dont saved, sry");
				System.out.println(e);
			}
		}
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void paintPixel(int X, int Y, Color c, Graphics2D g)
    {
        g.setPaint(c);
        g.fillRect(X, Y, 1, 1);
		if(imgSave == false)
			img.setRGB(X, Y, c.getRGB());
    }
}

public class GraphicBuilder
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater( () ->
        {
            sFrame frame = new sFrame();
            frame.setTitle("GraphicBuilder");
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
        this.add(new JScrollPane(new sComponent()));
        this.pack();
    }
}