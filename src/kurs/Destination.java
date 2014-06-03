package kurs;

public class Destination
{
	private String number;
	private String dest;
	Destination (){}
	Destination (String number, String dest)
	{
		this.number = number;
		this.dest = dest;
	}
	public String getNumber ()
	{
		return number;
	}
	public String getDest ()
	{
		return dest;
	}
	public String toString ()
	{
		return number + " " + dest;
	}
}
