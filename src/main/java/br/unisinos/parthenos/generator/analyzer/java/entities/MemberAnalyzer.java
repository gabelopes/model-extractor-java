package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.FactAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.LeafTypeCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.ModifiersCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.NameCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

import java.util.HashSet;
import java.util.Set;

public abstract class MemberAnalyzer implements FactAnalyzer, VertexCreator, NameCreator, ModifiersCreator, LeafTypeCreator {
  protected abstract Vertex getPartVertex();

  protected abstract EdgeLabel getPartLabel();

  protected Set<Fact> createSpecificFacts() {
    return null;
  }

  private Edge createConnectingEdge(Vertex memberVertex) {
    return new Edge(this.getPartVertex().getLabel(), this.getPartLabel(), memberVertex.getLabel());
  }

  @Override
  public Set<Fact> retrieveFacts() {
    final Set<Fact> memberFacts = new HashSet<>();

    final Vertex memberVertex = this.createVertex();
    final Edge connectingEdge = this.createConnectingEdge(memberVertex);
    final Edge nameEdge = this.createNameEdge(memberVertex);
    final Set<Fact> modifiersFacts = this.createModifiersFacts(memberVertex);
    final Set<Fact> leafTypeFacts = this.createLeafTypeFacts(memberVertex);
    final Set<Fact> specificFacts = this.createSpecificFacts();

    memberFacts.add(memberVertex);
    memberFacts.add(connectingEdge);
    memberFacts.add(nameEdge);
    memberFacts.addAll(modifiersFacts);

    if (specificFacts != null) {
      memberFacts.addAll(specificFacts);
    }

    if (leafTypeFacts != null) {
      memberFacts.addAll(leafTypeFacts);
    }

    return memberFacts;
  }
}
