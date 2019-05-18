package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.enumerator.java.JavaEdgeLabel;
import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class FieldVariableAnalyzer extends MemberAnalyzer implements EntityLocator<VariableDeclarator> {
  private Vertex typeVertex;
  private VariableDeclarator variableDeclarator;
  private Set<Modifier> modifiers;

  @Override
  protected Vertex getPartVertex() {
    return this.getTypeVertex();
  }

  @Override
  protected EdgeLabel getPartLabel() {
    return JavaEdgeLabel.ATTRIBUTE;
  }

  @Override
  public String getName() {
    return this.getVariableDeclarator().getNameAsString();
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.getTypeVertex().getLabel().getContent().toString(), this.getName());
  }

  @Override
  public VertexDescriptor getDescriptor() {
    return JavaVertexDescriptor.ATTRIBUTE;
  }

  @Override
  public Type getLeafType() {
    return this.getVariableDeclarator().getType();
  }

  @Override
  public EdgeLabel getLeafTypeLabel() {
    return JavaEdgeLabel.TYPE;
  }

  @Override
  public VariableDeclarator getEntity() {
    return this.getVariableDeclarator();
  }
}
