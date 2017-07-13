/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ACO;

import java.util.ArrayList;

/**
 *
 * @author wojdan
 */

//*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
// AntsTours Class
 class AntTour {
     public ArrayList<String> Tour= new ArrayList<>();
     public Double Fitness=0.0;
     public int targetword;

  }//end class

public class Public_var {
    
    // Public Variables declaration
 //*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^*^
    public static int num_ants; // number of ants
    public static double   evap=0.1;    /* pheromone evaporation factor2*/
    public static double   localevap=0.1;    /* pheromone evaporation factor*/
    public static double   alpha=1;   /* exponent for trail values*/
    public static double   beta=2;    /* exponent for distances*/
    public static double  lambda=3;   /* exponent for extra heuristic*/
    public static double   Q=1;       /* constant*/
    public static double InitPhero=1;        /* initial pheromone*/
    public static int Num_Iterations=100; /* maximum number of iterations*/
    public static double T0=0.01;
    public static double q0=0.9;
    
    
    
}
