package br.unisinos.parthenos.generator.prolog.knowledgeBase.java;

import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;

import java.util.Set;

public class JavaKnowledgeBase extends KnowledgeBase {
  private boolean isBranchType(Fact fact) {
    if (!(fact instanceof Vertex)) {
      return false;
    }

    final Vertex vertex = (Vertex) fact;

    return vertex.getDescriptor() == VertexDescriptor.CLASS
      || vertex.getDescriptor() == VertexDescriptor.INTERFACE;
  }

  public Set<Vertex> findBranchTypes() {
    return this.findVertices(this::isBranchType, null);
  }

  public static JavaKnowledgeBase from(Set<Fact> facts) {
    final JavaKnowledgeBase javaKnowledgeBase = new JavaKnowledgeBase();

    javaKnowledgeBase.addAll(facts);

    return javaKnowledgeBase;
  }
}
