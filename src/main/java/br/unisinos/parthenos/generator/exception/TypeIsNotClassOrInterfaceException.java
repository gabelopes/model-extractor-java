package br.unisinos.parthenos.generator.exception;

import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TypeIsNotClassOrInterfaceException extends RuntimeException {
  private TypeDeclaration<?> typeDeclaration;

  @Override
  public String toString() {
    return "Type " + this.typeDeclaration + " is not an abstract class, class or interface";
  }
}
