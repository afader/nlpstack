package org.allenai.nlpstack.parse.poly.polyparser

import spray.json._
import spray.json.DefaultJsonProtocol._

/** A StateRef allows you to figure out the token that corresponds to a particular aspect of a
  * TransitionParserState.
  *
  * For instance, we may want to know what token is at the top of the stack for a given state.
  * Applying StackRef(0) to the state will return the index of the token.
  * More accurately, a set is returned, which will be empty if the StateRef refers
  * to a non-existent element of the state. For instance, applying StackRef(3) to a state whose
  * stack has 3 or fewer elements will return the empty set.
  *
  * This set of classes is used primarily to facilitate feature creation (e.g. see
  * StateRefFeature).
  *
  */
sealed abstract class StateRef
    extends (TransitionParserState => Seq[Int]) {

  /** Provides a symbolic representation of the StateRef, used for creating feature names. */
  def name: Symbol
}

object StateRef {

  /** Boilerplate code to serialize a StateRef to JSON using Spray.
    *
    * NOTE: If a subclass has a field named `type`, this will fail to serialize.
    *
    * NOTE: IF YOU INHERIT FROM StateRef, THEN YOU MUST MODIFY THESE SUBROUTINES
    * IN ORDER TO CORRECTLY EMPLOY JSON SERIALIZATION FOR YOUR NEW SUBCLASS.
    */
  implicit object StateRefJsonFormat extends RootJsonFormat[StateRef] {
    def write(stateRef: StateRef): JsValue = stateRef match {
      case LastRef => JsString("LastRef")
      case FirstRef => JsString("FirstRef")
      case PreviousLinkCrumbRef => JsString("PreviousLinkCrumbRef")
      case PreviousLinkGretelRef => JsString("PreviousLinkGretelRef")
      case PreviousLinkCrumbGretelRef => JsString("PreviousLinkCrumbGretelRef")
      case PreviousLinkGrandgretelRef => JsString("PreviousLinkGrandgretelRef")
      case stackRef: StackRef => {
        JsObject(stackRefFormat.write(stackRef).asJsObject.fields +
          ("type" -> JsString("StackRef")))
      }
      case bufferRef: BufferRef => {
        JsObject(bufferRefFormat.write(bufferRef).asJsObject.fields +
          ("type" -> JsString("BufferRef")))
      }
      case stackWindowRef: StackWindowRef => {
        JsObject(stackWindowRefFormat.write(stackWindowRef).asJsObject.fields +
          ("type" -> JsString("StackWindowRef")))
      }
      case bufferWindowRef: BufferWindowRef => {
        JsObject(bufferWindowRefFormat.write(bufferWindowRef).asJsObject.fields +
          ("type" -> JsString("BufferWindowRef")))
      }
      case breadcrumbRef: BreadcrumbRef => {
        JsObject(breadcrumbRefFormat.write(breadcrumbRef).asJsObject.fields +
          ("type" -> JsString("BreadcrumbRef")))
      }
      case stackGretelsRef: StackGretelsRef => {
        JsObject(stackGretelsRefFormat.write(stackGretelsRef).asJsObject.fields +
          ("type" -> JsString("StackGretelsRef")))
      }
      case stackChildrenRef: StackChildrenRef => {
        JsObject(stackChildrenRefFormat.write(stackChildrenRef).asJsObject.fields +
          ("type" -> JsString("StackChildrenRef")))
      }
      case stackLeftGretelsRef: StackLeftGretelsRef => {
        JsObject(stackLeftGretelsRefFormat.write(stackLeftGretelsRef).asJsObject.fields +
          ("type" -> JsString("StackLeftGretelsRef")))
      }
      case stackRightGretelsRef: StackRightGretelsRef => {
        JsObject(stackRightGretelsRefFormat.write(stackRightGretelsRef).asJsObject.fields +
          ("type" -> JsString("StackRightGretelsRef")))
      }
      case bufferGretelsRef: BufferGretelsRef => {
        JsObject(bufferGretelsRefFormat.write(bufferGretelsRef).asJsObject.fields +
          ("type" -> JsString("BufferGretelsRef")))
      }
      case bufferChildrenRef: BufferChildrenRef => {
        JsObject(bufferChildrenRefFormat.write(bufferChildrenRef).asJsObject.fields +
          ("type" -> JsString("BufferChildrenRef")))
      }
      case bufferLeftGretelsRef: BufferLeftGretelsRef => {
        JsObject(bufferLeftGretelsRefFormat.write(bufferLeftGretelsRef).asJsObject.fields +
          ("type" -> JsString("BufferLeftGretelsRef")))
      }
      case bufferRightGretelsRef: BufferRightGretelsRef => {
        JsObject(bufferRightGretelsRefFormat.write(bufferRightGretelsRef).asJsObject.fields +
          ("type" -> JsString("BufferRightGretelsRef")))
      }
    }

    def read(value: JsValue): StateRef = value match {
      case JsString(typeid) => typeid match {
        case "LastRef" => LastRef
        case "FirstRef" => FirstRef
        case "PreviousLinkCrumbRef" => PreviousLinkCrumbRef
        case "PreviousLinkGretelRef" => PreviousLinkGretelRef
        case "PreviousLinkCrumbGretelRef" => PreviousLinkCrumbGretelRef
        case "PreviousLinkGrandgretelRef" => PreviousLinkGrandgretelRef
      }
      case JsObject(values) => values("type") match {
        case JsString("StackRef") => stackRefFormat.read(value)
        case JsString("BufferRef") => bufferRefFormat.read(value)
        case JsString("StackWindowRef") => stackWindowRefFormat.read(value)
        case JsString("BufferWindowRef") => bufferWindowRefFormat.read(value)
        case JsString("BreadcrumbRef") => breadcrumbRefFormat.read(value)
        case JsString("StackGretelsRef") => stackGretelsRefFormat.read(value)
        case JsString("StackChildrenRef") => stackChildrenRefFormat.read(value)
        case JsString("StackLeftGretelsRef") => stackLeftGretelsRefFormat.read(value)
        case JsString("StackRightGretelsRef") => stackRightGretelsRefFormat.read(value)
        case JsString("BufferGretelsRef") => bufferGretelsRefFormat.read(value)
        case JsString("BufferChildrenRef") => bufferChildrenRefFormat.read(value)
        case JsString("BufferLeftGretelsRef") => bufferLeftGretelsRefFormat.read(value)
        case JsString("BufferRightGretelsRef") => bufferRightGretelsRefFormat.read(value)
        case x => deserializationError(s"Invalid identifier for StateRef: $x")
      }
      case _ => deserializationError("Unexpected JsValue type. Must be JsString or JsObject.")
    }
  }

  val stackRefFormat: RootJsonFormat[StackRef] = jsonFormat1(StackRef.apply)
  val bufferRefFormat: RootJsonFormat[BufferRef] = jsonFormat1(BufferRef.apply)
  val stackWindowRefFormat: RootJsonFormat[StackWindowRef] = jsonFormat1(StackWindowRef.apply)
  val bufferWindowRefFormat: RootJsonFormat[BufferWindowRef] = jsonFormat1(BufferWindowRef.apply)
  val breadcrumbRefFormat: RootJsonFormat[BreadcrumbRef] = jsonFormat1(BreadcrumbRef.apply)
  val stackGretelsRefFormat: RootJsonFormat[StackGretelsRef] = jsonFormat1(StackGretelsRef.apply)
  val stackChildrenRefFormat: RootJsonFormat[StackChildrenRef] =
    jsonFormat1(StackChildrenRef.apply)
  val stackLeftGretelsRefFormat: RootJsonFormat[StackLeftGretelsRef] =
    jsonFormat1(StackLeftGretelsRef.apply)
  val stackRightGretelsRefFormat: RootJsonFormat[StackRightGretelsRef] =
    jsonFormat1(StackRightGretelsRef.apply)
  val bufferGretelsRefFormat: RootJsonFormat[BufferGretelsRef] =
    jsonFormat1(BufferGretelsRef.apply)
  val bufferChildrenRefFormat: RootJsonFormat[BufferChildrenRef] =
    jsonFormat1(BufferChildrenRef.apply)
  val bufferLeftGretelsRefFormat: RootJsonFormat[BufferLeftGretelsRef] =
    jsonFormat1(BufferLeftGretelsRef.apply)
  val bufferRightGretelsRefFormat: RootJsonFormat[BufferRightGretelsRef] =
    jsonFormat1(BufferRightGretelsRef.apply)
}

/** A StackRef is a StateRef (see above) whose apply operation returns the `index`th element of
  * the stack, if it exists.
  *
  * @param index the desired stack element, counting from 0 (i.e. 0 is the stack top)
  */
case class StackRef(val index: Int) extends StateRef {
  require(index >= 0)

  override def apply(state: TransitionParserState): Seq[Int] = {
    Seq(state.stack.lift(index)).flatten
  }

  @transient
  override val name: Symbol = Symbol("stackRef" + index)
}

/** A BufferRef is a StateRef (see above) whose apply operation returns the `index`th element of
  * the buffer, if it exists.
  *
  * @param index the desired buffer element, counting from 0 (i.e. 0 is the front of the buffer)
  */
case class BufferRef(val index: Int) extends StateRef {
  require(index >= 0)

  override def apply(state: TransitionParserState): Seq[Int] = {
    val result = state.bufferPosition + index
    if (result < state.sentence.tokens.size) {
      Seq(result)
    } else {
      Seq()
    }
  }

  @transient
  override val name: Symbol = Symbol("bufferRef" + index)
}

case class StackWindowRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a StackWindowRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    Range(0, index) flatMap { x => BufferRef(x)(state) }
  }

  @transient
  override val name: Symbol = Symbol("stackWindowRef" + index)
}

case class BufferWindowRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a BufferWindowRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    Range(0, index) flatMap { x => BufferRef(x)(state) }
  }

  @transient
  override val name: Symbol = Symbol("bufferWindowRef" + index)
}

case class StackGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a StackGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    StackRef(index)(state) flatMap { nodeIndex => state.getGretels(nodeIndex) }
  }

  @transient
  override val name: Symbol = Symbol("stackGretelRef" + index)
}

case class StackChildrenRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a StackChildrenRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    StackRef(index)(state) flatMap { nodeIndex => state.children.getOrElse(nodeIndex, Seq()) }
  }

  @transient
  override val name: Symbol = Symbol("stackChildrenRef" + index)
}

case class StackLeftGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a StackLeftGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    StackRef(index)(state) flatMap { nodeIndex =>
      state.getGretels(nodeIndex) filter { gretel =>
        gretel < nodeIndex
      }
    }
  }

  @transient
  override val name: Symbol = Symbol("stackLeftGretelRef" + index)
}

case class StackRightGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a StackRightGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    StackRef(index)(state) flatMap { nodeIndex =>
      state.getGretels(nodeIndex) filter { gretel =>
        gretel > nodeIndex
      }
    }
  }

  @transient
  override val name: Symbol = Symbol("stackRightGretelRef" + index)
}

case class BufferGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a BufferGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    BufferRef(index)(state) flatMap { nodeIndex => state.getGretels(nodeIndex) }
  }

  @transient
  override val name: Symbol = Symbol("bufferGretelRef" + index)
}

case class BufferChildrenRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a BufferChildrenRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    BufferRef(index)(state) flatMap { nodeIndex => state.children.getOrElse(nodeIndex, Seq()) }
  }

  @transient
  override val name: Symbol = Symbol("bufferChildrenRef" + index)
}

case class BufferLeftGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a BufferLeftGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    BufferRef(index)(state) flatMap { nodeIndex =>
      state.getGretels(nodeIndex) filter { gretel =>
        gretel < nodeIndex
      }
    }
  }

  @transient
  override val name: Symbol = Symbol("bufferLeftGretelRef" + index)
}

case class BufferRightGretelsRef(val index: Int) extends StateRef {
  require(index >= 0, "the index of a BufferLeftGretelsRef must be a nonnegative integer")

  override def apply(state: TransitionParserState): Seq[Int] = {
    BufferRef(index)(state) flatMap { nodeIndex =>
      state.getGretels(nodeIndex) filter { gretel =>
        gretel > nodeIndex
      }
    }
  }

  @transient
  override val name: Symbol = Symbol("bufferRightGretelRef" + index)
}

/** A BreadcrumbRef is a StateRef (see above) whose apply operation returns the breadcrumb of
  * the `index`th element of the stack, if it exists.
  *
  * @param index the desired stack element, counting from 0 (i.e. 0 is the stack top)
  */
case class BreadcrumbRef(val index: Int) extends StateRef {
  require(index >= 0)

  override def apply(state: TransitionParserState): Seq[Int] = {
    if (index < state.stack.size && state.breadcrumb.getOrElse(state.stack(index), -1) >= 0) {
      Seq(state.breadcrumb(state.stack(index)))
    } else {
      Seq()
    }
  }

  @transient
  override val name: Symbol = Symbol("crumbRef" + index)
}

case object PreviousLinkCrumbRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    state.previousLink match {
      case Some((crumb, _)) => Seq(crumb)
      case None => Seq()
    }
  }

  @transient
  override val name: Symbol = Symbol("prevLinkCrumb")
}

case object PreviousLinkCrumbGretelRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    PreviousLinkCrumbRef(state) flatMap { nodeIndex => state.getGretels(nodeIndex) }
  }

  @transient
  override val name: Symbol = Symbol("prevLinkCrumbGretel")
}

case object PreviousLinkGretelRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    state.previousLink match {
      case Some((_, gretel)) => Seq(gretel)
      case None => Seq()
    }
  }

  @transient
  override val name: Symbol = Symbol("prevLinkGretel")
}

case object PreviousLinkGrandgretelRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    PreviousLinkGretelRef(state) flatMap { nodeIndex => state.getGretels(nodeIndex) }
  }

  @transient
  override val name: Symbol = Symbol("prevLinkGrandgretel")
}

/** A LastRef is a StateRef (see above) whose apply operation returns the final element of
  * the sentence.
  */
case object LastRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    Seq(state.sentence.tokens.size - 1)
  }

  @transient
  override val name: Symbol = Symbol("lastRef")
}

/** A FirstRef is a StateRef (see above) whose apply operation returns the first element of
  * the sentence.
  */
case object FirstRef extends StateRef {

  override def apply(state: TransitionParserState): Seq[Int] = {
    if (state.sentence.tokens.size > 1) {
      Seq(1)
    } else {
      Seq()
    }
  }

  @transient
  override val name: Symbol = Symbol("firstRef")
}
