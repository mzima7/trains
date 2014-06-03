package kurs;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class RouteTable extends JFrame
{
	public RouteTable (final Run r)
	{
		super (r.getRoute().getDest().toString());
		Route k = r.getRoute();
		setSize(400, k.sizeStops() * 25 + 25);
		setLayout(new GridLayout(0, 3));
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent we)
			{
				dispose();
			}
		});
		
		add(new JLabel("Станция"));
		add(new JLabel("Время прибытия"));
		add(new JLabel("Время отправления"));
		JLabel la = new JLabel(r.getRoute().getStart().getPoint().getName());
		la.addMouseListener(new MouseListener()
		{
			public void mouseClicked (MouseEvent e)
			{
				new StationTable (r.getRoute().getStart().getPoint());
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
		});
		add(la);
		add(new JLabel());
		Time arr, dep;
		dep = k.getStart().getDepTime();
		printTime(dep);
		for (int i = 1; i < k.sizeStops() - 1; i++)
		{
			final Point p = r.getRoute().getStop(i).getPoint();
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
			add(l);
			arr = k.getStop(i).getArrTime();
			printTime(arr);
			dep = k.getStop(i).getDepTime();
			printTime(dep);
		}
		JLabel lb = new JLabel(r.getRoute().getEnd().getPoint().getName());
		lb.addMouseListener(new MouseListener()
		{
			public void mouseClicked (MouseEvent e)
			{
				new StationTable (r.getRoute().getEnd().getPoint());
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
		});
		add(lb);
		arr = k.getEnd().getArrTime();
		printTime(arr);
		
		setVisible(true);
	}
	protected void printTime(Time t)
	{
		DecimalFormat df = new DecimalFormat("00");
		add(new JLabel(String.valueOf(t.getHH()) + ":" + df.format(t.getMM())));
	}
}
