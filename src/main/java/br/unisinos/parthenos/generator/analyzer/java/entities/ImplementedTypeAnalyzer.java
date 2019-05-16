package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.LeafTypeCreator;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImplementedTypeAnalyzer implements LeafTypeCreator {
  private ClassOrInterfaceType implementedType;

  @Override
  public Type getLeafType() {
    return this.getImplementedType();
  }

  @Override
  public EdgeLabel getLeafTypeLabel() {
    return EdgeLabel.INTERFACE;
  }
}
