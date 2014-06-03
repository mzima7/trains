package kurs;

import java.util.Calendar;

public class Time
{
	private int hh, mm, dd;
	public Time (int hh, int mm)
	{
		this.hh = hh;
		this.mm = mm;
		dd = 0;
	}
	public Time (int hh, int mm, int dd)
	{
		this.hh = hh;
		this.mm = mm;
		this.dd = dd;
	}
	public int getHH ()
	{
		return hh;
	}
	public int getMM ()
	{
		return mm;
	}
	public int getDD ()
	{
		return dd;
	}
	public void setDD (int dd)
	{
		this.dd = dd;
	}
	public Calendar toCal (int diy)
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, diy + dd);
		c.set(Calendar.HOUR_OF_DAY, hh);
		c.set(Calendar.MINUTE, mm);
		return c;
	}
}
