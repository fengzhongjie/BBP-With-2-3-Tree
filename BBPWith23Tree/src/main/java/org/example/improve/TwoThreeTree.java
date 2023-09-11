package org.example.improve;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 2-3树的实现。
 * 2-3树是一种自平衡的树数据结构，每个节点可以包含1到2个关键字。
 * 树的节点有两种类型：内部节点和叶子节点。
 */
public class TwoThreeTree {
    private int size; // 树中节点的数量
    private TreeNode root; // 树的根节点
    private boolean successfulInsertion; // 插入操作是否成功
    private boolean successfulDeletion; // 删除操作是否成功
    private boolean split; // 是否进行了节点分裂操作
    private boolean underflow; // 是否进行了下溢操作
    private boolean first; // 是否是第一次插入操作
    private boolean singleNodeUnderflow; // 是否发生了单节点下溢操作
    // 枚举类型，表示内部节点的子节点类型
    private enum Nodes {
        LEFT, MIDDLE, RIGHT, DUMMY;
    }
    /**
     * 初始化2-3树。
     */
    public TwoThreeTree() {
        size = 0;
        root = null;
        successfulInsertion = false;
        successfulDeletion = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
    }

    // 内部节点类，表示2-3树的内部节点
    private class Node {

    }
    private class TreeNode extends Node {

        int keys[];
        Node children[];
        int degree;

        /**
         * 初始化内部节点。
         */
        TreeNode() {
            keys = new int[2];
            children = new Node[3];
            degree = 0;
        }

        /**
         * 打印节点的关键字。
         */
        void print() {
            if (degree == 1) {
                System.out.print("(-,-)");
            } else if (degree == 2) {
                System.out.print("(" + keys[0] + ",-) ");
            } else {
                System.out.print("(" + keys[0] + "," + keys[1] + ") ");
            }
        }
    }

    // 内部节点类，表示2-3树的叶子节点
    private class LeafNode extends Node {
        int key;
        /**
         * 初始化叶子节点。
         */
        LeafNode(int key) {
            this.key = key;
        }
        /**
         * 打印叶子节点的关键字。
         */
        void print() {
            System.out.print(key + " ");
        }
    }

    /**
     * 插入关键字到2-3树。
     *
     * @param key 要插入的关键字
     */
    private void insertKey(int key) {
        Node[] array = new Node[2];
        array = insertCurrent(key, root);
        if (array[1] == null) {
            root = (TreeNode) array[0];
        } else {
            TreeNode treeRoot = new TreeNode();
            treeRoot.children[0] = array[0];
            treeRoot.children[1] = array[1];
            updateTree(treeRoot);
            root = treeRoot;
        }
    }

    /**
     * 向B+树中插入键值
     *
     * @param key 要插入的键值
     * @param n 当前节点
     * @return 包含更新后节点的数组，[0]为当前节点，[1]为新分裂的节点（如果有）
     */
    private Node[] insertCurrent(int key, Node n) {
        Node array[] = new Node[2];
        Node catchArray[] = new Node[2];
        TreeNode t = null;
        // 如果节点是内部节点，则将其强制类型转换为TreeNode类型
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        // 如果根节点为空且不是第一次插入
        if (root == null && !first) {
            // 创建一个新的树节点作为根节点
            first = true;
            TreeNode newNode = new TreeNode();
            t = newNode;
            // 递归地插入键，并更新子节点
            t.children[0] = insertCurrent(key, t.children[0])[0];
            updateTree(t);
            array[0] = t;
            array[1] = null;
        }
        // 如果当前节点是内部节点且子节点不是叶节点
        else if (t != null && !(t.children[0] instanceof LeafNode)) {
            // 判断键值的范围并递归插入
            if (key < t.keys[0]) {
                catchArray = insertCurrent(key, t.children[0]);
                t.children[0] = catchArray[0];
                // 处理可能的分裂
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = t.children[1];
                        t.children[1] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[1] = catchArray[1];
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            else if (key >= t.keys[0] && (t.children[2] == null || key < t.keys[1])) {
                catchArray = insertCurrent(key, t.children[1]);
                t.children[1] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            else if (key >= t.keys[1]) {
                catchArray = insertCurrent(key, t.children[2]);
                t.children[2] = catchArray[0];
                if (split) {
                    if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[0];
                        newNode.children[1] = catchArray[1];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
        }
        // 如果当前节点是内部节点且子节点是叶节点
        else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            // 处理当前节点度小于等于2的情况
            if (t.degree <= 2) {
                if (t.degree == 1 && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = leaf;
                } else if (t.degree == 1 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l2.key && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = leaf;
                } else if (t.degree == 2) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = leaf;
                }
                updateTree(t);
                array[0] = t;
                array[1] = null;
            }
            // 处理当前节点度大于2的情况
            else if (t.degree > 2) {
                split = true;
                if (key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[0] = leaf;
                    t.children[1] = l1;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l1.key && key < l2.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[1] = leaf;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l2.key && key < l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = leaf;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = l3;
                    newNode.children[1] = leaf;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                }
            }
            successfulInsertion = true;
        } else if (n == null) {
            successfulInsertion = true;
            array[0] = new LeafNode(key);
            array[1] = null;
            return array;
        }
        return array;
    }


    private Node remove(int key, Node n) {
        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (n == null) {
            return null;
        }
        // 如果当前节点不是根节点且有子节点是内部节点
        if (t != null && t.children[0] instanceof TreeNode) {
            // 如果键值小于当前节点的第一个键
            if (key < t.keys[0]) {
                // 递归地从左子树移除键
                t.children[0] = remove(key, t.children[0]);
                // 处理单节点下溢情况
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        t.children[1] = t.children[2];
                        t.children[2] = null;
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    else if (rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[0];
                        newNode.children[1] = rightChild.children[0];
                        t.children[0] = newNode;
                        updateTree(newNode);
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    // 处理普通的下溢情况
                    underflow = false;
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 3) {
                        // 右子节点度为3
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        }
                        else {
                            Node ref = t.children[0];
                            t = (TreeNode) ref;
                            singleNodeUnderflow = true;
                        }
                    }
                }
                updateTree(t);
            }
            // 如果键值大于等于第一个键且小于第二个键，或者没有第二个键
            else if (key >= t.keys[0] && (t.children[2] == null || key < t.keys[1])) {
                t.children[1] = remove(key, t.children[1]);
                if (singleNodeUnderflow) {
                    // 单节点下溢情况处理
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];

                    if (leftChild.degree == 2) {
                        // 右子节点度为2
                        leftChild.children[2] = child;
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        updateTree(leftChild);
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    else if (rightChild != null && rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                    else if (leftChild.degree == 3) {

                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = child;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                        singleNodeUnderflow = false;
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = child;
                        newNode.children[1] = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[1] = null;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        }
                        else {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        }
                    }
                    else if (rightChild != null && rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                }
                updateTree(t);
            }
            // 如果键值大于等于第二个键
            else if (key >= t.keys[1]) {
                t.children[2] = remove(key, t.children[2]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[2];
                    TreeNode leftChild = (TreeNode) t.children[1];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[2] = null;
                        updateTree(leftChild);
                    }
                    else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = t.children[2];
                        t.children[2] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                    }
                    singleNodeUnderflow = false;
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[1];
                    TreeNode child = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }
                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[2] = null;
                    }
                }
                updateTree(t);
            }
        }
        // 如果当前节点不是根节点且子节点是叶节点
        else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            // 处理当前节点度为3的情况
            if (t.degree == 3) {
                if (key == l1.key) {
                    t.children[0] = l2;
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == l2.key) {
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == l3.key) {
                    t.children[2] = null;
                }
                updateTree(t);
            }
            // 处理当前节点度为2的情况
            else if (t.degree == 2) {
                underflow = true;
                if (l1.key == key) {
                    t.children[0] = l2;
                    t.children[1] = null;
                } else if (l2.key == key) {
                    t.children[1] = null;
                }
            }
            // 处理当前节点度为1的情况
            else if (t.degree == 1) {
                if (l1.key == key) {
                    t.children[0] = null;
                }
            }
            successfulDeletion = true;
        }
        return t;
    }

    /**
     * 更新树节点的信息，包括节点的度数和键值
     *
     * @param t 待更新的树节点
     */
    private void updateTree(TreeNode t) {
        if (t != null) {

            if (t.children[2] != null && t.children[1] != null && t.children[0] != null) {
                // 节点度数为3，更新键值
                t.degree = 3;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = getValueForKey(t, Nodes.RIGHT);
            } else if (t.children[1] != null && t.children[0] != null) {
                // 节点度数为2，更新键值
                t.degree = 2;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = 0;
            } else if (t.children[0] != null) {
                // 节点度数为1，更新键值
                t.degree = 1;
                t.keys[1] = t.keys[0] = 0;
            }
        }
    }

    /**
     * 获取节点的键值
     *
     * @param n        待获取键值的节点
     * @param whichVal 用于指示从哪个子节点获取键值
     * @return 节点的键值
     */
    private int getValueForKey(Node n, Nodes whichVal) {
        int key = -1;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (l != null) {
            key = l.key;
        }
        if (t != null) {
            if (null != whichVal) {
                switch (whichVal) {
                    case LEFT:
                        key = getValueForKey(t.children[1], Nodes.DUMMY);
                        break;
                    case RIGHT:
                        key = getValueForKey(t.children[2], Nodes.DUMMY);
                        break;
                    case DUMMY:
                        key = getValueForKey(t.children[0], Nodes.DUMMY);
                        break;
                    default:
                        break;
                }
            }
        }
        return key;
    }

    /**
     * 在树中搜索指定键值
     *
     * @param key 要搜索的键值
     * @param n   当前节点
     * @return 是否找到指定键值
     */
    private boolean search(int key, Node n) {
        boolean found = false;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.degree == 1) {
                found = search(key, t.children[0]);
            }
            else if (t.degree == 2 && key < t.keys[0]) {
                found = search(key, t.children[0]);
            }
            else if (t.degree == 2 && key >= t.keys[0]) {
                found = search(key, t.children[1]);
            }
            else if (t.degree == 3 && key < t.keys[0]) {
                found = search(key, t.children[0]);
            }
            else if (t.degree == 3 && key >= t.keys[0] && key < t.keys[1]) {
                found = search(key, t.children[1]);
            }
            else if (t.degree == 3 && key >= t.keys[1]) {
                found = search(key, t.children[2]);
            }
        }
        else if (l != null && key == l.key) {
            return true;
        }
        return found;
    }

    /**
     * 遍历树并输出键值的顺序列表
     *
     * @param n 当前节点
     */
    private void keyOrderList(Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.children[0] != null) {
                keyOrderList(t.children[0]);
            }
            if (t.children[1] != null) {
                keyOrderList(t.children[1]);
            }
            if (t.children[2] != null) {
                keyOrderList(t.children[2]);
            }
        }
        else if (l != null) {
            System.out.print(l.key + " ");
        }
    }



    private void bfsList(Node n) {
        // 创建两个队列，分别用于存储当前层和下一层的节点
        Queue<Node> queueOne = new LinkedList<>();
        Queue<Node> queueTwo = new LinkedList<>();
        // 如果起始节点为空，直接返回
        if (n == null) {
            return;
        }
        // 将起始节点添加到第一个队列
        queueOne.add(n);
        Node first = null;
        TreeNode t = null;
        // 使用两个队列交替进行层次遍历
        while (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
            // 遍历当前层的节点
            while (!queueOne.isEmpty()) {
                first = queueOne.poll();
                // 如果当前节点是内部节点，进行类型转换并输出信息
                if (first instanceof TreeNode) {
                    t = (TreeNode) first;
                    t.print();
                }
                // 将当前节点的子节点添加到下一层的队列
                if (t.children[0] != null && !(t.children[0] instanceof LeafNode)) {
                    queueTwo.add(t.children[0]);
                }
                if (t.children[1] != null && !(t.children[1] instanceof LeafNode)) {
                    queueTwo.add(t.children[1]);
                }
                if (t.children[2] != null && !(t.children[2] instanceof LeafNode)) {
                    queueTwo.add(t.children[2]);
                }
            }
            // 如果下一层不为空，输出换行符
            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
                System.out.println();
            }
            // 遍历下一层的节点
            while (!queueTwo.isEmpty()) {
                first = queueTwo.poll();
                // 如果当前节点是内部节点，进行类型转换并输出信息
                if (first instanceof TreeNode) {
                    t = (TreeNode) first;
                    t.print();
                }
                // 将当前节点的子节点添加到下一层的队列
                if (t.children[0] != null && !(t.children[0] instanceof LeafNode)) {
                    queueOne.add(t.children[0]);
                }
                if (t.children[1] != null && !(t.children[1] instanceof LeafNode)) {
                    queueOne.add(t.children[1]);
                }
                if (t.children[2] != null && !(t.children[2] instanceof LeafNode)) {
                    queueOne.add(t.children[2]);
                }

            }
            // 如果下一层不为空，输出换行符
            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
                System.out.println();
            }
        }
        // 输出换行符以分隔遍历结果和键顺序列表
        System.out.println();
        keyOrderList(root);
        System.out.println();
    }
    private int height(Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            return 1 + height(t.children[0]);
        }
        return 0;
    }

    private boolean found = false;

    private int foundValue = -1; // 用于存储找到的第一个满足条件的值

    private int findBest(int value, Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null && foundValue == -1) {
            if (t.children[0] != null) {
                foundValue = findBest(value, t.children[0]);
            }
            if (t.children[1] != null && foundValue == -1) {
                foundValue = findBest(value, t.children[1]);
            }
            if (t.children[2] != null && foundValue == -1) {
                foundValue = findBest(value, t.children[2]);
            }
        } else if (l != null && l.key >= value && foundValue == -1) {
            foundValue = l.key; // 找到第一个满足条件的值后，设置 foundValue 并停止进一步递归
        }

        return foundValue;
    }

    public int findBest(int value) {
        return findBest(value,root);
    }
    public boolean insertCurrent(int key) {
        boolean insert = false;
        split = false;
//        if (!search(key)) {
//            insertKey(key);
//        }
        insertKey(key);
        if (successfulInsertion) {
            size++;
            insert = successfulInsertion;
            successfulInsertion = false;
        }
        return insert;
    }
    public boolean search(int key) {
        return search(key, root);
    }
    public boolean remove(int key) {
        boolean delete = false;
        singleNodeUnderflow = false;
        underflow = false;
        if (search(key)) {
            root = (TreeNode) remove(key, root);
            if (root.degree == 1 && root.children[0] instanceof TreeNode) {
                root = (TreeNode) root.children[0];
            }
        }
        if (successfulDeletion) {
            size--;
            delete = successfulDeletion;
            successfulDeletion = false;
        }
        return delete;
    }
    public void keyOrderList() {
        System.out.println("Keys");
        keyOrderList(root);
        System.out.println();
    }
    public void bfsList() {
        System.out.println("Tree");
        bfsList(root);
    }
    public int numberOfNodes() {
        return size;
    }

    public int height() {
        return height(root);
    }



    /**测试代码*/

    public static void main(String[] args) throws IOException {
        //测试数据样例放在resources下面。
        TwoThreeTree tree = new TwoThreeTree();
        tree.insertCurrent(6);
        tree.insertCurrent(4);
        tree.insertCurrent(5);
        tree.insertCurrent(7);
        tree.insertCurrent(3);
        tree.insertCurrent(23);
        tree.keyOrderList();
        tree.bfsList();
        System.out.println(tree.findBest(10));

    }

}

