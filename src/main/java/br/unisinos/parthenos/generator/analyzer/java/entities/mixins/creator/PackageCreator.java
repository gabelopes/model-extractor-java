package br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator;

import br.unisinos.parthenos.generator.enumerator.java.JavaEdgeLabel;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

public interface PackageCreator {
  default String getPackageName() {
    return "";
  }

  default Edge createPackageEdge(Vertex typeVertex) {
    String packageName = this.getPackageName();

    if (typeVertex == null) {
      return null;
    }

    if (packageName == null) {
      packageName = "";
    }

    return new Edge(typeVertex.getLabel(), JavaEdgeLabel.PACKAGE, packageName);
  }
}
