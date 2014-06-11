package org.allenai.nlpstack.parse

import org.allenai.nlpstack.Format
import org.allenai.nlpstack.LineProcessor

import org.allenai.nlpstack.parse.graph._
import org.allenai.nlpstack.postag._
import org.allenai.nlpstack.postag.Postagger
import org.allenai.nlpstack.tokenize._

abstract class DependencyParserMain extends LineProcessor("dep-parser") {
  def tokenizer: Tokenizer
  def postagger: Postagger
  def dependencyParser: DependencyParser

  override def init(config: Config) {
    // for timing purposes
    val tokens = tokenizer("I want to initialize the parser.")
    val postagged = postagger.postagTokenized(tokens)
    dependencyParser.dependencyGraphPostagged(postagged)
  }

  override def process(line: String) = {
    val tokens = tokenizer(line)
    val postagged = postagger.postagTokenized(tokens)
    val dgraph = dependencyParser.dependencyGraphPostagged(postagged)
    DependencyParser.multilineStringFormat.write((postagged, dgraph))
  }
}
