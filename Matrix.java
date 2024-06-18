import java.util.Vector;

public class Matrix {
	
	MatrixBase BMatrix;
	
	Matrix(){
		BMatrix = new MatrixBase();
		
		Identity();
	}
	
	public void Identity()	{
		
		BMatrix._11 = 1.0; 	BMatrix._12 = 0.0; 	BMatrix._13 = 0.0;
		BMatrix._21 = 0.0; 	BMatrix._22 = 1.0; 	BMatrix._23 = 0.0;
		BMatrix._31 = 0.0; 	BMatrix._32 = 0.0; 	BMatrix._33 = 1.0;
	}
	
	public void MatrixBaseMultiply(MatrixBase mIn)	{
		MatrixBase mat_temp = new MatrixBase();

		mat_temp._11 = (BMatrix._11*mIn._11) + (BMatrix._12*mIn._21) + (BMatrix._13*mIn._31);
		mat_temp._12 = (BMatrix._11*mIn._12) + (BMatrix._12*mIn._22) + (BMatrix._13*mIn._32);
		mat_temp._13 = (BMatrix._11*mIn._13) + (BMatrix._12*mIn._23) + (BMatrix._13*mIn._33);

		mat_temp._21 = (BMatrix._21*mIn._11) + (BMatrix._22*mIn._21) + (BMatrix._23*mIn._31);
		mat_temp._22 = (BMatrix._21*mIn._12) + (BMatrix._22*mIn._22) + (BMatrix._23*mIn._32);
		mat_temp._23 = (BMatrix._21*mIn._13) + (BMatrix._22*mIn._23) + (BMatrix._23*mIn._33);

		mat_temp._31 = (BMatrix._31*mIn._11) + (BMatrix._32*mIn._21) + (BMatrix._33*mIn._31);
		mat_temp._32 = (BMatrix._31*mIn._12) + (BMatrix._32*mIn._22) + (BMatrix._33*mIn._32);
		mat_temp._33 = (BMatrix._31*mIn._13) + (BMatrix._32*mIn._23) + (BMatrix._33*mIn._33);
				
		BMatrix = mat_temp;		
	}

	public void Translate(double x, double y)	{
		MatrixBase mat = new MatrixBase();

		mat._11 = 1.0; 	mat._12 = 0.0; 	mat._13 = 0.0;
		mat._21 = 0.0; 	mat._22 = 1.0; 	mat._23 = 0.0;
		mat._31 = x;    mat._32 = y;    mat._33 = 1.0;

		MatrixBaseMultiply(mat);
	}

	public void Scale(double xScale, double yScale){
		MatrixBase mat = new MatrixBase();

		mat._11 = xScale;	mat._12 = 0.0; 		mat._13 = 0.0;
		mat._21 = 0.0; 		mat._22 = yScale; 	mat._23 = 0.0;
		mat._31 = 0.0; 		mat._32 = 0.0; 		mat._33 = 1.0;
		
		MatrixBaseMultiply(mat);
	}

	public void Rotate(double rot)	{
		MatrixBase mat = new MatrixBase();

		double Sin = Math.sin(rot);
		double Cos = Math.cos(rot);

		mat._11 = Cos;  	mat._12 = Sin; 		mat._13 = 0.0;
		mat._21 = -Sin; 	mat._22 = Cos; 		mat._23 = 0.0;
		mat._31 = 0.0; 		mat._32 = 0.0;		mat._33 = 1.0;

		MatrixBaseMultiply(mat);
	}

	public void Transform(Vector<Point> vecbuffer)	{
		for (int i=0; i<vecbuffer.size(); ++i){
			double tempX = (BMatrix._11*vecbuffer.get(i).x) + (BMatrix._21*vecbuffer.get(i).y) + (BMatrix._31);
			double tempY = (BMatrix._12*vecbuffer.get(i).x) + (BMatrix._22*vecbuffer.get(i).y) + (BMatrix._32);

			vecbuffer.get(i).x = tempX;
			vecbuffer.get(i).y = tempY;
		}
	}
}
