import akka.actor._

object NeighborSet
{
  class Node(var value: Double, var left:Node, var right:Node)

  private var root:Node = null

  def getNode(value: Double): Node = {
    var temp = root
    while(temp != null && temp.value != value)
    {
      temp = if(value < temp.value) temp.left else temp.right
    }
    if(temp != null)
      temp
    else
      null
  }

  def addNeighbor(value: Double) = {
    if(root == null)
      root = new Node(value, null, null)
    else{
      addNeighborHelper(value, root)
    }
  }

  def addNeighborHelper(value: Double, curr:Node): Any = {
    if(curr.left == null && value < curr.value)
      curr.left = new Node(value, null, null)
    else if(curr.right == null && value)
  }

  def removeNeighbor(value: Double) = {
    var curr = getNode(value)
    var temp = root
    if(curr != null){
      while(temp.left.value != value && temp.right.value != value) {
        if(temp.left.value == value)
          temp.left = null
        else if(temp.right.value == value)
          temp.right = null
        else {
          temp = if(value < temp.value) temp.left else temp.right
        }
      }
    }
  }

  def printTree() = {
    printTreeHelper(root)
  }

  def printTreeHelper(curr: Node): Any = {
    if(curr != null) {
      printTreeHelper(curr.left)
      println(curr.value)
      printTreeHelper(curr.right)
    }
  }
}

object tester extends App
{
  NeighborSet.addNeighbor(5.4)

}