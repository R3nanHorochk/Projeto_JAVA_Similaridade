//
// Árvore AVL (Rotações) - Exemplo de implementação em Java
// Copyright (C) 2024 André Kishimoto
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
//
package rha;


public class AVL extends BST {

	public AVL() {
		super();
	}

	public AVL(Node root) {
		super(root);
	}

	private void updateParentChild(Node parent, final Node child, Node newChild) {
		if (parent != null) {
			if (parent.getLeft() == child) {
				parent.setLeft(newChild);
			} else {
				parent.setRight(newChild);
			}
		} else {
			root = newChild;
			newChild.setParent(null);
		}
	}
	
	// Rotação LL.
	private Node rotateLeft(Node node) {
		if (node == null) {
			return null;
		}
		
		// O nó atual deve ter um filho direito, que será a nova raiz desta subárvore.
		Node newRoot = node.getRight();
		if (newRoot == null) {
			return null;
		}
		
		// Troca as conexões do nó pai (newRoot vira filho de parent, no lugar de node).
		Node parent = node.getParent();
		updateParentChild(parent, node, newRoot);
		
		// newRoot é a nova raiz desta subárvore, então seu filho esquerdo se torna o
		// filho direito de node (que deixa de ser raiz desta subárvore).
		Node left = newRoot.getLeft();
		node.setRight(left);

		// node agora vira filho esquerdo de newRoot.
		newRoot.setLeft(node);
		System.out.println("Rotação Left ");
		return newRoot;
	}
	
	// Rotação RR.
	private Node rotateRight(Node node) {
		if (node == null) {
			return null;
		}
		
		// O nó atual deve ter um filho esquerdo, que será a nova raiz desta subárvore.
		Node newRoot = node.getLeft();
		if (newRoot == null) {
			return null;
		}
		
		// Troca as conexões do nó pai (newRoot vira filho de parent, no lugar de node).
		Node parent = node.getParent();
		updateParentChild(parent, node, newRoot); 
		
		// newRoot é a nova raiz desta subárvore, então seu filho direito se torna o
		// filho esquerdo de node (que deixa de ser raiz desta subárvore).
		Node right = newRoot.getRight();
		node.setLeft(right);
		
		// node agora vira filho direito de newRoot.
		newRoot.setRight(node);
		System.out.println("Rotação Right");
		return newRoot;
	}
	
	// Rotação LR.
	private Node rotateLeftRight(Node node) {
		node.setLeft(rotateLeft(node.getLeft()));
		System.out.println("Rotação Left Right");
		return rotateRight(node);
		
	}
	
	// Rotação RL.
	private Node rotateRightLeft(Node node) {
		node.setRight(rotateRight(node.getRight()));
		System.out.println("Rotação Right Left");
		return rotateLeft(node);
	}
	

	// Teste de rotação (manual) para árvore AVL com inserção de 1, 2, 3 (nesta sequência).
	public void testRotateLeft() {
		rotateLeft(root);
	}
	
	// Teste de rotação (manual) para árvore AVL com inserção de 3, 2, 1 (nesta sequência).
	public void testRotateRight() {
		rotateRight(root);
	}
	
	// Teste de rotação (manual) para árvore AVL com inserção de 3, 1, 2 (nesta sequência).
	public void testRotateLeftRight() {
		rotateLeftRight(root);
	}
	
	// Teste de rotação (manual) para árvore AVL com inserção de 1, 3, 2 (nesta sequência).
	public void testRotateRightLeft() {
		rotateRightLeft(root);
	}
	
	@Override
	public String toString() { if(!isEmpty()) {
		return "AVL - isEmpty(): " + isEmpty()
				+ ", getDegree(): " + getDegree()
				+ ", getHeight(): " + getHeight()
				+ ", root => { " + root + " }";	
	}else {
		return "A";
	}
	}
	public int getBalance(Node no) {
		
		    int HeightRight = (no.getRight() == null) ? -1 : no.getRight().getHeight();
		    int HeightLeft  = (no.getLeft()  == null) ? -1 : no.getLeft().getHeight();
		    return HeightRight - HeightLeft;
		

	}

	public Node CheckBalance(Node no) {
		int balance = this.getBalance(no);
		
		if(balance > 1) {
			if (getBalance(no.getRight()) < 0) {
				return  rotateRightLeft(no); 
	        } else {
	        	return rotateLeft(no); 
	        }
		}else if(balance < -1 ) {
			
			if(getBalance(no.getLeft()) > 0) {
				return rotateLeftRight(no);
			}else {
				return rotateRight(no);
			}
		}
		return no;
	}
	
	public void insert_ALV(WordData data) {
		 root = insert_ALV(root, null, data);
	}
	
	protected Node insert_ALV(Node node, Node parent, WordData data) {
	    if (node == null) {
	        return new Node(data, parent);
	    }

	    int diff = string_int(data.getWord()) - string_int(node.getData().getWord());

	    if (diff < 0) {
	        node.setLeft(insert_ALV(node.getLeft(), node, data));
	    } else if (diff > 0) {
	        node.setRight(insert_ALV(node.getRight(), node, data));
	    } else {
	        throw new RuntimeException("Essa BST não pode ter duplicatas!");
	    }
	    Node balanced = CheckBalance(node);
	    return balanced;
	}
	
	
	public void remove_ALV(WordData data) {
		root = remove_ALV(root, null, data);
	}
	
	protected Node remove_ALV(Node node, Node parent, WordData data) {
		if (node == null) {
			return null;
		}

		int diff = Integer.parseInt(data.getWord()) - Integer.parseInt(node.getData().getWord());
		
		if (diff < 0) {
			node.setLeft(remove_ALV(node.getLeft(), node, data));
		} else if (diff > 0) {
			node.setRight(remove_ALV(node.getRight(), node, data));
		} else {
			node = removeNode(node);	
		}
		Node balanced = CheckBalance(node);
		return balanced;
	}
	
	private Node removeNode(Node node) {
		if (node.isLeaf()) {
			return null;
		}
		
		if (!node.hasLeftChild()) {
			return node.getRight();
		} else if (!node.hasRightChild()) {
			return node.getLeft();
		} else {
			Node predecessor = findPredecessor(node.getData());
			node.setData(predecessor.getData());
			node.setLeft(remove_ALV(node.getLeft(),null, predecessor.getData()));
		}
		
		return node;		
	}
	
	public void printTree() {
	    System.out.println("Árvore AVL:");
	    printTree(root, 0);
	}

	// Método recursivo auxiliar
	private void printTree(Node node, int level) {
	    if (node == null) {
	        return;
	    }

	    printTree(node.getRight(), level + 1);

	    for (int i = 0; i < level; i++) {
	        System.out.print("    ");
	    }
	    
//	    int HeightRight = (node.getRight() == null) ? -1 : node.getRight().getHeight();
//	    int HeightLeft  = (node.getLeft() == null) ? -1 : node.getLeft().getHeight();
//	    System.out.println(node.getData() + " - " + node.getBalanceFactor() 
//	    + " - " + HeightRight + " - " + HeightLeft);
	    
	    System.out.println(node);
	    

	    // Por fim, imprime o filho da esquerda (para aparecer “embaixo”)
	    printTree(node.getLeft(), level + 1);
	}

}
