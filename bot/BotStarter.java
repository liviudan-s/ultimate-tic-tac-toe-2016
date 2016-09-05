package bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;



/**
 * BotStarter class
 * 
 * 'Magic happens here.'
 * 
 *	Version X codename 'In dev'
 *
 */

public class BotStarter 
{
    public static final int INF = 65535;
    private HashMap<Integer, Integer> microboard_evaluations_hashmap;
    private static final int[][] winCases = {
                                             {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                                             {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                                             {0, 4, 8}, {2, 4, 6} 
                                            };
    private static int botId;
    private static int enemyId;

    public BotStarter() 
    {
        this.microboard_evaluations_hashmap = new HashMap<>();
    }

    public void setMicroboard_evaluations_hashmap
        (HashMap<Integer, Integer> microboard_evaluations_hashmap) 
        
    {
        this.microboard_evaluations_hashmap = microboard_evaluations_hashmap;
    }

    public HashMap<Integer, Integer> getMicroboard_evaluations_hashmap() {
        return microboard_evaluations_hashmap;
    }
        

    public static void setBotId(int botId) 
    {
        BotStarter.botId = botId;
    }

    public static void setEnemyId(int enemyId) 
    {
        BotStarter.enemyId = enemyId;
    }

    public static int getBotId() {
        return botId;
    }

    public static int getEnemyId() {
        return enemyId;
    }
    
    public int evaluate(Field field)
    {
        int result = 0, opositeValue = 1;
        int[] macroboard = new int[9];
        if(BotStarter.botId == 2)
        {
            opositeValue = -1;
        }
        
        for(int i =0; i < 9; i++)
        {
            int microboard = field.getMicroboard(i);
            int init_value = microboard_evaluations_hashmap.get(microboard);
            if(init_value == -1000)
            {
                init_value = 0;
            }
            int value = opositeValue * init_value;
            macroboard[i] = value;
        }
        
        for(int i = 0; i < 8; i++)
        {
            int partialResult = 0;
            int friendlyMicrosWon = 0, enemyMicrosWon = 0;
            for(int j = 0; j < 3; j++)
            {
                if(macroboard[winCases[i][j]] == 100)
                {
                    friendlyMicrosWon++;
                }
                if(macroboard[winCases[i][j]] == -100)
                {
                    enemyMicrosWon++;
                }
                partialResult += macroboard[winCases[i][j]];
            }
            
            if(friendlyMicrosWon > 0 && enemyMicrosWon > 0)
            {
                partialResult = 0;
            }
            else
            {
                if(friendlyMicrosWon == 3)
                {
                    result = INF;
                    break;
                }
                
                if(enemyMicrosWon == 3)
                {
                    result = -INF;
                    break;
                }
            }
            
            result += partialResult;
        }
        
        return result;
    }
    
    int alphabeta(Field field, int depth, int alpha, int beta, int player)
    {   
        int value, bestvalue;
        
        if(field.hasEnded() || depth == 0)
        {
            return this.evaluate(field);
        }
        
        ArrayList<Move> availableMoves = field.getAvailableMoves();
        if(player == BotStarter.botId)
        {
            bestvalue = alpha;
            for(Move m : availableMoves)
            {
                Field newField = new Field(field, m, player, this.microboard_evaluations_hashmap);
                value = alphabeta(newField, depth - 1, bestvalue, beta, BotStarter.enemyId);
                if(value > bestvalue)
                {
                    bestvalue = value;
                }
                if(bestvalue > beta)
                {
                    return beta;
                }
            }
            return bestvalue;
        }
        else
        {
            bestvalue = beta;
            for(Move m : availableMoves)
            {
                Field newField = new Field(field, m, player, this.microboard_evaluations_hashmap);
                value = alphabeta(newField, depth - 1, alpha, bestvalue, BotStarter.botId);
                if(value < bestvalue)
                {
                    bestvalue = value;
                }
                if(bestvalue < alpha)
                {
                    return alpha;
                }
            }
            return bestvalue;
        }
    }
     
    /**
     * Makes a turn.
     *
     * @return The move that was made.
     */
    public Move makeTurn(Field field) 
    {
        Move move = null;
        long time_init = System.currentTimeMillis();

        ArrayList<Move> availableMoves = field.getAvailableMoves();
        
        if(field.moveNr == 1)
        {
            move = new Move(4,4);
            field.placeMove(move, field.getBotId());
            return Field.transformIndex(move.getX(), move.getY());
        }
        
        int bestvalue = -INF, beta = INF, value;
        for(Move m : availableMoves)
        {
            Field newField = new Field(field, m, BotStarter.botId, this.microboard_evaluations_hashmap);
            int depth = 5;
            value = alphabeta(newField, depth, bestvalue, beta, BotStarter.enemyId);
            /*Move Tmove = Field.transformIndex(m.getX(), m.getY());
            System.out.println("Move: " + Tmove.getY() + " " + Tmove.getX() + " value: " + value);*/
            if(move == null)
            {
                move = m;
            }
            if(value > bestvalue)
            {
                bestvalue = value;
                move = m;
            }
        }
        
        field.placeMove(move, field.getBotId());
        int microboardValue = this.microboard_evaluations_hashmap.get
                                            (field.getMicroboard(move.getX()));
        if(microboardValue == -1000 || microboardValue == 100 
                || microboardValue == -100)
        {
            int macroboardEnded = field.getMacroboardEnded() | (1 << move.getX());
            field.setMacroboardEnded(macroboardEnded);
        }
        
        
        System.err.println(System.currentTimeMillis() - time_init);
        return Field.transformIndex(move.getX(), move.getY());
    }


    public static void main(String[] args) 
    {     
        BotParser parser = new BotParser(new BotStarter());
        try{
            File toRead=new File("./src/microboard_evaluations_hashmap");
            FileInputStream fis=new FileInputStream(toRead);
            ObjectInputStream ois=new ObjectInputStream(fis);

            parser.getBot().setMicroboard_evaluations_hashmap
                                ((HashMap<Integer, Integer>)ois.readObject());
            ois.close();
            fis.close();
        }
        catch(Exception e)
        {
            return;
        }
        parser.run();
    }
}
