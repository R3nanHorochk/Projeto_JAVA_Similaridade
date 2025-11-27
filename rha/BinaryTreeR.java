package rha;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTreeR {

    protected NodeR root;

    public BinaryTreeR() {
        this(null);
    }

    public BinaryTreeR(NodeR root) {
        this.root = root;
    }

    /** ------------------------------- PROPRIEDADES -------------------------------- */

    public boolean isEmpty() {
        return root == null;
    }

    public int getHeight() {
        return isEmpty() ? -1 : root.getHeight();
    }

    public int getDegree() {
        return getDegree(root);
    }

    private int getDegree(NodeR node) {
        if (node == null || node.isLeaf()) {
            return 0;
        }

        int degree = node.getDegree();
        degree = Math.max(degree, getDegree(node.getLeft()));
        degree = Math.max(degree, getDegree(node.getRight()));

        return degree;
    }

    /** ------------------------------- TRAVERSALS -------------------------------- */

    public String inOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        inOrder(sb, root);
        return sb.toString();
    }

    private void inOrder(StringBuilder sb, NodeR node) {
        if (node == null) return;

        // LNR — Em ordem
        inOrder(sb, node.getLeft());
        sb.append(node).append("\n");
        inOrder(sb, node.getRight());
    }

    public String preOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        preOrder(sb, root);
        return sb.toString();
    }

    private void preOrder(StringBuilder sb, NodeR node) {
        if (node == null) return;

        // NLR — Pré-ordem
        sb.append(node).append("\n");
        preOrder(sb, node.getLeft());
        preOrder(sb, node.getRight());
    }

    public String postOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        postOrder(sb, root);
        return sb.toString();
    }

    private void postOrder(StringBuilder sb, NodeR node) {
        if (node == null) return;

        // LRN — Pós-ordem
        postOrder(sb, node.getLeft());
        postOrder(sb, node.getRight());
        sb.append(node).append("\n");
    }

    public String levelOrderTraversal() {
        if (isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        Queue<NodeR> queue = new LinkedList<>();

        queue.add(root);

        while (!queue.isEmpty()) {
            NodeR visited = queue.remove();
            sb.append(visited).append("\n");

            if (visited.hasLeftChild()) queue.add(visited.getLeft());
            if (visited.hasRightChild()) queue.add(visited.getRight());
        }

        return sb.toString();
    }

    /** --------------------------- EXIBIÇÃO ASCII REVERSA --------------------------- */
    public String inReversedOrderAscii() {
        StringBuilder sb = new StringBuilder();
        reversedAscii(sb, root);
        return sb.toString();
    }

    private void reversedAscii(StringBuilder sb, NodeR node) {
        if (node == null) return;

        // Ordem RNL — direita → nó → esquerda

        // R
        if (!node.hasRightChild()) {
            appendNullPointer(sb, node.getLevel(), "dir null");
        }
        reversedAscii(sb, node.getRight());

        // N
        appendIndent(sb, node.getLevel());
        sb.append("├─» ").append(node.getData()).append("\n");

        // L
        if (!node.hasLeftChild()) {
            appendNullPointer(sb, node.getLevel(), "esq null");
        }
        reversedAscii(sb, node.getLeft());
    }

    private void appendNullPointer(StringBuilder sb, int level, String label) {
        for (int i = 0; i < level + 1; ++i) {
            sb.append("¦   ");
        }
        sb.append("├─» ").append(label).append("\n");
    }

    private void appendIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level; ++i) {
            sb.append("¦   ");
        }
    }

    /** ------------------------------- TOSTRING -------------------------------- */

    @Override
    public String toString() {
        return "BinaryTree - isEmpty(): " + isEmpty()
                + ", getDegree(): " + getDegree()
                + ", getHeight(): " + getHeight()
                + ", root => { " + root + " }";
    }
}
