package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		if(graph == null)
			return null;
		//1 - make new list of PTs
		PartialTreeList L = new PartialTreeList();
		//2 - fill in list with data from graph
		for(Vertex v : graph.vertices) {
			PartialTree T = new PartialTree(v);
			Vertex.Neighbor ptr = v.neighbors;
			while(ptr != null) {
				Arc arc = new Arc(v, ptr.vertex, ptr.weight);
				T.getArcs().insert(arc);
				ptr = ptr.next;
			}
			L.append(T);
		}
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		ArrayList<Arc> MST = new ArrayList<Arc>();
		
		while(ptlist.size() > 1) {
			//3 - remove PT from L
			PartialTree PTX = ptlist.remove();
			//4 - remove min from heap
			MinHeap<Arc> heap = PTX.getArcs();
			Arc minArc = heap.deleteMin();
			while(minArc != null) {
				//4 - get vertices
				Vertex v1 = minArc.getv1();
				Vertex v2 = minArc.getv2();
				//5/7 - checks if v2 belongs to PTX and removes v2's PT from L
				PartialTree PTY = ptlist.removeTreeContaining(v1);
				if(PTY == null)
					PTY = ptlist.removeTreeContaining(v2);
				if(PTY != null) {
					//6 - add arc to MST
					MST.add(minArc);
					//8 - merge PTs and assign to PTX
					PTX.merge(PTY);
					ptlist.append(PTX);
					break;
				}
				
				//9 - remove and continue to next if it exists
				minArc = heap.deleteMin();
			}
		}
		return MST;
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    	if(rear == null)
    		throw new NoSuchElementException("Start out null.");
    	PartialTree result = null;
    	
    	Node ptr = rear;
    	do {
    		//Checks if vertex is in PT
    		Vertex ptr2 = vertex;
    		while(ptr2.parent != ptr2)
    			ptr2 = ptr2.parent;
    		if(ptr2 == ptr.tree.getRoot()) {
    			
    			result = ptr.tree;
                
                //Removes tree from list
    			Node prev = ptr;
    			while(prev.next != ptr)
    				prev = prev.next;
    			//1 node
    			if(ptr.next == ptr && prev == ptr)
    				rear = null;
    			//2 nodes
    			else if(ptr.next == prev) {
    				if(ptr == rear)
    					rear = rear.next;
    				ptr.next.next = ptr.next;
    			}
    			//3+ nodes
    			else {
    				if(ptr == rear)
    					rear = prev;
    				prev.next = ptr.next;
    			}
    			size--;
    			ptr.next = null;
    			break;
    		}
    		ptr = ptr.next;
    	} while(ptr != rear);
    	return result;
     }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


