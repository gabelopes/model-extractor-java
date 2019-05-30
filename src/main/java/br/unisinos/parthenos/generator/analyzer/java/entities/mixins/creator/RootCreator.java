package br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator;

import br.unisinos.parthenos.generator.prolog.fact.Root;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;

public interface RootCreator extends VertexCreator {
  default Root createRoot() {
    return new Root(this.createVertex());
  }
}
