package br.unisinos.parthenos.generator.enumerator.java;

import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;

public enum JavaVertexDescriptor implements VertexDescriptor {
  PRIMITIVE,
  NO_TYPE,
  CLASS,
  INTERFACE,
  UNKNOWN_TYPE,
  ATTRIBUTE,
  METHOD,
  PARAMETER,
  MODIFIER,
  ARRAY,
  DEFAULT_TYPE
}
