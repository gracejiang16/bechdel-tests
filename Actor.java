
/**
 * Represents an object of type Actor. An Actor has a name and a gender.
 *
 * @author (Stella K.) gracejiang, lindawang, elizabethchoi
 * @version 20221204
 */
public class Actor
{
    private String name; // name of Actor
    private String gender; // gender of Actor

    /**
     * Constructor for objects of class Actor
     * @param n name of Actor
     * @param g gender of Actor
     */
    public Actor(String n, String g) {
    	name = n;
    	gender = g;
    }
    
    /**
     * Sets name of Actor
     * @param n name to set to
     */
    public void setName(String n) {
    	name = n;
    }
    
    /**
     * Gets name of Actor
     * @return name of Actor
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Sets gender of Actor
     * @param g gender to set to
     */
    public void setGender(String g) {
    	gender = g;
    }
    
    /**
     * Gets gender of Actor
     * @return gender of Actor
     */
    public String getGender() {
    	return gender;
    }
    
    /**
     * Returns String representation of this Actor, including name and gender
     * @return String representation of this Actor
     */
    public String toString() {
    	return name + " is a " + gender.toLowerCase() + ".";
    }

    /**
     * This method is defined here because Actor (mutable) is used as a key in a Hashtable.
     * It makes sure that same Actors have always the same hash code.
     * So, the hash code of any object taht is used as key in a hash table,
     * has to be produced on an *immutable* quantity,
     * like a String (such a string is the name of the actor in our case)
     * 
     * @return an integer, which is the has code for the name of the actor
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Tests this actor against the input one and determines whether they are equal.
     * Two actors are considered equal if they have the same name and gender.
     * 
     * @param other Object to be compared to this Actor
     * @return true if both objects are of type Actor, 
     * and have the same name and gender, false in any other case.
     */
    public boolean equals(Object other) {
        if (other instanceof Actor) {
            return this.name.equals(((Actor) other).name) && 
            this.gender.equals(((Actor) other).gender); // Need explicit (Actor) cast to use .name
        } else {
            return false;
        }
    }
    
    /**
     * testing
     * @param args args
     */
    public static void main (String[] args) {
    	Actor alice = new Actor("Alice Bobson", "Female");
    	System.out.println("testing toString; expect \"Alice Bobson is a female.\", got: " + alice.toString());
    	System.out.println("changing alice's name to bob aliceson, gender to male: ");
    	alice.setGender("Male");
    	alice.setName("Bob Aliceson");
    	System.out.println("testing getName, getGender; expect \"Bob Aliceson\" and \"Male\", got: " + alice.getName() + ", " + alice.getGender());
    	Actor bob = new Actor("Bob Aliceson", "Male");
    	System.out.println("testing alice.equals(bob), expect true, got: " + alice.equals(bob));
    	System.out.println("changing bob's name to bub");
    	bob.setName("Bub");
    	System.out.println("testing alice.equals(bob), expect false, got: " + alice.equals(bob));

    }

}
