package br.unisinos.parthenos.generator.analyzer.java.entities.mixins;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.List;

public interface EntityLocator<T extends Node> {
  T getEntity();

  default <S> S findParent(Class<S> parentClass) {
    return this.findParent(parentClass, this.getEntity().getParentNode().orElse(null));
  }

  default <S> S findParent(Class<S> parentClass, Node parentNode) {
    if (parentNode == null) {
      return null;
    }

    if (parentClass.isInstance(parentNode)) {
      return parentClass.cast(parentNode);
    }

    return this.findParent(parentClass, parentNode.getParentNode().orElse(null));
  }

  default TypeDeclaration<?> findEnclosingType() {
    return this.findParent(TypeDeclaration.class);
  }

  default String findEnclosingTypeName() {
    final TypeDeclaration<?> typeDeclaration = this.findEnclosingType();

    if (typeDeclaration == null) {
      return null;
    }

    return typeDeclaration.getNameAsString();
  }

  default CompilationUnit findCompilationUnit() {
    if (this.getEntity() instanceof CompilationUnit) {
      return (CompilationUnit) this.getEntity();
    }

    return this.findParent(CompilationUnit.class);
  }

  default PackageDeclaration findPackage() {
    final CompilationUnit compilationUnit = this.findCompilationUnit();

    if (compilationUnit == null) {
      return null;
    }

    return compilationUnit.getPackageDeclaration().orElse(null);
  }

  default String findPackageName() {
    final PackageDeclaration packageDeclaration = this.findPackage();

    if (packageDeclaration == null) {
      return null;
    }

    return packageDeclaration.getNameAsString();
  }

  default List<ImportDeclaration> findImports() {
    final CompilationUnit compilationUnit = this.findCompilationUnit();

    if (compilationUnit == null) {
      return null;
    }

    return compilationUnit.getImports();
  }
}
