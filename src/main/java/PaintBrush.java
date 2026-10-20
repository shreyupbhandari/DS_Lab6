import java.util.Stack;
import javafx.scene.paint.Color;

public class PaintBrush
{
    Paint paint;

    enum BrushMode {
        pointMode, fillMode, pattern1Mode, pattern2Mode
    }

    BrushMode mode;
    Paint Gold = new PaintColor(Color.GOLD);
    Paint White = new PaintColor(Color.WHITE);
    Paint Green = new PaintColor(Color.GREEN);
    Paint Black = new PaintColor(Color.BLACK);
    

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public void setBrighter()
    {
        paint = new PaintBrighter(paint);
    }

    public void setDarker()
    {
        paint = new PaintDarker(paint);
    }

    public void paint(int x, int y, Paint[][] mesh)
    {
    	
	    if(mode==BrushMode.pointMode)
	       {
	    		mesh[x][y]=paint;
	    		return;
	       }
    
	    Paint original=mesh[x][y]; //Keeping track of what the original paint is for eligibility check later on.
	    boolean[][] visited = new boolean[mesh.length][mesh[0].length];
	    
	    if (mode==BrushMode.fillMode)
		    {
		    	recursiveFill(x,y,mesh,visited,original,false);//The pattern mode will be false in this case.
		    }
	    if (mode==BrushMode.pattern1Mode)
		    {
		    	recursiveFill(x,y,mesh,visited,original,true);//The pattern mode will be true in this case.
		    }
	    if (mode==BrushMode.pattern2Mode)
		    {
		    	stackFill(x, y, mesh, visited, original);
		    }
	    
    }
    
    public boolean inBounds(int x, int y, Paint[][] mesh)
    {
    	return (x>=0)
    			&&(y>=0)
    			&&(x<mesh.length)
    			&&(y<mesh[0].length);
    }
    public boolean eligible(int x, int y, Paint[][] mesh, boolean[][] visited, Paint original)
    {
    	return inBounds(x,y,mesh)
    			&&!(visited[x][y])
    			&&(mesh[x][y]!=null)
    			&&mesh[x][y].equals(original);
    }
    
    public void recursiveFill(int x, int y, Paint[][] mesh, boolean[][]visited, Paint original, boolean patternMode)
    {
    	if(!eligible(x, y, mesh, visited, original))
    	{
    		return;
    	}
    	
    	visited[x][y]=true;
    	
    	if(patternMode) //When patternMode is true, the pixels will be separated.
    	{
    		if (x%2==0)
    		{
    			mesh[x][y]=Gold;
    		}
    		else
    		{
    			mesh[x][y]=White;
    		}
    	}
    	
    	else
    	{
    		mesh[x][y]=paint;
    	}
    	
    	recursiveFill(x+1, y, mesh, visited, original, patternMode);
    	recursiveFill(x-1, y, mesh, visited, original, patternMode);
    	recursiveFill(x, y+1, mesh, visited, original, patternMode);
    	recursiveFill(x, y-1, mesh, visited, original, patternMode);
    	
    }
    
    public void stackFill(int x, int y, Paint[][] mesh, boolean[][] visited, Paint original)
    {
    	Stack<int[]> coordinateStack = new Stack<>();
    	pushInStack(x, y, mesh, visited, original, coordinateStack);
    	
    	while (!coordinateStack.isEmpty())
    	{
    		int[] popValue=coordinateStack.pop();
    		int px=popValue[0];
    		int py=popValue[1];
    		
    		if ((px+py)%2==0)
    		{
    			mesh[px][py]=Black;
    		}
    		else
    		{
    			mesh[px][py]=White;
    		}
    		
    		pushInStack(px+1, py, mesh, visited, original, coordinateStack);
    		pushInStack(px-1, py, mesh, visited, original, coordinateStack);
    		pushInStack(px, py+1, mesh, visited, original, coordinateStack);
    		pushInStack(px, py-1, mesh, visited, original, coordinateStack);
    	}
    }
    
   public void pushInStack(int x, int y, Paint[][] mesh, boolean[][] visited, Paint original, Stack<int[]>pattern)
   {
	   if(eligible(x, y, mesh, visited, original))
	   {
		   visited[x][y]=true;
		   int[] pushing = new int[] {x,y};
		   pattern.push(pushing);
	   }
   }

    public void pointMode()
    {
        mode = BrushMode.pointMode;
    }

    public void fillMode()
    {
        mode = BrushMode.fillMode;
    }

    public void pattern1Mode()
    {
        mode = BrushMode.pattern1Mode;
    }

    public void pattern2Mode()
    {
        mode = BrushMode.pattern2Mode;
    }
}
