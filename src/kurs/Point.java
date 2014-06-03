package kurs;

public class Point
{
	private double lat;
	private double lon;
	private String name;
	public Point ()
	{
		lat = 45;
		lon = 30;
		name = "";
	}
	public Point (double lat, double lon, String name)
	{
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}
	public void setLat (double lat)
	{
		this.lat = lat;
	}
	public double getLat ()
	{
		return lat;
	}
	public void setLon (double lon)
	{
		this.lon = lon;
	}
	public double getLon ()
	{
		return lon;
	}
	public void setName (String name)
	{
		this.name = name;
	}
	public String getName ()
	{
		return name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	public boolean equalCoord (Point p)
	{
		if (lat == p.lat || lon == p.lon)
			return true;
		else
			return false;
	}
}
