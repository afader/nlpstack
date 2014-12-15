package org.allenai.nlpstack.parse.poly.fsm

import org.allenai.common.json._
import org.allenai.nlpstack.parse.poly.polyparser.ArcEagerTransitionSystem
import org.allenai.nlpstack.parse.poly.polyparser.labeler.ParseLabelerTransitionSystem
import spray.json.DefaultJsonProtocol._
import spray.json._

trait TransitionSystem {
  def initialState(marbleBlock: MarbleBlock): Option[State]
  def guidedCostFunction(goldObj: MarbleBlock): Option[StateCostFunction]
  val feature: StateFeature
  def toSculpture(state: State): Option[Sculpture]
  def interpretConstraint(constraint: TransitionConstraint): ((State, StateTransition) => Boolean)
}

object TransitionSystem {

  implicit object TransitionSystemJsonFormat extends RootJsonFormat[TransitionSystem] {
    implicit val arcEagerFormat =
      jsonFormat2(ArcEagerTransitionSystem.apply).pack("type" -> "ArcEagerTransitionSystem")

    def write(transitionSystem: TransitionSystem): JsValue = transitionSystem match {
      case ParseLabelerTransitionSystem => JsString("ParseLabelerTransitionSystem")
      case aeSys: ArcEagerTransitionSystem => aeSys.toJson
      case x => deserializationError(s"Cannot serialize this state type: $x")
    }

    def read(value: JsValue): TransitionSystem = value match {
      case JsString(typeid) => typeid match {
        case "ParseLabelerTransitionSystem" => ParseLabelerTransitionSystem
        case x => deserializationError(s"Invalid identifier for TaskIdentifier: $x")
      }
      case jsObj: JsObject => jsObj.unpackWith(arcEagerFormat)
      case _ => deserializationError("Unexpected JsValue type.")
    }
  }

  def trivialConstraint(state: State, transition: StateTransition): Boolean = false
}
