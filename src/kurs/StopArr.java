package kurs;

import java.util.ArrayList;
import java.util.List;

public abstract class StopArr
{
	protected List<Stop> vStop = new ArrayList<Stop> ();
	public StopArr(){}
	public StopArr(List<Stop> vStop)
	{
		this.vStop = vStop;
	}
	public List<Stop> getStops ()
	{
		return vStop;
	}
	public void addStop (Stop s)
	{
		vStop.add(s);
	}
	public Stop getStop (int pos)
	{
		return vStop.get(pos);
	}
	public int sizeStops ()
	{
		return vStop.size();
	}
}
