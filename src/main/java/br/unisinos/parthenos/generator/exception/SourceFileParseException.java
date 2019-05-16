package br.unisinos.parthenos.generator.exception;

import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public class SourceFileParseException extends RuntimeException {
  private File sourceFile;

  @Override
  public String toString() {
    return "Could not parse file " + sourceFile + ".";
  }
}
