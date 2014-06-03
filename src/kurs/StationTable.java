package kurs;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class StationTable extends JFrame
{
	StationTable(Point p)
	{
		super (p.getName());
		Station s = new Station (p);
		setSize(600, s.sizeStops() * 25 + 25);
		setLayout(new GridLayout(0, 3));
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent we)
			{
				dispose();
			}
		});
		
		add(new JLabel("Маршрут"));
		add(new JLabel("Время прибытия"));
		add(new JLabel("Время отправления"));
		for (int i = 0; i < s.sizeStops(); i++)
		{
			final Destination dest = s.getStop(i).getDest();
			JLabel l = new JLabel(dest.toString());
			l.addMouseListener(new MouseListener()
			{
				public void mouseClicked (MouseEvent e)
				{
					for (int i = 0; i < Kurs.vRoute.size(); i++)
					{
						if (Kurs.vRoute.get(i).equalDest(dest))
							new RouteTable (new Run (Kurs.vRoute.get(i), Kurs.diy));
					}
				}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
				public void mousePressed(MouseEvent e){}
			});
			add(l);
			if (s.getStop(i).getStatus() != -1)
			{
				Time t = s.getStop(i).getArrTime();
				printTime (t);
			}
			else
				add(new JLabel());
			if (s.getStop(i).getStatus() != 1)
			{
				Time t = s.getStop(i).getDepTime();
				printTime (t);
			}
			else
				add(new JLabel());
		}
		setVisible(true);
	}
	protected void printTime(Time t)
	{
		DecimalFormat df = new DecimalFormat("00");
		add(new JLabel(String.valueOf(t.getHH()) + ":" + df.format(t.getMM())));
	}
}
