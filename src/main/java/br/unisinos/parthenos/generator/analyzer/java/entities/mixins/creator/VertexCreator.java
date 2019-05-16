package br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.Qualified;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

public interface VertexCreator extends Qualified {
  VertexDescriptor getDescriptor();

  default Vertex createVertex() {
    return new Vertex(this.getDescriptor(), this.getQualifiedNameAtom());
  }
}
