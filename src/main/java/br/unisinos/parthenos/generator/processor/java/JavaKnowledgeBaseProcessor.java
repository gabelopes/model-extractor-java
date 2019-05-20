package br.unisinos.parthenos.generator.processor.java;

import br.unisinos.parthenos.generator.analyzer.java.entities.ConcreteClassAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.VertexCreator;
import br.unisinos.parthenos.generator.annotation.Language;
import br.unisinos.parthenos.generator.enumerator.java.JavaEdgeLabel;
import br.unisinos.parthenos.generator.enumerator.java.Modifier;
import br.unisinos.parthenos.generator.enumerator.java.PrimitiveType;
import br.unisinos.parthenos.generator.io.SourceFile;
import br.unisinos.parthenos.generator.io.repository.Repository;
import br.unisinos.parthenos.generator.processor.KnowledgeBaseProcessor;
import br.unisinos.parthenos.generator.prolog.fact.Edge;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import br.unisinos.parthenos.generator.prolog.fact.Vertex;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.KnowledgeBase;
import br.unisinos.parthenos.generator.prolog.knowledgeBase.java.JavaKnowledgeBase;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import lombok.Getter;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Getter
@Language("java")
public class JavaKnowledgeBaseProcessor implements KnowledgeBaseProcessor {
  private static final String JAVA_LANG_PACKAGE = "java.lang";

  private Set<Vertex> branchTypes;

  public JavaKnowledgeBaseProcessor() {
    this.branchTypes = new HashSet<>();
  }

  private Set<Class<?>> getDefaultTypes() {
    try {
      return ClassPath.from(KnowledgeBaseProcessor.class.getClassLoader()).getTopLevelClasses(JAVA_LANG_PACKAGE)
        .stream()
        .map(ClassInfo::load)
        .filter(clazz -> java.lang.reflect.Modifier.isPublic(clazz.getModifiers()))
        .collect(Collectors.toSet());
    } catch (IOException e) {
      return new HashSet<>();
    }
  }

  private Set<Fact> createDefaultTypesFacts() {
    final Set<Class<?>> defaultTypes = this.getDefaultTypes();
    final Set<Fact> defaultTypeFacts = new HashSet<>();

    for (Class<?> defaultType : defaultTypes) {
      final ConcreteClassAnalyzer concreteClassAnalyzer = new ConcreteClassAnalyzer(defaultType);
      defaultTypeFacts.addAll(concreteClassAnalyzer.retrieveFacts());
    }

    return defaultTypeFacts;
  }

  private void addSupportedTypes(Collection<KnowledgeBase> knowledgeBases) {
    final Set<Fact> defaultTypesFacts = this.createDefaultTypesFacts();

    for (KnowledgeBase knowledgeBase : knowledgeBases) {
      knowledgeBase.addAll(defaultTypesFacts);
    }
  }

  private Set<Vertex> createVertices(VertexCreator... vertexCreators) {
    return Arrays.stream(vertexCreators)
      .map(VertexCreator::createVertex)
      .collect(Collectors.toSet());
  }

  private void addSupportedPrimitiveTypes(Collection<KnowledgeBase> knowledgeBases) {
    final Set<Vertex> primitiveTypesVertices = this.createVertices(PrimitiveType.values());

    for (KnowledgeBase knowledgeBase : knowledgeBases) {
      knowledgeBase.addAll(primitiveTypesVertices);
    }
  }

  private void addSupportedModifiers(Collection<KnowledgeBase> knowledgeBases) {
    final Set<Vertex> modifiersVertices = this.createVertices(Modifier.values());

    for (KnowledgeBase knowledgeBase : knowledgeBases) {
      knowledgeBase.addAll(modifiersVertices);
    }
  }

  private Edge createSourceEdge(SourceFile sourceFile, Vertex typeVertex) {
    return new Edge(typeVertex.getLabel(), JavaEdgeLabel.SOURCE, sourceFile.getFileAtom());
  }

  private void addSourceEdges(Map<SourceFile, KnowledgeBase> knowledgeBases) {
    for (Entry<SourceFile, KnowledgeBase> entry : knowledgeBases.entrySet()) {
      final SourceFile sourceFile = entry.getKey();
      final KnowledgeBase knowledgeBase = entry.getValue();
      final Set<Vertex> branchTypes = JavaKnowledgeBase.from(knowledgeBase).findBranchTypes();

      for (Vertex typeVertex : branchTypes) {
        final Edge sourceFileEdge = this.createSourceEdge(sourceFile, typeVertex);

        knowledgeBase.add(sourceFileEdge);
      }
    }
  }

  @Override
  public void process(Repository repository, Map<SourceFile, KnowledgeBase> knowledgeBaseMap) {
    final Collection<KnowledgeBase> knowledgeBases = knowledgeBaseMap.values();
    final BranchTypeReconnector branchTypeReconnector = new BranchTypeReconnector(knowledgeBases);
    final UnknownTypeResolver unknownTypeResolver = new UnknownTypeResolver(knowledgeBases);

    this.addSourceEdges(knowledgeBaseMap);

    branchTypeReconnector.reconnect();
    unknownTypeResolver.resolve();

    this.addSupportedModifiers(knowledgeBases);
    this.addSupportedPrimitiveTypes(knowledgeBases);
    this.addSupportedTypes(knowledgeBases);
  }
}
