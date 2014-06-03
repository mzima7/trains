package kurs;

public class InterStop extends Stop
{
	private Time arr;
	private Time dep;
	public InterStop()
	{
		super ();
	}
	public InterStop (Point p, Time arr, Time dep)
	{
		super (p);
		this.arr = arr;
		this.dep = dep;
	}
	public void setArrTime (Time arr)
	{
		this.arr = arr;
	}
	public Time getArrTime ()
	{
		return arr;
	}
	public void setDepTime (Time dep)
	{
		this.dep = dep;
	}
	public Time getDepTime ()
	{
		return dep;
	}
	public int getStatus ()
	{
		return 0;
	}
}
