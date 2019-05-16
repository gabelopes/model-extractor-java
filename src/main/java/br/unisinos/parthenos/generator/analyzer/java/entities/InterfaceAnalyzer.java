package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;
import br.unisinos.parthenos.generator.prolog.term.Text;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InterfaceAnalyzer extends BranchTypeAnalyzer<ClassOrInterfaceDeclaration> {
  public InterfaceAnalyzer(ClassOrInterfaceDeclaration typeDeclaration) {
    super(typeDeclaration);
  }

  private List<ClassOrInterfaceType> getExtendedTypes() {
    return ((ClassOrInterfaceDeclaration) this.getTypeDeclaration()).getExtendedTypes();
  }

  private List<ClassOrInterfaceType> getImplementedTypes() {
    return ((ClassOrInterfaceDeclaration) this.getTypeDeclaration()).getImplementedTypes();
  }

  private List<MethodDeclaration> getMethods() {
    return this.getTypeDeclaration().getMethods();
  }

  private Set<Fact> createExtendedTypeFacts(ClassOrInterfaceType extendedType) {
    final Vertex interfaceVertex = this.createVertex();
    final ExtendedTypeAnalyzer extendedTypeAnalyzer = new ExtendedTypeAnalyzer(extendedType);

    return extendedTypeAnalyzer.createLeafTypeFacts(interfaceVertex);
  }

  private Set<Fact> createImplementedTypeFacts(ClassOrInterfaceType implementedType) {
    final Vertex interfaceVertex = this.createVertex();
    final ImplementedTypeAnalyzer implementedTypeAnalyzer = new ImplementedTypeAnalyzer(implementedType);

    return implementedTypeAnalyzer.createLeafTypeFacts(interfaceVertex);
  }

  private Set<Fact> createMethodFacts(MethodDeclaration methodDeclaration, int number) {
    final Vertex interfaceVertex = this.createVertex();
    final Integer methodNumber = number == 0 ? null : number + 1;
    final MethodAnalyzer methodAnalyzer = new MethodAnalyzer(interfaceVertex, methodDeclaration, methodNumber);

    return methodAnalyzer.retrieveFacts();
  }

  private int getMethodNumber(Set<Fact> facts, MethodDeclaration methodDeclaration) {
    final String methodName = methodDeclaration.getNameAsString();
    return KnowledgeBase
      .from(facts)
      .findEdges(null, EdgeLabel.NAME, new Text(methodName))
      .size();
  }

  private Set<Fact> createExtendedTypesFacts() {
    return this.getExtendedTypes()
      .stream()
      .map(this::createExtendedTypeFacts)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }

  private Set<Fact> createImplementedTypesFacts() {
    return this.getImplementedTypes()
      .stream()
      .map(this::createImplementedTypeFacts)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }

  private Set<Fact> createMethodsFacts(Set<Fact> interfaceFacts) {
    final Set<Fact> methodsFacts = new HashSet<>();
    final List<MethodDeclaration> methods = this.getMethods();

    for (final MethodDeclaration methodDeclaration : methods) {
      final int methodNumber = this.getMethodNumber(interfaceFacts, methodDeclaration);
      interfaceFacts.addAll(this.createMethodFacts(methodDeclaration, methodNumber));
    }

    return methodsFacts;
  }

  @Override
  protected Set<Fact> retrieveSpecificFacts() {
    final Set<Fact> interfaceFacts = new HashSet<>();

    interfaceFacts.addAll(this.createExtendedTypesFacts());
    interfaceFacts.addAll(this.createImplementedTypesFacts());
    interfaceFacts.addAll(this.createMethodsFacts(interfaceFacts));

    return interfaceFacts;
  }
}
