package application.services.linkedList;

public class MasterLink<T> implements Link<T> {
  private T data;
  private MasterLink<T> next;
  public BabyLinkedList<T> babyList = new BabyLinkedList<>(); // list of words that follow
  
  public MasterLink(T data) {
    this.data = data;
  }
  
  public MasterLink<T> getNext() {
    return next;
  }
  
  public void setNext(Link<T> nextLink) {
    this.next = (MasterLink<T>) nextLink;
  }
  
  public T getData() {
    return data;
  }
  
  public void display() {
    System.out.print(data + " ");
  }
}
