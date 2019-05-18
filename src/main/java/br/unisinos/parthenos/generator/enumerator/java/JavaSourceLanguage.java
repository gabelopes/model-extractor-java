package br.unisinos.parthenos.generator.enumerator.java;

import br.unisinos.parthenos.generator.pool.SourceLanguage;

public enum JavaSourceLanguage implements SourceLanguage {
  JAVA(".java");

  String[] extensions;

  JavaSourceLanguage(String... extensions) {
    this.extensions = extensions;
  }

  @Override
  public String getName() {
    return this.name().toLowerCase();
  }

  public String[] getExtensions() {
    return this.extensions;
  }
}
