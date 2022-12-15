import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

import javafoundations.PriorityQueue;

/**
 * MovieCollection is a collection of movies and actors. There are two
 * LinkedLists of movies and actors, ensuring no repeats.
 * 
 * @author gracejiang, lindawang, elizabethchoi
 * @version 20221206
 */
public class MovieCollection {

	private LinkedList<Movie> allMovies;
	private LinkedList<Actor> allActors;

	/**
	 * Constructor for MovieCollection class. Initializes allMovies, allActors.
	 */
	public MovieCollection() {
		allMovies = new LinkedList<Movie>();
		allActors = new LinkedList<Actor>();
	}

	/**
	 * Returns all the movies in a LinkedList
	 * 
	 * @return a LinkedList with all the movies, each complete with its title,
	 *         actors and Bechdel test results.
	 */
	public LinkedList<Movie> getMovies() {
		return allMovies;
	}

	/**
	 * Returns the titles of all movies in the collection
	 * 
	 * @return a LinkedList with the titles of all the movies
	 */
	public LinkedList<String> getMovieTitles() {
		LinkedList<String> titles = new LinkedList<String>();
		for (Movie m : allMovies) {
			titles.add(m.getTitle());
		}
		return titles;
	}

	/**
	 * Returns all the Actors in the collection
	 * 
	 * @return a LinkedList with all the Actors, each complete with their name and
	 *         gender.
	 */
	public LinkedList<Actor> getActors() {
		return allActors;
	}

	/**
	 * Returns the names of all actors in the collection
	 * 
	 * @return a LinkedList with the names of all actors
	 */
	public LinkedList<String> getActorNames() {
		LinkedList<String> names = new LinkedList<String>();
		for (Actor a : allActors) {
			names.add(a.getName());
		}
		return names;
	}

	/**
	 * Reads the input file, and uses its first column (movie title) to create all
	 * movie objects. Adds the included information on the Bachdel test results to
	 * each movie. It is perhaps better to make this method private
	 * 
	 * @param fileName file with movie data
	 */
	public void readMovies(String fileName) {
		try {
			Scanner fileScan = new Scanner(new File(fileName));
			fileScan.nextLine(); // skipping first line of data file

			while (fileScan.hasNext()) {
				String line = fileScan.nextLine();
				String movieName = line.substring(0, line.indexOf(",")); // separating movie from test results
				String testResults = line.substring(line.indexOf(",") + 1); // ^

				Movie movie = new Movie(movieName);

				for (Movie m : allMovies) {
					if (m.equals(new Movie(movieName))) {
						continue; // if repeat movie, go to nextLine
					}
				}
				movie.setTestResults(testResults);
				allMovies.add(movie);
			}
		} catch (IOException ex) {
			System.out.println("File does not exist.");
		}
	}

	/**
	 * Reads the casts for each movie, from input casts file. If a movie does not
	 * have any test results, it is ignored and not included in the collection.
	 * 
	 * @param fileName file with cast data
	 */
	private void readCasts(String fileName) {
		try {
			Scanner fileScan = new Scanner(new File(fileName));
			fileScan.nextLine(); // skipping first line of data file

			while (fileScan.hasNext()) {
				String line = fileScan.nextLine();
				String movieName = (line.substring(0, line.indexOf(",")).replaceAll("\"", "")); // separating movie from
																								// actor info
				String actorInfo = (line.substring(line.indexOf(",") + 1).replaceAll("\"", "")); // ^

				addActorToMovie(movieName, actorInfo); // MOVIE CLASS METHOD INVOKED IN THIS HELPER METHOD!!!!
			}
		} catch (IOException ex) {
			System.out.println("File does not exist.");
		}
	}

	/**
	 * Helper method to add an Actor to a Movie for readCasts method.
	 * 
	 * @param movieName Movie for actorInfo to be added into
	 * @param actorInfo Actor to add into movieName
	 */
	private void addActorToMovie(String movieName, String actorInfo) {
		for (Movie m : allMovies) {
			if (m.equals(new Movie(movieName))) { // if movie already exists
				String name = actorInfo.split(",")[0];
				String gender = actorInfo.substring(actorInfo.lastIndexOf(",") + 1);
				Actor temp = new Actor(name, gender);
				if (!allActors.contains(temp)) { // if actor isn't already in allActors
					allActors.add(m.addOneActor(actorInfo));
				} else { // if actor is already in allActors (don't want repeats in allActors)
					m.addOneActor(actorInfo);
				}
				return;
			}
		}
		Movie m = new Movie(movieName); // if movie doesn't exist yet
		allMovies.add(m);
		String name = actorInfo.split(",")[0];
		String gender = actorInfo.substring(actorInfo.lastIndexOf(",") + 1);
		Actor temp = new Actor(name, gender);
		if (!allActors.contains(temp)) { // if actor isn't already in allActors
			allActors.add(m.addOneActor(actorInfo));
		} else { // if actor is already in allActors (don't want repeats in allActors)
			m.addOneActor(actorInfo);
		}
	}

	/**
	 * Returns a list of all Movies that pass the n-th Bechdel test
	 * 
	 * @param n integer identifying the n-th test in the list of 12 Bechdel
	 *          alternatives, starting from zero
	 * @return A list of all Movies which have passed the n-th test
	 */
	public LinkedList<Movie> findAllMoviesPassedTestNum(int n) {
		LinkedList<Movie> passedMovies = new LinkedList<Movie>();
		if (n > 12) { // if n is out of range
			return passedMovies;
		}

		for (Movie m : allMovies) {
			Vector<String> results = m.getAllTestResults();
			if (results.get(n).equals("1")) { // if Movie m passes test n, add to LinkedList
				passedMovies.add(m);
			}
		}
		return passedMovies;
	}

	/**
	 * find all movies that passed the Bechdel test. 
	 * @return LinkedList of movies that passed Bechdel
	 */
	public LinkedList<Movie> findAllMoviesPassedBechdel() {
		return findAllMoviesPassedTestNum(0);
	}

	/**
	 * find all movies that passed either the Peirce or the Landau test
	 * @return LinkedList of Movies that passed either the Peirce or the Landau test
	 */
	public LinkedList<Movie> findAllMoviesPassedPierceOrLandau() {
		LinkedList<Movie> passedMovies = new LinkedList<Movie>();

		for (Movie m : allMovies) {
			Vector<String> results = m.getAllTestResults();
			if (results.get(1).equals("1") || results.get(2).equals("1")) {
				passedMovies.add(m);
			}
		}
		return passedMovies;
	}

	/**
	 * find all movies that passed the White test but did *not* pass the Rees-Davies test
	 * @return LinkedList of Movies that passed the White test but did *not* pass the Rees-Davies test
	 */
	public LinkedList<Movie> findAllMoviesPassedWhiteFailedRD() {
		LinkedList<Movie> passedMovies = new LinkedList<Movie>();

		for (Movie m : allMovies) {
			Vector<String> results = m.getAllTestResults();
			if (results.get(11).equals("1") && results.get(12).equals("0")) {
				passedMovies.add(m);
			}
		}
		return passedMovies;
	}

	/**
	 * returns a PriorityQueue of movies in the provided data based on their
	 * feminist score. if score is the same, number of tests passed is used to break
	 * ties
	 * 
	 * @return a PriorityQueue of movies in the provided data based on their
	 *         feminist score
	 */
	public PriorityQueue<Movie> rankMovies() {
		PriorityQueue<Movie> result = new PriorityQueue<Movie>();
		for (Movie m : allMovies) {
			result.enqueue(m); // enqueue all Movies in PQ
		}
		return result;
	}

	/**
	 * Returns a String representing this MovieCollection
	 * 
	 * @return a String representation of this collection, including the number of
	 *         movies and the movies themselves.
	 */
	public String toString() {
		String result = "This MovieCollection contains " + allMovies.size() + " movies. They are: \n";
		for (String m : getMovieTitles()) {
			result += m + "\n";
		}

		return result;
	}

	/**
	 * testing
	 * @param args args
	 */
	public static void main(String[] args) {
		MovieCollection mc = new MovieCollection();
		mc.readMovies(
				"/Users/gracejiang/Desktop/Everything/CS230/FINALBechdelProject_StartingCode/data/nextBechdel_allTests.txt");
		mc.readCasts(
				"/Users/gracejiang/Desktop/Everything/CS230/FINALBechdelProject_StartingCode/data/nextBechdel_castGender.txt");
		System.out.println(mc); // also implicitly testing getMovieTitles

		// testing findAllMoviesPassedTestNum()
		System.out.println();
		System.out.println();
		System.out.println("**************testing findAllMoviesPassedTestNum(2)**************");
		LinkedList<Movie> passed2 = mc.findAllMoviesPassedTestNum(2);
		System.out.println("Testing findAllMoviesPassedTestNum(2), expect 33, got: ");
		System.out.println("There are " + passed2.size() + " movies that pass test#2-landau:");
		for (Movie m : passed2) {
			System.out.println(m.getTitle());
		}

		// testing findAllMoviesPassedBechdel()
		System.out.println();
		System.out.println();
		System.out.println("**************testing findAllMoviesPassedBechdel()**************");
		LinkedList<Movie> passedBechdel = mc.findAllMoviesPassedBechdel();
		System.out.println("Testing findAllMoviesPassedBechdel(), expect 18, got: ");
		System.out.println("There are " + passedBechdel.size() + " movies that pass test#0-bechdel:");
		for (Movie m : passedBechdel) {
			System.out.println(m.getTitle());
		}

		// testing findAllMoviesPassedPierceOrLandau()
		System.out.println();
		System.out.println();
		System.out.println("**************testing findAllMoviesPassedPierceOrLandau()**************");
		LinkedList<Movie> passedPierceOrLandau = mc.findAllMoviesPassedPierceOrLandau();
		System.out.println("Testing findAllMoviesPassedPierceOrLandau(), expect 40, got: ");
		System.out.println("There are " + passedPierceOrLandau.size() + " movies that pass pierce or landau:");
		for (Movie m : passedPierceOrLandau) {
			System.out.println(m.getTitle());
		}

		// testing findAllMoviesPassedWhiteFailedRD()
		System.out.println();
		System.out.println();
		System.out.println("**************testing findAllMoviesPassedWhiteFailedRD()**************");
		LinkedList<Movie> passedWhiteFailedRD = mc.findAllMoviesPassedWhiteFailedRD();
		System.out.println("Testing findAllMoviesPassedWhiteFailedRD(), expect 15, got: ");
		System.out.println("There are " + passedWhiteFailedRD.size() + " movies that pass white, fail reese-davies:");
		for (Movie m : passedWhiteFailedRD) {
			System.out.println(m.getTitle());
		}

		// testing getMovieTitles()
		System.out.println();
		System.out.println();
		System.out.println("Testing getMovieTitles(), got: " + mc.getMovieTitles().size() + " titles.");

		// testing getActorNames()
		System.out.println();
		System.out.println();
		System.out.println("Testing getActorNames(), got: " + mc.getActorNames().size() + " actors.");

		// testing feministScore
		System.out.println();
		System.out.println();
		LinkedList<Movie> allMovies = mc.getMovies();
		for (Movie m : allMovies) {
			System.out.println(m.getTitle() + " has a feminist score of " + m.feministScore());
		}

		// testing rankMovies()
		System.out.println();
		System.out.println();
		System.out.println("Testing rankMovies(): ");
		PriorityQueue<Movie> ranked = mc.rankMovies();
		while (!ranked.isEmpty()) {
			Movie m = ranked.dequeue();
			System.out.println(
					m.getTitle() + "'s score: " + m.feministScore() + "; passed tests: " + m.numberOfPassedTests());
		}
	}

}
