package br.unisinos.parthenos.generator.enumerator.java;

import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.prolog.term.Atom;

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
  DEFAULT_TYPE;

  @Override
  public String getContent() {
    return this.name();
  }

  @Override
  public String getTerm() {
    return new Atom(this.getContent().toLowerCase()).getTerm();
  }
}
