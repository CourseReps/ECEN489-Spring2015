import java.util.*;

//Abstract Method
public abstract interface Main {
    public Collection Data = new ArrayList();
    public abstract void addElement();
    public abstract void removeElement();
    public abstract void obtainElement();
    public abstract void listAllElement();
}


//This interface is the basic usage for a collection. 
//By using this abstract interface, user can implement whatever way they want to deal with the data under different sensor situation. 
