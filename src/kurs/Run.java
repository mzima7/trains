package kurs;

import java.util.Calendar;


public class Run
{
	private Route r;
	private int diy;
	private Calendar m[][];
	public Run (Route r, int diy)
	{
		this.r = r;
		this.diy = diy;
		m = new Calendar [2][r.sizeStops()];
		Stop as = r.getStart();
		m[1][0] = Calendar.getInstance();
		m[1][0].set(Calendar.DAY_OF_MONTH, diy + as.getDepTime().getDD());
		m[1][0].set(Calendar.HOUR_OF_DAY, as.getDepTime().getHH());
		m[1][0].set(Calendar.MINUTE, as.getDepTime().getMM());
		for (int i = 1; i < r.sizeStops() - 1; i++)
		{
			Stop s = r.getStop(i);
			m[0][i] = Calendar.getInstance();
			m[0][i].set(Calendar.DAY_OF_MONTH, diy + s.getArrTime().getDD());
			m[0][i].set(Calendar.HOUR_OF_DAY, s.getArrTime().getHH());
			m[0][i].set(Calendar.MINUTE, s.getArrTime().getMM());
			m[1][i] = Calendar.getInstance();
			m[1][i].set(Calendar.DAY_OF_MONTH, diy + s.getDepTime().getDD());
			m[1][i].set(Calendar.HOUR_OF_DAY, s.getDepTime().getHH());
			m[1][i].set(Calendar.MINUTE, s.getDepTime().getMM());
		}
		Stop bs = r.getEnd();
		m[0][r.sizeStops() - 1] = Calendar.getInstance();
		m[0][r.sizeStops() - 1].set(Calendar.DAY_OF_MONTH, diy + bs.getArrTime().getDD());
		m[0][r.sizeStops() - 1].set(Calendar.HOUR_OF_DAY, bs.getArrTime().getHH());
		m[0][r.sizeStops() - 1].set(Calendar.MINUTE, bs.getArrTime().getMM());
	}
	public int getDiy ()
	{
		return diy;
	}
	public Route getRoute ()
	{
		return r;
	}
	public Point getPlaceWhile (Calendar t)
	{
		if (t.before(m[1][0]) || t.equals(m[1][0]))
			return r.getStart().getPoint();
		if (t.after(m[0][r.sizeStops() - 1]) || t.equals(m[0][r.sizeStops() - 1]))
			return r.getEnd().getPoint();
		int i;
		for (i = 0; i < r.sizeStops() - 1; i++)
		{
			if (m[1][i].after(t) || m[1][i].equals(t))
				break;
		}
		if (t.after(m[0][i]) || t.equals(m[0][i]))
			return r.getStop(i).getPoint();
		long a = t.getTimeInMillis() - m[1][i-1].getTimeInMillis();
		long b = m[0][i].getTimeInMillis() - m[1][i-1].getTimeInMillis();
		double c = (double)a / (double)b;
		double d = r.getDistance(r.getStop(i - 1), r.getStop(i));
		double e = c * d;
		Point p = r.getStop(i - 1).getPoint();
		Point q = r.getStop(i).getPoint();
		Line l = Kurs.vLine.get(r.searchLine(p, q));
		int p1 = l.searchPoint(p);
		int p2 = l.searchPoint(q);
		double x = 0;
		int n = p1;
		Point p3, p4;
		double z;
		if (p1 < p2)
		{
			while (x < e)
			{
				x = l.getDistance(n, p1);	// distance between stop and intermediate point
				if (x > e)
				{
					n--;
					x = l.getDistance(n, p1);
					break;
				}
				n++;
			}
			double y = e - x;	// distance between intermediate and current point
			z = y / l.getDistance(n+1, n);
			p3 = l.getPoint(n);
			p4 = l.getPoint(n+1);
		}
		else
		{
			while (x < e)
			{
				x = l.getDistance(n, p1);	// distance between stop and intermediate point
				if (x > e)
				{
					n++;
					x = l.getDistance(n, p1);
					break;
				}
				n--;
			}
			double y = e - x;	// distance between intermediate and current point
			z = y / l.getDistance(n-1, n);
			p3 = l.getPoint(n);
			p4 = l.getPoint(n-1);
		}
		double lat = p3.getLat() + z * (p4.getLat() - p3.getLat());
		double lon = p3.getLon() + z * (p4.getLon() - p3.getLon());
		return new Point(lat, lon, "");
	}

}
