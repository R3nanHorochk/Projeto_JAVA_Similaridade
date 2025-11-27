package rha;

import java.util.ArrayList;
import java.util.List;

public class BSTR extends BinaryTreeR {

    public BSTR() {
        super();
    }

    public BSTR(NodeR root) {
        super(root);
    }

    /** ------------------------------- SEARCH ----------------------------------- */

    public NodeR search(Resultado data) {
        return search(root, data);
    }

    private NodeR search(NodeR node, Resultado data) {
        if (node == null) return null;

        double diff = data.getSimilaridade() - node.getData().getSimilaridade();

        if (diff < 0) return search(node.getLeft(), data);
        else if (diff > 0) return search(node.getRight(), data);
        else return node;
    }

    /** ------------------------------- INSERT ----------------------------------- */

    public void insert(Resultado data) {
        root = insert(root, null, data);
    }

    // retornamos NodeR (não Node)
    protected NodeR insert(NodeR node, NodeR parent, Resultado data) {
        if (node == null) {
            return new NodeR(data, parent);
        }

        double diff = data.getSimilaridade() - node.getData().getSimilaridade();

        if (diff < 0) {
            node.setLeft(insert(node.getLeft(), node, data));
        } else if (diff > 0) {
            node.setRight(insert(node.getRight(), node, data));
        } else {
            // chave duplicada: substituir o conteúdo
            node.setData(data);
        }

        return node;
    }

    /** ------------------------------- REMOVE ----------------------------------- */

    public void remove(Resultado data) {
        root = remove(root, data);
    }

    protected NodeR remove(NodeR node, Resultado data) {
        if (node == null) {
            throw new RuntimeException("Nó com chave " + data + " não existe na BST!");
        }

        double diff = data.getSimilaridade() - node.getData().getSimilaridade();

        if (diff < 0) {
            node.setLeft(remove(node.getLeft(), data));
        } else if (diff > 0) {
            node.setRight(remove(node.getRight(), data));
        } else {
            node = removeNode(node);
        }

        return node;
    }

    private NodeR removeNode(NodeR node) {
        // folha
        if (node.isLeaf()) {
            return null;
        }

        // um filho (somente direita)
        if (!node.hasLeftChild()) {
            NodeR r = node.getRight();
            if (r != null) r.setParent(node.getParent());
            return r;
        } else if (!node.hasRightChild()) { // um filho (somente esquerda)
            NodeR l = node.getLeft();
            if (l != null) l.setParent(node.getParent());
            return l;
        }

        // dois filhos -> predecessor
        NodeR predecessor = findPredecessor(node.getData());
        node.setData(predecessor.getData());
        // ao remover predecessor, o parent da chamada recursiva deve ser o node atual
        node.setLeft(remove(node.getLeft(), predecessor.getData()));

        return node;
    }

    /** ----------------------- MIN, MAX, PREDECESSOR, SUCCESSOR -------------------- */

    public NodeR findMin() {
        return findMin(root);
    }

    private NodeR findMin(NodeR node) {
        if (node == null) return null;
        while (node.hasLeftChild()) node = node.getLeft();
        return node;
    }

    public NodeR findMax() {
        return findMax(root);
    }

    private NodeR findMax(NodeR node) {
        if (node == null) return null;
        while (node.hasRightChild()) node = node.getRight();
        return node;
    }

    public NodeR findPredecessor(Resultado data) {
        NodeR node = search(data);
        return predecessor(node);
    }

    private NodeR predecessor(NodeR node) {
        if (node == null) return null;

        if (node.hasLeftChild()) return findMax(node.getLeft());

        NodeR current = node;
        NodeR parent = node.getParent();

        while (parent != null && current == parent.getLeft()) {
            current = parent;
            parent = current.getParent();
        }
        return parent;
    }

    public NodeR findSuccessor(Resultado data) {
        NodeR node = search(data);
        return successor(node);
    }

    private NodeR successor(NodeR node) {
        if (node == null) return null;

        if (node.hasRightChild()) return findMin(node.getRight());

        NodeR current = node;
        NodeR parent = node.getParent();

        while (parent != null && current == parent.getRight()) {
            current = parent;
            parent = current.getParent();
        }
        return parent;
    }

    /** ------------------------------- CLEAR ----------------------------------- */

    public void clear() {
        root = clear(root);
    }

    private NodeR clear(NodeR node) {
        if (node == null) return null;
        node.setLeft(clear(node.getLeft()));
        node.setRight(clear(node.getRight()));
        node.setParent(null);
        return null;
    }

    /** ------------------------------- HELPERS ----------------------------------- */

    public List<Resultado> getAllData() {
        List<Resultado> result = new ArrayList<>();
        inOrder(this.root, result);
        return result;
    }

    // inOrder usa NodeR
    private void inOrder(NodeR node, List<Resultado> list) {
        if (node == null) return;
        inOrder(node.getLeft(), list);
        list.add(node.getData());
        inOrder(node.getRight(), list);
    }

    @Override
    public String toString() {
        return "BST - isEmpty(): " + isEmpty()
                + ", getDegree(): " + getDegree()
                + ", getHeight(): " + getHeight()
                + ", root => { " + root + " }";
    }
}
