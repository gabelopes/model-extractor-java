package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.FactAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.NameCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.PackageCreator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ConcreteClassAnalyzer implements FactAnalyzer, VertexCreator, NameCreator, PackageCreator {
  private Class<?> concreteClass;

  @Override
  public String getName() {
    return this.getConcreteClass().getSimpleName();
  }

  @Override
  public String getPackageName() {
    return this.getConcreteClass().getPackage().getName();
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.getPackageName(), this.getName());
  }

  @Override
  public VertexDescriptor getDescriptor() {
    if (this.getConcreteClass().isInterface()) {
      return JavaVertexDescriptor.INTERFACE;
    }

    return JavaVertexDescriptor.CLASS;
  }

  @Override
  public Set<Fact> retrieveFacts() {
    final Set<Fact> facts = new HashSet<>();
    final Vertex typeVertex = this.createVertex();

    facts.add(typeVertex);
    facts.add(this.createNameEdge(typeVertex));
    facts.add(this.createPackageEdge(typeVertex));

    return facts;
  }
}
