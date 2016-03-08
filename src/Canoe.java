import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Canoe class
 * @author Joe Greive, Andy Bleich
 *
 */
public class Canoe {

	Node[][] data;
	Node root;
	Stack<Node> nodeStack = new Stack<Node>();
	ArrayList<Stack<Node>> setList = new ArrayList<Stack<Node>>();
	Stack<Node> cheapestSet;
	int totalSize;
	int cheapestPrice = 0;
	
	public Canoe(int size){
		totalSize = size;
		buildData();
		buildTree();
		
		//Dynamic iterative method. Works on all sizes.
		System.out.println("\nDynamic iteration");
		dynamicIteration(data);
		
		
		/* Because all these methods rely on recursion,
		 * we have limited the size to 10. Larger numbers cause
		 * and immense amount of time and will usually result of 
		 * out of memory exception*/
		if(size <= 10){
			//Print out all sets
			System.out.println("\nAll available paths");
			printOutSets();
			
			//brute force
			bruteForce();
			
			//Divide and conquer
			setList.clear();
			nodeStack.clear();
			System.out.println("\nDivide and Conquer");
			System.out.println("The cheapest path is: " +divideAndConquer(root, 0));
			printDAndC(divideAndConquer(root, 0));
			
			//Dynamic recursive method
			System.out.println("\nDynamic recursion");
			dynamicRecursion(root, 0);
			System.out.println("The cheapest path is: "+cheapestPrice);
			printCheapestSet();		
		}
		
			
			
	}
		
	/**
	 * Builds a random 2D array based off size
	 * @param size the size of the 2D array.   size x size
	 */
	void buildData(){
		Random r = new Random();
		data = new Node[totalSize][totalSize];
		for(int i = 0; i < totalSize; i++){
			for(int j = 0; j < totalSize; j++){
				data[i][j] = new Node(0);
				if(j == i){
					data[i][j].cost = 0;
				} else if(j < i){
					data[i][j].cost = -1;
				} else {
					data[i][j].cost = data[i][j-1].cost + (int)(r.nextFloat() * 10) + 1;
				}
			}
		}
		
		//Prints out the 2D array
		for(int i = 0; i < totalSize; i++){
			for(int j = 0; j < totalSize; j++){
				System.out.print(data[i][j].cost+"\t");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	
	/**
	 * Turns the info we need from the 2D array into a tree.
	 * Asymptotic complexity = O(n * (n/2)) = O(n^2)
	 */
	void buildTree(){
		root = data[0][1];
		for(int i = 0; i < totalSize; i++){
			for(int j = i+1; j < totalSize-1; j++){
				data[i][j].right = data[i][j+1];
				data[i][j].left = data[j][j+1];
			}
		}
	}
	
	
	
	/**
	 * Generate all sets based on DFS.  O(n)
	 * @param root initial root
	 */
	void generateSets(Node root){
		
		nodeStack.push(root);
		if(root.left != null){					//if left recur left
			generateSets(root.left);
		}
		if(root.right != null){					//if right, remove current Node and recur right
			nodeStack.pop();
			generateSets(root.right);
		}
		if(root.isLeaf()){						//if leaf node, make new set and add to list, then remove node
			Stack<Node> temp = (Stack<Node>) nodeStack.clone();
			setList.add(temp);
			nodeStack.pop();
		}		
	}
	
	
	
	
	/**
	 * Dynamic method to recursively find the cheapest path
	 * Anytime we go left, we must increment the value of our current cost. O(n)
	 * @param root The Node we are currently at
	 * @param currentCost the current cost of the node.
	 */
	void dynamicRecursion(Node root, int currentCost){
	
		nodeStack.push(root);
		if(root.left != null){
			dynamicRecursion(root.left, root.cost + currentCost);
		}
		if(root.right != null){
			nodeStack.pop();
			dynamicRecursion(root.right, currentCost);
		}
		
		if(root.isLeaf()){
			Stack<Node> temp = (Stack<Node>) nodeStack.clone();
			setList.add(temp);
			nodeStack.pop();
			if(root.cost + currentCost < cheapestPrice || cheapestPrice == 0){
				cheapestSet = temp;
				cheapestPrice = root.cost + currentCost;
			}
		}		
	}	
	
	/**
	 * Recursively divides and conquers based of the minimum of the left and right node. O(n)
	 * @param root The Node you wish to start with
	 * @param currentCost the current cost of the node
	 * @return the minimum value of the two nodes.
	 */
	int divideAndConquer(Node root, int currentCost){
		if(root.isLeaf())		return root.cost + currentCost; 
		else					return Math.min(divideAndConquer(root.left, currentCost + root.cost), 
												divideAndConquer(root.right, currentCost));
	}
	
	
	/**
	 * Iterates through all the sets and prints out the cheapest one.
	 * O(n^2)
	 * @param shortestPath
	 */
	void printDAndC(int shortestPath){
		generateSets(root);
		int lowestPrice = 0;
		Stack<Node> cheapSet = new Stack<Node>();
		for(Stack<Node> s : setList){
			Stack<Node> temp = (Stack<Node>) s.clone();
			int total = 0;
			while(temp.size() > 0){
				total += temp.pop().cost;
			}
			if(lowestPrice == 0 || lowestPrice > total){
				lowestPrice = total;
				cheapSet = s;
			}
		}
		Stack<Node> temp = flipStack(cheapSet);
		while(temp.size() > 0){
			System.out.print(temp.pop().cost+"\t");
		}
		System.out.println();
		
	}
	
	
	
	/**
	 * Brute for implementation for finding the cheapest path. O(n^2 + n)
	 */
	void bruteForce(){
		System.out.println("Brute Force");
		generateSets(root);
		int[] setSizes = new int[setList.size()];
		int counter = 0;
		int lowestPrice = 0;
		int cheapestPath = 1;
		Stack<Node> cheapSet = new Stack<Node>();
		for(Stack<Node> s : setList){
			Stack<Node> temp = (Stack<Node>) s.clone();
			int total = 0;
			while(temp.size() > 0){
				total += temp.pop().cost;
			}
			setSizes[counter++] = total;
			if(lowestPrice == 0 || lowestPrice > total){
				cheapestPath = counter;
				lowestPrice = total;
				cheapSet = s;
			}
		}
		System.out.println("The cheapest price is: "+lowestPrice+" from path "+cheapestPath);
		Stack<Node> temp = flipStack(cheapSet);
		while(temp.size() > 0){
			System.out.print(temp.pop().cost+"\t");
		}
		System.out.println();
		setList.clear();
		nodeStack.clear();
		
	}	
	
	/*
	 * ANDY PLEASE COMMENT WITH RUNTIME
	 */
	void dynamicIteration(Node[][] m) {

		int maxLen = m.length;
  
		//new matrix to hold min cost to any specific post
		int [] cTP = new int[maxLen]; //cost to post
		int [] lV = new int[maxLen]; // Last visited at each post
  
		cTP[0] = 0;
		for (int i = 1; i < maxLen; i++) {
			cTP[i] = cTP[i - 1] + m[i - 1][i].cost;
			lV[i] = i - 1;
			for (int j = i - 2; j >= 0; j--) {
				if (cTP[j] + m[j][i].cost < cTP[i]) {
					cTP[i] = cTP[j] + m[j][i].cost;
					lV[i] = j;
				}
			}
		}
  
		System.out.println("Shortest path costs: " + cTP[maxLen - 1]);
		for (int i = 1; i < cTP.length; i++) {
			System.out.print(cTP[i]);
			if (i != cTP.length - 1) 
				System.out.print(", \t");
			else
				System.out.println();
		}
		System.out.println("Nodes visited: ");
		for (int i = 1; i < lV.length; i++) {
			System.out.print(lV[i] + 1);
			if (i != lV.length - 1) 
				System.out.print(", \t");
			else
				System.out.println();
		}
	}
	
	
	/**
	 * Prints out all built sets
	 */
	void printOutSets(){
		setList.clear();
		generateSets(root);
		int printCounter = 1;
		int total = 0;
		
		for(Stack<Node> s : setList){
			total = 0;
			Stack<Node> temp = flipStack(s);
			System.out.print("Path "+printCounter++ +": ");
			while(temp.size() > 0){
				int current = temp.pop().cost;
				total += current;
				System.out.print(current + "\t");
			}
			System.out.print(" = "+total);
			System.out.println();
		}
		System.out.println();
		setList.clear();
		nodeStack.clear();
	}
	
	/**
	 * Flips a stack and prints it out.
	 */
	void printCheapestSet(){
		Stack<Node> temp = flipStack(cheapestSet);
		while(temp.size()>0){
			System.out.print(temp.pop().cost+"\t");
		}
		System.out.println();
	}
	
	/**
	 * Flips the stack to help with printing clarification
	 * @param stack the Stack you want to flip
	 * @return flipped stack
	 */
	Stack<Node> flipStack(Stack<Node> stack)
	{
		Stack<Node> returnStack = new Stack<Node>();
		while(stack.size() > 0){
			Node addNode = new Node(stack.pop().cost);
			returnStack.push(addNode);
		}
		return returnStack;
	}
	
	
	
	
	
	/**
	 * Private class used to turn our 2D array into a tree.
	 * @author Joe Greive
	 *
	 */
	private class Node {
		private Node left;
		private Node right;
		public int cost;
		
		/**
		 * Sets the cost of the current Node
		 * @param cost cost of the Node
		 */
		private Node(int cost){
			this.cost = cost;
		}
		
		/**
		 * Checks if current node has any children
		 * @return whether or not current Node has children
		 */
		private boolean isLeaf() {
			return (left == null && right == null);
		}
	}
}
