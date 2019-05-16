package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.EntityLocator;
import br.unisinos.parthenos.generator.analyzer.java.representation.QualifiedName;
import br.unisinos.parthenos.generator.enumerator.VertexDescriptor;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class LeafTypeAnalyzer extends TypeAnalyzer implements EntityLocator<Type> {
  private static final String ARRAY_TYPE_NOTATION = "[]";

  private Type type;

  private String getSimpleName() {
    return this.getEnclosedTypeQualifiedName().getName();
  }

  @Override
  public String getName() {
    final String simpleName = this.getSimpleName();

    if (this.isArray()) {
      return simpleName + ARRAY_TYPE_NOTATION;
    }

    return simpleName;
  }

  private QualifiedName getEnclosedTypeQualifiedName() {
    return QualifiedName.from(this.getEnclosedType().asString());
  }

  @Override
  public VertexDescriptor getDescriptor() {
    if (this.getType().isPrimitiveType()) {
      return VertexDescriptor.PRIMITIVE;
    }

    if (this.getType().isVoidType()) {
      return VertexDescriptor.NO_TYPE;
    }

    if (this.getType().isArrayType()) {
      return VertexDescriptor.ARRAY;
    }

    return VertexDescriptor.UNKNOWN_TYPE;
  }

  @Override
  public QualifiedName getQualifiedName() {
    final String name = this.getName();
    final String packageName = this.getPackageName();

    return QualifiedName.from(packageName, name);
  }

  @Override
  public String getPackageName() {
    if (this.getType().isPrimitiveType()) {
      return null;
    }

    final QualifiedName fullName = this.getEnclosedTypeQualifiedName();

    if (fullName.hasPackage()) {
      return fullName.getPackage();
    }

    final String packageName = getPackageFromImports();

    if (packageName == null && this.hasEnclosingTypeName()) {
      return this.findPackageName();
    }

    return packageName;
  }

  @Override
  public Type getEntity() {
    return this.getType();
  }

  private boolean isArray() {
    return this.getType().isArrayType();
  }

  private Type getEnclosedType() {
    if (this.isArray()) {
      return this.getType().asArrayType().getComponentType();
    }

    return this.getType();
  }

  private ImportDeclaration findImport() {
    for (ImportDeclaration importDeclaration : this.findImports()) {
      final String importTypeName = importDeclaration.getName().getIdentifier();

      if (importTypeName.equals(this.getSimpleName())) {
        return importDeclaration;
      }
    }

    return null;
  }

  private ImportDeclaration findAsteriskImport() {
    return this.findImports()
      .stream()
      .filter(ImportDeclaration::isAsterisk)
      .findFirst()
      .orElse(null);
  }

  private ImportDeclaration findMatchingImport() {
    final ImportDeclaration importDeclaration = this.findImport();

    if (importDeclaration == null) {
      return this.findAsteriskImport();
    }

    return importDeclaration;
  }

  private String getPackageFromImports() {
    final ImportDeclaration importDeclaration = this.findMatchingImport();

    if (importDeclaration == null) {
      return null;
    }

    return QualifiedName.from(importDeclaration.getNameAsString()).getPackage();
  }

  private boolean hasEnclosingTypeName() {
    final String enclosingTypeName = this.findEnclosingTypeName();
    return Objects.equals(this.getSimpleName(), enclosingTypeName);
  }
}
