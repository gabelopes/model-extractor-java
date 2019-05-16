package br.unisinos.parthenos.generator.analyzer.java.entities.mixins;

import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.prolog.term.Atom;

public interface Qualified {
  QualifiedName getQualifiedName();

  default Atom getQualifiedNameAtom() {
    return new Atom(this.getQualifiedName().toString());
  }

}
