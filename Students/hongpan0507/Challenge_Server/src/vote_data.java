/**
 * Created by hpan on 2/6/15.
 */
public class vote_data {
    private int[] vote;

    //constructor
    public vote_data (int[] _vote) {
        vote = _vote;
    }

    public int result () {
        int maxKey = 0;
        int maxCounts = 0;
        int[] counts = new int[vote.length];

        for (int i=0; i < vote.length; i++) {
            counts[vote[i]]++;
            if (maxCounts < counts[vote[i]]) {
                maxCounts = counts[vote[i]];
                maxKey = vote[i];
            }
        }
        return maxKey;
    }
}
