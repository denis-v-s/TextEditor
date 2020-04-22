package application.services.linkedList;

public class BabyLink<T> implements Link<T> {
  private T data;
  private BabyLink<T> next;
  
  public BabyLink(T data) {
    this.data = data;
  }
  
  public BabyLink<T> getNext() {
    return next;
  }
  
  public void setNext(Link<T> nextLink) {
    this.next = (BabyLink<T>) nextLink;
  }
  
  public T getData() {
    return data;
  }
  
  public void display() {
    System.out.print(data + " ");
  }
}
