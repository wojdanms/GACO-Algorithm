 package MGA;
/*
 * Individual.java
 *
 */

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// The Import
import static MGA.Public_var.*; 
import java.util.*;
import static WSD.Public_var.*;
import net.sf.extjwnl.JWNLException;

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Individual Class
class Individual  {

    // Variables declaration
    float  Fitness_FT=0;
    public float Individual[];
    public int remove=0;
    public float LBtour=(float) 0.0;
    public int Iter=0;
    public ArrayList<Integer> indexIn=new ArrayList<Integer>();
    

 
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
 // Constructor
Individual() throws JWNLException, CloneNotSupportedException
{
    Individual= new float[Num_Genes];
    Create_Individual_Randomly_MMAS();
    Calc_fitness();
}
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
 // Constructor
Individual(Individual copy)
{
    Individual= new float[Num_Genes];
    for(int i=0;i<Num_Genes;i++)
        Individual[i]=copy.Individual[i];
    Fitness_FT=copy.Get_fitness();
    remove=copy.remove;
    LBtour=copy.LBtour;
    Iter=copy.Iter;
    indexIn=new ArrayList<Integer>();
    indexIn.addAll(copy.indexIn);
}

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Create_Individual_Randomly_MMAS Function: creat randomly individual
void Create_Individual_Randomly_MMAS()
{
    Random randomGenerator = new Random();
    //m
    Individual[0]=1+(randomGenerator.nextInt((2*Numnode)));
    //alpha
    Individual[1]=randomGenerator.nextInt(6);
    //beta
    Individual[2]=randomGenerator.nextInt(11);
    //lambda
    Individual[3]=randomGenerator.nextInt(11);
    //evap
    Individual[4]=(float) (((float)randomGenerator.nextInt(101))/100.0);
    //Q
    Individual[5]=randomGenerator.nextInt(101);
 
}// End Create_Individual_Randomly function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Calc_fitness Function: calculate the fitness value of the individual
public void Calc_fitness()
{
    Calc_fitness_P();
   
}//End Calc_fitness Function

//.,.,.,.,,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,..
//Calc_fitness_P Function
void Calc_fitness_P()
{
    float num=(float) 0.0;
    for(int i=0;i<ResultOfPre.size();i++)
        num=num+ResultOfPre.get(i).Gloss.size();
    Fitness_FT=0;
    double F1;
        F1=2.5*(1/((GBtour+1)-LBtour));
    Fitness_FT=(float) (Fitness_FT+F1);
    double F2=0.5*(Math.exp(-(Iter/(5*num))));
    Fitness_FT=(float) (Fitness_FT+F2);
    double F3=0.5*(Math.exp(-(Individual[0]/(10*num))));
    Fitness_FT=(float) (Fitness_FT+F3);
}//End Calc_fitness_P Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Get_fitness Function: Return the fitness for Individual
public float Get_fitness()
{
    return Fitness_FT;
}// End Get_fitness function


//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Print_Individual Function: Print the Individual
void Print_Individual()
{
    System.out.println("\nIndividual:  ");
   for(int w=0; w<Num_Genes; w++)
    {
           System.out.print(" "+ Individual[w]);
    }
    System.out.println("\n Individual Fittnes: "+ Fitness_FT);
    System.out.println("\n-----------------------------");

}//End Print_Individual Function


}// End class

