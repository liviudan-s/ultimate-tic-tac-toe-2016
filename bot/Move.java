package bot;

import java.io.Serializable;

/**
 * Move class
 * 
 * Stores a move.
 * 
 *	Version X codename 'In dev'
 *
 */

public class Move implements Serializable
{
    int X, Y;
	
    public Move() 
    {
    }

    public Move(int x, int y) 
    {
            X = x;
            Y = y;
    }

    public int getX() { return X; }
    public int getY() { return Y; }

    public void setX(int X) 
    {
        this.X = X;
    }

    public void setY(int Y) 
    {
        this.Y = Y;
    }
    
    public String toString()
    {
        return X + " " + Y;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(obj == null)
        {
            return false;
        }
        
        Move m = (Move) obj;
        if(m.getX() == this.X && m.getY() == this.Y)
        {
            return true;
        }
        
        return false;
    }
    
}
