package edu.iastate.cs472.proj2;

/**
 * Child-sibling node type for an n-ary tree.
 */
public class CSNode<E>
{
  protected CSNode<E> parent;
  protected CSNode<E> firstChild;
  protected CSNode<E> nextSibling;
  protected E data;
  int value =0;
  int numVisited = 0;
  
  public CSNode(){}
  
  public CSNode(E data)
  {
    this(data, null, null);
  }
  
  public CSNode(E data, CSNode<E> child, CSNode<E> sibling)
  {
    this.firstChild = child;
    this.nextSibling = sibling;
    this.data = data;
    parent = null;
  }

  public CSNode<E> getParent() {
    return parent;
  }

  public void setParent(CSNode<E> parent) {
    this.parent = parent;
  }

  public boolean isLeaf()
  {
    return firstChild == null;
  }
  
  public CSNode<E> getChild()
  {
    return firstChild;
  }
  
  public void setChild(CSNode<E> child)
  {
    this.firstChild = child;
  }
  
  public CSNode<E> getSibling()
  {
    return nextSibling;
  }
  
  public void setSibling(CSNode<E> sibling)
  {
    this.nextSibling = sibling;
  }
  
  public E getData()
  {
    return data;
  }
  
  public void setData(E data)
  {
    this.data = data;
  }

  public void setValue (int value){
    this.value= value;
  }
  public int getValue(){
    return value;
  }

  public int getNumVisited() {
    return numVisited;
  }

  public void setNumVisited(int numVisited) {
    this.numVisited = numVisited;
  }
}

