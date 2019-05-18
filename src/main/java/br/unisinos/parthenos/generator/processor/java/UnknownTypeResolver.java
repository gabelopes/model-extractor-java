package br.unisinos.parthenos.generator.processor.java;

import br.unisinos.parthenos.generator.analyzer.java.entities.ConcreteClassAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.enumerator.java.JavaVertexDescriptor;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;
import br.unisinos.parthenos.generator.prolog.term.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static br.unisinos.parthenos.generator.enumerator.java.JavaEdgeLabel.*;

@Getter
@AllArgsConstructor
public class UnknownTypeResolver {
  private Collection<KnowledgeBase> knowledgeBases;

  private Set<Vertex> findUnknownTypes(KnowledgeBase knowledgeBase) {
    return knowledgeBase.findVertices(JavaVertexDescriptor.UNKNOWN_TYPE, null);
  }

  private boolean isProperLabel(Fact fact) {
    if (!(fact instanceof Edge)) {
      return false;
    }

    final Edge edge = (Edge) fact;
    final List<EdgeLabel> properLabels = Arrays.asList(ATTRIBUTE, TYPE, METHOD, RETURN, PARAMETER, PARENT, INTERFACE);

    return properLabels.contains(edge.getLabel());
  }

  private boolean isBranchType(Vertex vertex) {
    return vertex.getDescriptor() == JavaVertexDescriptor.CLASS
      || vertex.getDescriptor() == JavaVertexDescriptor.INTERFACE;
  }

  private Vertex findBranchType(KnowledgeBase knowledgeBase, Vertex vertex) {
    if (this.isBranchType(vertex)) {
      return vertex;
    }

    final Set<Edge> incomingEdges = knowledgeBase.findEdges(null, this::isProperLabel, (fact) -> ((Edge) fact).getTail().equals(vertex.getLabel()));

    for (Edge incomingEdge : incomingEdges) {
      final Term<?> head = incomingEdge.getHead();
      final Vertex knowledgeBaseVertex = knowledgeBase.findVertex(null, head);
      final Vertex foundBranchType = this.findBranchType(knowledgeBase, knowledgeBaseVertex);

      if (foundBranchType != null) {
        return foundBranchType;
      }
    }

    return null;
  }

  private String getPackage(KnowledgeBase knowledgeBase, Vertex typeVertex) {
    final Edge packageEdge = knowledgeBase.findEdge(typeVertex.getLabel(), PACKAGE, null);

    if (packageEdge != null) {
      return packageEdge.getTail().getContent().toString();
    }

    return null;
  }

  private String getName(KnowledgeBase knowledgeBase, Vertex typeVertex) {
    final Edge packageEdge = knowledgeBase.findEdge(typeVertex.getLabel(), NAME, null);

    if (packageEdge != null) {
      return packageEdge.getTail().getContent().toString();
    }

    return null;
  }

  private boolean hasTypeDescriptor(Fact fact) {
    if (!(fact instanceof Vertex)) {
      return false;
    }

    final Vertex vertex = (Vertex) fact;

    return vertex.getDescriptor() == JavaVertexDescriptor.CLASS
      || vertex.getDescriptor() == JavaVertexDescriptor.INTERFACE;
  }

  private Vertex findTypeWithNameAndPackage(KnowledgeBase knowledgeBase, String typeName, String typePackage) {
    final Set<Vertex> vertices = knowledgeBase.findVertices(this::hasTypeDescriptor, null);

    for (Vertex vertex : vertices) {
      final String vertexName = this.getName(knowledgeBase, vertex);
      final String vertexPackage = this.getPackage(knowledgeBase, vertex);

      if (Objects.equals(typeName, vertexName) && Objects.equals(typePackage, vertexPackage)) {
        return vertex;
      }
    }

    return null;
  }

  private Pair<KnowledgeBase, Vertex> findKnownType(KnowledgeBase knowledgeBase, Vertex unknownType) {
    final Vertex branchType = this.findBranchType(knowledgeBase, unknownType);

    if (branchType == null) {
      return null;
    }

    final String unknownTypeName = this.getName(knowledgeBase, unknownType);
    final String branchTypePackage = this.getPackage(knowledgeBase, branchType);

    for (KnowledgeBase candidateKnowledgeBase : this.getKnowledgeBases()) {
      final Vertex typeVertex = this.findTypeWithNameAndPackage(candidateKnowledgeBase, unknownTypeName, branchTypePackage);

      if (typeVertex != null) {
        return new ImmutablePair<>(candidateKnowledgeBase, typeVertex);
      }
    }

    return null;
  }

  private Pair<KnowledgeBase, Vertex> findKnownByQualifiedName(QualifiedName qualifiedName) {
    final Class<?> knownClass;

    try {
      knownClass = Class.forName(qualifiedName.toString());
    } catch (ClassNotFoundException e) {
      return null;
    }

    final ConcreteClassAnalyzer concreteClassAnalyzer = new ConcreteClassAnalyzer(knownClass);
    final Set<Fact> knownTypeFacts = concreteClassAnalyzer.retrieveFacts();
    final KnowledgeBase knownTypeKnowledgeBase = KnowledgeBase.from(knownTypeFacts);
    final Vertex knownTypeVertex = knownTypeKnowledgeBase.findAnyVertex();

    return new ImmutablePair<>(knownTypeKnowledgeBase, knownTypeVertex);
  }

  private Pair<KnowledgeBase, Vertex> getKnownType(KnowledgeBase knowledgeBase, Vertex unknownType) {
    final QualifiedName qualifiedName = QualifiedName.from(unknownType.getLabel().getContent().toString());

    if (qualifiedName.hasPackage()) {
      final Pair<KnowledgeBase, Vertex> knownType = this.findKnownByQualifiedName(qualifiedName);

      if (knownType != null) {
        return knownType;
      }
    }

    return this.findKnownType(knowledgeBase, unknownType);
  }

  private void replaceEdgesHeads(Set<Edge> edges, Term<?> head) {
    for (Edge edge : edges) {
      edge.setHead(head);
    }
  }

  private void replaceEdgesTails(Set<Edge> edges, Term<?> tail) {
    for (Edge edge : edges) {
      edge.setTail(tail);
    }
  }

  private void removeUnknownType(KnowledgeBase knowledgeBase, Vertex unknownType) {
    final Edge unknownTypeNameEdge = knowledgeBase.findEdge(unknownType.getLabel(), NAME, null);
    final Edge unknownTypePackageEdge = knowledgeBase.findEdge(unknownType.getLabel(), PACKAGE, null);

    knowledgeBase.remove(unknownType);
    knowledgeBase.remove(unknownTypeNameEdge);
    knowledgeBase.remove(unknownTypePackageEdge);
  }

  private void addKnownType(KnowledgeBase knowledgeBase, Pair<KnowledgeBase, Vertex> knownTypePair) {
    final Vertex knownType = knownTypePair.getRight();
    final KnowledgeBase knownTypeKnowledgeBase = knownTypePair.getLeft();
    final Edge nameEdge = knownTypeKnowledgeBase.findEdge(knownType.getLabel(), NAME, null);
    final Edge packageEdge = knownTypeKnowledgeBase.findEdge(knownType.getLabel(), PACKAGE, null);

    knowledgeBase.add(knownType);
    knowledgeBase.add(nameEdge);
    knowledgeBase.add(packageEdge);
  }

  private void replaceUnknownTypes(KnowledgeBase knowledgeBase, Vertex unknownType, Pair<KnowledgeBase, Vertex> knownTypePair) {
    this.removeUnknownType(knowledgeBase, unknownType);
    this.addKnownType(knowledgeBase, knownTypePair);

    final Set<Edge> outgoingEdges = knowledgeBase.findEdges(unknownType.getLabel(), null, null);
    final Set<Edge> incomingEdges = knowledgeBase.findEdges(null, null, unknownType.getLabel());

    this.replaceEdgesHeads(outgoingEdges, knownTypePair.getRight().getLabel());
    this.replaceEdgesTails(incomingEdges, knownTypePair.getRight().getLabel());
  }

  private void setTypeAsDefault(Vertex typeVertex) {
    typeVertex.setDescriptor(JavaVertexDescriptor.DEFAULT_TYPE);
  }

  private void resolveFor(KnowledgeBase knowledgeBase) {
    System.out.println("Finding unresolved types in knowledge base");
    final Set<Vertex> unknownTypes = this.findUnknownTypes(knowledgeBase);

    for (Vertex unknownType : unknownTypes) {
      System.out.println("Trying to resolve " + unknownType.getLabel());
      final Pair<KnowledgeBase, Vertex> knownTypePair = this.getKnownType(knowledgeBase, unknownType);

      if (knownTypePair != null) {
        System.out.println("Found " + knownTypePair.getRight().getLabel() + ", replacing in knowledge base");
        this.replaceUnknownTypes(knowledgeBase, unknownType, knownTypePair);
      } else {
        this.setTypeAsDefault(unknownType);
      }
    }
  }

  public void resolve() {
    for (KnowledgeBase knowledgeBase : this.getKnowledgeBases()) {
      this.resolveFor(knowledgeBase);
    }
  }
}
