package MGA;
/*
 * Population.java
 *
 */
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// The Import
import static MGA.Public_var.*;
import TACS.*;
import static WSD.Public_var.ResultOfPre;
import java.util.*;
import net.sf.extjwnl.JWNLException;

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Population Class
class Population
{
 // Variables declaration
  double Sum_Fitness;
  public Vector<Individual> POP= new Vector<Individual>();// The Population
  Vector<Integer> Par_POP= new Vector<Integer>(); //Parents population
  public double  average, Max_Fitness;
  public int pselection=-1;

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Constructor
Population() throws JWNLException, CloneNotSupportedException
{
    Generate_Population();
}

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Generate_Population Function: Generate the Population
void Generate_Population() throws JWNLException, CloneNotSupportedException
{
    for(int i=0;i<Population_Size;i++)
    {
        Individual New_Individual=new Individual();
        POP.addElement(New_Individual);
    }
}//End Generate_Population function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Clear_Population Function: Clear the Population
void Clear_Population(){
     POP.removeAllElements();
     Par_POP.removeAllElements();
     Sum_Fitness=0;
   
}//End Clear_Population function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Print_Population Function: Print the Population
void Print_Population(){

    System.out.print("\n -- ^^ Population ^^ --  ");
    for(int i=0;i<Population_Size;i++)
    {
        System.out.print("\n Indivisual # "+i);
        POP.elementAt(i).Print_Individual();

    }
}//End Print_Population function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Sum_Fitness Function: Calculate the Sum of the Fitness
double Sum_Fitness()
{
    Sum_Fitness=0;
    double Fitness=0;
    for(int i=0;i<POP.size();i++)
    {
        Fitness= POP.get(i).Get_fitness();
        Sum_Fitness=Sum_Fitness+Fitness;
    }
    
    return Sum_Fitness;

}//End Sum_Fitness function

//.,.,.,.,.,.,.,.,.,.,.,.,.
//Parents_Selection_pool Function: select parent
void Parents_Selection_pool(int num)
{
    Stochastic_Universal_Sampling(num);
}//End Parents_Selection_pool Function

//,.,.,.,.,.,.,.,.,.,,.,.,.,.,.,.,.,.,
//Stochastic_Universal_Sampling Function: select parent by roulette well way
void Stochastic_Universal_Sampling(int tnum)
{
    int n=(int) (K*tnum);
    //Calculate total fitness of population
    double S=Sum_Fitness();
    //Calculate distance between the pointers
    double mean=S/tnum;
    Random random = new Random();
    double scaled=random.nextDouble();
    double sum=POP.get(0).Get_fitness();
    int j=0;
    for (int i = 0; i < n && j<tnum; i++) {
         // Determine pointer to a segment in the population
            double delta=(mean*scaled)+(i*mean);
             //Find segment, which corresponds to the pointer
            if (sum >= delta) {
                Integer k= new Integer(j);
                Par_POP.addElement(k);

            } 
            else {
                for (++j; j < tnum; j++) {
                    sum=sum+POP.get(j).Get_fitness();
                    if (sum >= delta) 
                    {
                        Integer k= new Integer(j);
                        Par_POP.addElement(k);
                        break;
                    }
                }
            }
        }
   
}//End Stochastic_Universal_Sampling Function

//.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
//Parent_Selection Function: select parent from parent pool
Individual Parent_Selection()
{
     // ----- Pick individual Randomly from parents
    Random randomGenerator = new Random();
    int RNum = randomGenerator.nextInt(Par_POP.size());
    while (RNum == pselection) {
        RNum = randomGenerator.nextInt(Par_POP.size());
    }
    pselection=RNum;
    return POP.get(Par_POP.elementAt(RNum).intValue());
} //End Parent_Selection Function

//,,.,.,,,,,,,......,,,,,,..,.,.,.,.,.,.,.
//Elitist_Survivor Function : select best individuals to next generation 
Vector<Individual> Elitist_Survivor(int num)
{
    // Local Variables declaration
    Vector<Individual> TMP= new Vector<>();
    Vector<Individual> TMP_POP= new Vector<>();
    int NPOP=POP.size();
    ArrayList<Integer> select = new ArrayList<>();
    
    for(int f=0;f<num;f++)
    {
        Integer k= new Integer(f);
        select.add(k);
    }
    int next,ind;
    for(int f=num;f<NPOP;f++)
    {
        next=f;
        for(int i=0; i<select.size(); i++)
        {
            ind=select.get(i);
            if(POP.get(next).Get_fitness()>POP.get(ind).Get_fitness())
            {
                select.set(i, next);
                next=ind;
            }
                
        }
    }
    //
    for(int d=0;d<POP.size();d++)
        POP.get(d).remove=0;
  
    for(int j=0;j<select.size();j++)
    {
      Individual TMP1= new Individual(POP.get(select.get(j)));
      TMP.add(TMP1);
      POP.get(select.get(j)).remove=-1;
      
    }
    //
    for(int d=0;d<POP.size();d++)
    {
        if(POP.get(d).remove!=-1)
            TMP_POP.add(POP.get(d));
    }
    Clear_Population();
    POP.addAll(TMP_POP);
    return TMP;
}//End Elitist_Survivor Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
//RunTACO: call threed ACO algorithm
void RunTACO()
{
   GBtour=(float) 0.0;
   TACS[] tacs = new TACS[POP.size()];
   Thread[] acsThread = new Thread[POP.size()];
   for(int i=0;i<POP.size();i++)
    {
        tacs[i]=new TACS();
        for(int p=0;p<Num_Genes;p++)
        {
            tacs[i].Par[p]=POP.get(i).Individual[p];
        }
        tacs[i].Initial_TACS(indextarget);
        acsThread[i] = new Thread(tacs[i]);
        acsThread[i].start(); // start thread i
       
    }
   // wait for termination of each service (thread)
   try {
       for(int i = 0; i < POP.size(); i++)
           acsThread[i].join();
   } catch(InterruptedException e) {
   }
   
   //set the parameter
   for(int f=0;f<POP.size();f++)
    {
         //
        POP.get(f).LBtour=tacs[f].FitACS;
        if(tacs[f].FitACS>GBtour)
            GBtour=tacs[f].FitACS;
        //
        POP.get(f).Iter=tacs[f].IterACS;
        //
        POP.get(f).indexIn.clear();
        POP.get(f).indexIn.addAll(tacs[f].indexACS);    
    }
   
   // set the fitness
   for(int f=0;f<POP.size();f++)
    {
        POP.get(f).Calc_fitness();
    }
}

 //*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
//RunTACO: call ACO algorithm for one of individual
void RunTACO(Individual tomut)
{
   float GB=(float) 0.0;
   TACS tacs = new TACS();
   for(int p=0;p<Num_Genes;p++)
   {
       tacs.Par[p]=tomut.Individual[p];
   }
   tacs.Initial_TACS(indextarget);
   tacs.run();
  
   //set the parameter
   //
   tomut.LBtour=tacs.FitACS;
   if(tacs.FitACS>GBtour)
       GB=tacs.FitACS;
   else
       GB=GBtour;
    //
    tomut.Iter=tacs.IterACS;
    //
    tomut.indexIn.clear();
    tomut.indexIn.addAll(tacs.indexACS);    
   
   // set the fitness
    float num=(float) 0.0;
    for(int i=0;i<ResultOfPre.size();i++)
        num=num+ResultOfPre.get(i).Gloss.size();
    tomut.Fitness_FT=0;
    double F1;
        F1=2.5*(1/((GB+1)- tomut.LBtour));
     tomut.Fitness_FT=(float) ( tomut.Fitness_FT+F1);
    double F2=0.5*(Math.exp(-( tomut.Iter/(5*num))));
     tomut.Fitness_FT=(float) ( tomut.Fitness_FT+F2);
    double F3=0.5*(Math.exp(-( tomut.Individual[0]/(10*num))));
     tomut.Fitness_FT=(float) ( tomut.Fitness_FT+F3);
}
 
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
//Generational Function: generated next generation
void Generational(Vector<Individual> NewPOP)
{
    Clear_Population();
    POP.addAll(NewPOP);
    
}//End Generational Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Crossover1Point Function: performs the crossover operation on two Individuals and generates two new Individuals
Vector<Individual> Crossover1Point(Individual IND1,Individual IND2) throws JWNLException, CloneNotSupportedException{

    // Local Variables declaration
     Vector<Individual> TMP= new Vector<Individual>();
     Individual TMP1= new Individual(IND1);
     Individual TMP2= new Individual(IND2);
      Random randomGenerator = new Random();
      int i=randomGenerator.nextInt(Num_Genes);

      // one cross point-exchanging heads
      for(int j=0;j<=i;j++)
      {
          float temp=TMP1.Individual[j];
          TMP1.Individual[j]=TMP2.Individual[j];
          TMP2.Individual[j]=temp;
      }
      
      RunTACO(TMP1);
      RunTACO(TMP2);

      TMP.add(TMP1);
      TMP.add(TMP2);

      return TMP;
    
}// End  Crossover1Point Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// UniformCrossover Function: performs the crossover operation on two Individuals and generates two new Individuals
Vector<Individual> UniformCrossover(Individual IND1,Individual IND2) throws JWNLException, CloneNotSupportedException{

    // Local Variables declaration
     Vector<Individual> TMP= new Vector<Individual>();
     Individual TMP1= new Individual(IND1);
     Individual TMP2= new Individual(IND2);
     Random cgenerator = new Random();
     double PS;
     float temp;
     //
      for(int j=0;j<Num_Genes;j++)
      {
          PS = cgenerator.nextDouble();
          if(PS<=0.50)
          {
            temp=TMP1.Individual[j];
            TMP1.Individual[j]=TMP2.Individual[j];
            TMP2.Individual[j]=temp;
          }
      }
      
      RunTACO(TMP1);
      RunTACO(TMP2);
 
      TMP.add(TMP1);
      TMP.add(TMP2);

      return TMP;
    
}// End UniformCrossover Function

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// UniformMutation_ACS Function: performs the mutation operation on the Individual then return the new Individual
 Individual UniformMutation_ACS(Individual tomut) throws JWNLException, CloneNotSupportedException
 {
     Random randomGenerator = new Random();
     double i;
     Individual TMP_MUT= new Individual(tomut);
     Random cgenerator = new Random();
     double PM;
      
     for(int h=0;h<Num_Genes;h++)
     {
         PM = cgenerator.nextDouble();
          if(PM<=0.50)
          {
                //set a new value
                //m
                if(h==0)
                {
                    i=1+(randomGenerator.nextInt((2*(Numnode))));
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=1+(randomGenerator.nextInt((2*(Numnode))));
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
                //q0
                if(h==1)
                {
                    i=(((double)randomGenerator.nextInt(101))/100.0);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=(((double)randomGenerator.nextInt(101))/100.0);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
                //alpha
                if(h==2)
                {
                    i=randomGenerator.nextInt(6);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=randomGenerator.nextInt(6);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
                //beta
                if(h==3)
                {
                    i=randomGenerator.nextInt(11);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=randomGenerator.nextInt(11);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
               //evap
                if(h==4)
                {
                    i=(((double)randomGenerator.nextInt(101))/100.0);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=(((double)randomGenerator.nextInt(101))/100.0);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
                //localevap
                if(h==5)
                {
                    i=(((double)randomGenerator.nextInt(101))/100.0);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=(((double)randomGenerator.nextInt(101))/100.0);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
               //Q
                if(h==6)
                {
                    i=randomGenerator.nextInt(101);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=randomGenerator.nextInt(101);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
               //T0
                if(h==7)
                {
                    i=(((double)randomGenerator.nextInt(51))/100.0);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=(((double)randomGenerator.nextInt(51))/100.0);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
                //lambda
                if(h==8)
                {
                    i=randomGenerator.nextInt(11);
                    while(i==TMP_MUT.Individual[h])
                    {
                        i=randomGenerator.nextInt(11);
                        //System.out.print("\n# "+i);
                    }
                    TMP_MUT.Individual[h]= (float) i;    
                }
          }
     }
     return TMP_MUT;
 }//End UniformMutation_ACS
 
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Mutation1Point_ACS Function: performs the mutation operation on the Individual then return the new Individual
 Individual Mutation1Point_ACS(Individual tomut) throws JWNLException, CloneNotSupportedException
 {
     Random randomGenerator = new Random();
     double i;
     int h=randomGenerator.nextInt(Num_Genes);
     Individual TMP_MUT= new Individual(tomut);
     
     //set a new value
     //m
     if(h==0)
     {
         i=1+(randomGenerator.nextInt((2*(Numnode))));
         while(i==TMP_MUT.Individual[h])
         {
             i=1+(randomGenerator.nextInt((2*(Numnode))));
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
     //q0
     if(h==1)
     {
         i=(((double)randomGenerator.nextInt(101))/100.0);
         while(i==TMP_MUT.Individual[h])
         {
             i=(((double)randomGenerator.nextInt(101))/100.0);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
     //alpha
     if(h==2)
     {
         i=randomGenerator.nextInt(6);
         while(i==TMP_MUT.Individual[h])
         {
             i=randomGenerator.nextInt(6);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
     //beta
     if(h==3)
     {
         i=randomGenerator.nextInt(11);
         while(i==TMP_MUT.Individual[h])
         {
             i=randomGenerator.nextInt(11);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
    //evap
     if(h==4)
     {
         i=(((double)randomGenerator.nextInt(101))/100.0);
         while(i==TMP_MUT.Individual[h])
         {
             i=(((double)randomGenerator.nextInt(101))/100.0);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
     //localevap
     if(h==5)
     {
         i=(((double)randomGenerator.nextInt(101))/100.0);
         while(i==TMP_MUT.Individual[h])
         {
             i=(((double)randomGenerator.nextInt(101))/100.0);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
    //Q
     if(h==6)
     {
         i=randomGenerator.nextInt(101);
         while(i==TMP_MUT.Individual[h])
         {
             i=randomGenerator.nextInt(101);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
    //T0
     if(h==7)
     {
         i=(((double)randomGenerator.nextInt(51))/100.0);
         while(i==TMP_MUT.Individual[h])
         {
             i=(((double)randomGenerator.nextInt(51))/100.0);
             //System.out.print("\n# "+i);
         }
         TMP_MUT.Individual[h]= (float) i;    
     }
     //lambda
    if(h==8)
    {
        i=randomGenerator.nextInt(11);
        while(i==TMP_MUT.Individual[h])            
        {
            i=randomGenerator.nextInt(11);            
            //System.out.print("\n# "+i);
        }
        TMP_MUT.Individual[h]= (float) i;                
    }
     return TMP_MUT;
  }//End Mutation1Point_ACS
 
//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Set_Max_Ave Function:  Set MAX to the Individual that has Maximum fitness
//and calculate the average
 void Set_Max_Ave()
 {
     // Local Variables declaration
     double temp,sum=0;
     MAX=0;
     temp=POP.get(0).Get_fitness();
     sum+=temp;
     Max_Fitness=temp;
     
    //Set max
     for(int i=1;i<Population_Size;i++)
    {
         temp=POP.get(i).Get_fitness();
         sum+=temp;
        if(temp>POP.get(MAX).Get_fitness())
        {
            MAX=i;
            Max_Fitness=temp;
        }
    }

     //Calculate the average
     average= ((double)(sum/Population_Size));
   

 }// End Set_Max_Ave Function
 
}//End Class










