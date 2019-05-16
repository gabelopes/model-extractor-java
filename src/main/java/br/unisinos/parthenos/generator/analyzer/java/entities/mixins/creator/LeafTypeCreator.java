package br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator;

import br.unisinos.parthenos.generator.analyzer.java.entities.LeafTypeAnalyzer;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;
import com.github.javaparser.ast.type.Type;

import java.util.Set;

public interface LeafTypeCreator {
  Type getLeafType();

  EdgeLabel getLeafTypeLabel();

  default Edge createLeafTypeEdge(Vertex vertex, Vertex leafTypeVertex) {
    return new Edge(vertex.getLabel(), this.getLeafTypeLabel(), leafTypeVertex.getLabel());
  }

  default Set<Fact> createLeafTypeFacts(Vertex vertex) {
    final LeafTypeAnalyzer leafTypeAnalyzer = new LeafTypeAnalyzer(this.getLeafType());
    final Set<Fact> leafTypeFacts = leafTypeAnalyzer.retrieveFacts();

    if (leafTypeFacts == null) {
      return null;
    }

    final Vertex leafTypeVertex = KnowledgeBase.from(leafTypeFacts).findAnyVertex();
    final Edge leafTypeEdge = this.createLeafTypeEdge(vertex, leafTypeVertex);

    leafTypeFacts.add(leafTypeEdge);

    return leafTypeFacts;
  }
}
