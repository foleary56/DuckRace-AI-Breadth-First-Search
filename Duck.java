package project1;

import java.util.Objects;

public class Duck{
    //assigned energy max capacity, current energy, current position, and whether duck has flag
    private int capacity;
    private int currentEnergy;
    private int position;
    private boolean flag;

    //constructors
    public Duck(){
        this.capacity = 0;
        this.currentEnergy = 0;
        this.position = 0;
        this.flag = false;
    }

    public Duck(int capacity){
        this.capacity = capacity;
        this.currentEnergy = capacity;
        this.position = 0;
        this.flag = false;
    }

    
    //creates a clone of the Duck without changing original
    //used in state expansion for temp
    public Duck clone() {
        Duck clonedDuck = new Duck();
        clonedDuck.capacity = this.capacity;
        clonedDuck.currentEnergy = this.currentEnergy;
        clonedDuck.position = this.position;
        clonedDuck.flag = this.flag;
        return clonedDuck;
    }

    //transfers energy from one duck to another
    public boolean transfer(Duck obj){
        Duck d2 = (Duck)obj;
        //if ducks are close enough to share energy, and the source duck has energy to share,
        //and the target duck's energy isn't full, transfer one unit of energy from source to target and return true
        if(this.position==d2.position){
            if(d2.currentEnergy!=d2.capacity){
                if(this.currentEnergy!=0)
                {
                    this.currentEnergy--;
                    d2.currentEnergy++;
                    return true;
                }
            }
            return false;
        }
        return false;
        
    }

    //if at the final position grab the flag
    //moves the duck and returns the ducks new pos, -1 is flag if no move
    public int move(String direction){
        if(this.currentEnergy!=0){
            if(direction == "L") {
                this.position++;
                this.currentEnergy--;
                return this.position;
            }
            
            else {
                this.position--;
                this.currentEnergy--;
                return this.position;
            }
        }
        else return -1; //FLAG START HERE
    }

    //GETTERS AND SETTER
    public int getPosition(){
        return this.position;
    }
    
    public int getCapacity(){
        return this.capacity;
    }

    public int getEnergy(){
        return this.currentEnergy;
    }

    //getter for flag
    public boolean hasFlag(){
        return this.flag;
    }
    
    public void setFlag(boolean setTo){
        this.flag = setTo;
    }

    //TO STRING
    public String toString(){
        return("Capacity: "+this.capacity+" Energy: "+this.currentEnergy+" Position: "+this.position);
    }

    //checks if one duck is equal to the other duck
    public boolean equals(Object obj){
        Duck d2 = (Duck)obj;
        if (flag != d2.flag || capacity != d2.capacity ||  currentEnergy!= d2.currentEnergy || position != d2.position) {
            return false;
        }
        return true;
    }

    //hashcode override
    public int hashCode() {
        return Objects.hash(flag, position, currentEnergy, capacity);
    }

    


}