import akka.actor.Actor._
import akka.actor.{ActorSystem, Props}

class NonRoot extends NodeActors
{
  // private var levels:Array[Int] = new Array[Int](100)
  private var levels:Map[NodeActors, Int] = Map.empty
  private var sent_mass:Map[NodeActors, Int] = Map.empty
  private var received_mass:Map[NodeActors, Int] = Map.empty

  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  //private var adjacent:Array[NodeActors] = new Array[NodeActors](100)
  private var adjacent:Set[NodeActors] = Set.empty
  private var broadcast:Boolean = false
  private var index:Int = 0
  val system = ActorSystem("NodeActors")

  def new_entry(nodeActors:NodeActors)
  {
    // adjacent(index) = nodeActors
    adjacent += nodeActors
    sent_mass = sent_mass + (nodeActors -> 0)
    received_mass = received_mass + (nodeActors -> 0)
    index = index + 1
    levels += nodeActors -> index
  }

  def remove_entry(nodeActors:NodeActors)
  {
    adjacent -= nodeActors
  }

  def level(nodeActors:NodeActors): Int =
  {
    levels.get(nodeActors).get
  }

  def parent()
  {

  }

  def send(nodeActors:NodeActors, value:Int)
  {
    val act = system.actorOf(Props(nodeActors))
    act ! value
  }

  def broadcast(value:Int)
  {
    for (na <- adjacent)
    {
      send (na,value)
    }
  }

  // helper function
  def get_index(values:Array[Int], value:Int):Int=
  {
    for(i <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return 0
  }

  def get_index(values:Array[NodeActors], value:NodeActors): Int =
  {
    for((i) <- 0 until values.length)
    {
      if(values(i) == value)
        return i
    }
    return 0
  }

  def send(arg1: NodeActors, status: Status) =
  {

  }

  def receive: Receive = {
    case New(arg1) => val result = {
      send(arg1, Status(arg1,0))
      new_entry(arg1)
    }

    case Fail(arg1) => val result = {
      val sent_index:Int = get_index(adjacent.toArray, arg1)
      adjacent -= arg1
      // adjacent(sent_index) = null     // effectively removes element
      val sent_val:Option[Int] = sent_mass.get(arg1)
      val received_val:Option[Int] = received_mass.get(arg1)
      aggregate_mass = aggregate_mass + sent_val.get - received_val.get
    }

    case Aggregate(arg1, arg2) => val result = {
      aggregate_mass = aggregate_mass + arg2
      val index:Int = get_index(adjacent.toArray, arg1)
      received_mass.get(arg1).get + arg2
    }

    case Drop(arg1, arg2) => val result = {
      aggregate_mass = aggregate_mass + arg2
      //val index:Int = get_index(adjacent, arg1)
      val sent_val:Int = sent_mass(arg1)

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