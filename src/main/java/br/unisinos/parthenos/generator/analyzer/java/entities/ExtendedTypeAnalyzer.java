package br.unisinos.parthenos.generator.analyzer.java.entities;

import br.unisinos.parthenos.generator.analyzer.java.entities.mixins.creator.LeafTypeCreator;
import br.unisinos.parthenos.generator.enumerator.EdgeLabel;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExtendedTypeAnalyzer implements LeafTypeCreator {
  private ClassOrInterfaceType extendedType;

  @Override
  public Type getLeafType() {
    return this.getExtendedType();
  }

  @Override
  public EdgeLabel getLeafTypeLabel() {
    return EdgeLabel.PARENT;
  }
}
