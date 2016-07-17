import akka.actor._

case class AdditionMessage(arg1:Double,agr2:Double)
case class SubtractionMessage(arg1:Double,agr2:Double)
case class MultiplicationMessage(arg1:Double,agr2:Double)
case class DivisionMessage(arg1:Double,agr2:Double)

class CalculatorWithActors extends Actor
{
  override def receive: Receive = {
    case AdditionMessage(arg1,arg2) =>val sum= Calculator.addition(arg1,arg2)
      println("The sum is : "+sum)
      sender ! "The sum is : "+sum

    case SubtractionMessage(arg1,arg2) =>val sum= Calculator.subtraction(arg1,arg2)
      println("The sum is : "+sum)
      sender ! "The sum is : "+sum

    case MultiplicationMessage(arg1,arg2) =>val sum= Calculator.multiplication(arg1,arg2)
      println("The sum is : "+sum)
      sender ! "The sum is : "+sum

    case DivisionMessage(arg1,arg2) =>val sum= Calculator.division(arg1,arg2)
      println("The sum is : "+sum)
      sender ! "The sum is : "+sum
  }
}

object CalculatorTest extends App
{
  val system = ActorSystem("CalculatorSystem")
  val calc = system.actorOf(Props[CalculatorWithActors], name = "Calculators")
  // start them going
  calc ! AdditionMessage(3, 5)
  calc ! SubtractionMessage(3, 5)
  calc ! MultiplicationMessage(3, 5)
  calc ! DivisionMessage(3, 5)
}