package br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator;

import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

public interface NameCreator {
  String getName();

  default Edge createNameEdge(Vertex typeVertex) {
    final String name = this.getName();

    if (typeVertex == null || name == null) {
      return null;
    }

    return new Edge(typeVertex.getLabel(), EdgeLabel.NAME, this.getName());
  }
}
