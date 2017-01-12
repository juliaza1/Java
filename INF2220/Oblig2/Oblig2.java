import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Task {

	public Task() {
	}

	public Task(int taskID) {
		id = taskID;
	}

	protected int id, time, staff;
	protected String name;
	protected int earliestStart, latestStart;
	protected List<Task> outEdges;
	protected int cntPredecessors;

	boolean done = false;
	/*
	 * Get task information
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Identity of this task: ").append(id).append("\n");
		sb.append("\tName of this task: ").append(name).append("\n");
		sb.append("\tTime estimate for this task: ").append(time).append("\n");
		sb.append("\tManpower requirements: ").append(staff).append("\n");
		if (cntPredecessors > 0) {
			sb.append("\tDependency edges: ");
			for (Task task: outEdges) {
				sb.append(task.id).append(" ");
			}
		}
		return sb.toString();
	}
}

class Event implements Comparable<Event>{

	public Event() {

	}

	public Event(int time, int staff, String operator) {
		this.time = time;
		this.staff = staff;
		this.operator = operator;
	}

	protected int time;
	protected int staff;
	protected String operator;

	@Override
	public int compareTo(Event o) {
		if (time < o.time)
			return -1;
		else
			if (time > o.time)
				return 1;
		return 0;
	}
}

public class Oblig2 {

	private static int numberOfTasks;            // number of tasks
	private static List<Task> tasks;        	 // task list
	private static List<Integer>[] adjacencyList;// adjacency list
	private static Map<Integer, Task> map;  	 // map id and task
	private static boolean[] visited;       	 // visited[v] = has vertex v (task v) been marked?
    private static int[] edgeTo;            	 // edgeTo[v] = previous vertex on path to v
    private static boolean[] onStack;       	 // onStack[v] = is vertex on the stack?
    private static Stack<Integer> cycle;    	 // directed cycle (or null if no such cycle)

	/*
	 * Read task list from input file
	 * @param filePath
	 */
	@SuppressWarnings("unchecked")
	public static void readData(String filePath) {
		BufferedReader br = null;
		tasks = new ArrayList<Task>();
		map = new HashMap<Integer, Task>();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filePath));
			sCurrentLine = br.readLine();
			numberOfTasks = Integer.parseInt(sCurrentLine);
			adjacencyList = new List[numberOfTasks + 1];
			for (int i = 1; i <= numberOfTasks; i++)
				adjacencyList[i] = new ArrayList<Integer>();
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.trim().length() > 0) {
					Task task = new Task();
					task.outEdges = new ArrayList<Task>();
					sCurrentLine = sCurrentLine.replaceAll("\\s+", " ");
					String[] chunks = sCurrentLine.split(" ");
					task.id = Integer.parseInt(chunks[0]);
					task.name = chunks[1];
					task.time = Integer.parseInt(chunks[2]);
					task.staff = Integer.parseInt(chunks[3]);
					task.cntPredecessors = chunks.length - 5;
					for (int i = 4; i < chunks.length - 1; i++) {
						int taskID = Integer.parseInt(chunks[i]);
						task.outEdges.add(new Task(taskID));
						adjacencyList[taskID].add(task.id);
					}
					tasks.add(task);
					map.put(task.id, task);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

    // check that algorithm computes either the topological order or finds a directed cycle
	private static void dfs(int v) {
		onStack[v] = true;
        visited[v] = true;
        for (int w: adjacencyList[v]) {
            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!visited[w]) {
                edgeTo[w] = v;
                dfs(w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
                assert checkCycle();
            }
        }
        onStack[v] = false;
	}


	// certify that digraph has a directed cycle if it reports one
    private static boolean checkCycle() {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

    /*
     * Does the digraph have a directed cycle?
     * @return <tt>true</tt> if the digraph has a directed cycle, <tt>false</tt> otherwise
     */
    private static boolean hasCycle() {
        return cycle != null;
    }

    /*
     * Returns a directed cycle if the digraph has a directed cycle, and <tt>null</tt> otherwise.
     * @return a directed cycle (as an iterable) if the digraph has a directed cycle,
     *    and <tt>null</tt> otherwise
     */
    private static Iterable<Integer> cycle() {
        return cycle;
    }

	private static void solve() {
		visited = new boolean[numberOfTasks + 1];
		onStack = new boolean[numberOfTasks + 1];
		edgeTo = new int[numberOfTasks + 1];
		for (int v = 1; v <= numberOfTasks; v++)
			if (!visited[v] && cycle == null)
				dfs(v);
	}


	//_________________________________________________________________________






		static Task finnNyNodeMedInngradNull() {
			for (Task v : tasks) {
				if (!v.done && v.cntPredecessors == 0) {
					return v;
				}
			}

			return null;
		}

		static void topsort() {
			Task v;

			for (int teller = 0;  teller < numberOfTasks; teller++) {
				v = finnNyNodeMedInngradNull();

				if (v == null) {
					System.out.println("Error - cycle found");
					return;
				} else {
					// When cntPredecessors == 0, the task can begin, and we have
					// the earliest start calculated based on the earliestFinish of the slowest predecessor
					v.done = true;

					int earliestFinish = v.earliestStart + v.time;

					System.out.format("Earliest start for task  %s (%d) is %d\n", v.name, v.id, v.earliestStart);

					for (int we : adjacencyList[v.id]) {
						Task w = map.get(we);
						w.cntPredecessors--;
						// w can not start before earliestFinish for v
						if (w.earliestStart < earliestFinish) {
							w.earliestStart = earliestFinish;
						}
					}

				}
			}
		}



		//_________________________________________________________________________

	public static void main(String[] args) {
		String input = "";
		int manpower;
		try {
			input = args[0];
			manpower = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("invalid command. Please try again!!!");
			System.out.println("javac Oblig2");
			System.out.println("java Oblig2 input_file manpower");
			return;

		}
		readData(input);

		// uncomment if you want to print input data
		/*
		System.out.println("Number of tasks is " + numberOfTasks);
		for (int i = 1; i <= numberOfTasks; i++) {
			System.out.println(tasks.get(i - 1).toString());
			System.out.println("--------------------------------------");
		}
		*/
		solve(); // run topo sort algorithm
		if (hasCycle()) {
			System.out.print("The graph contains directed cycle: ");
			// Print directed cycle
			List<Integer> nodes = new ArrayList<Integer>();
            for (int v : cycle()) {
            	nodes.add(v);
            	System.out.print(v + " ");
            }
            System.out.println();
            for (int i = 0; i < nodes.size() - 1; i++) {
            	Task task = map.get(nodes.get(i));
            	System.out.print("(id:" + task.id + ", name:" + task.name + ") ---> ");
            }
            Task task = map.get(nodes.get(0));
        	System.out.println("(id:" + task.id + ", name:" + task.name + ")");
        }

        else {
        	// Replaced with code based on the example slide to calculate earliest start
        	topsort();

            /*int count = 0;
            int totalTime = 0;

            boolean[] done = new boolean[numberOfTasks + 1];

            List<Event> events = new ArrayList<Event>();

            while (count < numberOfTasks) {
            	List<Integer> list = new ArrayList<Integer>();
            	int m = 0;
            	for (int v = 1; v <= numberOfTasks; v++) {
            		boolean ok = true;
            		if (done[v])
            			continue;
            		for (Task t: map.get(v).outEdges) {
            			int j = t.id;
            			if (!done[j]) {
            				ok = false;
            				break;
            			}
            		}
            		if (ok) {
            			events.add(new Event(totalTime, map.get(v).staff, "Starting: " + v));
            			Task task = map.get(v);
            			events.add(new Event(totalTime + task.time, -map.get(v).staff, "Finished: " + v));
            			list.add(v);
            			m = m > task.time ? m : task.time;
            			count++;
            		}
            	}
            	totalTime += m;
            	for (int v: list) {
            		done[v] = true;
            	}
            }
            Collections.sort(events);
            int staff = 0;
            int time = -1;
            for (Event event: events) {
            	if (event.time > time) {
            		if (time > -1)
            			System.out.println("   Current staff: " + staff);
            		time = event.time;
            		System.out.println("Time: " + time);
            	}
            	System.out.println("\t" + event.operator);
            	staff += event.staff;
            }
            System.out.println("**** Shortest possible project execution is " + totalTime + " ****");*/
        }
	}
}









