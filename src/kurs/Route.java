package kurs;
import java.util.List;

public class Route extends StopArr
{
	private Destination dest;
	public Route ()
	{
		dest = new Destination ("", "");
	}
	public Route (String number, List<Stop> vStop)
	{
		super(vStop);
		setDest (number);
	}
	public Destination getDest ()
	{
		return dest;
	}
	public void setDest (String number)
	{
		this.dest = new Destination (number, getStart().getPoint().getName() + " - " + getEnd().getPoint().getName());
		for (int i = 0; i < vStop.size(); i++)
			vStop.get(i).setDest(dest);
	}
	public Stop getStart ()
	{
		return vStop.get(0);
	}
	public Stop getEnd ()
	{
		return vStop.get(vStop.size() - 1);
	}
	public String getDirection ()
	{
		return getStart().getPoint().getName() + " - " + getEnd().getPoint().getName();
	}
	public int searchLine (Point p1, Point p2)
	{
		for (int i = 0; i < Kurs.vLine.size(); i++)
		{
			if (Kurs.vLine.get(i).containPoint(p1) && Kurs.vLine.get(i).containPoint(p2))
				return i;
		}
		return 0;
	}
	public double getDistance (Stop s1, Stop s2)
	{
		Line l = Kurs.vLine.get(searchLine(s1.getPoint(), s2.getPoint()));
		return l.getDistance(l.searchPoint(s1.getPoint()), l.searchPoint(s2.getPoint()));
	}
	public boolean equalDest (Destination d)
	{
		if (dest == d)
			return true;
		else
			return false;
	}
}
