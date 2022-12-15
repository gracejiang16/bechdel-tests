import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents an object of type Movie. A Movie object has a title, some Actors,
 * and results for the twelve Bechdel tests.
 *
 * @author (Stella K.) gracejiang, lindawang, elizabethchoi
 * @version 20221204
 */
public class Movie implements Comparable<Movie> {

	private String title; // title of Movie
	private Hashtable<Actor, String> roles; // roles of Actors in the Movie
	private Vector<String> results; // test results of 13 gender imbalance tests

	/**
	 * Constructor for Movie class
	 * 
	 * @param t title of Movie
	 */
	public Movie(String t) {
		title = t;
		roles = new Hashtable<Actor, String>();
		results = new Vector<String>();
	}

	/**
	 * Returns title of Movie
	 * 
	 * @return title of Movie
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set title of Movie
	 * 
	 * @param t title of Movie
	 */
	public void setTitle(String t) {
		title = t;
	}

	/**
	 * Returns all Actors and their roles
	 * 
	 * @return hashtable of all actors
	 */
	public Hashtable<Actor, String> getAllActors() {
		return roles;
	}

	/**
	 * Returns LinkedList of Actors' names
	 * 
	 * @return LinkedList of Actors' names
	 */
	public LinkedList<String> getActors() {
		LinkedList<String> result = new LinkedList<String>();

		Enumeration<Actor> e = roles.keys();
		while (e.hasMoreElements()) {
			Actor anActor = e.nextElement();
			// and now you can print the actor and its information:
			result.add(anActor.getName());
		}
		return result;
	}

	/**
	 * returns a Vector with all the test results for this movie
	 * 
	 * @return all test results
	 */
	public Vector<String> getAllTestResults() {
		return results;
	}

	/**
	 * populates the testResults vector with "0" and "1"s, each representing the
	 * result of the coresponding test on this movie.
	 * 
	 * @param input data for the test results
	 */
	public void setTestResults(String input) {
		String[] tempResults = input.split(",");
		for (String s : tempResults) {
			results.add(s);
		}
	}

	/**
	 * Takes in a String, formatted as lines are in the input file, generates an
	 * Actor, and adds the object to the actors of this movie.
	 * 
	 * @param input data for this Actor
	 * @return the actor that was added
	 */
	public Actor addOneActor(String input) {
		String[] tempResults = input.split(",");
		Actor actor = new Actor(tempResults[0], tempResults[4]);
		roles.put(actor, tempResults[1]);
		return actor;
	}

	/**
	 * Reads the input file, and adds all its Actors to this movie.
	 * 
	 * @param file input file from which data comes
	 */
	public void addAllActors(String file) {
		try {
			Scanner fileScan = new Scanner(new File(file));
			fileScan.nextLine(); // skip first line
			while (fileScan.hasNext()) {
				String line = fileScan.nextLine();
				String movieName = line.substring(0, line.indexOf(",")).replaceAll("\"", "");
				if (movieName.equalsIgnoreCase(title)) {
					String actorInfo = line.substring(line.indexOf(",") + 1).replaceAll("\"", "");
					addOneActor(actorInfo);
				}
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}

	}

	/**
	 * Tests this movie object with the input one and determines whether they are
	 * equal.
	 * 
	 * @param other Object to be compared to this Movie
	 * @return true if both objects are movies and have the same title, false in any
	 *         other case.
	 */
	public boolean equals(Object other) {
		if (other instanceof Movie) {
			return this.title.equals(((Movie) other).title); // Need explicit (Movie) cast to use .title
		} else {
			return false;
		}
	}

	/**
	 * @return String representation of Movie, including title and number of actors
	 */
	public String toString() {
		return title + " has " + roles.size() + " actors.";
	}

	/**
	 * Calculates and returns a "feminist score" of this Movie according to this
	 * criteria: Ranking of 2-26, with 26 being the "most feminist." Possible scores
	 * [0,182]
	 * <br>2 points - LANDAU
	 * <br>4 points - UPHOLD
	 * <br>6 points - BECHDEL
	 * <br>8 points - REES DAVIES
	 * <br>10 points - HAGEN
	 * <br>12 points - KOEZE-DOTTLE
	 * <br>14 points - KO
	 * <br>16 points - FELDMAN
	 * <br>18 points - VILLARREAL
	 * <br>20 points - WAITHE
	 * <br>22 points - WHITE
	 * <br>24 points - VILLALOBOS
	 * <br>26 points - PEIRCE
	 * 
	 * @return feminist score
	 */
	public int feministScore() {
		return Integer.parseInt(results.get(0)) * 6 // bechdel
				+ Integer.parseInt(results.get(1)) * 26 // pierce
				+ Integer.parseInt(results.get(2)) * 2 // landau
				+ Integer.parseInt(results.get(3)) * 16 // feldman
				+ Integer.parseInt(results.get(4)) * 18 // villareal
				+ Integer.parseInt(results.get(5)) * 10 // hagan
				+ Integer.parseInt(results.get(6)) * 14 // ko
				+ Integer.parseInt(results.get(7)) * 24 // villalobos
				+ Integer.parseInt(results.get(8)) * 20 // waithe
				+ Integer.parseInt(results.get(9)) * 12 // koeze_dottle
				+ Integer.parseInt(results.get(10)) * 4 // uphold
				+ Integer.parseInt(results.get(11)) * 22 // white
				+ Integer.parseInt(results.get(12)) * 8; // reese-davies
	}

	/**
	 * Method to help break ties in feminist ranking.
	 * @return number of tests this Movie passes
	 */
	public int numberOfPassedTests() {
		int result = 0;
		for (String s : results) {
			if (s.equals("1"))
				result++;
		}
		return result;
	}

	/**
	 * Compares Movies based on their feminist scores. A higher score means a Movie
	 * is "greater".
	 * If initially this and other are "equal," check number of passed tests to break tie. if tie not broken, return 0
	 * 
	 * @param other other Movie to compare
	 * @return 1 if this>other, 0 if this==other, -1 if this<other
	 */
	public int compareTo(Movie other) {
		if (this.feministScore() > (other).feministScore())
			return 1;
		if (this.feministScore() == (other).feministScore()) {
			// if feminist score is same, compare Movies with number of passed tests
			if (this.numberOfPassedTests() > other.numberOfPassedTests()) {
				return 1;
			}
			else if (this.numberOfPassedTests() < other.numberOfPassedTests()) {
				return -1;
			}
			else {
				return 0; // if fem score and num passed tests are same, we can consider two Movies to be similarly "feminist"
			}
		}
		return -1;
	}

	/**
	 * testing
	 * 
	 * @param args args
	 */
	public static void main(String[] args) {
		Movie alpha = new Movie("Alpha");
		Movie beta = new Movie("Beta");
		Movie gamma = new Movie("Gamma");

		alpha.addAllActors(
				"/Users/gracejiang/Desktop/Everything/CS230/FINALBechdelProject_StartingCode/data/small_castGender.txt");
		beta.addAllActors(
				"/Users/gracejiang/Desktop/Everything/CS230/FINALBechdelProject_StartingCode/data/small_castGender.txt");
		gamma.addAllActors(
				"/Users/gracejiang/Desktop/Everything/CS230/FINALBechdelProject_StartingCode/data/small_castGender.txt");

		System.out.println("Alpha: " + alpha);
		System.out.println("Beta: " + beta);
		System.out.println("Gamma: " + gamma);

		/* BELOW to test other methods */
		System.out.println("creating new movie: Hunger Games");
		Movie movie1 = new Movie("Hunger Games");
		movie1.addOneActor("Jennifer Lawrence,Katniss Everdeen,Leading,1,Female");
		movie1.addOneActor("Liam Hemsworth,Gale Hawthorne,Supporting,2,Male");

		System.out.println("testing getActors() for movie1, expect Jennifer, Liam, got: " + movie1.getActors());

		// testing test results methods
		movie1.setTestResults("1,1,1,1,1,1,1,1,1,1,1,1,0");
		System.out.println(
				"testing get/setTestResults(), expect 1,1,1,1,1,1,1,1,1,1,1,1,1, got: " + movie1.getAllTestResults());
		System.out.println("testing toString(), expect Hunger Games has 2 actors, got: " + movie1.toString());

		Movie movie2 = new Movie("Hunger Games");
		System.out.println("testing equals, expect true, got: " + movie1.equals(movie2));
		System.out.println("changing movie2 title to Shrek using setTitle()");
		movie2.setTitle("Shrek");
		System.out.println("testing getTitle(), expect Shrek, got: " + movie2.getTitle());
		System.out.println("testing equals, expect false, got: " + movie1.equals(movie2));

		// testing getAllActors()
		LinkedList<String> allActors = new LinkedList<String>();
		Enumeration<Actor> e = movie1.getAllActors().keys();
		while (e.hasMoreElements()) {
			Actor anActor = e.nextElement();
			// and now you can print the actor and its information:
			allActors.add(anActor.getName());
		}
		System.out.println("Movie1's getAllActors(), expect Jennifer, Liam, got: " + allActors);

		// testing feministScore()
		System.out.println("Testing feministScore(), expect 87, got: " + movie1.feministScore());
	}

}
