#include <hge.h>
#include <hgesprite.h>
#include <hgefont.h>
#include <hgeparticle.h>
#include <iostream>
#include <cstdlib>
#include <ctime>
#include <vector>
using namespace std;

HGE *hge = 0;


hgeSprite* map;
hgeSprite* point;
hgeSprite* train;
hgeFont* fnt;
hgeFont* text;

HTEXTURE tex;
HTEXTURE p;
HTEXTURE tra;

vector<class Point> vPoint;
vector<class Line> vLine;
vector<class Route> vRoute;
vector<class Route> vRoute2;

bool operator < (tm t1, tm t2)
{
	if (t1.tm_year == t2.tm_year)
	{
		if (t1.tm_mon == t2.tm_mon)
		{
			if (t1.tm_mday == t2.tm_mday)
			{
				if (t1.tm_hour == t2.tm_hour)
				{
					if (t1.tm_min == t2.tm_min)
					{
						if (t1.tm_sec < t2.tm_sec)
							return true;
						else
							return false;
					}
					else
					{
						if (t1.tm_min < t2.tm_min)
							return true;
						else
							return false;
					}
				}
				else
				{
					if (t1.tm_hour < t2.tm_hour)
						return true;
					else
						return false;
				}
			}
			else
			{
				if (t1.tm_mday < t2.tm_mday)
					return true;
				else
					return false;
			}
		}
		else
		{
			if (t1.tm_mon < t2.tm_mon)
				return true;
			else
				return false;
		}
	}
	else
	{
		if (t1.tm_year < t2.tm_year)
			return true;
		else
			return false;
	}
}
bool operator <= (tm t1, tm t2)
{
	if (t1.tm_year == t2.tm_year)
	{
		if (t1.tm_mon == t2.tm_mon)
		{
			if (t1.tm_mday == t2.tm_mday)
			{
				if (t1.tm_hour == t2.tm_hour)
				{
					if (t1.tm_min == t2.tm_min)
					{
						if (t1.tm_sec > t2.tm_sec)
							return false;
						else
							return true;
					}
					else
					{
						if (t1.tm_min > t2.tm_min)
							return false;
						else
							return true;
					}
				}
				else
				{
					if (t1.tm_hour > t2.tm_hour)
						return false;
					else
						return true;
				}
			}
			else
			{
				if (t1.tm_mday > t2.tm_mday)
					return false;
				else
					return true;
			}
		}
		else
		{
			if (t1.tm_mon > t2.tm_mon)
				return false;
			else
				return true;
		}
	}
	else
	{
		if (t1.tm_year > t2.tm_year)
			return false;
		else
			return true;
	}
}
class Point
{
private:
	float lat;
	float lon;
	char* name;
public:
	Point()
	{
		lat = 45;
		lon = 30;
		name = new char [2];
		strcpy(name, "");
	}
	Point(const Point &p)
	{
		lat = p.lat;
		lon = p.lon;
		name = new char [strlen(p.name) + 1];
		strcpy(name, p.name);
	}
	Point(float lat, float lon, const char* name)
	{
		this->lat = lat;
		this->lon = lon;
		this->name = new char [strlen(name) + 1];
		strcpy(this->name, name);
	}
	~Point()
	{
		//		delete [] name;
	}
	void set(float lat, float lon, const char* name)
	{
		this->lat = lat;
		this->lon = lon;
		this->name = new char [strlen(name) + 1];
		strcpy(this->name, name);
	}
	float getLat() const
	{
		return lat;
	}
	float getLon() const
	{
		return lon;
	}
	const char* getName() const
	{
		return name;
	}
	bool operator ==(const Point &p)
	{
		if (strcmp(p.name, name) == 0 && p.lat == lat && p.lon == lon)
			return true;
		else
			return false;
	}
};
class Line
{
private:
	vector<class Point> vp;
public:
	Line()
	{
	}
	Line(const Line& l)
	{
		vp = l.vp;
	}
	Line(vector<class Point> vp)
	{
		this->vp = vp;
	}
	vector<class Point> getPoints() const
	{
		return vp;
	}
	void setPoint(Point p)
	{
		vp.push_back(p);
	}
	Point getPoint(int pos) const
	{
		return vp[pos];
	}
	Point getStart() const
	{
		return vp[0];
	}
	Point getEnd() const
	{
		return vp[vp.size() - 1];
	}
	double SpanDistance(int pos) const
	{
		double clat = abs(vp[pos+1].getLat() - vp[pos].getLat());
		double clon = abs(vp[pos+1].getLon() - vp[pos].getLon());
		double cxa = abs((vp[pos+1].getLat() + vp[pos].getLat()) / 2) / 90;
		double llat = clat * 40007.86 / 360;
		double llon = cos(cxa * 3.14159265359 / 2) * clon * 40075.017 / 360;
		return sqrt(llat * llat + llon * llon);
	}
	double getDistance(int pos1, int pos2) const
	{
		double d = 0;
		if (pos1 < pos2)
		{
			for (int i = pos1; i < pos2; i++)
			{
				d+= SpanDistance(i);
			}
			return d;
		}
		else
			swap(pos1, pos2);
			for (int i = pos1; i < pos2; i++)
			{
				d+= SpanDistance(i);
			}
			return d;
	}
	int getNPoints() const
	{
		return vp.size();
	}
	Point searchPoint(char* name) const
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (strcmp(vPoint[i].getName(), name) == 0)
			{
				return vPoint[i];
			}
		}
		return Point::Point();
	}
	Point searchPoint(Point p) const
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (vPoint[i] == p)
			{
				return vPoint[i];
			}
		}
		return Point::Point();
	}
	bool searchPointinLine(Point p)
	{
		for(int i = 0; i < vp.size(); i++)
		{
			if (vp[i] == p)
			{
				return true;
			}
		}
		return false;
	}
	int getPos(Point p)
	{
		for(int i = 0; i < vp.size(); i++)
		{
			if (vp[i] == p)
			{
				return i;
			}
		}
		return 0;
	}
};
class Stop
{
protected:
	Point p;
public:
	Stop()
	{
	}
	Stop(Point p)
	{
		this->p = p;
	}
	Stop(char* name)
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (strcmp(vPoint[i].getName(), name) == 0)
			{
				p = vPoint[i];
				break;
			}
		}
	}
	Stop(const Stop &s)
	{
		p = s.p;
	}
	void setPoint(Point p)
	{
		this->p = p;
	}
	Point getPoint() const
	{
		return p;
	}
	Point searchPoint(char* name) const
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (strcmp(vPoint[i].getName(), name) == 0)
			{
				return vPoint[i];
			}
		}
		return vPoint[0];
	}
	virtual void setArrTime(tm ArrTime) = 0;
	virtual tm getArrTime() const = 0;
	virtual void setDepTime(tm DepTime) = 0;
	virtual tm getDepTime() const = 0;
	virtual int getStatus() const = 0;
};
class StartStop: virtual public Stop
{
protected:
	tm DepTime;
public:
	StartStop(): Stop()
	{
	}
	StartStop(Point p, tm DepTime): Stop(p)
	{
		this->DepTime = DepTime;
	}
	StartStop(char* name, tm DepTime): Stop(name)
	{
		this->DepTime = DepTime;
	}
	StartStop(const StartStop &s): Stop(s)
	{
		DepTime = s.DepTime;
	}
	void setDepTime(tm DepTime)
	{
		this->DepTime = DepTime;
	}
	tm getDepTime() const
	{
		return DepTime;
	}
	void setArrTime(tm ArrTime)
	{
	}
	tm getArrTime() const
	{
		return DepTime;
	}
	int getStatus() const
	{
		return -1;
	}
};
class EndStop: virtual public Stop
{
protected:
	tm ArrTime;
public:
	EndStop(): Stop()
	{
	}
	EndStop(Point p, tm ArrTime): Stop(p)
	{
		this->ArrTime = ArrTime;
	}
	EndStop(char* name, tm ArrTime): Stop(name)
	{
		this->ArrTime = ArrTime;
	}
	EndStop(const EndStop &s): Stop(s)
	{
		ArrTime = s.ArrTime;
	}
	void setArrTime(tm ArrTime)
	{
		this->ArrTime = ArrTime;
	}
	tm getArrTime() const
	{
		return ArrTime;
	}
	void setDepTime(tm DepTime)
	{
	}
	tm getDepTime() const
	{
		return ArrTime;
	}
	int getStatus() const
	{
		return 1;
	}
};
class InterStop: public StartStop, public EndStop
{
public:
	InterStop(): StartStop(), EndStop()
	{
	}
	InterStop(Point p, tm ArrTime, tm DepTime): StartStop(p, DepTime), EndStop(p, ArrTime)
	{
	}
	InterStop(char* name, tm ArrTime, tm DepTime)
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (strcmp(vPoint[i].getName(), name) == 0)
			{
				p = vPoint[i];
				break;
			}
		}
		this->ArrTime = ArrTime;
		this->DepTime = DepTime;
	}
	InterStop(const InterStop &s): StartStop(s), EndStop(s)
	{
	}
	void setArrTime(tm ArrTime)
	{
		this->ArrTime = ArrTime;
	}
	tm getArrTime() const
	{
		return ArrTime;
	}
	void setDepTime(tm DepTime)
	{
		this->DepTime = DepTime;
	}
	tm getDepTime() const
	{
		return DepTime;
	}
	int getStatus() const
	{
		return 0;
	}
};
/*class Stop
{
private:
	Point p;
	tm ArrTime;
	tm DepTime;
	int status;		// -1 start point, 0 intermediate point, 1 end point
public:
	Stop()
	{
		status = 0;
	}
	Stop(Point p, tm ArrTime, tm DepTime)
	{
		this->p = p;
		this->ArrTime = ArrTime;
		this->DepTime = DepTime;
	}
	Stop(Point p, tm Time, int status)
	{
		this->p = p;
		if(status == -1)
			DepTime = Time;
		else
			ArrTime = Time;
	}
	Stop(const Stop &s)
	{
		p = s.p;
		ArrTime = s.ArrTime;
		DepTime = s.DepTime;
		status = s.status;
	}
	~Stop()
	{
	}
	void setPoint(Point p)
	{
		this->p = p;
	}
	Point getPoint() const
	{
		return p;
	}
	void setArrTime(tm ArrTime)
	{
		this->ArrTime = ArrTime;
	}
	tm getArrTime() const
	{
		return ArrTime;
	}
	void setDepTime(tm DepTime)
	{
		this->DepTime = DepTime;
	}
	tm getDepTime() const
	{
		return DepTime;
	}
	void setStatus(int status)
	{
		this->status = status;
	}
	int getStatus() const
	{
		return status;
	}
	Point searchPoint(char* name) const
	{
		for(int i = 0; i < vPoint.size(); i++)
		{
			if (strcmp(vPoint[i].getName(), name) == 0)
			{
				return vPoint[i];
			}
		}
		return vPoint[0];
	}
};*/
class Route
{
private:
	char* number;
	vector<Stop*> vStop;
public:
	Route()
	{
	}
	Route(const Route&r)
	{
		number = new char [strlen(r.number) + 1];
		strcpy(number, r.number);
		vStop = r.vStop;
	}
	Route(char* number, vector<Stop*> vStop)
	{
		this->number = new char [strlen(number) + 1];
		strcpy(this->number, number);
		this->vStop = vStop;
	}
	~Route()
	{
	}
	void setNumber(char* number)
	{
		this->number = new char [strlen(number) + 1];
		strcpy(this->number, number);
	}
	char* getNumber() const
	{
		return number;
	}
	void addStop(Stop* s)
	{
		vStop.push_back(s);
	}
	vector<Stop*> getRoute() const
	{
		return vStop;
	}
	Stop* getStop(int pos) const
	{
		return vStop[pos];
	}
	int getNStops() const
	{
		return vStop.size();
	}
	Stop* getStart() const
	{
		return vStop[0];
	}
	Stop* getEnd() const
	{
		return vStop[vStop.size() - 1];
	}
	void lastStop()
	{
		Point p = getEnd()->getPoint();
		tm ArrTime = getEnd()->getArrTime();
		vStop.pop_back();
		Stop* s = new EndStop(p, ArrTime);
		vStop.push_back(s);
	}
	void setArr(int pos, tm t)
	{
		vStop[pos]->setArrTime(t);
	}
	void setDep(int pos, tm t)
	{
		vStop[pos]->setDepTime(t);
	}
	Line searchLine(Point p1, Point p2)
	{
		for(int i = 0; i < vLine.size(); i++)
		{
			if((vLine[i].searchPointinLine(p1)) && (vLine[i].searchPointinLine(p2)))
				return vLine[i];
		}
		return vLine[0];
	}
	double SpanDistance(Stop* s1, Stop* s2) const
	{
		int i;
		for(i = 0; i < vLine.size(); i++)
		{
			if((vLine[i].searchPoint(s1->getPoint()) == s1->getPoint()) && (vLine[i].searchPoint(s2->getPoint()) == s2->getPoint()))
				break;
		}
		Line l = vLine[i];
		return l.getDistance(l.getPos(s1->getPoint()), l.getPos(s2->getPoint()));
	}
	Point getPlaceWhile(tm t)
	{
		int i;
		for (i = 0; i < vStop.size() - 1; i++)
		{
			if (t <= vStop[i]->getDepTime())
				break;
		}
		if (i == 0)
			return vStop[i]->getPoint();
		if (vStop[i]->getArrTime() <= t)
			return vStop[i]->getPoint();
		double a = t.tm_hour * 3600 + t.tm_min * 60 + t.tm_sec - vStop[i-1]->getDepTime().tm_hour * 3600 - vStop[i-1]->getDepTime().tm_min * 60 - vStop[i-1]->getDepTime().tm_sec;
		double b = vStop[i]->getArrTime().tm_hour * 3600 + vStop[i]->getArrTime().tm_min * 60 + vStop[i]->getArrTime().tm_sec - vStop[i-1]->getDepTime().tm_hour * 3600 - vStop[i-1]->getDepTime().tm_min * 60 - vStop[i-1]->getDepTime().tm_sec;
		double c = a / b;	// relation of time differences
		Line l = searchLine(vStop[i-1]->getPoint(), vStop[i]->getPoint());	// searching the line needed
		Point p1 = l.searchPoint(vStop[i-1]->getPoint());	// start point
		Point p2 = l.searchPoint(vStop[i]->getPoint());	// end point
		double d = l.getDistance(l.getPos(p1), l.getPos(p2)); // distance between two stops
		double e = c * d;	// distance between stop and current position
		int j = l.getPos(p1);	// pointer to start point
		int k = l.getPos(p2);	// pointer to end point
		double x = 0;
		if (l.getPos(p1) < l.getPos(p2))
		{
			int n = j;	// counter of points
			while (x < e)
			{
				x = l.getDistance(n, j);	// distance between stop and intermediate point
				if (x > e)
				{
					n--;
					x = l.getDistance(n, j);
					break;
				}
				n++;
			}
			double y = e - x;	// distance between intermediate and current point
			double z = y / l.getDistance(n+1, n);
			Point p3 = l.getPoint(n);
			Point p4 = l.getPoint(n+1);
			float lat = p3.getLat() + z * (p4.getLat() - p3.getLat());
			float lon = p3.getLon() + z * (p4.getLon() - p3.getLon());
			return Point(lat, lon, "");
		}
		else
		{
			int n = j;	// counter of points
			while (x < e)
			{
				x = l.getDistance(n, j);	// distance between stop and intermediate point
				if (x > e)
				{
					n++;
					x = l.getDistance(n, j);
					break;
				}
				n--;
			}
			double y = e - x;	// distance between intermediate and current point
			double z = y / l.getDistance(n-1, n);
			Point p3 = l.getPoint(n);
			Point p4 = l.getPoint(n-1);
			float lat = p3.getLat() + z * (p4.getLat() - p3.getLat());
			float lon = p3.getLon() + z * (p4.getLon() - p3.getLon());
			return Point(lat, lon, "");
		}
	}
};

void get_points()
{
	FILE* fp;
	if ((fp = fopen("points.txt", "r")) == NULL)
		cout << "Error while opening the file\n";
	while(feof(fp) == 0)
	{
		char name [30];
		float lat, lon;
		fscanf(fp, "%s\t", name);
		fscanf(fp, "%f\t", &lat);
		fscanf(fp, "%f\n", &lon);
		Point p;
		p.set(lat, lon, name);
		vPoint.push_back(p);
	}
	fclose(fp);
}
void get_lines()
{
	FILE* fp;
	if ((fp = fopen("lines.txt", "r")) == NULL)
		cout << "Error while opening the file\n";
	while(feof(fp) == 0)
	{
		Line l;
		while(1)
		{
			char name [30];
			fscanf(fp, "%s\n", &name);
			if (strcmp(name, "/") == 0)
				break;
			l.setPoint(l.searchPoint(name));
		}
		vLine.push_back(l);
	}
	fclose(fp);
}
void get_routes()
{
	FILE* fp;
	if ((fp = fopen("routes.txt", "r")) == NULL)
		cout << "Error while opening the file\n";
	while(feof(fp) == 0)
	{
//		as.setStatus(-1);
		char number [30];
		fscanf(fp, "%s\n", &number);
		char astop [30];
		fscanf(fp, "%s\t", &astop);
		int dhh, dmm;
		fscanf(fp, "%d:", &dhh);
		fscanf(fp, "%d\n", &dmm);
		tm adep;
		adep.tm_hour = dhh;
		adep.tm_min = dmm;
		Stop* as = new StartStop(astop, adep);
//		as.setPoint(as.searchPoint(astop));
//		as.setDepTime(adep);
		Route r;
		r.setNumber(number);
		r.addStop(as);
		while (1)
		{
			char stop [30];
			fscanf(fp, "%s\t", &stop);
			if (strcmp(stop, "/") == 0)
				break;
			tm arr, dep;
			int hh1, mm1, hh2, mm2;
			fscanf(fp, "%d:", &hh1);
			fscanf(fp, "%d\t", &mm1);
			arr.tm_hour = hh1;
			arr.tm_min = mm1;
			fscanf(fp, "%d:", &hh2);
			fscanf(fp, "%d\n", &mm2);
			dep.tm_hour = hh2;
			dep.tm_min = mm2;
			Stop* s = new InterStop(stop, arr, dep);
//			s.setPoint(s.searchPoint(stop));
//			s.setArrTime(arr);
//			s.setDepTime(dep);
			r.addStop(s);
		}
		r.lastStop();
//		r.getEnd().setStatus(1);
		vRoute.push_back(r);
	}
	fclose(fp);
}
float xstart = 21.2;
float xend = 40.3;
float convert_x(float lon)
{
	return ((lon - xstart) / (xend - xstart)) * 800;
}
float ystart = 44.16;
float yend = 53;
float convert_y(float lat)
{
	return 559 - (((lat - ystart) / (yend - ystart)) * 559);
}
float* xx;
float* yy;
float** xa;
float** xb;
float** ya;
float** yb;
float* xtr;
float* ytr;
char** number;
void dates()
{
	int k = vRoute.size();
	for (int j = 0; j < k; j++)
	{
		char number [30];
		strcmp(number, vRoute[j].getNumber());
		Stop* s1 = new StartStop(vRoute[j].getStart()->getPoint(), vRoute[j].getStart()->getDepTime());
		Route r;
		r.setNumber(number);
		r.addStop(s1);
		for (int l = 1; l < vRoute[j].getNStops() - 1; l++)
		{
			Stop* s2 = new InterStop(vRoute[j].getStop(l)->getPoint(), vRoute[j].getStop(l)->getArrTime(), vRoute[j].getStop(l)->getDepTime());
			r.addStop(s2);
		}
		Stop* s3 = new StartStop(vRoute[j].getEnd()->getPoint(), vRoute[j].getEnd()->getArrTime());
		r.addStop(s3);
		vRoute.push_back(r);
	}
//	vRoute2 = vRoute;
	for (int i = 0; i < k; i++)
	{
		time_t timer;
		time_t cl = time(&timer);
		tm arr = *localtime(&cl);
		tm dep = *localtime(&cl);
		for (int j = 0; j < vRoute[i].getNStops(); j++)
		{
			if (vRoute[i].getStop(j)->getStatus() >= 0)
			{
				arr.tm_hour = vRoute[i].getStop(j)->getArrTime().tm_hour;
				arr.tm_min = vRoute[i].getStop(j)->getArrTime().tm_min;
				if (vRoute[i].getStop(j)->getArrTime().tm_hour < vRoute[i].getStop(j-1)->getDepTime().tm_hour)
				{
					arr.tm_mday++;
					dep.tm_mday++;
				}
				tm arr2 = arr;
				vRoute[i].setArr(j, arr);
				arr2.tm_mday--;
				vRoute[k + i].setArr(j, arr2);
			}
			if (vRoute[i].getStop(j)->getStatus() <= 0)
			{
				dep.tm_hour = vRoute[i].getStop(j)->getDepTime().tm_hour;
				dep.tm_min = vRoute[i].getStop(j)->getDepTime().tm_min;
				if (vRoute[i].getStop(j)->getDepTime().tm_hour < vRoute[i].getStop(j)->getArrTime().tm_hour && vRoute[i].getStop(j)->getStatus() == 0)
				{
					dep.tm_mday++;
					arr.tm_mday++;
				}
				tm dep2 = dep;
				vRoute[i].setDep(j, dep);
				dep2.tm_mday--;
				vRoute[k + i].setDep(j, dep2);
			}
		}
	}
}
tm getTime()
{
	time_t timer;
	time_t cl = time(&timer);
	tm t = *localtime(&cl);
	return t;
}
bool FrameFunc()
{
	float dt=hge->Timer_GetDelta();
	Point* p = new Point [vRoute.size()];
	xtr = new float [vRoute.size()];
	ytr = new float [vRoute.size()];
	number = new char* [vRoute.size()];
	for (int i = 0; i < vRoute.size(); i++)
		number[i] = new char [30];
	tm t = getTime();
	for (int i = 0; i < vRoute.size(); i++)
	{
		p[i] = vRoute[i].getPlaceWhile(t);
		xtr[i] = convert_x(p[i].getLon());
		ytr[i] = convert_y(p[i].getLat());
		strcpy(number[i], vRoute[i].getNumber());
	}
	if (hge->Input_GetKeyState(HGEK_ESCAPE)) return true;
	return false;
}


bool RenderFunc()
{
	hge->Gfx_BeginScene();
	hge->Gfx_Clear(0);
	map->Render(0, 0);
	char hour [10];
	char min [10];
	sprintf(hour, "%d:", getTime().tm_hour);
	sprintf(min, "%d", getTime().tm_min);
	fnt->Render(7, 7, HGETEXT_LEFT, hour);
	fnt->Render(20, 7, HGETEXT_LEFT, min);
	for(int i = 0; i < vPoint.size(); i++)
	{
		point->Render(xx[i], yy[i]);
		text->Render((int)xx[i] + 4, (int)yy[i] - 16, HGETEXT_LEFT, vPoint[i].getName());
	}
	for(int i = 0; i < vLine.size(); i++)
	{
		for(int j = 0; j < vLine[i].getNPoints() - 1; j++)
		{
			hge->Gfx_RenderLine(xa[i][j], ya[i][j], xb[i][j], yb[i][j], 0xFF000000, 1);
		}
	}
	for (int i = 0; i < vRoute.size(); i++)
	{
		if (vRoute[i].getStart()->getDepTime() < getTime() && getTime() < vRoute[i].getEnd()->getArrTime())
		{
			train->Render((int)xtr[i], (int)ytr[i]);
			fnt->Render((int)xtr[i] + 8, (int)ytr[i] - 16, HGETEXT_LEFT, number[i]);
		}
	}
	hge->Gfx_EndScene();

	return false;
}


int WINAPI WinMain(HINSTANCE, HINSTANCE, LPSTR, int)
{
	hge = hgeCreate(HGE_VERSION);

	hge->System_SetState(HGE_LOGFILE, "project");
	hge->System_SetState(HGE_FRAMEFUNC, FrameFunc);
	hge->System_SetState(HGE_RENDERFUNC, RenderFunc);
	hge->System_SetState(HGE_TITLE, "Project");
	hge->System_SetState(HGE_FPS, 100);
	hge->System_SetState(HGE_WINDOWED, true);
	hge->System_SetState(HGE_SCREENWIDTH, 800);
	hge->System_SetState(HGE_SCREENHEIGHT, 559);
	hge->System_SetState(HGE_SCREENBPP, 32);

	if(hge->System_Initiate()) {

	get_points();
	get_lines();
	get_routes();
	dates();
	xx = new float [vPoint.size()];
	yy = new float [vPoint.size()];
	for(int i = 0; i < vPoint.size(); i++)
	{
		xx[i] = convert_x(vPoint[i].getLon());
		yy[i] = convert_y(vPoint[i].getLat());
	}
	xa = new float* [vLine.size()];
	xb = new float* [vLine.size()];
	ya = new float* [vLine.size()];
	yb = new float* [vLine.size()];
	for(int i = 0; i < vLine.size(); i++)
	{
		xa[i] = new float [vLine[i].getNPoints()];
		xb[i] = new float [vLine[i].getNPoints()];
		ya[i] = new float [vLine[i].getNPoints()];
		yb[i] = new float [vLine[i].getNPoints()];
	}
	for(int i = 0; i < vLine.size(); i++)
	{
		for(int j = 0; j < vLine[i].getNPoints() - 1; j++)
		{
			xa[i][j] = convert_x(vLine[i].getPoint(j).getLon());
			ya[i][j] = convert_y(vLine[i].getPoint(j).getLat());
			xb[i][j] = convert_x(vLine[i].getPoint(j+1).getLon());
			yb[i][j] = convert_y(vLine[i].getPoint(j+1).getLat());
		}
	}
		tex = hge->Texture_Load("800px-Map_of_Ukraine_simple_blank.png");
		p = hge->Texture_Load("point.png");
		tra = hge->Texture_Load("train.png");

		map = new hgeSprite(tex, 0, 0, 800, 559);
		map->SetHotSpot(0,0);
		
		point = new hgeSprite(p, 0, 0, 8, 8);
		point->SetColor(0xFFFFFFFF);
		point->SetHotSpot(4, 4);

		train = new hgeSprite(tra, 0, 0, 32, 32);
		train->SetColor(0xFFFFFFFF);
		train->SetHotSpot(16, 16);

		fnt=new hgeFont("font2.fnt");
		fnt->SetColor(0xFFFF8800);

		text = new hgeFont("fontArial.fnt");
		text->SetColor(0xFF000000);

		hge->System_Start();

		delete map;
		delete fnt;
		delete point;
		delete train;
		hge->Texture_Free(tex);
		hge->Texture_Free(tra);
	}

	hge->System_Shutdown();
	hge->Release();
	return 0;
}