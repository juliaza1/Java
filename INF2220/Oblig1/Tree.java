import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Tree {
	
	Node root;

	public void addNode (String name){
		//create a new Node and initialize it
		Node newNode = new Node(name);
        root = addNode(root, newNode);
    }

    // add the node recursively to the tree (root)
    private static Node addNode(Node root, Node node) {
      if (root == null) {
        // reach a null branch, return the node as the child
        return node;
      }

      // now compare the strings in the root and node
      // to decide which subtree to add the node into
      int diff = root.name.compareTo(node.name);
      if (diff < 0) { // go to the right tree
        root.rightChild = addNode(root.rightChild, node);
      } else if (diff > 0) { // go to the left tree
        root.leftChild = addNode(root.leftChild, node);
      } else {
        // the two values are the same, which indicates duplicate words
        // in dictionary
        // should not happen...
      }
      return root;
    }


	public boolean remove(String name) {
        Node target = findNode(name);
        if (target != null) {
          root = remove(root, name);
          return true;
        }
        return false;
	}

    // remove the node with the given name.
    private static Node remove(Node root, String name) {
      if (root == null) {
        // reach a null branch, which means
        // there is no such a node with the given name.
        return null;
      }

      // now compare the strings in the root and node
      // to decide which subtree to remove the node
      int diff = root.name.compareTo(name);

      if (diff < 0) { // go to the right tree
        root.rightChild = remove(root.rightChild, name);
      } else if (diff > 0) { // go to the left tree
        root.leftChild = remove(root.leftChild, name);
      } else {
        // the two values are the same
        // thus remove this node
        if (root.leftChild == null && root.rightChild == null) {
          // no children, safe to remove directly
          return null;
        }
        if (root.leftChild == null) {
          // has only right child, return it as the new child of the parent
          // thus this node is removed.
          return root.rightChild;
        }
        if (root.rightChild == null) {
          // has only left child
          return root.leftChild;
        }
        // this node has both children
        // now remove the smallest word in the right child
        // and put it at this node.
        // then remove that smallest word in the right child
        Node smallest = root.rightChild;
        while (smallest.leftChild != null) {
          smallest = smallest.leftChild;
        }
        root.name = smallest.name;
        // remove the duplicate smallest word in the right child
        root.rightChild = remove(root.rightChild, root.name);
      }

      return root;
    }

	public void deleteWord() {
		System.out.println("Please enter a word to remove.");
		Scanner tast = new Scanner(System.in);
		String input = tast.nextLine().toLowerCase().trim();
		System.out.println(remove(input));
	}
	
	public void addWord() {
		System.out.println("Please enter a word to add.");
		Scanner tast = new Scanner(System.in);
		String input = tast.nextLine().toLowerCase().trim();
		if (findNode(input) == null) {
			addNode(input);
			System.out.println(input + " is now a part of the family.");
		} else {
			System.out.println("Your word is already in this fine dictionary.");
		}
	}

	public void searchWord() {
		System.out.println("Please enter a word to search.");
		Scanner tast = new Scanner(System.in);
		String input = tast.nextLine().toLowerCase().trim();
		Node target = findNode(input);
        if (target != null) {
          System.out.println(target);
        } else {
          // no word is found, perform the spell check
          System.out.printf("'%s' not found in dictionary.\n", input);
          System.out.println("-- Spell Check Result --");
          spellcheck(input);
        }
	}

    private void spellcheck(String name) {
      long start = System.nanoTime(); // start time

      // calculate similar words according to each rule
      String[][] rules = {
        similarOne(name), similarTwo(name),
        similarThree(name), similarFour(name)
      };

      int positives = 0; // number of positives

      // now check each replacement
      for (String[] words : rules) {
        for (String word : words) {
          Node target = findNode(word);
          if (target != null) {
            System.out.println(target);
            positives ++;
          }
        }
      }

      long end = System.nanoTime(); // end time
      double duration = (end - start) / 100000.0; // time in milliseconds

      System.out.println("Number of positives: " + positives);
      System.out.printf("Time cost: %.2f milliseconds\n", duration);
    }

    // generate similar words according to rule one
    public String[] similarOne(String word){
      char[] word_array = word.toCharArray();
      char[] tmp;
      String[] words = new String[word_array.length-1];
      for(int i = 0; i < word_array.length - 1; i++){
        tmp = word_array.clone();
        words[i] = swap(i, i+1, tmp);
      }
      return words;
    }

    private String swap(int a, int b, char[] word) {
      char tmp = word[a];
      word[a] = word[b];
      word[b] = tmp;
      return new String(word);
    }

    // generate similar words according to rule two
    public String[] similarTwo(String word){
      String[] words = new String[word.length() * 26];
      char[] word_array = word.toCharArray();
      char[] tmp;
      int k = 0;
      for (int i = 0; i < word_array.length; i++) {
        for (char c = 'a'; c <= 'z'; c++) {
          if (word_array[i] != c) {
            tmp = word_array.clone();
            tmp[i] = c;
            words[k++] = new String(tmp);
          }
        }
      }
      String[] result = new String[k];
      for (int i = 0; i < k; i++) {
        result[i] = words[i];
      }
      return result;
    }

    // generate similar words according to rule three
    public String[] similarThree(String word){
      String[] words = new String[(word.length() + 1) * 26];
      int k = 0;
      for (int i = 0; i <= word.length(); i++) {
        String firstPart = word.substring(0, i);
        String lastPart = word.substring(i);
        for (char c = 'a'; c <= 'z'; c++) {
          words[k++] = firstPart + c + lastPart;
        }
      }
      return words;
    }

    // generate similar words according to rule four
    public String[] similarFour(String word) {
      if (word.length() <= 1) return new String[0];

      String[] words = new String[word.length()];
      for (int i = 0; i < word.length(); i++) {
        String firstPart = word.substring(0, i);
        String lastPart = word.substring(i + 1);
        words[i] = firstPart + lastPart;
      }
      return words;
    }


	public Node findNode(String name) {
		//Start at the top of the tree
		Node focusNode = root;
		//while we haven't found the Node
		//keep looking

		while (focusNode != null && focusNode.name.compareTo(name) != 0) {
			//if we should search to the left
			if(name.compareTo(focusNode.name) < 0) {
				//shift focus to left child
				focusNode = focusNode.leftChild;
			} else {
				//shift focus to right child
				focusNode = focusNode.rightChild;
			}
		}
		return focusNode;
	}

    // return the depth of the tree
    public int depth() {
      return depth(root, 0);
    }

    private static int depth(Node root, int d) {
      if (root == null) {
        return d;
      }

      // the depth of the left child and right child
      int dL = d, dR = d;
      if (root.leftChild != null) {
        dL = depth(root.leftChild, d + 1);
      }
      if (root.rightChild != null) {
        dR = depth(root.rightChild, d + 1);
      }
      // return the larger one
      if (dL >= dR) {
        return dL;
      }
      return dR;
    }

    public void countLevel() {
      System.out.println("Number of words in each level:");
      // count the nodes in each level / level tranversal
      ArrayList<Node> q1 = new ArrayList<Node>();
      if (root != null) {
        q1.add(root);
      }
      int k = 1;
      while (!q1.isEmpty()) {
        System.out.printf("Level %d: %d node(s)\n", k++, q1.size());
        // gather the nodes in the next level
        // and put them into another queue
        ArrayList<Node> q2 = new ArrayList<Node>();
        for (Node node : q1) {
          if (node.leftChild != null) {
            q2.add(node.leftChild);
          }
          if (node.rightChild != null) {
            q2.add(node.rightChild);
          }
        }
        q1 = q2;
      }
    }

    // calculate the average depth of the nodes
    public double averageNodeDepth() {
      // = total depth / number of nodes
      long sumOfDepth = sumOfNodeDepth(root, 0);
      int num = numOfNodes(root);
      return sumOfDepth * 1.0 / num;
    }

    // calculate the sum of the depth of nodes in the tree (pointed by root)
    private static long sumOfNodeDepth(Node root, int d) {
      if (root == null) {
        return 0;
      }
      long sum = d;
      if (root.leftChild != null) {
        sum = sum + sumOfNodeDepth(root.leftChild, d + 1);
      }
      if (root.rightChild != null) {
        sum = sum + sumOfNodeDepth(root.rightChild, d + 1);
      }
      return sum;
    }

    // calculate the number of nodes in the tree
    private static int numOfNodes(Node root) {
      if (root == null) {
        return 0;
      }
      return 1 + numOfNodes(root.leftChild) + numOfNodes(root.rightChild);
    }

    // find the minimal word in the tree
    public String min() {
      Node curr = root;
      // go to the leftmost child
      while (curr != null && curr.leftChild != null) {
        curr = curr.leftChild;
      }
      if (curr != null) {
        return curr.name;
      }
      return null;
    }

    // find the maximal word in the tree
    public String max() {
      Node curr = root;
      // go to the right child
      while (curr != null && curr.rightChild != null) {
        curr = curr.rightChild;
      }
      if (curr != null) {
        return curr.name;
      }
      return null;
    }

	
	public static void main(String[] args) throws FileNotFoundException {
		Tree theTree = new Tree();	
		boolean run = true;
		//reading into tree	
		try{
			File text = new File("dictionary.txt");
			Scanner scnr = new Scanner(text);
			int lineNumber = 1;
			
			while(scnr.hasNextLine()){
				String line = scnr.nextLine();
				theTree.addNode(line);
				lineNumber++;
			}
			scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        System.out.println("Number of words: " + numOfNodes(theTree.root));

		System.out.println("Word 'busybody' removed: " + theTree.remove("busybody"));
		theTree.addNode("busybody");
		System.out.println("Word 'busybody' readded.");


		//Menu
		while(run) {
			System.out.println("CHOOSE FROM MENU\na: search\nb: add\nc: delete\nq: quit");
			Scanner tast = new Scanner(System.in);
			char input = tast.nextLine().trim().charAt(0);
			switch (input) {
				case 'a': theTree.searchWord();
				break;
				case 'b': theTree.addWord();
				break;
				case 'c': theTree.deleteWord();
				break;
				case 'q': run = false;
				break;
				default: System.out.println("Wrong input. Try again.");
			}
            System.out.println();
		}

        // display the statistics of the tree
        System.out.println("<statistics of the tree>");
        System.out.println("Depth: " + theTree.depth());
        theTree.countLevel(); // count words in each level
        System.out.printf("Average node depth: %.2f\n", theTree.averageNodeDepth());
        System.out.println("First word: " + theTree.min());
        System.out.println("Last word:  " + theTree.max());
	}
}


class Node {
	String name;
	
	Node leftChild;
	Node rightChild;
	
	Node(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "'" + name + "' is found";
	}
}
