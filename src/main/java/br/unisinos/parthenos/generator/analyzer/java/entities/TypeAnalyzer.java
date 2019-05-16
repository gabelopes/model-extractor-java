package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.FactAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.NameCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.PackageCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

import java.util.HashSet;
import java.util.Set;

public abstract class TypeAnalyzer implements FactAnalyzer, VertexCreator, NameCreator, PackageCreator {
  @Override
  public Set<Fact> retrieveFacts() {
    final Set<Fact> typeFacts = new HashSet<>();

    final Vertex typeVertex = this.createVertex();
    final Edge nameEdge = this.createNameEdge(typeVertex);
    final Edge packageEdge = this.createPackageEdge(typeVertex);

    typeFacts.add(typeVertex);
    typeFacts.add(nameEdge);

    if (packageEdge != null) {
      typeFacts.add(packageEdge);
    }

    return typeFacts;
  }
}
