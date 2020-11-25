import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Treap<E extends Comparable<E>> {
    private static class Node<E extends Comparable<E>>{
        public E data;
        public int priority;
        public Node<E> left;
        public Node<E> right;

        public Node(E data, int priority) {
            if(data == null){
                throw new RuntimeException("data can not be null");
            }
            this.data = data;
            this.priority = priority;
        }

        Node<E> rotateLeft(){
            if(this.right != null){
                Node<E> right = this.right;
                Node<E> rightLeft = right.left;
                right.left = this;
                right.left.right = rightLeft;
                return right;
            }
            return this;
        }

        Node<E> rotateRight(){
            if(this.left != null){
                Node<E> left = this.left;
                Node<E> leftRight = left.right;
                left.right = this;
                left.right.left = leftRight;
                return left;
            }
            return this;
        }

        public String toString(){
            return "(key=" + data.toString() + ", priority=" + priority + ")";
        }
    }

    private final Random priorityGenerator;
    private Node<E> root;

    public Treap(){
        priorityGenerator = new Random();
    }

    public Treap(long seed){
        priorityGenerator = new Random(seed);
    }

    boolean add(E key){
        return add(key, priorityGenerator.nextInt());
    }

    boolean add(E key, int priority){
        Node<E> newNode = new Node<>(key, priority);
        if(root == null){
            root = newNode;
            return true;
        }
        Deque<Node<E>> stack = new ArrayDeque<>();
        Node<E> current = root;
        while(true){
            if(current.data.compareTo(key) == 0)
                return false;
            stack.push(current);
            if(current.data.compareTo(key) < 0){
                if(current.right == null){
                    current.right = newNode;
                    break;
                }
                current = current.right;
            }else{
                if(current.left == null){
                    current.left = newNode;
                    break;
                }
                current = current.left;
            }
        }
        //assume priority are repeatable
        reheap(newNode, stack);
        return true;
    }
    /*
    * Because of the Random(seed) always generate same priority, do not handle the same priority issue.
    * */
    boolean reheap(Node<E> node, Deque<Node<E>> stack){
        while(!stack.isEmpty() && stack.peek().priority < node.priority){
            Node<E> parent = stack.pop();
            if(!isLeftChild(parent, node)){
                if(!stack.isEmpty()){
                    boolean isLeftChild = isLeftChild(stack.peek(), parent);
                    parent = parent.rotateLeft();
                    if(isLeftChild)
                        stack.peek().left = parent;
                    else
                        stack.peek().right = parent;
                }else{
                    parent = parent.rotateLeft();
                }
            }else{
                if(!stack.isEmpty()){
                    boolean isLeftChild = isLeftChild(stack.peek(), parent);
                    parent = parent.rotateRight();
                    if(isLeftChild)
                        stack.peek().left = parent;
                    else
                        stack.peek().right = parent;
                }else{
                    parent = parent.rotateRight();
                }
            }
            node = parent;
        }
        if(stack.size() == 0){
            root = node;
        }
        return true;
    }

    boolean isLeftChild(Node<E> parent, Node<E> child){
        if(parent == null || parent.left == null)
            return false;
        return parent.left.data.compareTo(child.data) == 0;
    }

    boolean delete(E key){
        Node<E> current = root;
        Node<E> parent = null;
        while(current != null){
            if(current.data.compareTo(key) == 0){
                break;
            }
            if(current.data.compareTo(key) > 0){
                parent = current;
                current = current.left;
            }else{
                parent = current;
                current = current.right;
            }
        }
        if(current == null)
            return false;
        if(current.left == null && current.right == null && parent == null){
            root = null;
            return true;
        }
        //just copy and paste without reuse codes, needs to rewrite.
        while(current.left != null || current.right != null){
            if(current.left == null){
                if(parent == null){
                    current = current.rotateLeft();
                    root = current;
                    parent = current;
                }else{
                    if(isLeftChild(parent, current)){
                        current = current.rotateLeft();
                        parent.left = current;
                        parent = parent.left;
                    }else{
                        current = current.rotateLeft();
                        parent.right = current;
                        parent = parent.right;
                    }
                }
                current = current.left;
            }else if(current.right == null){
                if(parent == null){
                    current = current.rotateRight();
                    root = current;
                    parent = current;
                }else{
                    if(isLeftChild(parent, current)){
                        current = current.rotateRight();
                        parent.left = current;
                        parent = parent.left;
                    }else{
                        current = current.rotateRight();
                        parent.right = current;
                        parent = parent.right;
                    }
                }
                current = current.right;
            }else if(current.left.priority > current.right.priority){ //Same logic with current.right is null
                if(parent == null){
                    current = current.rotateRight();
                    root = current;
                    parent = current;
                }else{
                    if(isLeftChild(parent, current)){
                        current = current.rotateRight();
                        parent.left = current;
                        parent = parent.left;
                    }else{
                        current = current.rotateRight();
                        parent.right = current;
                        parent = parent.right;
                    }
                }
                current = current.right;
            }else{ // same logic with current.left is null
                if(parent == null){
                    current = current.rotateLeft();
                    root = current;
                    parent = current;
                }else{
                    if(isLeftChild(parent, current)){
                        current = current.rotateLeft();
                        parent.left = current;
                        parent = parent.left;
                    }else{
                        current = current.rotateLeft();
                        parent.right = current;
                        parent = parent.right;
                    }
                }
                current = current.left;
            }
        }
        if(isLeftChild(parent,current)){
            parent.left = null;
        }else{
            parent.right = null;
        }
        return true;
    }

    private boolean find(Node<E> root, E key){
        Node<E> current = root;
        while(current != null){
            if(current.data.compareTo(key) == 0)
                return true;
            if(current.data.compareTo(key) > 0){
                current = current.left;
            }else{
                current = current.right;
            }
        }
        return false;
    }

    public boolean find(E key){
        return find(root, key);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        preOrder(root, sb, 0, "  ");
        return sb.toString();
    }
    public void preOrder(Node<E> root, StringBuilder sb, int level, String pre){
        if(root == null){
            for(int i = 0; i < level; ++i)
                sb.append(pre);
            sb.append("null" + "\n");
            return;
        }
        for(int i = 0; i < level; ++i)
            sb.append(pre);
        sb.append(root.toString()).append("\n");
        preOrder(root.left, sb, level + 1, pre);
        preOrder(root.right, sb, level + 1, pre);
    }

    public static void main(String[] args) {
        Treap<Integer> testTree = new Treap<>();
//        testTree.add(4,14);
//        testTree.add(2,31);
//        testTree.add(6,70);
//        testTree.add(1,84);
//        testTree.add(3,12);
        testTree.add(5,83);
//        testTree.add(7,26);
        testTree.delete(5);
        System.out.println(testTree.toString());
    }
}
