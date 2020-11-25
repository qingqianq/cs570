import java.util.ArrayList;

public class IDLList<E> {

    class Node<E>{
        private E data;
        Node<E> next;
        Node<E> prev;
        Node(E elem){
            data = elem;
        }
        Node(E elem, Node<E> prev, Node<E> next){
            data = elem;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;
    private ArrayList<Node<E>> indices;

    public IDLList(){
        indices = new ArrayList();
        size = indices.size();
        head = null;
        tail = null;
    }

    public boolean add(int index, E elem){
        if(elem == null){
            throw new RuntimeException("Add null error");
        }
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index : " + index + ", Size : " + size);
        }
        Node newNode = new Node(elem);
        int preIndex = index - 1;
        int nextIndex = index + 1;
        if(preIndex >= 0){
            Node previous = indices.get(preIndex);
            previous.next = newNode;
            newNode.prev = previous;
        }
        if(nextIndex <= size - 1){
            Node next = indices.get(nextIndex);
            newNode.next = next;
            next.prev = newNode;
        }
        indices.add(index, newNode);
        size = indices.size();
        head = indices.get(0);
        tail = indices.get(size - 1);
        return true;
    }

    /*
     * To maintain the ArrayList, this will be O(n), but for LinkedList, it should be O(1)
     * */
    public boolean add(E elem){
        return add(0, elem);
    }


    public boolean append(E elem){
        if(size == 0)
            return add(elem);
        Node newNode = new Node(elem);
        Node previous = indices.get(size - 1);
        previous.next = newNode;
        newNode.prev = previous;
        indices.add(newNode);
        size = indices.size();
        tail = indices.get(size - 1);
        return true;
    }

    public E get(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index : " + index + ", Size : " + size);
        }
        return indices.get(index).data;
    }

    public E getHead(){
        return head == null ? null : head.data;
    }

    public E getLast(){
        return tail == null ? null : tail.data;
    }

    public int size(){
        return size;
    }

    public E remove(){
        return removeAt(0);
    }

    public E removeLast(){
        return removeAt(size - 1);
    }

    public E removeAt(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index : " + index + ", Size : " + size);
        }
        int preIndex = index - 1;
        int nextIndex = index + 1;
        Node previous = null, next = null;
        if(preIndex >= 0){
            previous = indices.get(preIndex);
        }
        if(nextIndex <= size - 1){
            next = indices.get(nextIndex);
        }
        if(previous != null){
            previous.next = next;
        }
        if(next != null){
            next.prev = previous;
        }
        E e = indices.remove(index).data;
        size = indices.size();
        if(size == 0){
            head = null;
            tail = null;
        }else{
            head = indices.get(0);
            tail = indices.get(size - 1);
        }
        return e;
    }

    public boolean remove(E elem){
        if(elem == null){
            throw new RuntimeException("Remove null error");
        }
        for(int i = 0; i < size; ++i){
            E data = indices.get(i).data;
            if(data.equals(elem)){
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < size; ++i){
            E data = indices.get(i).data;
            stringBuilder.append("Index " + i + ", value: " + data.toString() + "\n");
        }
        return stringBuilder.toString();
    }
}
