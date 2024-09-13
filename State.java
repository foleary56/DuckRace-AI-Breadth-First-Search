package project1;
import java.util.Arrays;
import java.util.Objects;

public class State implements Comparable<State>{
    private Duck[] ducks;
    private int numDucks;
    private int flagLane;
    private int rowLength; //have this one too high
    private int maxCapacity;
    private int g;
    private int f;
    private String directions;


    //constructors
    public State(){
        this.numDucks = 0;
        this.flagLane = 0;
        this.rowLength = 0;
        this.maxCapacity = 0;
        this.ducks = new Duck[0];
        this.g = 0;
        this.f=0;
        this.directions = "";
    }

    public State(int numDucks,int row,int flagLane,int maxCapacity){
        this.numDucks = numDucks;
        this.flagLane = flagLane;
        this.rowLength = row-1;
        this.maxCapacity = maxCapacity;
        this.g = 0;
        this.f = g;
        this.directions = "";
        this.ducks = new Duck[numDucks];
        for(int i = 0;i<numDucks;i++){
            this.ducks[i] = new Duck(this.maxCapacity);
        }
        
    }

    // calculates what the total energy of the ducks is 
    public int totalEnergy(){
        int total = 0;
        for(int i =0;i<this.numDucks;i++){
            total = total + this.ducks[i].getEnergy();
        }
        return total;
    }

    //counts the minimum energy needed to finish
    public int minEnergyNeeded(){
        int total=0;
        for(int i =0;i<this.numDucks;i++){
            if(i==flagLane){
                if(!ducks[i].hasFlag()){//if duck doesnt have flag
                    total = total +(this.rowLength-ducks[i].getPosition())+this.rowLength;//add distance to flag and back from duck
                }
                else{//if duck has flag
                    total = total+ducks[i].getPosition();//add ducks current position
                }
                
            }
            else{//non flag ducks
                total = total+ducks[i].getPosition();//add current position
            }
        }
        return total;
    }

    //checks if there is possible solution
    public boolean isPossible(){
        if(this.totalEnergy()>=this.minEnergyNeeded()) return true;
        else return false;
    }

    //checks if we have a solution
    public boolean solutionFound(){
        if(ducks[this.getFlagLane()].hasFlag()){
            for(Duck duck:ducks){
                if(duck.getPosition()!=0) return false;
            }
        }
        return true;
    }




    //if at final position pick up flag
    public void move(int duck, String direction){
        if(this.ducks[duck].getPosition()==0 && this.ducks[flagLane].hasFlag())//if duck is at pos 0 and the flag duck has flag then no moving is needed
        {
            return;
        }
        int pos = this.ducks[duck].move(direction);//moves duck
        if(pos !=-1)//if duck cant move
        {
            if(pos==this.rowLength && duck==this.flagLane){
                this.ducks[duck].setFlag(true);//if its flag lane duck and he's at end, grab flag
            }
            this.g++;
            this.f =calcF(); //recalculates total cost once g is incremented
            this.directions += direction+duck+" "; //logs movement
        }
        
    }
    
    //transfers energy to duck, put this and other transfer makes sure it is transferable
    public void transfer(int duckGiving, int duckRecieving){
        boolean res = this.ducks[duckGiving].transfer(this.ducks[duckRecieving]);
        
        if(res){//if transfer works
            this.g++;
            this.f =calcF(); //recalculates total cost once g is incremented
            this.directions += "T"+duckGiving+" --> "+duckRecieving+" "; //logs transfer
        }
        
        
    }

    //Clones the state 
    public State clone() {
        State clonedState = new State();
        clonedState.flagLane = this.flagLane;
        clonedState.maxCapacity = this.maxCapacity;
        clonedState.numDucks = this.numDucks;
        clonedState.rowLength = this.rowLength;
        clonedState.g = this.g;
        clonedState.directions = this.directions;

        // Deep copy of ducks array
        if (this.ducks != null) {
            clonedState.ducks = new Duck[this.ducks.length];
            for (int i = 0; i < this.ducks.length; i++) {
                if (this.ducks[i] != null) {
                    clonedState.ducks[i] = this.ducks[i].clone();
                }
            }
        }

        return clonedState;
    }
    
    public int calcF(){
        return this.g+calcH();//f = g + h
    }

    public int calcH(){
        int total=0;
        int pos;
        if(this.ducks[this.flagLane].hasFlag()){//if has flag
            pos = this.ducks[this.flagLane].getPosition(); //gets spot of duck with flag
            total = total+pos; //adds flag duck position to total
            for(int i =0;i<this.numDucks;i++){
                pos = this.ducks[i].getPosition(); //get position of each duck
                total = total+pos; //add to total
            }
            total = total+this.maxCapacity; //overestimates
        }
        else{//if does not have flag
            pos = this.ducks[this.flagLane].getPosition(); //get the position of flag lane duck
            pos = (this.rowLength*2)-pos; //distance to get flag and return to position
            total = pos+maxCapacity; //adds this plus max for overestimate to total
        }
        return total;
    }
    
    //GETTERS AND SETTERS
    public void setDucks(Duck[] ducks){
        this.ducks=ducks;
    }

    public void setFlagLane(int lane){
        this.flagLane=lane;
    }

    public void setCapacity(int cap){
        this.maxCapacity=cap;
    }

    public void setNumDucks(int num){
        this.numDucks=num;
    }

    public void setRowLength(int len){
        this.rowLength=len;
    }

    public Duck[] getDucks(){
        return ducks;
    }

    public int getNumDucks(){
        return this.numDucks;
    }
    
    public int getFlagLane(){
        return flagLane;
    }

    public int getCapacity(){
        return this.maxCapacity;
    }

    public String getDirections(){
        return directions;
    }
    
    public int getRowLength(){
        return this.rowLength;
    }

    // //TO STRING
    // public String toString(){
    //     String ret = " ";
    //     for(Duck element:this.ducks){
    //         ret = ret+element+", ";
    //     }
    //     return(ret+"/ "+"Row length: "+this.rowLength+" "+this.flagLane+" "+this.maxCapacity+" "+this.numDucks+". G: "+this.g+" F: "+this.f);
    // }


    public String toString(){
        //string builder
        StringBuilder ret = new StringBuilder();
        //start with ducks
        ret.append("\nDucks: ");
        //adds each ducks cap, energy, and pos to string
        for (Duck duck : this.ducks){
            ret.append(duck.toString() + "\n");
        }
        //spacing for summary
        ret.append("\n");
        ret.append("\nRace Summary: \n");
        //summary of current race details (input)
        ret.append(String.format("Flag Lane: %d, Max Capacity: %d, # of Ducks: %d | G value: %d, F value: %d", 
        this.flagLane, this.maxCapacity, this.numDucks, this.g, this.f));

        return ret.toString().trim();

    }

    
    //COMPARE TO
    public int compareTo(State s2){
        if(this.f>s2.f){
            return 1;
        }
        else if(this.f<s2.f){
            return -1;
        }
        else{
            return 0;
        }

    }
    
    //EQUALS
    public boolean equals(Object obj){
        State other = (State)obj;
        if (numDucks != other.numDucks || flagLane != other.flagLane || rowLength != other.rowLength || maxCapacity != other.maxCapacity) {
            return false;
        }

        for(int i = 0;i<ducks.length;i++){
            if(!ducks[i].equals(other.ducks[i])) {
                return false;
            }
        }
        return true;
        
    }

    //hashcode override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(ducks), numDucks, flagLane, rowLength, maxCapacity);
    }


    
}