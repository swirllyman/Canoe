import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Canoe {

	Node[][] data;
	Node root;
	Stack<Node> nodeStack = new Stack<Node>();
	ArrayList<Stack<Node>> sets = new ArrayList<Stack<Node>>();
	int sizeSet;
	int path = 1;
	int fastestPath = 0;
	int lowestPathCost = 0;
	
	public Canoe(int size){
		sizeSet = size;
		buildData(sizeSet);
		buildTree();
		
		System.out.println("Brute Force");
		recursiveBrute(root, 0);
		
		path = 1;
		System.out.println("\nDivide and Conquer");
		divideAndConquer(root, 0);
		
		System.out.println("\nFastest path costs: "+lowestPathCost+"\n");
		
		
		System.out.println("\nDynamic");
		dynamicIteration(data);
		
		System.out.println("\nAll available paths");
		printOutSets();
		
		
	}
	
	void buildData(int size){
		Random r = new Random();
		data = new Node[size][size];
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data.length; j++){
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
		
		for(int i = 0; i < data.length; i++){
			for(int j = 1; j < data.length; j++){
				System.out.print(data[i][j].cost+"\t");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	int printCounter = 1;
	void printOutSets(){
		for(Stack<Node> s : sets){
			System.out.print("Path "+printCounter++ +": ");
			while(s.size() > 0){
				System.out.print(s.pop().cost + "\t");
			}
			System.out.println();
		}
	}
	
	
	
	void buildTree(){
		root = data[0][1];
		for(int i = 0; i < data.length; i++){
			for(int j = i+1; j < data.length-1; j++){
				data[i][j].right = data[i][j+1];
				data[i][j].left = data[j][j+1];
			}
		}
		
		recursiveSetGeneration(root);
	}
	
		
	Stack<Node> flipStack(Stack<Node> stack)
	{
		Stack<Node> returnStack = new Stack<Node>();
		while(stack.size() > 0){
			returnStack.push(stack.pop());
		}
		return returnStack;
	}
	
	
	void recursivePrint(Node root){
		if(root.left != null){
			recursivePrint(root.left);
		}
		if(root.right != null){
			recursivePrint(root.right);
		}
		
		System.out.print(root.cost + " ");
		
	}
	
	void recursiveSetGeneration(Node root){
		
		nodeStack.push(root);
		
		if(root.left != null){
			recursiveSetGeneration(root.left);
		}
		if(root.right != null){
			recursiveSetGeneration(root.right);
		}
		
		if(root.isLeaf()){
			Stack<Node> temp = (Stack<Node>) nodeStack.clone();
			temp = flipStack(temp);
			sets.add(temp);
		}
		
		nodeStack.pop();
	}

	/**
	 * Brute force method to recursively find the cheapest path
	 * O(2^(n-2))
	 * @param root
	 * @param currentCost
	 * @return
	 */
	int recursiveBrute(Node root, int currentCost){
		if(root.left != null){
			recursiveBrute(root.left, root.cost + currentCost);
		}
		if(root.right != null){
			recursiveBrute(root.right, currentCost);
		}
		if(root.isLeaf()){
			System.out.println("Path "+ path++ +" cost: "+ (root.cost +currentCost));
			
			if(root.cost + currentCost < lowestPathCost || lowestPathCost == 0){
				//fastestPath++;
				lowestPathCost = root.cost + currentCost;
			}
			
			return root.cost + currentCost;
		}
		else
			return -1;
	}
	
	
	int divideAndConquer(Node root, int currentCost){
		if(root.isLeaf()){
			System.out.println("Path "+ path++ +" cost: "+ (root.cost +currentCost));			
			return root.cost + currentCost;
		}
		else
			return Math.min(divideAndConquer(root.left, currentCost + root.cost), divideAndConquer(root.right, currentCost));
	}
	
	
	Node[] memo = new Node[sizeSet];
	int dynamicize(Node root){
		
		return 0;
	}
	
	
	
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private class Node {
		private Node left;
		private Node right;
		public int cost;
		
		private Node(int cost){
			this.cost = cost;
		}
		
		
		private boolean isLeaf() {
			return (left == null && right == null);
		}
	}
}
