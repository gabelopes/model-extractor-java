package br.unisinos.parthenos.generator.enumerator.java;


import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.prolog.term.Atom;

public enum JavaEdgeLabel implements EdgeLabel {
  NAME,
  PACKAGE,
  RETURN,
  PARENT,
  TYPE,
  MODIFIER,
  PARAMETER,
  INTERFACE,
  METHOD,
  ATTRIBUTE,
  ORDER,
  SOURCE;

  @Override
  public String getContent() {
    return this.name();
  }

  @Override
  public String getTerm() {
    return new Atom(this.getContent().toLowerCase()).getTerm();
  }
}
