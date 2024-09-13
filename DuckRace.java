package project1;


import java.util.HashSet;
import java.util.Queue;
import java.util.PriorityQueue;


public class DuckRace{
    private HashSet<State> reached;
    private Queue<State> frontier;
    private State solution;

    //constructor
    public DuckRace(){
        this.reached = new HashSet<State>();
        this.frontier = new PriorityQueue<State>();
        this.solution = null;
    }

    //expands node to all possible solutions
    public void expand(State current){
        State temp = current.clone();
        Duck[] ducks;
            
        //initial state analysis
            if(current.isPossible()){ // can state continue game?
                ducks = current.getDucks();
                if(ducks[temp.getFlagLane()].hasFlag()){ //if flag duck has flag
                    for(int i = 0;i<ducks.length;i++){ 
                        temp = current.clone(); //creates temp clone
                        temp.move(i,"R"); //moves clone to right
                        if(!reached.contains(temp)){ //checks to see if state is in "reached"
                            reached.add(temp); //if not, add current to reached
                            frontier.add(temp); //and to frontier to mark for expansion
                            if(temp.solutionFound()){ //if this move leads to the solution we exit loop
                                solution = temp;
                                break;
                        }
                        }
                    }
                    //for potential transfers
                    for(int i = 0;i<ducks.length;i++){
                        temp = current.clone(); //temp clone
                        if(i==0){ //first duck
                            temp.transfer(i, i+1); //energy transfer to next duck
                        }
                        else if(i==ducks.length-1){ //last duck
                            temp.transfer(i, i-1); //transfer to previous
                        }
                        else{//any other ducks
                            temp.transfer(i,i+1);//trannsfer to next duck
                            temp = current.clone(); //another clone to test previous transfer
                            temp.transfer(i,i-1); //previous transfer
                        }
                        if(!reached.contains(temp)){//add it to reached and frontier if not explored
                            reached.add(temp);
                            frontier.add(temp);
                        }
                        
                    }
                }
                else{ //flag has not been picked up, so we move left
                    
                    for(int i = 0;i<ducks.length;i++){ //go through every duck
                        temp = current.clone(); //clone original state
                        temp.move(i,"L"); //move left, because we have not gotten the flag yet
                        if(!reached.contains(temp)){ //check if state already reached, if not add to frontier and reached
                            reached.add(temp);
                            frontier.add(temp);
                        }
                        
                    }
                    for(int i = 0;i<ducks.length;i++){ //goes through every duck
                        temp = current.clone(); //clones original state
                        if(i==0){ //first duck and only transer to one next to it
                            temp.transfer(i, i+1); //transfer methods checks for if ducks next to eachother
                        }
                        else if(i==ducks.length-1){ //last duck can only transfer to one next to it
                            temp.transfer(i, i-1);
                        }
                        else{//any other duck
                            temp.transfer(i,i+1);
                            temp = current.clone();
                            temp.transfer(i,i-1);
                        }
                        if(!reached.contains(temp)){//check if state already reached, if not add to frontier and reached
                            reached.add(temp);
                            frontier.add(temp);
                        }
                        
                    }
                }

            }

    }

    

    //checks if the state found a solution
    public boolean solutionFound(State state){
        if(state.getDucks()[state.getFlagLane()].hasFlag()){//duck has flag
            for(int i =0;i<state.getNumDucks();i++){
                if(state.getDucks()[i].getPosition()!=0){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    

    public static void main(String[] args){
        State start1 = new State(2,3, 0, 3);
        State start2 = new State(7,4, 3, 5);
        State start3 = new State(4,5, 1, 4);
    	State start4 = new State(5,4, 3, 2);
        DuckRace solve = new DuckRace();
        solve.expand(start4);

        boolean done = false;
        State current = new State();
        while(!done){
            if(solve.frontier.size()!=0)//makes sure frontier isnt empty, or else theres no solution
            {
                current = solve.frontier.poll();
                solve.expand(current);
                if(solve.solution!=null){
                    System.out.println("Solution Found!!");
                    System.out.println(solve.solution);
                    System.out.println();
                    System.out.println(solve.solution.getDirections());
                    done = true;
                }
            }
            else{
                System.out.println("No possible solution");
                done=true;
            }
        }

    }
}



