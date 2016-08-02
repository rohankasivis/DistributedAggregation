import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.duration.Duration

class NonRoot extends NodeActors
{

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

  def receive: Receive = {
    case New(neighbor) => {
      System.out.println("received New(" + neighbor + ") in " + self)

      level(adjacent, levels) match
      {
        case Some(lv) => {
          neighbor ! Status(self, Some(lv))
        }
        case None => ()
      }

      adjacent += neighbor
      sent_mass = sent_mass + (neighbor -> 0)
      received_mass = received_mass + (neighbor -> 0)

    }

    case Fail(neighbor) => {
      System.out.println("received Fail(" + neighbor + ") in " + self)

      sent_mass.get(neighbor) match
      {
        case Some(sent) =>
          received_mass.get(neighbor) match
          {  
            case Some(received) => {
              if (level(adjacent, levels) != level(adjacent - neighbor, levels - neighbor)) {
                broadcast = true
              }

              adjacent = adjacent - neighbor
              levels = levels - neighbor
              aggregate_mass = aggregate_mass + sent - received
              System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
            }
            case None => ()
          }
        case None => ()
      }      
    }

    case Aggregate(sender, aggregate) => {
      System.out.println("received Aggregate(" + sender + ", " + aggregate + ") in " + self)
      received_mass.get(sender) match
      {
        case Some(received) => {
          aggregate_mass = aggregate_mass + aggregate
          received_mass = received_mass + (sender -> (received + aggregate))
        }
        case None => ()
      }
      System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
    }

    case Drop(arg1, arg2) => {

    }

    case Local(local) => {
      aggregate_mass = aggregate_mass + local - local_mass
      local_mass = local
      System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
    }

    case Status(sender, status) => {
      status match 
      {
        case Some(lv) => {
          if (level(adjacent, levels) != level(adjacent, levels + (sender -> lv))) {
            broadcast = true
          }

          levels = levels + (sender -> lv)
        }
        case None => {
          if (level(adjacent, levels) != level(adjacent, levels - sender)) {
            broadcast = true
          }

          levels = levels - sender
        }
      }
    }

    case SendAggregate() => {
      if (aggregate_mass != 0) {
        parent(adjacent, levels) match
        {
          case Some(pr) => {
            sent_mass.get(pr) match
            {
              case Some(sent) => {
                pr ! Aggregate(self, aggregate_mass)
                sent_mass = sent_mass + (pr -> (sent + aggregate_mass))
                aggregate_mass = 0
                System.out.println("aggregate_mass of " + self + " is now " + aggregate_mass)
              }
              case None => ()
            }
          }
          case None => ()
        }
      }
    }

    case Broadcast() => {
      if (broadcast) {
        for (neighbor <- adjacent) {
          neighbor ! Status(self, level(adjacent, levels))
        }
        broadcast = false
      }
    }

  }
}
