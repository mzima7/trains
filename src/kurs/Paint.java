package kurs;
/*
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Paint extends JComponent
{
	public Paint()
	{
		super();
	}
	protected void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        try
        {
        	BufferedImage map = ImageIO.read(new File("800px-Map_of_Ukraine_simple_blank.png"));
        	g2.drawImage(map, 0, 0, null);
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        for (int i = 0; i < Kurs.vPoint.size(); i++)
        {
        	int x = (int)Kurs.convert_x(Kurs.vPoint.get(i).getLon()) - 4;
        	int y = (int)Kurs.convert_y(Kurs.vPoint.get(i).getLat()) - 4;
        	g2.fillOval(x, y, 8, 8);
        	Font f = new Font ("Arial", Font.PLAIN, 10);
        	g2.setFont(f);
        	g2.drawString(Kurs.vPoint.get(i).getName(), x, y);
        }
        for (int i = 0; i < Kurs.vLine.size() - 1; i++)
        {
        	for (int j = 0; j < Kurs.vLine.get(i).sizePoints() - 1; j++)
        	{
        		Point p1 = Kurs.vLine.get(i).getPoint(j);
        		Point p2 = Kurs.vLine.get(i).getPoint(j + 1);
        		g2.drawLine((int)Kurs.convert_x(p1.getLon()), (int)Kurs.convert_y(p1.getLat()), (int)Kurs.convert_x(p2.getLon()), (int)Kurs.convert_y(p2.getLat()));
        	}
        }
	}
}*/
