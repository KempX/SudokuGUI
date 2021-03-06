import java.util.*;

public class Score {
    private Map<Integer, Integer> scores = new HashMap();

    public Score(int size){
        scores.clear();
        for(int i = 1; i < (size + 1); i++) {
            scores.put(i, 0);
        }
    }

    public void reset(int size){
        scores.clear();
        for(int i = 1; i < (size + 1); i++) {
            scores.put(i, 0);
        }
    }

    public void increaseScore (int key){
        if(key != 0){
            int count = scores.get(key);
            scores.put(key, count + 1);
        }
    }

    public int getScore (int key){
        return scores.get(key);
    }
}