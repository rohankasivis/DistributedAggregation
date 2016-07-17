/*
import scala.collection.mutable

class BSTMap[K, V](comp:(K,K) => Int) extends mutable.Map[K, V]
{
  class Node(var key: K, var value: V, var left: Node, var right: Node)
  private var root:Node = null

  def += (kv: (K, V)) = {
    this
  }

  def -= (key: K) = {
    this
  }

  def get(key: K): Option[V] = {
    var temp = root
    while(temp != null && comp(temp.key, key) != 0){
      temp = if(comp(key, temp.key) < 0) temp.left else temp.right
    }
    if(temp == null)
      None
    else
      Some(temp.value)
  }

  def iterator = iterator = {
    def next = null
    def hasNext = false
  }
}
*/