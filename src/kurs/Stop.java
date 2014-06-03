package kurs;

public abstract class Stop
{
	protected Destination dest;
	protected Point p;
	public Stop ()
	{
	}
	public Stop (Point p)
	{
		this.p = p;
	}
	public Point getPoint ()
	{
		return p;
	}
	public Destination getDest()
	{
		return dest;
	}
	public void setDest (Destination dest)
	{
		this.dest = dest;
	}
	public abstract void setArrTime (Time ArrTime);
	public abstract Time getArrTime ();
	public abstract void setDepTime (Time DepTime);
	public abstract Time getDepTime ();
	public abstract int getStatus ();
}
