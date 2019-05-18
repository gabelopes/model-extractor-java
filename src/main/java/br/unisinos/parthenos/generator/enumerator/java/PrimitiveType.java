package br.unisinos.parthenos.generator.enumerator.java;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;

public enum PrimitiveType implements VertexCreator {
  INT,
  SHORT,
  LONG,
  FLOAT,
  DOUBLE,
  CHAR,
  BOOLEAN,
  BYTE;

  @Override
  public VertexDescriptor getDescriptor() {
    return JavaVertexDescriptor.PRIMITIVE;
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.name().toLowerCase());
  }
}
