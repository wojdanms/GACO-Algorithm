package MGA;
/*
 * public_var.java
 *
 */
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// The Import
//import AWN.*;
import TextAnalyzer.*;
import WSD.Senses;
import java.util.*;

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// genration Class
 class Generation {
    public double ind; // value of max fitness
    public ArrayList<String> sense=new ArrayList<>(); // senses that selected for target word/s
    public ArrayList<Integer> indexresult=new ArrayList<>();
    public int GIter; 
    public float Best[];
  }//end class
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^


//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Public_var Class
public class Public_var {

    // Public Variables declaration
 //*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
    public static int Num_Genes;
    public static int MAX;
    public static ArrayList<String> targetsenses [];
    
  //*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
  //Parameters
    public static int Population_Size=50;
    public static int Numnode;
    public static double Crossover_Probability;
    public static double Mutation_Probability;
    public static int NumIterations_toStop=100;
    public static double K=0.40;
    public static double Elitist_Survivor=0.12;
    public static float GBtour=(float) 0.0;
    public static int indextarget;
    
}
// End Class