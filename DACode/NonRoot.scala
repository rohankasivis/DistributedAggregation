import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.duration.Duration

class NonRoot extends NodeActors
{
  private var levels:Map[ActorRef, Int] = Map.empty
  private var sent_mass:Map[ActorRef, Int] = Map.empty
  private var received_mass:Map[ActorRef, Int] = Map.empty

  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  //private var adjacent:Array[NodeActors] = new Array[NodeActors](100)
  private var adjacent:Set[ActorRef] = Set.empty
  private var broadcast:Boolean = false
  val system = ActorSystem("NodeActors")

  def new_entry(nodeActors:ActorRef)
  {
    adjacent += nodeActors
    sent_mass = sent_mass + (nodeActors -> 0)
    received_mass = received_mass + (nodeActors -> 0)
  }

  def remove_entry(nodeActors:ActorRef)
  {
    adjacent -= nodeActors        // remove an element from the nodeactors set
    // remove the element corresponding to the nodeactor from level map
  }

  def parent(nodeActors:Set[ActorRef], levels:Map[ActorRef, Int]): Option[ActorRef] =
  {
    par(nodeActors, levels) match
    {
      case Some((parentRef, _)) => Some(parentRef)
      case None => None
    }
  }

  def level(nodeActors:Set[ActorRef], levels:Map[ActorRef, Int]): Option[Int] =
  {
    par(nodeActors, levels) match
    {
      case Some((_, parentLevel)) => Some(parentLevel + 1)
      case None => None
    }
  }

  def par(nodeActors:Set[ActorRef], levels:Map[ActorRef, Int]): Option[Tuple2[ActorRef, Int]] =
  {
    if (nodeActors.isEmpty)
      return None

    val currRef: ActorRef = nodeActors.head

    par(nodeActors.tail, levels) match
    {
      case Some((parRef, parLevel)) =>
        levels.get(currRef) match
        {
          case Some(currLevel) =>
            if (currLevel < parLevel)
              Some((currRef, currLevel))
            else
              Some((parRef, parLevel))
          case None =>
            Some((parRef, parLevel))
        }
      case None =>
        levels.get(currRef) match
        {
          case Some(currLevel) =>
            Some((currRef, currLevel))
          case None =>
            None
        }
    }
  }

  def send(nodeActors:Set[ActorRef], value:Status)
  {
    for(curr <- nodeActors)
      curr ! value
  }


  def broadcast_var()
  {
    println("Value of broadcast : "+broadcast+" in ActorRef: "+self.toString())
    if (broadcast) {
      println ("Entering broadcast_var")
      send (adjacent, Status (self, level (adjacent, levels) ) )
      broadcast = false
      println ("Exiting broadcast_var")
    }
  }

  def send(arg1: ActorRef, status: Status) =
  {
    arg1 ! status
  }

  def send_agg(arg1:ActorRef, adjacent:Aggregate) = {
    arg1 ! adjacent
  }

  def handle_aggregate() =
  {
    parent(adjacent, levels) match {
      case None => ()
      case Some(parent_ref) =>
        sent_mass.get(parent_ref) match {
          case None => ()
          case Some(parent_mass) =>
            println ("Self :" + self.toString () + "levels size :" + levels.size + " adjacent size:" + adjacent.size)
            println (self.toString () + " sending Aggregate(" + aggregate_mass + ") to " + parent_ref.toString () )
            send_agg(parent_ref, Aggregate(self, aggregate_mass))
            sent_mass = sent_mass + (parent_ref -> (parent_mass + aggregate_mass))
            aggregate_mass = 0
        }
    }
  }

  def receive: Receive = {
    case New(arg1) => val result = {
      System.out.println ("Start Calling in NonRoot Case New :" + arg1.toString () )

      level (adjacent, levels) match {
        case Some (lv) => send (arg1, Status (self, Some(lv)))
        case None => ()
      }

      new_entry (arg1)

      System.out.println ("Finish Calling in NonRoot Case New :" + arg1.toString () )
      sender ! true
    }

    case Fail(arg1) => val result = {
      arg1 match {
        case null => None
        case value =>
          sent_mass.get(arg1) match
          {
            case Some(s) =>
              received_mass.get(arg1) match {
                case Some(s) =>
                  System.out.println ("Inside Fail")
                  println ("Fail message received from " + arg1.toString () + " to ActorRef :" + self.toString () )
                  val first: Int = level (adjacent, levels).get
                  val temp: Set[ActorRef] = adjacent - arg1
                  val temp_lvl: Map[ActorRef, Int] = levels.filterKeys (_!= arg1) // created two temp variables to do the level check condition
                  if (level (adjacent, levels) != level (temp, temp_lvl) )
                    broadcast = true

                  adjacent -= arg1
                  levels = levels.filterKeys (_!= arg1)
                  val sent_val: Option[Int] = sent_mass.get (arg1)
                  val received_val: Option[Int] = received_mass.get (arg1)
                  aggregate_mass = aggregate_mass + sent_val.get - received_val.get
                  System.out.println ("Inside Fail")
                case None => None
              }
            case None => None
          }
      }
    }

    case Aggregate(arg1, arg2) => val result = {
      arg1 match {
        case null => None
        case some =>
          println ("received Aggregate(" + arg2 + ") from " + arg1.toString () )
          aggregate_mass = aggregate_mass + arg2
          println ("Aggregate Mass value = " + aggregate_mass)
          //aggregate_mass = aggregate_mass + arg2
          val tmp_val = received_mass.get (arg1) match {
            case Some (s) => s
            case None => 0
          }
          tmp_val + arg2
          handle_aggregate ()
      }
    }

    case Drop(arg1, arg2) => val result = {
      aggregate_mass = aggregate_mass + arg2
      val tmp_val=sent_mass.get(arg1)match
      {
        case Some(s) => s
        case None => 0
      }
      tmp_val - arg2
    }

    case Local(arg1) => val result = {
      println("Received Aggregate in "+self.toString()+" node  :"+arg1)
      aggregate_mass = aggregate_mass + arg1 - local_mass
      println(" Aggregate in "+self.toString()+" node  :"+aggregate_mass)
      // handle_aggregate()

      local_mass = arg1
    }
    case SendAggregate() => {
      handle_aggregate()
    }

    case sendBroadcast() => {
      broadcast_var()
    }

    case Status(arg1, arg2) => val result = {
      // check the adjacent contains the passed in arg1 if not
      // add it
      arg1 match {
        case null => None
        case some =>
          System.out.println ("Start Calling in NonRoot Case Status :" + arg1.toString () )
          println(self.toString()+" adjacent size in status "+adjacent.size)
          if (! adjacent.contains (arg1) ) {
            adjacent += arg1
          }
          levels += (arg1 -> arg2.get)
          val temp_lvl: Map[ActorRef, Int] = levels.filterKeys (_!= arg1)
          if (level (adjacent, levels) != level (adjacent, temp_lvl) )
            broadcast = true
          //  levels = levels.filterKeys(_ != arg1)
          System.out.println ("Stop Calling in NonRoot Case Status :" + arg1.toString () )
      }
    }

    case terminate() => {
      println("Inside terminate before sending fail message")
      //  import system.dispatcher
      for(curr <- adjacent) {
        curr ! Fail(self)
      }
      println("Inside terminate after sending fail message")
      context.stop(self)
    }
  }
}