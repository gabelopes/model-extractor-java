package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassAnalyzer extends InterfaceAnalyzer {
  public ClassAnalyzer(ClassOrInterfaceDeclaration interfaceDeclaration) {
    super(interfaceDeclaration);
  }

  private List<FieldDeclaration> getFields() {
    return this.getTypeDeclaration().getFields();
  }

  private Set<Fact> createFieldFacts(Vertex typeVertex, FieldDeclaration fieldDeclaration) {
    final FieldAnalyzer fieldAnalyzer = new FieldAnalyzer(typeVertex, fieldDeclaration);
    return fieldAnalyzer.retrieveFacts();
  }

  private List<Fact> createFieldsFacts() {
    final Vertex typeVertex = this.createVertex();
    final List<FieldDeclaration> fields = this.getFields();
    final List<Fact> fieldsFacts = new ArrayList<>();

    for (FieldDeclaration fieldDeclaration : fields) {
      fieldsFacts.addAll(this.createFieldFacts(typeVertex, fieldDeclaration));
    }

    return fieldsFacts;
  }

  @Override
  public Set<Fact> retrieveSpecificFacts() {
    final Set<Fact> facts = super.retrieveSpecificFacts();

    facts.addAll(this.createFieldsFacts());

    return facts;
  }
}
