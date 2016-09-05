/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

/**
 * MoveWithValue class
 * 
 *  Move + Value of the piece in that square
 * 
 *	Version X codename 'In dev'
 *
 */
public class MoveWithValue 
{
    Move move;
    int value;

    public MoveWithValue(Move move, int value) 
    {
        this.move = move;
        this.value = value;
    }

    public Move getMove() 
    {
        return move;
    }

    public void setMove(Move move) 
    {
        this.move = move;
    }

    public int getValue() 
    {
        return value;
    }

    public void setValue(int value) 
    {
        this.value = value;
    }
    
}
