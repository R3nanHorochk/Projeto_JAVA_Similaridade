package rha;

public class NodeR {

    protected Resultado data;
    private double balanceFactor;
    protected NodeR parent;
    protected NodeR left;
    protected NodeR right;

    public NodeR(Resultado data, NodeR parent) {
        this.data = data;
        this.balanceFactor = 0;
        this.parent = parent;
        this.left = null;
        this.right = null;
    }

    public NodeR(Resultado data) {
        this(data, null);
    }

    public Resultado getData() {
        return this.data;
    }

    public void setData(Resultado data) {
        this.data = data;
    }

    public NodeR getParent() {
        return parent;
    }

    public void setParent(NodeR parent) {
        this.parent = parent;
    }

    public NodeR getLeft() {
        return left;
    }

    public void setLeft(NodeR left) {
        this.left = left;

        if (this.left != null) {
            this.left.setParent(this);
        }

        updateBalanceFactor();
    }

    public NodeR getRight() {
        return right;
    }

    public void setRight(NodeR right) {
        this.right = right;

        if (this.right != null) {
            this.right.setParent(this);
        }

        updateBalanceFactor();
    }

    public boolean hasLeftChild() {
        return left != null;
    }

    public boolean hasRightChild() {
        return right != null;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public int getDegree() {
        int degree = 0;

        if (hasLeftChild()) {
            ++degree;
        }

        if (hasRightChild()) {
            ++degree;
        }

        return degree;
    }

    public int getLevel() {
        if (isRoot()) {
            return 0;
        }

        return parent.getLevel() + 1;
    }

    public int getHeight() {
        if (isLeaf()) {
            return 0;
        }

        int height = 0;

        if (hasLeftChild()) {
            height = Math.max(height, left.getHeight());
        }

        if (hasRightChild()) {
            height = Math.max(height, right.getHeight());
        }

        return height + 1;
    }

    public double getBalanceFactor() {
        return balanceFactor;
    }

    private void updateBalanceFactor() {
        int leftHeight = hasLeftChild() ? left.getHeight() : -1;
        int rightHeight = hasRightChild() ? right.getHeight() : -1;
        balanceFactor = rightHeight - leftHeight;
    }

    // ========= ALTERAÇÃO MAIS IMPORTANTE ==========
    // Key agora é a similaridade do Resultado
    public double getKey() {
        return data.getSimilaridade();
    }

    @Override
    public String toString() {
        return "similaridade: " + data.getSimilaridade() +
               ", file1: " + data.getNomeFile1() +
               ", file2: " + data.getNomeFile2() +
               ", pai: " + (parent != null ? parent.getKey() : "null") +
               ", esq: " + (left != null ? left.getKey() : "null") +
               ", dir: " + (right != null ? right.getKey() : "null") +
               ", Raiz: " + isRoot() +
               ", Folha: " + isLeaf() +
               ", Alt: " + getHeight() +
               ", Balanceamento: " + getBalanceFactor();
    }
}
