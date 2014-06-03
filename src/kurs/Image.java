package kurs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.Calendar;


public class Image extends JComponent implements Runnable
{
	private BufferedImage train;
	public Image (BufferedImage train)
	{
		super();
		Thread t = new Thread (this, "Trains");
		this.train = train;
		t.start();
	}
	protected void printTime(JFrame f, Time t)
	{
		f.add(new JLabel(String.valueOf(t.getHH()) + ":" + t.getMM()));
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
        	final Point p = Kurs.vPoint.get(i);
        	int x = (int)Kurs.convert_x(p.getLon()) - 4;
        	int y = (int)Kurs.convert_y(p.getLat()) - 4;
        	g2.fillOval(x, y, 8, 8);
        	Font f = new Font ("Arial", Font.PLAIN, 10);
        	g2.setFont(f);
        	g2.drawString(p.getName(), x, y);
			JLabel l = new JLabel();
			l.setLocation(x, y);
			l.setSize(8, 8);
			l.addMouseListener(new MouseListener()
			{
				public void mouseClicked (MouseEvent e)
				{
					new StationTable (p);
				}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			});
			l.setVisible(true);
			add(l);    	
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
		Calendar d = Calendar.getInstance();
		g2.drawString(d.getTime().toString(), 10, 10);
		for (int i = 0; i < Kurs.vRun.size(); i++)
		{
			final Run r = Kurs.vRun.get(i);
			Point p = r.getPlaceWhile(d);
			if (!p.equalCoord(r.getRoute().getStart().getPoint()) && !p.equalCoord(r.getRoute().getEnd().getPoint()))
			{
				g2.drawImage(train, (int)Kurs.convert_x(p.getLon()) - 16, (int)Kurs.convert_y(p.getLat()) - 14, null);
				g2.setColor(Color.RED);
				g2.drawString(r.getRoute().getDest().getNumber(), (int)Kurs.convert_x(p.getLon()), (int)Kurs.convert_y(p.getLat()));
				JLabel l = new JLabel();
				l.setLocation((int)Kurs.convert_x(p.getLon()) - 16, (int)Kurs.convert_y(p.getLat()) - 14);
				l.setSize(32, 28);
				l.addMouseListener(new MouseListener()
				{
					public void mouseClicked (MouseEvent e)
					{
						new RouteTable (r);
					}
					public void mouseEntered(MouseEvent e){}
					public void mouseExited(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
				});
				l.setVisible(true);
				add(l);
			}
		}
	}
	public void run ()
	{
		while (true)
		{
			repaint();
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
