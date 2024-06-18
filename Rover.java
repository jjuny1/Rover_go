import java.util.Vector;

public class Rover {
	
	public double 	PI			= 3.14159265358979;
	public double 	TolerFloat 	= 0.00009;

	public int 		ROVER_SCALE = 10;

	/////////////////////////////////////////////////
	double	        Rotation;
	double			scale; 
	Point 			Position; 
	
	int 					NumRoverVt	= 8;
	private Point 			roverVts[];
	Vector<Point>   		RoverTransVB; 
	int 					NumSensorVt	= 2;
	private Point 			sensorVts[];
	Vector<Point>   		SensorTransVB;
		
	Rover(Point startpos) {
		
		Rotation 	= PI;		
		scale 		= ROVER_SCALE;
		Position 	= new Point(startpos.x, startpos.y);
		
		RoverTransVB = new Vector<Point>();
		SensorTransVB = new Vector<Point>();
		
		roverVts = new Point[NumRoverVt];
		for(int i=0; i < NumRoverVt; ++i){
			roverVts[i] = new Point(0, 0);
		}
		
		sensorVts = new Point[NumSensorVt];
		for(int i=0; i < NumSensorVt; ++i){
			sensorVts[i] = new Point(0, 0);
		}
		
		roverVts[0].x= 1.0;roverVts[0].y= 1.0;
		roverVts[1].x= 1.0;roverVts[1].y= -1.0;
		roverVts[2].x= -1.0;roverVts[2].y= -1.0;
		roverVts[3].x= -1.0;roverVts[3].y= 1.0;
		roverVts[4].x= -0.25;roverVts[4].y= 1.0;
		roverVts[5].x= -0.25;roverVts[5].y= 1.75;
		roverVts[6].x= 0.25;roverVts[6].y= 1.75;
		roverVts[7].x= 0.25;roverVts[7].y= 1.0;
		
		sensorVts[0].x=0;sensorVts[0].y=0.5;		
		sensorVts[1].x=0;sensorVts[1].y=2.75;

		for(int i=0; i < NumRoverVt; ++i){  
			RoverTransVB.add(new Point(0, 0));
		}
		
		for(int i=0; i < NumSensorVt; ++i){ 
			SensorTransVB.add(new Point(0, 0));
		}
	}
	
	public void moveRover(double TimeElapsed, VectorArray obvts[]) {	
		boolean Isec = false;
		for(int i=0; i<obvts.length; ++i){
			for(int j=0; j<obvts[i].Size; j=j+2){
				Isec = LineIntersection(SensorTransVB.get(0), SensorTransVB.get(1), obvts[i].TheVec.get(j), obvts[i].TheVec.get(j+1));
				if(Isec) {
					i = obvts.length;
					Rotation += PI/2; 
					Isec = false;
					if (Rotation<0)	{
						Rotation += 2*PI;
					}
					if (Rotation>2*PI)	{
						Rotation -= 2*PI;
					}
					break;
				}
			}
		}
		
		//update the rover's position
		if( ((PI - TolerFloat) <= Rotation) && (Rotation <= (PI + TolerFloat)) ){	// heading north
			Position.y = Position.y - TimeElapsed*20;
		}
		else if( ( (-TolerFloat <= Rotation) && (Rotation <= TolerFloat) ) || 
				 ( ((2*PI - TolerFloat) <= Rotation) && (Rotation <= (2*PI + TolerFloat)) ) ) {	// heading south
			Position.y = Position.y + TimeElapsed*20;
		}
		else if( ((PI/2 - TolerFloat) <= Rotation) && (Rotation <= (PI/2 + TolerFloat)) ){	// heading east
			Position.x = Position.x + TimeElapsed*20;
		}
		else if( ((3*PI/2 - TolerFloat) <= Rotation) && (Rotation <= (3*PI/2 + TolerFloat)) ){	// heading west
			Position.x = Position.x - TimeElapsed*20;
		}
		else{ 
		}
		
		for(int i=0; i < NumRoverVt; ++i){
			RoverTransVB.get(i).x = roverVts[i].x;
			RoverTransVB.get(i).y = roverVts[i].y;
		}
		for(int i=0; i < NumSensorVt; ++i){
			SensorTransVB.get(i).x = sensorVts[i].x;
			SensorTransVB.get(i).y = sensorVts[i].y;
		}
		
		WorldTransform(RoverTransVB);
		WorldTransform(SensorTransVB);
	}
	
	public void WorldTransform(Vector<Point> vecbuffer){
		Matrix matTransform = new Matrix();
		
		matTransform.Scale(scale, scale);

		matTransform.Rotate(-Rotation);

		matTransform.Translate(Position.x, Position.y);

		matTransform.Transform(vecbuffer);
	}

	public boolean LineIntersection(Point A, Point B, Point C, Point D){
		double rTop = (A.y-C.y)*(D.x-C.x)-(A.x-C.x)*(D.y-C.y);
		double rBot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

		double sTop = (A.y-C.y)*(B.x-A.x)-(A.x-C.x)*(B.y-A.y);
		double sBot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

		if ( (rBot == 0) || (sBot == 0) ) { 
			return false;
		}

		double r = rTop/rBot;
		double s = sTop/sBot;

		if( (r > 0) && (r < 1.0f) && (s > 0) && (s < 1.0f) )  {
			return true;
		}
		else  {
			return false;
		}
	}
}
