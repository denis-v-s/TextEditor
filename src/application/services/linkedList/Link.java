package application.services.linkedList;

public interface Link<T> {
  public T getData();
  public Link<T> getNext();
  public void setNext(Link<T> nextLink);
  public void display();
}
