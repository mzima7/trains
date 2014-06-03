package kurs;

public class StartStop extends Stop
{
//	private Calendar DepTime;
	private Time dep;
	public StartStop ()
	{
		super ();
	}
	public StartStop (Point p, Time dep)
	{
		super (p);
		this.dep = dep;
	}
	public void setArrTime (Time dep)
	{
	}
	public Time getArrTime ()
	{
		return dep;
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
		return -1;
	}
}
