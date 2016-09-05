package microboardevaluation_hashmap_calculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MicroboardEvaluation_HashMap_Calculator class
 * 
 * x
 * 
 *	Version X codename 'In dev'
 *
 * @author ItworksWhy Team
 */
public class MicroboardEvaluation_HashMap_Calculator 
{
    private static final int[][] winCases = {
                                             {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                                             {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                                             {0, 4, 8}, {2, 4, 6} 
                                            };
    private static final int[][] heuristicWeights = {
                                                     {0, -1, -10},
                                                     {1, 0, 0},
                                                     {10, 0, 0},
                                                    };
    
    public static void doBKT(int[] board, int place, HashMap<Integer, Integer> hashmap) 
    {
        if(place >= 9)
        {
            return;
        }
        
        for(int i = 0 ;i <= 2; i++)
        {
            board[place] = i;
            int value = evaluation(board);
            int microboard = boardToInt(board);
            if(value == 0)
            {
                int count = 0;
                for(int j = 0; j < 9; j++)
                {
                    if(board[j] != 0)
                    {
                        count++;
                    }
                }
                int ended = count >= 9 ?  1 : 0;
                
                if(ended == 1)
                {
                    hashmap.put(microboard, -1000);
                }
                else
                {
                    hashmap.put(microboard, 0);
                }
            }
            else
            {
                hashmap.put(microboard, value);
            }
            /*if(value != 100 && value != -100)
            {*/
                doBKT(board, place + 1, hashmap);
            //}
        
        }
        board[place] = 0;

    }
    
    public static int evaluation(int[] board)
    {
        int playerPieces = 0, opponentPieces = 0, piece, result = 0;

        for(int i = 0; i < 8; i++)
        {
            playerPieces = 0;
            opponentPieces = 0;
            for(int j = 0; j < 3; j++)
            {
                piece = board[winCases[i][j]];
                if(piece == 1)
                    playerPieces++;
                else if(piece == 2)
                    opponentPieces++;
            }
            if(playerPieces == 3)
            {
                return 100;
            }
            if(opponentPieces == 3)
            {
                return -100;
            }
            result += heuristicWeights[playerPieces][opponentPieces];
        }

        return result;
    }
    
    public static int boardToInt(int board[])
    {
        int microboard = 0;
        
        for(int i = 0; i < 9; i++)
        {
            int place = i * 2, value = board[i];
        
            if(value != 0)
            {
                if(value == 2)
                {
                    place++;
                }

                microboard |= 1 << place;
            }
        }
        
        return microboard;
    }
    
    public static void main(String[] args) 
    {
        int[] board = new int[9];
        for(int i = 0; i < 9; i++)
        {
            board[i] = 0;
        }
        HashMap<Integer, Integer> hashmap = new HashMap<>();
        
        MicroboardEvaluation_HashMap_Calculator.doBKT(board, 0, hashmap);
        
        
        try
        {
            File fileOne=new File("microboard_evaluations_hashmap");
            FileOutputStream fos=new FileOutputStream(fileOne);
            ObjectOutputStream oos=new ObjectOutputStream(fos);

            oos.writeObject(hashmap);
            oos.flush();
            oos.close();
            fos.close();
        }
        catch(Exception e){}
        
        try{
            File toRead=new File("microboard_evaluations_hashmap");
            FileInputStream fis=new FileInputStream(toRead);
            ObjectInputStream ois=new ObjectInputStream(fis);

            HashMap<Integer, Integer> hashmap2 =(HashMap<Integer, Integer>)ois.readObject();

            ois.close();
            fis.close();
            if(hashmap.equals(hashmap2))
            {
                System.out.println("Yay, momentary success! ( :");
            }
            else
            {
                System.out.println("neggative");
            }
            
            /*if(hashmap2.containsKey(0))
                hashmap2.put(0, new Move(35, 0));*/
            //hashmap2.put(0, 37);
            for(Map.Entry<Integer, Integer> m : hashmap2.entrySet())
            {
                if(m.getKey() == 161297)
                    System.out.println(m.getKey()+" : "+m.getValue());
                
            }
        }catch(Exception e){}
    }
    
}
