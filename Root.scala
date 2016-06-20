import akka.actor
import akka.actor.Actor._

class Root extends NodeActors
{
  private var levels:Array[Int] = new Array[Int](100)
  private var sent_mass:Array[Int] = new Array[Int](100)
  private var received_mass:Array[Int] = new Array[Int](100)
  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  private var adjacent:Array[NodeActors] = new Array[NodeActors](100)
  private var broadcast:Boolean = false
  private var index:Int = 0

  def new_entry(nodeActors:NodeActors)
  {
    adjacent(index) = nodeActors
    sent_mass(index) = 0
    received_mass(index) = 0
    index = index + 1
  }

  def remove_entry(nodeActors:NodeActors)
  {

  }

  def update_entry(nodeActors:NodeActors)
  {

  }

  def level(nodeActors:NodeActors)
  {

  }

  def parent()
  {

  }

  def send(nodeActors:NodeActors, value:Int)
  {

  }

  def broadcast(value:Int)
  {

  }

  // helper function
  def get_index(values:Array[Int], value:Int)
  {
    for(i <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return null
  }

  def get_index(values:Array[NodeActors], value:NodeActors)
  {
    for((i) <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return null
  }

  def receive: Receive = {
    case New(arg1) => val result = {
      send(arg1, Status(0,0))
      new_entry(arg1)
    }

    case Fail(arg1) => val result = {
      val sent_index:Int = get_index(adjacent, arg1)
      adjacent(sent_index) = null     // effectively removes element
      val sent_val:Int = sent_mass(sent_index)
      val received_val:Int = received_mass(sent_index)
      aggregate_mass = aggregate_mass + sent_val - received_val
    }

    case Aggregate(arg1, arg2) => val result = {
      aggregate_mass = aggregate_mass + arg2
      val index:Int = get_index(adjacent, arg1)
      received_mass(index) = received_mass(index) + arg2
    }

    case Local(arg1) => val result = {
      aggregate_mass = aggregate_mass + arg1 - local_mass
      local_mass = arg1
    }

    case Status(arg1, arg2) => val result = {
      // dont do anything here
    }
  }
}
