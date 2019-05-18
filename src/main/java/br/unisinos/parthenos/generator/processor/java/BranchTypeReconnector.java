package br.unisinos.parthenos.generator.processor.java;

import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.java.JavaKnowledgeBase;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class BranchTypeReconnector {
  private Collection<KnowledgeBase> knowledgeBases;
  private Set<Vertex> branchTypes;

  public BranchTypeReconnector(Collection<KnowledgeBase> knowledgeBases) {
    this.knowledgeBases = knowledgeBases;
    this.branchTypes = new HashSet<>();
  }

  private boolean isMatchingUnknownType(Fact fact, Vertex typeVertex) {
    if (!(fact instanceof Vertex)) {
      return false;
    }

    final Vertex vertex = (Vertex) fact;

    return vertex.getDescriptor() == JavaVertexDescriptor.UNKNOWN_TYPE
      && vertex.getLabel().equals(typeVertex.getLabel());
  }

  private void reconnectBranchType(KnowledgeBase knowledgeBase, Vertex typeVertex) {
    final boolean unknownTypeRemoved = knowledgeBase.removeIf(fact -> this.isMatchingUnknownType(fact, typeVertex));

    if (unknownTypeRemoved) {
      knowledgeBase.add(typeVertex);
    }
  }

  private void reconnectBranchTypes() {
    for (Vertex typeVertex : this.getBranchTypes()) {
      System.out.println("Reconnecting types for " + typeVertex.getLabel().getContent());
      for (KnowledgeBase knowledgeBase : this.getKnowledgeBases()) {
        this.reconnectBranchType(knowledgeBase, typeVertex);
      }
    }
  }

  private void addBranchTypes(Set<Fact> facts) {
    this.getBranchTypes().addAll(JavaKnowledgeBase.from(facts).findBranchTypes());
  }

  private void populateBranchTypes() {
    for (KnowledgeBase knowledgeBase : this.getKnowledgeBases()) {
      this.addBranchTypes(knowledgeBase);
    }
  }

  public void reconnect() {
    this.populateBranchTypes();
    this.reconnectBranchTypes();
  }
}
