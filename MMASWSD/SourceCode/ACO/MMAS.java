/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ACO;

import static ACO.Public_var.*;
import static WSD.Public_var.ResultOfPre;
import static WSD.Public_var.indexresult;
import WSD.Senses;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author wojdan
 */
public class MMAS {
    
    // Variables declaration 
    public ArrayList<AntTour> AntsTours= new ArrayList<AntTour>();
    public ArrayList<AntTour> GBestTours= new ArrayList<AntTour>();
    public ArrayList<AntTour> IBestTours= new ArrayList<AntTour>();
    public int indextarget;
    public double num_words=0;
    public double Root;
    //*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// Constructor
public MMAS(int TInd)
{
    indextarget=TInd;
    Tmax=evap* Neighbor(1);
    for(int i=0;i<ResultOfPre.size();i++)
        num_words=num_words+ResultOfPre.get(i).Gloss.size();
    
        double intph=1/num_words;
        Root=Math.pow(0.05,intph);
        Tmin=(Tmax*(1-Root))/(((num_words/2)-1)*Root);
        InitPhero=Tmax;
        Run();
}

//.,.,.,.,.,.,..,,..,.,.,.,.,.,.,.,.,.,
//Neighbor Function: apply Nearest Neighbor algorithm
double Neighbor(int Kbest)
{
    double check,check1,check2;
    double max=0.0, length=0, fit=0.0;
    int tw = 0,ts = 0;
    ArrayList<Integer> NN= new ArrayList<Integer>();
    Random randomGenerator = new Random();
    int w=indextarget;
    NN.add(indextarget);
    int s=randomGenerator.nextInt((ResultOfPre.get(indextarget).Gloss.size()));
    int fs=s;
    while(NN.size() < ResultOfPre.size())
    {
        for(int i=0;i<ResultOfPre.size();i++)
        {
            if(!(NN.contains(i)))
            {
                for(int j=0;j<ResultOfPre.get(i).Gloss.size();j++)
                {
                    if(Kbest==0)
                    {
                        check=ResultOfPre.get(w).RelatednessBP.get(Integer.toString(s)+Integer.toString(i)+Integer.toString(j));
                        if(check>=max)
                        {
                            max=check;
                            fit=max;
                            tw=i;
                            ts=j;     
                        }
                    }
                    else
                    {
                        check1=ResultOfPre.get(w).RelatednessBP.get(Integer.toString(s)+Integer.toString(i)+Integer.toString(j));
                        check2=ResultOfPre.get(indextarget).RelatednessBP.get(Integer.toString(fs)+Integer.toString(i)+Integer.toString(j));
                        check=check1*check2;
                        if(check>=max)
                        {
                            max=check;
                            fit=check2;
                            tw=i;
                            ts=j;     
                        }
                    }
                    
                }
            }
        }
        w=tw;
        s=ts;
        length=length+fit;
        NN.add(w);
    }
    if(Kbest==0)
    {
        check=ResultOfPre.get(w).RelatednessBP.get(Integer.toString(s)+Integer.toString(NN.get(0))+Integer.toString(fs));
        length=length+check;
    }
    
    return length;
}//End Neighbor Function

//.,.,.,.,.,.,.,.,..,.,.,.,.,.,.
//InitializedHExtra Function:set the initial position for each ant and phromaon for each node
void InitializedHExtra(int a)
{
    int select;
    num_ants=0;
    AntsTours.clear();
    Random randomGenerator = new Random();
    for(int i=0;i<ResultOfPre.size();i++)
    {
        for(int j=0;j<ResultOfPre.get(i).Gloss.size();j++)
        { 
            if(num_ants>=ResultOfPre.get(indextarget).Offset.size())
                select=randomGenerator.nextInt(ResultOfPre.get(indextarget).Offset.size());
            else
                select=num_ants;
            if(a==0)
                ResultOfPre.get(i).Pheromone.add(InitPhero);
            ArrayList<String> visit=new ArrayList<String>();
            visit.add(Integer.toString(indextarget)+"-"+Integer.toString(select));
            AntTour AT=new AntTour();
            AT.Tour.addAll(visit);
            AT.targetword=0;
            AntsTours.add(AT);
            num_ants++;
        }
    }
}//End InitializedHExtra Function

//.,.,..,.,.,.,.,.,..,.,..,.,.,
//Run Function: Start the algorithm
void Run()
{
    
    int finished;
    for(int iter=0;iter<Num_Iterations;iter++)
    {
        InitializedHExtra(iter);
        finished=1;
        while(finished<ResultOfPre.size())
        {
            for(int a=0;a<num_ants;a++)
            {
                Move(a);
            }
            finished++;
        }
        Calc_Fitness();
        BestTours(iter);
        UpdatePhoro();
    }
    FinalResultPath();
}//End Run Function

//.,.,.,.,.,.,.,.,.,.
//Move Function:each ant choose the next city
void Move(int ant)
{
    String vertex=AntsTours.get(ant).Tour.get(0);
    int index=vertex.indexOf('-');
    String TSV=vertex.substring((index+1));
        
    //
    int current=(AntsTours.get(ant).Tour.size())-1;
    vertex=AntsTours.get(ant).Tour.get(current);
    index=vertex.indexOf('-');
    int LastWV=Integer.valueOf(vertex.substring(0, index));
    
    String Svisit=vertex.substring((index+1));
    
    //
    ExplorationHExtra(ant,LastWV, Svisit, TSV);
    
    //
    int last=(AntsTours.get(ant).Tour.size())-1;
    vertex=AntsTours.get(ant).Tour.get(last);
    index=vertex.indexOf('-');
    int LWV=Integer.valueOf(vertex.substring(0, index));
    //
    if(LWV==indextarget)
    {
        AntsTours.get(ant).targetword=last;
   }
    
}//End Move Function

//.,,.,.,.,.,.,.,.,.,.,.,.,.,.
// IsVisited Function: check if an ant has visited a specific node
boolean IsVisited(int a,int word)
{
    String check1,check2,temp;
    check1=Integer.toString(word);
    for(int v=0;v<AntsTours.get(a).Tour.size();v++)
    {
        temp=AntsTours.get(a).Tour.get(v);
        check2=temp.substring(0,temp.indexOf('-'));
        if(check1.equalsIgnoreCase(check2))
        {
            return true;
        }
    }
    return false;
}//End IsVisited Function

//.,.,.,.,.,.,.,.,.,.,.,.,
//ExplorationHExtra Function: apply Exploration rul to choose the next node by an ant
void ExplorationHExtra(int ant,int WV, String SV, String TS)
{
    ArrayList<String> Next= new ArrayList<String>();
    ArrayList<Double> dist = new ArrayList<Double>();
    ArrayList<Double> pro = new ArrayList<Double>();
    double sum=0;
    double check1,check2, check3, max=0, dis = 0;
    String next = null, temp;
    
    for(int w=0;w<ResultOfPre.size();w++)
    {
        if(!(IsVisited(ant,w)))
        {
            for(int s=0;s<ResultOfPre.get(w).Gloss.size();s++)
            {
                
                temp=Integer.toString(w)+"-"+Integer.toString(s);
                Next.add(temp);
                
                check1=ResultOfPre.get(WV).RelatednessBP.get(SV+Integer.toString(w)+Integer.toString(s));
                check3=ResultOfPre.get(indextarget).RelatednessBP.get(TS+Integer.toString(w)+Integer.toString(s));
                check2=Math.pow(check1,beta)*Math.pow(check3,lambda)*Math.pow(ResultOfPre.get(w).Pheromone.get(s),alpha);
                pro.add(check2);
                sum=sum+check2;
                dist.add(check1); 
                
            }
        }
    }
    if(sum==0)
    {
        Random randomGenerator = new Random();
        int w=randomGenerator.nextInt((Next.size()));
        next=Next.get(w);
        dis=dist.get(w);
        
    }
    else
    {
        Random cgenerator = new Random();
        double v = cgenerator.nextDouble();
        for(int i=0;i<Next.size();i++)
        {
            if(v<=(pro.get(i)/sum))
            {
                next=Next.get(i);
                dis=dist.get(i);
                break;
            }
        }
        if(next==null)
        {
            int last=(Next.size()-1);
            next=Next.get(0);
            dis=dist.get(0);
        }
    }

        AntsTours.get(ant).Tour.add(next);
    
}//End ExplorationHExtra Function

//.,.,.,.,.,.,.,.,.,.,.
//UpdatePhoro Function: apply global update phoromone rule
void UpdatePhoro()
{
    double sum=0;
    double new_phor=0;
    for(int w=0;w<ResultOfPre.size();w++)
    {
        for(int s=0;s<ResultOfPre.get(w).Gloss.size();s++)
        {
            sum=0;
            for(int a=0;a<IBestTours.size();a++)
            {
                if(IBestTours.get(a).Tour.contains(Integer.toString(w)+"-"+Integer.toString(s)))
                    sum=sum+(Q*GBestTours.get(a).Fitness);
            }
                new_phor=(1-evap)*ResultOfPre.get(w).Pheromone.get(s)+sum;
                if(new_phor>Tmax)
                new_phor=Tmax;
                if(Tmin>new_phor)
                new_phor=Tmin;
                ResultOfPre.get(w).Pheromone.set(s, new_phor);
        }
    }
}//End UpdatePhoro Function

//.,.,..,.,,.,.,.,.,.,.,.,.,.,.,.,...,.,.,.,
//BestTours Function: choose the best tours
void BestTours(int it)
{
    IBestTours.clear();
    if(it==0)
    {
        FBestFitness();
    }
        
    else
    {
        IntBestFitness();
    }
    
    Tmax=evap* GBestTours.get(0).Fitness;
    Tmin=(Tmax*(1-Root))/(((num_words/2)-1)*Root);
        
}//End BestTours Function

//.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
//FBestFitness Function: choose the best tours at frist itreation
void FBestFitness()
{
    int max=0;
    for(int fr=1;fr<AntsTours.size();fr++)
    {
        if(AntsTours.get(max).Fitness<AntsTours.get(fr).Fitness)
            max=fr;
    }
    AntTour temp=new AntTour();
    temp.Tour.addAll(AntsTours.get(max).Tour);
    temp.Fitness=AntsTours.get(max).Fitness;
    temp.targetword=AntsTours.get(max).targetword;
    GBestTours.add(temp);
    IBestTours.add(temp);
    for(int fr=(max+1);fr<AntsTours.size();fr++)
    {
        double fit1=AntsTours.get(max).Fitness;
        double fit2=AntsTours.get(fr).Fitness;
        if(fit1==fit2)
        {
            
                AntTour temp1=new AntTour();
                temp1.Tour.addAll(AntsTours.get(fr).Tour);
                temp1.Fitness=AntsTours.get(fr).Fitness;
                temp1.targetword=AntsTours.get(fr).targetword;
                GBestTours.add(temp1);
                IBestTours.add(temp1);
        }
    }
}//End FBestFitness Function

//.,.,.,.,.,.,.,.,.,.,.,.,.,.,
//IntBestFitnes Function: choose the best tours at the next of frist itreation
void IntBestFitness()
{
    int max=0;
    boolean change=false, change1=false;
    for(int f=1;f<AntsTours.size();f++)
    {
        if((AntsTours.get(max).Fitness<AntsTours.get(f).Fitness))
            max=f;
    }
    
    double fit1=AntsTours.get(max).Fitness;
    double fit2=GBestTours.get(0).Fitness;
    if(fit1>fit2)
        change=true;
    if(fit2==fit1)
        change1=true;
    
    if(change)
        GBestTours.clear();
    AntTour temp=new AntTour();
    temp.Tour.addAll(AntsTours.get(max).Tour);
    temp.Fitness=AntsTours.get(max).Fitness;
    temp.targetword=AntsTours.get(max).targetword;
    if(change==true || change1==true)
        GBestTours.add(temp);
    IBestTours.add(temp);
    for(int fr=(max+1);fr<AntsTours.size();fr++)
    {
        fit1=AntsTours.get(max).Fitness;
        fit2=AntsTours.get(fr).Fitness;
        if(fit1==fit2)
        {
                AntTour temp1=new AntTour();
                temp1.Tour.addAll(AntsTours.get(fr).Tour);
                temp1.Fitness=AntsTours.get(fr).Fitness;
                temp1.targetword=AntsTours.get(fr).targetword;
                if(change==true || change1==true)
                    GBestTours.add(temp1);
                IBestTours.add(temp1);
        }
    }
}//End IntBestFitnes Function

//.,.,...,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.
//Calc_Fitness Function: calculate the fitness value for the ants solutions
public void Calc_Fitness()
{
    String key="",key1=""; 
    for(int ant=0;ant<AntsTours.size();ant++)
    {
        int Target=AntsTours.get(ant).targetword;
        String vertex=AntsTours.get(ant).Tour.get(Target);
        int  index=vertex.indexOf('-');
        //int LastWV=Integer.valueOf(vertex.substring(0, index));
        String SV=vertex.substring((index+1));
        key=SV;
        for(int j=0;j<AntsTours.get(ant).Tour.size();j++)
        {
            if(j!=Target)
            {
                vertex=AntsTours.get(ant).Tour.get(j);
                index=vertex.indexOf('-');
                String WV=vertex.substring(0, index);
                SV=vertex.substring((index+1));
                key1=key+WV+SV;
                AntsTours.get(ant).Fitness=AntsTours.get(ant).Fitness+ResultOfPre.get(indextarget).RelatednessBP.get(key1); 
            }  
        }
    }
}//End Calc_Fitness Function

//.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,.,
//FinalResultPath Function: set the final resulte
void FinalResultPath()
{
    //indexresult.clear();
    for(int a=0;a<GBestTours.size();a++)
    {
        int Target=GBestTours.get(a).targetword;
        System.out.println("Target"+Target+" "+GBestTours.get(a).Tour);
        String vertex=GBestTours.get(a).Tour.get(Target);
        int  index=vertex.indexOf('-');
        int LastWV=Integer.valueOf(vertex.substring(0, index));
        int SV=Integer.valueOf(vertex.substring((index+1)));
        if(!(indexresult.contains(SV)))
            indexresult.add(SV);
        System.out.println("LastWV"+LastWV+"SV"+SV);
        System.out.println("TourLength"+GBestTours.get(a).Fitness);
    }
}// End FinalResultPath Function

}
