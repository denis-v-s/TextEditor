package application.services.linkedList;

public class LinkedList<T> {
  private Link<T> first;
  private ListIterator<T> iterator = null;
  private int size = 0;
  
  public LinkedList() {
    first = null;
  }
  
  public ListIterator<T> getIterator() {
    if (iterator == null) {
      iterator = new ListIterator<T>(this);
      return iterator;      
    }
    else {
      return iterator;
    }
  }
  
  public Link<T> getFirst() {
    return first;
  }
  
  public void setFirst(Link<T> link) {
    first = link;
  }
  
  public int getSize() {
    return size;
  }
  
  public void setSize(int size) {
    this.size = size;
  }
  
  public boolean isEmpty() {
    return first == null;
  }
  
  public void display() {
    Link<T> current = first;
    while (current != null) {
      current.display();
      current = current.getNext();
    }
    System.out.println();
  }

//  public static void main(String[] args) {
//    MasterLinkedList<String> list = new MasterLinkedList<>();
//    ListIterator<String> iter1 = list.getIterator();
//    iter1.insertAfter("1");
//    iter1.insertAfter("2");
//    iter1.insertAfter("3");
//    //iter1.insertBefore(0);
//    iter1.insertAfter("4");
//    //iter1.insertBefore(-1);
//    list.display();
//  }
}
