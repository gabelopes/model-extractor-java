package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import br.unisinos.parthenos.generator.enumerator.java.JavaEdgeLabel;
import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class MethodAnalyzer extends MemberAnalyzer implements EntityLocator<MethodDeclaration> {
  private Vertex typeVertex;
  private MethodDeclaration methodDeclaration;
  private Integer methodNumber;

  @Override
  protected Vertex getPartVertex() {
    return this.getTypeVertex();
  }

  @Override
  protected EdgeLabel getPartLabel() {
    return JavaEdgeLabel.METHOD;
  }

  @Override
  public String getName() {
    return this.getMethodDeclaration().getNameAsString();
  }

  @Override
  public QualifiedName getQualifiedName() {
    final Integer number = this.getMethodNumber();
    final String methodNumber = number == null ? "" : number.toString();
    final String methodName = this.getName() + methodNumber;

    return QualifiedName.from(this.getTypeVertex().getLabel().getContent().toString(), methodName);
  }

  @Override
  public VertexDescriptor getDescriptor() {
    return JavaVertexDescriptor.METHOD;
  }

  @Override
  public Set<Modifier> getModifiers() {
    return this.getMethodDeclaration().getModifiers();
  }

  @Override
  public Type getLeafType() {
    return this.getMethodDeclaration().getType();
  }

  @Override
  public JavaEdgeLabel getLeafTypeLabel() {
    return JavaEdgeLabel.RETURN;
  }

  @Override
  public MethodDeclaration getEntity() {
    return this.getMethodDeclaration();
  }

  private List<Parameter> getParameters() {
    return this.getMethodDeclaration().getParameters();
  }

  private Set<Fact> createParameterFacts(Vertex methodVertex, Parameter parameter, int parameterOrder) {
    final ParameterAnalyzer parameterAnalyzer = new ParameterAnalyzer(methodVertex, parameter, parameterOrder);
    return parameterAnalyzer.retrieveFacts();
  }

  private Set<Fact> createParametersFacts() {
    final Vertex methodVertex = this.createVertex();
    final List<Parameter> parameters = this.getParameters();
    final Set<Fact> parametersFacts = new HashSet<>();

    for (int i = 0; i < parameters.size(); i++) {
      final Parameter parameter = parameters.get(i);
      parametersFacts.addAll(this.createParameterFacts(methodVertex, parameter, i));
    }

    return parametersFacts;
  }

  @Override
  protected Set<Fact> createSpecificFacts() {
    return this.createParametersFacts();
  }
}
