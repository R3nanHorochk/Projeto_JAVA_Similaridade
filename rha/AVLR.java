package rha;

import java.util.ArrayList;
import java.util.List;

public class AVLR extends BSTR {

    private int count = 0; // usado por printTop

    public AVLR() {
        super();
    }

    public AVLR(NodeR root) {
        super(root);
    }

    /**
     * Atualiza a ligação do parent para apontar para newChild em vez de child.
     * Se parent for null, atualiza a raiz.
     */
    private void updateParentChild(NodeR parent, final NodeR child, NodeR newChild) {
        if (parent != null) {
            if (parent.getLeft() == child) parent.setLeft(newChild);
            else parent.setRight(newChild);
        } else {
            root = newChild;
            if (newChild != null) newChild.setParent(null);
        }
    }

    // Rotação à esquerda (LL case)
    private NodeR rotateLeft(NodeR node) {
        if (node == null) return null;
        NodeR newRoot = node.getRight();
        if (newRoot == null) return null;

        NodeR parent = node.getParent();
        // reconnect parent -> newRoot
        updateParentChild(parent, node, newRoot);

        // subtree adjustments
        NodeR leftOfNewRoot = newRoot.getLeft();
        node.setRight(leftOfNewRoot); // pode ser null
        newRoot.setLeft(node);

        // ajustar parents (setLeft/setRight já fazem setParent)
        return newRoot;
    }

    // Rotação à direita (RR case)
    private NodeR rotateRight(NodeR node) {
        if (node == null) return null;
        NodeR newRoot = node.getLeft();
        if (newRoot == null) return null;

        NodeR parent = node.getParent();
        updateParentChild(parent, node, newRoot);

        NodeR rightOfNewRoot = newRoot.getRight();
        node.setLeft(rightOfNewRoot);
        newRoot.setRight(node);

        return newRoot;
    }

    // LR
    private NodeR rotateLeftRight(NodeR node) {
        node.setLeft(rotateLeft(node.getLeft()));
        return rotateRight(node);
    }

    // RL
    private NodeR rotateRightLeft(NodeR node) {
        node.setRight(rotateRight(node.getRight()));
        return rotateLeft(node);
    }

    /** ------------- Balance checks ------------- */

    public int getBalance(NodeR no) {
        if (no == null) return 0;
        int hr = (no.getRight() == null) ? -1 : no.getRight().getHeight();
        int hl = (no.getLeft() == null) ? -1 : no.getLeft().getHeight();
        return hr - hl;
    }

    public NodeR checkBalance(NodeR no) {
        if (no == null) return null;

        int balance = getBalance(no);

        if (balance > 1) { // right heavy
            if (getBalance(no.getRight()) < 0) { // RL
                return rotateRightLeft(no);
            } else { // RR
                return rotateLeft(no);
            }
        } else if (balance < -1) { // left heavy
            if (getBalance(no.getLeft()) > 0) { // LR
                return rotateLeftRight(no);
            } else { // LL
                return rotateRight(no);
            }
        }
        return no;
    }
  
    /** ------------------------------- INSERT AVL ----------------------------------- */

    public void insertAVL(Resultado data) {
        root = insertAVL(root, null, data);
    }
    private int extrairNumero(String nome) {
    return Integer.parseInt(nome.replaceAll("\\D+", ""));
    }

    // assinatura corrigida: data é Resultado (não NodeR)
    protected NodeR insertAVL(NodeR node, NodeR parent, Resultado data) {
        if (node == null) {
            return new NodeR(data, parent);
        }
        int num1 =extrairNumero(data.getNomeFile1());
        int num2 =extrairNumero(data.getNomeFile2()) ;
        int num3 =extrairNumero(node.getData().getNomeFile1()) ;
        int num4 =extrairNumero(node.getData().getNomeFile2()) ;
        int num11 = num1 + num2;
        int num22 = num3 + num4;
        int diff = num11 - num22;

        if (diff < 0 ) {
            node.setLeft(insertAVL(node.getLeft(), node, data));
        } else if (diff > 0) {
            node.setRight(insertAVL(node.getRight(), node, data));
        } else {
            throw new RuntimeException("AVL não permite duplicatas para a chave similaridade!");
        }

        NodeR balanced = checkBalance(node);
        return balanced;
    }

    /** ------------------------------- REMOVE AVL ----------------------------------- */

    public void removeAVL(Resultado data) {
        root = removeAVL(root, null, data);
    }

    // retorna NodeR (não Node)
    protected NodeR removeAVL(NodeR node, NodeR parent, Resultado data) {
        if (node == null) return null;

        double diff = data.getSimilaridade() - node.getData().getSimilaridade();

        if (diff < 0) {
            node.setLeft(removeAVL(node.getLeft(), node, data));
        } else if (diff > 0) {
            node.setRight(removeAVL(node.getRight(), node, data));
        } else {
            node = removeNode(node);
        }

        NodeR balanced = checkBalance(node);
        return balanced;
    }

    // Reuso do removeNode do BST (mantém predecessor)
    private NodeR removeNode(NodeR node) {
        if (node.isLeaf()) return null;

        if (!node.hasLeftChild()) {
            NodeR r = node.getRight();
            if (r != null) r.setParent(node.getParent());
            return r;
        } else if (!node.hasRightChild()) {
            NodeR l = node.getLeft();
            if (l != null) l.setParent(node.getParent());
            return l;
        } else {
            NodeR predecessor = findPredecessor(node.getData());
            node.setData(predecessor.getData());
            // ao remover predecessor, passe o parent correto (node)
            node.setLeft(removeAVL(node.getLeft(), node, predecessor.getData()));
        }

        return node;
    }

    /** ------------------------------- UTILITÁRIOS ----------------------------------- */

    public void printTree() {
        System.out.println("Árvore AVL:");
        printTree(root, 0);
    }

    public List<Resultado> getAllData() {
        List<Resultado> result = new ArrayList<>();
        inOrder(this.root, result);
        return result;
    }

    // printTop usando getters e variable count
    private void printTop(NodeR node, int x) {
        if (node == null || count >= x) {
            return;
        }

        // Percorre do maior para o menor
        printTop(node.getRight(), x);

        if (count < x) {
            Resultado r = node.getData();
            System.out.println(
                r.getNomeFile1() + " <-> " +
                r.getNomeFile2() + " = " +
                String.format("%.2f", r.getSimilaridade())
            );
            count++;
        }

        printTop(node.getLeft(), x);
    }

    public void printTop(int x) {
        count = 0;
        printTop(root, x);
    }

    private void inOrder(NodeR node, List<Resultado> list) {
        if (node == null) return;
        inOrder(node.getLeft(), list);
        list.add(node.getData());
        inOrder(node.getRight(), list);
    }

    private void printTree(NodeR node, int level) {
        if (node == null) return;

        printTree(node.getRight(), level + 1);

        for (int i = 0; i < level; i++) System.out.print("    ");
        System.out.println(node);

        printTree(node.getLeft(), level + 1);
    }

    @Override
    public String toString() {
        if (!isEmpty()) {
            return "AVL - isEmpty(): " + isEmpty()
                    + ", getDegree(): " + getDegree()
                    + ", getHeight(): " + getHeight()
                    + ", root => { " + root + " }";
        } else {
            return "AVL - empty";
        }
    }
}
