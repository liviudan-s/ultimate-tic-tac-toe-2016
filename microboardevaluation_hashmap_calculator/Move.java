/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microboardevaluation_hashmap_calculator;

import java.io.Serializable;

/**
 *
 * @author liviu
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
    
/*int counter = 0;
        for(int i = 0; i < 4; i++)
        {
            if((microboard[m.getX()] & (1 << (i + 18))) != 0)
            {
               counter |= 1 << i; 
            }
        }
        
        counter++;
        
        for(int i = 0; i < 4; i++)
        {
            int counterBit = counter & (1 << i);
            if(counterBit != 0 && (microboard[m.getX()] & (1 << (i + 18))) == 0)
            {
               microboard[m.getX()] |= 1 << (i + 18); 
            }
            else if (counterBit == 0 && (microboard[m.getX()] & (1 << (i + 18))) != 0)
            {
                microboard[m.getX()] &= ~(1 << (i + 18));
            }
        }
        
        if(counter == 9)
        {
            this.macroboardEnded |= 1 << m.getX();
        }*/
}

