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
  }

  def remove_entry(nodeActors:NodeActors)
  {
    adjacent -= nodeActors        // remove an element from the nodeactors set
    // remove the element corresponding to the nodeactor from level map
  }

  def level(nodeActors:Set[NodeActors], levels:Map[NodeActors, Int]): Int=
  {
    0
  }

  def parent(nodeActors:Set[NodeActors], level:Map[NodeActors, Int]): NodeActors =
  {
    null
  }

  def send(nodeActors:NodeActors, value:Int)
  {
    val act = system.actorOf(Props[NodeActors])
    act ! value
  }

  def send(nodeActors:Set[NodeActors], value:Status)
  {
    val act = system.actorOf(Props[NodeActors])
    act ! value
  }

  def broadcast(value:Int)
  {
    send(adjacent, Status(null, level(adjacent, levels)))
    broadcast = false
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

  def send_agg(arg1:NodeActors, adjacent:Aggregate) = {

  }

  def handle_aggregate() =
  {
    if(aggregate_mass != 0)
    {
      val res:NodeActors = parent(adjacent, levels)
      send_agg(res, Aggregate(null, aggregate_mass))
      sent_mass.get(res).get + aggregate_mass
      aggregate_mass = 0
    }
  }

  def receive: Receive = {
    case New(arg1) => val result = {
      val first:Int = level(adjacent, levels)
      if(first != -1)     // then the level does exist
        send(arg1, Status(arg1, first))
      new_entry(arg1)
    }

    case Fail(arg1) => val result = {
      val first:Int = level(adjacent, levels)
      val temp:Set[NodeActors] = adjacent - arg1
      val temp_lvl:Map[NodeActors, Int] = levels.filterKeys(_ != arg1)   // created two temp variables to do the level check condition
      if(level(adjacent, levels) != level(temp, temp_lvl))
        broadcast = true

      adjacent -= arg1
      levels = levels.filterKeys(_ != arg1)
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
      val index:Int = get_index(adjacent.toArray, arg1)
      sent_mass.get(arg1).get - arg2
    }

    case Local(arg1) => val result = {
      aggregate_mass = aggregate_mass + arg1 - local_mass
      local_mass = arg1
    }

    case Status(arg1, arg2) => val result = {
      val temp_lvl:Map[NodeActors, Int] = levels.filterKeys(_ != arg1)
      if(level(adjacent, levels) != level(adjacent, temp_lvl))
        broadcast = true
      levels = levels.filterKeys(_ != arg1)
    }
  }
}