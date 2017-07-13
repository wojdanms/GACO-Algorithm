package MGA;
/**
 *  MGA.java
 * @author AL-Saeedan
 */

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// The Import
import static MGA.Public_var.*; 
import static WSD.Public_var.ResultOfPre;
import static WSD.Public_var.indexresult;
import java.util.*;
import net.sf.extjwnl.JWNLException;
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// MGA Class
public class MGA {

   
    // Variables declaration
    /** ************************************************************ */
    Population P_POP;
    int pc=1,pm=2;
    public double maxfitness;
    public double RunAverage;
    int run;
    ArrayList<Generation> resultofgeneration= new ArrayList<>();

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Constructor
public MGA(int num, int TInt) throws JWNLException, CloneNotSupportedException
{
    Num_Genes=num;
    indextarget=TInt;
    targetsenses =new ArrayList[ResultOfPre.size()];
    Numnode=0;
    for(int i=0;i<ResultOfPre.size();i++)
       {
           targetsenses[i] = new ArrayList<String>();
           targetsenses[i].addAll(ResultOfPre.get(i).Gloss);
           Numnode=Numnode+ targetsenses[i].size();

       }
    Begin_MGA();
}

// Begin_MGAFunction: start MGA
void Begin_MGA() throws JWNLException, CloneNotSupportedException
{
        resultofgeneration.clear();
        P_POP = new Population();
        P_POP.RunTMMAS();
        P_POP.Set_Max_Ave();
        Set_resultofgeneration();
        int Iteration=1;
        int Stop = Population_Size;
        while (Iteration<= NumIterations_toStop) 
        {
            
            // --- Variabe Declearation ---
            Vector<Individual> P_T = new Vector<>();
            Vector<Individual> TMP_POP = new Vector<>();
            Vector<Individual> TMP = new Vector<>();
            
            //----Elitist Survivor Strategy.
            int Survivor=(int) (Elitist_Survivor*Population_Size);
            TMP_POP.addAll(P_POP.Elitist_Survivor(Survivor));
            int Therest=Population_Size-Survivor;
            
             //----- Roulette_Well_Selection to select the parents.
               P_POP.Parents_Selection_pool(Therest);
            
            //----- Perform Crossover1Point and UniformMutation.
            int i = 0;
            while (i < Therest) {
                
                TMP.clear();
                P_T.clear();
                Individual Parent1=new Individual(P_POP.Parent_Selection());
                Individual Parent2=new Individual(P_POP.Parent_Selection());
                TMP.add(Parent1);
                TMP.add(Parent2);
                P_POP.pselection=-1;
                
                //----- Perform crossover.
                // ----- Call Mutation with a probability Crossover_Probability.
                //Set crossover probability
                double FmaxParent;
                if(Parent1.Get_fitness()>Parent2.Get_fitness())
                    FmaxParent=Parent1.Get_fitness();
                else
                    FmaxParent=Parent2.Get_fitness();
                Set_Crossover_Probability(FmaxParent);
                
                //.,.,.,.,.,.,.,.,.,.
                Random cgenerator = new Random();
                double PROBCROSSOVER = cgenerator.nextDouble();
                if (PROBCROSSOVER <= Crossover_Probability) {
                    // ----- If crossover is performed, put the new individual in P_T.
                    if(pc==1)
                        P_T.addAll(P_POP.Crossover1Point(Parent1, Parent2));
                    if(pc==2)
                        P_T.addAll(P_POP.UniformCrossover(Parent1, Parent2));
                }
                else
                {
                    //----- otherwise, put the individual picked in P_T
                    P_T.addAll(TMP);
                }
                    
                    //----- Perform Mutation.
                    for (int m = 0; m < P_T.size(); m++) {
                        
                        // ----- Call Mutation with a probability Mutation_Probability.
                        //Set mutation probability
                        Set_Mutation_Probability(P_T.elementAt(m).Fitness_FT);
                        //,.,.,.....,,,,..,,,..,,,
                        Random mgenerator = new Random();
                        double PROBMUTATION = mgenerator.nextDouble();
                        if (PROBMUTATION <= Mutation_Probability) 
                        {
                            // ----- If Mutation is performed, put the new individual in TMP_POP.
                            if(pm==1)
                                TMP_POP.addElement(P_POP.Mutation1Point_MMAS(P_T.elementAt(m)));
                            if(pm==2)
                                TMP_POP.addElement(P_POP.UniformMutation_MMAS(P_T.elementAt(m)));
                        } 
                        else 
                        {
                            //----- otherwise, put the individual picked in TMP_POP
                            TMP_POP.addElement(P_T.elementAt(m));
                        }
                    }
                    i = i + 2;    
            }
                P_POP.Generational(TMP_POP);
                P_POP.RunTMMAS();
                P_POP.Set_Max_Ave();
                Set_resultofgeneration();
                Iteration++;
        }
        Run_Max_Ave();
}// End Begin_MGA  Function

//.,.,.,.,.,.,..,,.....,,,...,,
//Set_Crossover_Probabilit Function: Set crossover probability
void Set_Crossover_Probability(double maxp)
{
    if(maxp>=P_POP.average && P_POP.average!=0)
    {
        pc=1;
        if(P_POP.Max_Fitness!=P_POP.average)
        {
            Crossover_Probability=(P_POP.Max_Fitness-maxp)/(P_POP.Max_Fitness-P_POP.average);
            if(Crossover_Probability>1)
                Crossover_Probability=1.0;
            if(Crossover_Probability<0)
                Crossover_Probability=0.0;
        }
        else
            Crossover_Probability=0.0;
    }
    else
    {
        pc=2;
        Crossover_Probability=1.0;
    }
        
}//End Set_Crossover_Probabilit Function

//,.,.....,,,.....,,,,,,..
//.,.,.,.,.,.,..,,.....,,,...,,
//Set_Mutation_Probability Function: Set Mutation probability
void Set_Mutation_Probability(double fitness)
{
    if(fitness>=P_POP.average && P_POP.average!=0)
    {
        pm=1;
        if(P_POP.Max_Fitness!=P_POP.average)
        {
            Mutation_Probability=0.50*(P_POP.Max_Fitness-fitness)/(P_POP.Max_Fitness-P_POP.average);
            if(Mutation_Probability>1)
                Mutation_Probability=1.0;
            if(Mutation_Probability<0)
                Mutation_Probability=0.0;
        }
        else
            Mutation_Probability=0.0;
    }
    else
    {
        pm=2;
        Mutation_Probability=0.50;
    }
}//End Set_Mutation_Probability Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Set_resultofgeneration Function: set the final result of generation
void Set_resultofgeneration()
{
    
    int temp,j=1;
    Generation group=new Generation();
    group.ind=P_POP.Max_Fitness;
    group.Best= new float[Num_Genes];
    group.indexresult.addAll(P_POP.POP.get(MAX).indexIn);
    group.GIter=P_POP.POP.get(MAX).Iter;
    for(int i=0;i<Num_Genes;i++)
        group.Best[i]=P_POP.POP.get(MAX).Individual[i];
    
     for(int s=(MAX+1);s<Population_Size;s++)
     {
         if(P_POP.Max_Fitness==P_POP.POP.get(s).Get_fitness())
         {
             group.GIter=group.GIter+P_POP.POP.get(s).Iter;
             for(int b=0;b<Num_Genes;b++)
                 group.Best[b]=group.Best[b]+P_POP.POP.get(s).Individual[b];
             
            for(int ind=0;ind<P_POP.POP.get(s).indexIn.size();ind++)
            {
                temp=P_POP.POP.get(s).indexIn.get(ind);
                if(!group.indexresult.contains(temp))
                {
                    group.indexresult.add(temp);
                }
            }
            j++;
         }
     }
     for(int bg=0;bg<Num_Genes;bg++)
         group.Best[bg]=group.Best[bg]/j;
     group.GIter=group.GIter/j;
     resultofgeneration.add(group);

}// End Set_resultofgeneration function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Run_Max_Ave Function:  Set MAX for the run
//and calculate the average and standard deviation from max fitness for all generation
void Run_Max_Ave()
{
    maxfitness = 0;
    int index;
    double temp;
    int sum = 0;
    index=(resultofgeneration.size()-1);
    maxfitness = resultofgeneration.get(index).ind;
    sum += maxfitness;
    for (int m = 1; m < resultofgeneration.size(); m++) {
            temp = resultofgeneration.get(m).ind;
            sum += temp;
        }
        indexresult.addAll(resultofgeneration.get(index).indexresult);
        //
        RunAverage=((double) sum /resultofgeneration.size());
}//End Run_Max_Ave Function

}//End class
