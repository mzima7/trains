package kurs;

public class EndStop extends Stop
{
	private Time arr;
	public EndStop ()
	{
		super ();
	}
	public EndStop (Point p, Time arr)
	{
		super (p);
		this.arr = arr;
	}
	public void setArrTime (Time arr)
	{
		this.arr = arr;
	}
	public Time getArrTime ()
	{
		return arr;
	}
	public void setDepTime (Time DepTime)
	{
	}
	public Time getDepTime ()
	{
		return arr;
	}
	public int getStatus ()
	{
		return 1;
	}
}
