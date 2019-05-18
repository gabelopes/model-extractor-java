package br.unisinos.parthenos.generator.enumerator.java;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;

public enum Modifier implements VertexCreator {
  PUBLIC,
  PRIVATE,
  PROTECTED,
  STATIC,
  FINAL,
  ABSTRACT,
  TRANSIENT,
  SYNCHRONIZED,
  VOLATILE;

  @Override
  public VertexDescriptor getDescriptor() {
    return JavaVertexDescriptor.MODIFIER;
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.name().toLowerCase());
  }
}
