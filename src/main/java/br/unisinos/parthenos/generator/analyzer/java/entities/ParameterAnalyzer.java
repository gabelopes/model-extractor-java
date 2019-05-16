package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.term.Number;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

import static br.unisinos.parthenos.generator.enumerator.EdgeLabel.ORDER;

@AllArgsConstructor
@Getter
public class ParameterAnalyzer extends MemberAnalyzer implements EntityLocator<Parameter> {
  private Vertex methodVertex;
  private Parameter parameter;
  private int parameterOrder;

  @Override
  protected Vertex getPartVertex() {
    return this.getMethodVertex();
  }

  @Override
  protected EdgeLabel getPartLabel() {
    return EdgeLabel.PARAMETER;
  }

  @Override
  public String getName() {
    return this.getParameter().getNameAsString();
  }

  @Override
  public QualifiedName getQualifiedName() {
    return QualifiedName.from(this.getMethodVertex().getLabel().getContent().toString(), this.getName());
  }

  @Override
  public VertexDescriptor getDescriptor() {
    return VertexDescriptor.PARAMETER;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return this.getParameter().getModifiers();
  }

  @Override
  public Type getLeafType() {
    return this.getParameter().getType();
  }

  @Override
  public EdgeLabel getLeafTypeLabel() {
    return EdgeLabel.TYPE;
  }

  @Override
  public Parameter getEntity() {
    return this.getParameter();
  }

  private Edge createOrderEdge(Vertex parameterVertex) {
    final Number<Integer> orderNumber = new Number<>(this.getParameterOrder());
    return new Edge(parameterVertex.getLabel(), ORDER, orderNumber);
  }

  @Override
  protected Set<Fact> createSpecificFacts() {
    final Vertex parameterVertex = this.createVertex();
    return Collections.singleton(this.createOrderEdge(parameterVertex));
  }
}
