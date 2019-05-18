package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.FactAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class FieldAnalyzer implements FactAnalyzer, EntityLocator<FieldDeclaration> {
  private Vertex typeVertex;
  private FieldDeclaration fieldDeclaration;

  protected List<VariableDeclarator> getVariables() {
    return this.getFieldDeclaration().getVariables();
  }

  protected List<Modifier> getModifiers() {
    return this.getFieldDeclaration().getModifiers();
  }

  protected Set<Fact> createVariableFacts(VariableDeclarator variableDeclarator) {
    final FieldVariableAnalyzer fieldVariableAnalyzer = new FieldVariableAnalyzer(this.getTypeVertex(), variableDeclarator, this.getModifiers());
    return fieldVariableAnalyzer.retrieveFacts();
  }

  private Set<Fact> createVariablesFacts() {
    final Set<Fact> variablesFacts = new HashSet<>();

    for (VariableDeclarator variableDeclarator : this.getVariables()) {
      variablesFacts.addAll(this.createVariableFacts(variableDeclarator));
    }

    return variablesFacts;
  }

  @Override
  public FieldDeclaration getEntity() {
    return this.getFieldDeclaration();
  }

  @Override
  public Set<Fact> retrieveFacts() {
    return this.createVariablesFacts();
  }
}
