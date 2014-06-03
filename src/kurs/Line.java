package kurs;
import java.util.*;

public class Line
{
	private List<Point> vp = new ArrayList<Point> ();
	public Line()
	{
	}
	public Line(List<Point> vp)
	{
		this.vp = vp;
	}
	public List<Point> getPoints()
	{
		return vp;
	}
	public void addPoint(Point p)
	{
		vp.add(p);
	}
	public Point getPoint(int pos)
	{
		return vp.get(pos);
	}
	public int sizePoints()
	{
		return vp.size();
	}
	public Point getStart()
	{
		return vp.get(0);
	}
	public Point getEnd()
	{
		return vp.get(vp.size() - 1);
	}
	public double SpanDistance(int pos)
	{
		double clat = Math.abs(vp.get(pos + 1).getLat() - vp.get(pos).getLat());
		double clon = Math.abs(vp.get(pos + 1).getLon() - vp.get(pos).getLon());
		double cxa = Math.abs(vp.get(pos + 1).getLat() + vp.get(pos).getLat() / 2) / 90;
		double llat = clat * 40007.86 / 360;
		double llon = Math.cos(cxa * 3.14159265359 / 2) * clon * 40075.017 / 360;
		return Math.sqrt(llat * llat + llon * llon);
	}
	public double getDistance(int pos1, int pos2)
	{
		double d = 0;
		if (pos1 > pos2)
		{
			int t = pos1;
			pos1 = pos2;
			pos2 = t;
		}
		for (int i = pos1; i < pos2; i++)
		d += SpanDistance(i);
		return d;
	}
	public boolean containPoint (Point p)
	{
		return vp.contains(p);
	}
	public int searchPoint (Point p)
	{
		return vp.indexOf(p);
	}
}
