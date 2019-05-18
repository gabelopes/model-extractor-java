package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.ModifiersCreator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public abstract class BranchTypeAnalyzer<T extends TypeDeclaration<?>> extends TypeAnalyzer implements ModifiersCreator, EntityLocator<TypeDeclaration<?>> {
  private TypeDeclaration<T> typeDeclaration;

  protected abstract Set<Fact> retrieveSpecificFacts();

  @Override
  public String getName() {
    return this.getTypeDeclaration().getNameAsString();
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.getPackageName(), this.getName());
  }

  @Override
  public String getPackageName() {
    return this.findPackageName();
  }

  @Override
  public VertexDescriptor getDescriptor() {
    if (!this.getTypeDeclaration().isClassOrInterfaceDeclaration()) {
      return null;
    }

    if (((ClassOrInterfaceDeclaration) this.getTypeDeclaration()).isInterface()) {
      return JavaVertexDescriptor.INTERFACE;
    }

    return JavaVertexDescriptor.CLASS;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return this.getTypeDeclaration().getModifiers();
  }

  @Override
  public TypeDeclaration<?> getEntity() {
    return this.getTypeDeclaration();
  }

  @Override
  public Set<Fact> retrieveFacts() {
    final Set<Fact> typeFacts = super.retrieveFacts();

    final Vertex typeVertex = this.createVertex();

    typeFacts.addAll(this.createModifiersFacts(typeVertex));
    typeFacts.addAll(this.retrieveSpecificFacts());

    return typeFacts;
  }
}
