package kurs;
import java.util.*;
import java.util.List;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.imageio.ImageIO;

public class Kurs extends JFrame
{
	public Kurs ()
	{
		super ("Trains visio");
		setSize(816, 620);
		setLayout(new GridLayout(1, 1));
		try
		{
			Image t = new Image(ImageIO.read(new File("train.png")));
			add(t);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent we)
			{
				System.exit(0);
			}
		});
		JMenuBar menuBar = new JMenuBar ();
		JMenuItem allStations = new JMenuItem ("Список станций");
		JMenuItem allRoutes = new JMenuItem ("Список поездов");
		JMenuItem quit = new JMenuItem ("Выход");
		allStations.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				final JFrame f = new JFrame ("Список станций");
				f.setSize(400, 730);
				f.setLayout(new GridLayout(0, 1));
				f.addWindowListener(new WindowAdapter()
				{
					public void windowClosing (WindowEvent we)
					{
						f.dispose();
					}
				});
				for (int i = 0; i < vPoint.size(); i++)
				{
					final Point p = vPoint.get(i);
					JLabel l = new JLabel(p.getName());
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
					f.add(l);
				}
				f.setVisible(true);
			}
		});
		allRoutes.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				final JFrame f = new JFrame ("Список поездов");
				f.setSize(400, 730);
				f.setLayout(new GridLayout(0, 1));
				f.addWindowListener(new WindowAdapter()
				{
					public void windowClosing (WindowEvent we)
					{
						f.dispose();
					}
				});
				for (int i = 0; i < vRoute.size(); i++)
				{
					final Run r = new Run (vRoute.get(i), diy);
					JLabel l = new JLabel(r.getRoute().getDest().toString());
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
					f.add(l);
				}
				f.setVisible(true);
			}
		});
		quit.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				System.exit(0);
			}
		});
		menuBar.add(allStations);
		menuBar.add(allRoutes);
		menuBar.add(quit);
		setJMenuBar(menuBar);
	}
	public static List<Point> vPoint = new ArrayList<Point> ();
	public static List<Line> vLine = new ArrayList<Line> ();
	public static List<Route> vRoute = new ArrayList<Route> ();
	public static List<Run> vRun = new ArrayList<Run> ();
	public static int diy;
	public static void getPoints()
	{
		File f = new File("points.txt");
		try
		{
			Scanner input = new Scanner(f);
			while (input.hasNextLine())
			{
				String[] line = input.nextLine().split(" ");
				float lat = Float.valueOf(line[1]);
				float lon = Float.valueOf(line[2]);
				Point p = new Point(lat, lon, line[0]);
				vPoint.add(p);
			}
			input.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
	public static void getLines ()
	{
		File f = new File("lines.txt");
		try
		{
			Scanner input = new Scanner(f);
			while (input.hasNextLine())
			{
				Line l = new Line ();
				while (input.hasNextLine())
				{
					String name = input.nextLine();
					if (name.contains("/"))
						break;
					l.addPoint(vPoint.get(vPoint.indexOf(new Point(0, 0, name))));
				}
				vLine.add(l);
			}
			input.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
	public static void getRoutes ()
	{
		File f = new File("routes.txt");
		try
		{
			Scanner input = new Scanner(f);
			while (input.hasNextLine())
			{
				while (input.hasNextLine())
				{
					String number = input.nextLine();
					if (number.contains("/"))
						break;
					Route r = new Route ();
					String[] aline = input.nextLine().split("\t");
					String[] tdep = aline[1].split(":");
					Stop as = new StartStop(vPoint.get(vPoint.indexOf(new Point(0, 0, aline[0]))), new Time (Integer.valueOf(tdep[0]), Integer.valueOf(tdep[1])));
					r.addStop(as);
					while (input.hasNextLine())
					{
						String[] line = input.nextLine().split("\t");
						if (line.length == 1)
							break;
						String[] barr = line[1].split(":");
						if (line.length == 2)
						{
							Stop bs = new EndStop(vPoint.get(vPoint.indexOf(new Point(0, 0, line[0]))), new Time (Integer.valueOf(barr[0]), Integer.valueOf(barr[1])));
							r.addStop(bs);
							break;
						}
						String[] bdep = line[2].split(":");
						Stop s = new InterStop(vPoint.get(vPoint.indexOf(new Point(0, 0, line[0]))), new Time (Integer.valueOf(barr[0]), Integer.valueOf(barr[1])), new Time (Integer.valueOf(bdep[0]), Integer.valueOf(bdep[1])));
						r.addStop(s);
					}
					r.setDest(number);
					vRoute.add(r);
				}
			}
			input.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
		}
	}
	public static void createRoutes (int diy)
	{
		int k = vRoute.size();
		for (int i = 0; i < k; i++)
		{
			Route r = vRoute.get(i);
			for (int j = 1; j < r.sizeStops(); j++)
			{
				Stop s = r.getStop(j);
				Stop s1 = r.getStop(j-1);
				if (s.getArrTime().toCal(diy).before(s1.getDepTime()))
				{
					Time t = s.getArrTime();
					t.setDD(t.getDD() + 1);
					s.setArrTime(t);
				}
				if (s.getDepTime().toCal(diy).before(s.getArrTime()))
				{
					Time t = s.getDepTime();
					t.setDD(t.getDD() + 1);
					s.setDepTime(t);
				}
			}
			Run n = new Run (r, diy);
			vRun.add(n);
			Run n1 = new Run (r, diy - 1);
			vRun.add(n1);
		}
	}
	public static double xstart = 21.2;
	public static double xend = 40.3;
	public static double convert_x (double lon)
	{
		return ((lon - xstart) / (xend - xstart)) * 800;
	}
	public static double ystart = 44.16;
	public static double yend = 53;
	public static double convert_y (double lat)
	{
		return (559 - (((lat - ystart) / (yend - ystart)) * 559));
	}
	public static void main(String[] args)
	{
		getPoints();
		getLines();
		getRoutes();
		diy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		createRoutes(diy);
		createRoutes(diy - 1);
		Kurs k = new Kurs();
		k.setVisible(true);
	}

}
