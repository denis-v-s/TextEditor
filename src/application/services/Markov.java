package application.services;
import application.services.linkedList.*;

public class Markov {
  private MasterLinkedList<String> masterList;
  private ListIterator<String> masterIter;
  private ListIterator<String> childIter;
  
  public Markov(String text) {
    textToList(text);
  }
  
  // @params:
  // * word - starting word within the master list
  // * n - number of random words to generate
  public String generateRandomText(String word, int n) {
    MasterLink<String> node = (MasterLink<String>) masterIter.getLinkReference(word);
    String randomWord;
    
    if (node == null) {
      return "no such word in master linked list";
    }
    
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < n; i++) {
      randomWord = masterIter.getRandomLinkValue(node);
      result.append(randomWord + " ");
      node = (MasterLink<String>) masterIter.getLinkReference(randomWord);
    }
    
    return result.toString();
  }
  
  private void textToList(String text) {
    masterList = new MasterLinkedList<>();
    masterIter = masterList.getIterator();
    
    MasterLink<String> node = null;
    for (String word : text.split("(\\s+|\\,|\\.|\\!|\\?)")) {
      word = word.trim();
      // add a word that follows
      if (node != null) {
        childIter = node.babyList.getIterator();
        childIter.insertAfter(new BabyLink<String>(word));
      }
      
      node = (MasterLink<String>) masterIter.getLinkReference(word);
      // if this is a new word, then add it to the master list
      if (node == null) {
        node = (MasterLink<String>) masterIter.insertAfter(new MasterLink<String>(word));
      }
    }
  }
}
