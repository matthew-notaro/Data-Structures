package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node sumFront = null;
		Node ptrSum = null;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		
		//Keep going until both ptrs == null
		while(ptr1 != null || ptr2 != null) {
				
			//If poly1 null / reach end of poly1 first
			if(ptr1 == null) {
				if(ptrSum == null) {
					ptrSum = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					sumFront = ptrSum;
				}
				else {
					ptrSum.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					ptrSum = ptrSum.next;
				}
			}
			
			//If poly2 null / reach end of poly2 first
			else if(ptr2 == null) {
				if(ptrSum == null) {
					ptrSum = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					sumFront = ptrSum;
				}
				else {
					ptrSum.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					ptrSum = ptrSum.next;
				}
			}
			
			//If can add together terms, move up pointers
			else if(ptr1.term.degree == ptr2.term.degree) {
				
				//If coeff+coeff=0, skip term,
				if(ptr1.term.coeff + ptr2.term.coeff == 0) {
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
				}
				else if(ptrSum == null) {
					ptrSum = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					sumFront = ptrSum;
				}
				else {
					ptrSum.next = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
					ptrSum = ptrSum.next;
				}
			}
			
			//If poly1's degree bigger
			else if(ptr1.term.degree > ptr2.term.degree) {
				if(ptrSum == null) {
					ptrSum = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					sumFront = ptrSum;
				}
				else {		
					ptrSum.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
					ptr2 = ptr2.next;
					ptrSum = ptrSum.next;
				}
			}
			//If poly2's degree bigger
			else if(ptr1.term.degree < ptr2.term.degree) {
				if(ptrSum == null) {
					ptrSum = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					sumFront = ptrSum;
				}
				else {		
					ptrSum.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
					ptr1 = ptr1.next;
					ptrSum = ptrSum.next;
				}
			}
		}
		return sumFront;
	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node prodFront = null; 
		Node ptrProd, tempFront, ptrTemp;
		Node ptr1 = poly1;
		Node ptr2 = poly2;

		//Multiply 1st term in poly1 thru poly2 and store result in prodFront
		if(ptr1 != null && ptr2 != null) {
			prodFront = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, null);
			ptrProd = prodFront;
			ptr2 = ptr2.next;
			while(ptr2 != null) {
				ptrProd.next = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, null);
				ptrProd = ptrProd.next;
				ptr2 = ptr2.next;
			}
			ptrProd = prodFront;
			ptr2 = poly2;
			ptr1 = ptr1.next;
		}
		
		//Multiply next term in poly2 thru poly2 and store result in tempFront
		while(ptr1 != null && ptr2 != null) {
			tempFront = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, null);
			ptrTemp = tempFront;
			ptr2 = ptr2.next;
			while(ptr2 != null) {
				ptrTemp.next = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree+ptr2.term.degree, null);
				ptrTemp = ptrTemp.next;
				ptr2 = ptr2.next;
			}
			
			//Add together prodFront and tempFront and store result in prodFront
			prodFront = Polynomial.add(prodFront, tempFront);
			ptr2 = poly2;
			ptr1 = ptr1.next;
		}
		return prodFront;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		Node ptr = poly;
		float eval = 0;
		while(ptr != null) {
			eval += Math.pow(x, ptr.term.degree) * ptr.term.coeff;
			ptr = ptr.next;
		}
		return eval;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
