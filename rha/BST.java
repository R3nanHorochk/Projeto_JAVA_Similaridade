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


public class BST extends BinaryTree {

	public BST() {
		super();
	}

	public BST(Node root) {
		super(root);
	}
	
	  int string_int(String palavra) {
			int sum = 0;
			int word = 0;
			for(int i = 0; i < palavra.length(); i++) {
				word = palavra.charAt(i);
				sum += word;
				
			}
			return sum;
		}

	
	public Node search(WordData data) {
		return search(root, data);
	}

	private Node search(Node node, WordData data) {
		if (node == null) {
			return null;
		}
		
		int diff = string_int(data.getWord()) - string_int(node.getData().getWord());

		if (diff < 0) {
			return search(node.getLeft(), data);
		} else if (diff > 0) {
			return search(node.getRight(), data);
		} else {
			return node;
		}
	}
	
	public void insert(WordData data) {
		root = insert(root, null, data);
	}
	
	protected Node insert(Node node, Node parent, WordData data) {
		if (node == null) {
			return new Node(data, parent);
		}

		int diff = Integer.parseInt(data.getWord()) - Integer.parseInt(node.getData().getWord());

		if (diff < 0) {
			node.setLeft(insert(node.getLeft(), node, data));
		} else if (diff > 0) {
			node.setRight(insert(node.getRight(), node, data));
		} else {
			node.getData().setFrequency((node.getData().getFrequency() + 1));
		}
		
		return node;
	}
	
	public void remove(WordData data) {
		root = remove(root, data);
	}
	
	protected Node remove(Node node, WordData data) {
		if (node == null) {
			//return null;
			throw new RuntimeException("Nó com chave " + data + " não existe na BST!");
		}
		
		int diff = string_int(data.getWord()) - string_int(node.getData().getWord());
	
		if (diff < 0) {
			node.setLeft(remove(node.getLeft(), data));
		} else if (diff > 0) {
			node.setRight(remove(node.getRight(), data));
		} else {
			node = removeNode(node);
		}
		
		return node;		
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
			node.setLeft(remove(node.getLeft(), predecessor.getData()));
		}
		
		return node;		
	}
	
	public Node findMin() {
		return findMin(root);
	}
	
	private Node findMin(Node node) {
		if (node == null) {
			return null;
		}

		while (node.hasLeftChild()) {
			node = node.getLeft();
		}
		return node;
	}
	
	public Node findMax() {
		return findMax(root);
	}
	
	private Node findMax(Node node) {
		if (node == null) {
			return null;
		}

		while (node.hasRightChild()) {
			node = node.getRight();
		}
		return node;
	}
	
	public Node findPredecessor(WordData data) {
		Node node = search(data);
		return predecessor(node);
	}
	
	private Node predecessor(Node node) {
		if (node == null) {
			return null;
		}
		
		if (node.hasLeftChild()) {
			return findMax(node.getLeft());
		} else {
			Node current = node;
			Node parent = node.getParent();

			while (parent != null && current == parent.getLeft()) {
				current = parent;
				parent = current.getParent();
			}
			
			return parent;
		}
	}
	
	public Node findSuccessor(WordData data) {
		Node node = search(data);
		return successor(node);		
	}
	
	private Node successor(Node node) {
		if (node == null) {
			return null;
		}

		if (node.hasRightChild()) {
			return findMin(node.getRight());
		} else {
			Node current = node;
			Node parent = node.getParent();

			while (parent != null && current == parent.getRight()) {
				current = parent;
				parent = current.getParent();
			}
			
			return parent;
		}
	}
	
	public void clear() {
		root = clear(root);
	}
	
	private Node clear(Node node) {
		if (node == null) {
			return null;
		}

		// Pós-ordem = percurso LRN.
		node.setLeft(clear(node.getLeft()));
		node.setRight(clear(node.getRight()));
		node.setParent(null);

		return null;
	}

	@Override
	public String toString() {
		return "BST - isEmpty(): " + isEmpty()
				+ ", getDegree(): " + getDegree()
				+ ", getHeight(): " + getHeight()
				+ ", root => { " + root + " }";				
	}

}
