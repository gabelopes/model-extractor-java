package br.unisinos.parthenos.generator.analyzer.java;

import br.unisinos.parthenos.generator.analyzer.FactAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.ClassAnalyzer;
import br.unisinos.parthenos.generator.analyzer.java.entities.InterfaceAnalyzer;
import br.unisinos.parthenos.generator.annotation.Language;
import br.unisinos.parthenos.generator.exception.SourceFileParseException;
import br.unisinos.parthenos.generator.exception.TypeIsNotClassOrInterfaceException;
import br.unisinos.parthenos.generator.exception.TypeIsNotPresentException;
import br.unisinos.parthenos.generator.prolog.fact.Fact;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;

@Getter
@Language("java")
@AllArgsConstructor
public class JavaFactAnalyzer implements FactAnalyzer {
  private File sourceFile;

  @Override
  public Set<Fact> retrieveFacts() {
    System.out.println("Retrieving facts for " + this.getSourceFile());
    final ParseResult<CompilationUnit> parseResult = parseSource();

    if (!parseResult.isSuccessful()) {
      throw new SourceFileParseException(this.getSourceFile());
    }

    final CompilationUnit parsedSource = parseResult.getResult().get();
    final ClassOrInterfaceDeclaration primaryType;

    try {
      primaryType = this.getPrimaryType(parsedSource);
    } catch (TypeIsNotClassOrInterfaceException e) {
      return null;
    }

    return this.getTypeAnalyzer(primaryType).retrieveFacts();
  }

  protected FactAnalyzer getTypeAnalyzer(ClassOrInterfaceDeclaration typeDeclaration) {
    if (typeDeclaration.isInterface()) {
      return new InterfaceAnalyzer(typeDeclaration);
    }

    return new ClassAnalyzer(typeDeclaration);
  }

  private ParseResult<CompilationUnit> parseSource() {
    try {
      return new JavaParser().parse(this.getSourceFile());
    } catch (FileNotFoundException e) {
      throw new SourceFileParseException(this.getSourceFile());
    }
  }

  private ClassOrInterfaceDeclaration getPrimaryType(CompilationUnit parsedSource) {
    final Optional<TypeDeclaration<?>> optionalTypeDeclaration = parsedSource.getPrimaryType();

    if (optionalTypeDeclaration.isPresent()) {
      final TypeDeclaration<?> typeDeclaration = optionalTypeDeclaration.get();

      if (typeDeclaration.isClassOrInterfaceDeclaration()) {
        return (ClassOrInterfaceDeclaration) typeDeclaration;
      }

      throw new TypeIsNotClassOrInterfaceException(typeDeclaration);
    }

    throw new TypeIsNotPresentException();
  }
}
