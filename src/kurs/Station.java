package kurs;

import java.util.List;

public class Station extends StopArr
{
	private Point p;
	public Station()
	{
	}
	public Station(Point p)
	{
		this.p = p;
		addStops();
	}
	public Station(Point p, List<Stop> vStop)
	{
		super(vStop);
		this.p = p;
	}
	public void setPoint (Point p)
	{
		this.p = p;
	}
	public Point getPoint ()
	{
		return p;
	}
	public void addStops ()
	{
		for (int i = 0; i < Kurs.vRoute.size(); i++)
		{
			Route r = Kurs.vRoute.get(i);
			for (int j = 0; j < r.sizeStops(); j++)
			{
				Stop s = r.getStop(j);
				if (s.getPoint() == p)
					vStop.add(s);
			}
		}
	}
}
