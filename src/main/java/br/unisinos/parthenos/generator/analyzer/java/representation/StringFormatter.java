package br.unisinos.parthenos.generator.analyzer.java.representation;

import java.util.function.Function;

public class StringFormatter {
  private static final String WORD_REGEX = "_|\\s+|.";

  public static String[] splitWords(final String string) {
    return string.split(WORD_REGEX);
  }

  public static String capitalize(final String word) {
    return word.substring(0, 1).toUpperCase() + word.substring(1);
  }

  public static String uncapitalize(final String word) {
    return word.substring(0, 1).toLowerCase() + word.substring(1);
  }

  public static String toCamelCase(final String string) {
    final String pascalCaseString = StringFormatter.toPascalCase(string);
    return uncapitalize(pascalCaseString);
  }

  public static String toUpperSnakeCase(final String string) {
    final String darwinCaseString = StringFormatter.toDarwinCase(string);
    return darwinCaseString.toUpperCase();
  }

  public static String toLowerSnakeCase(final String string) {
    final String darwinCaseString = StringFormatter.toDarwinCase(string);
    return darwinCaseString.toLowerCase();
  }

  public static String toDarwinCase(final String string) {
    final String lowerCaseString = string.toLowerCase();
    return formatWords(lowerCaseString, StringFormatter::capitalize, "_");
  }

  public static String toPascalCase(final String string) {
    final String lowerCaseString = string.toLowerCase();
    return formatWords(lowerCaseString, StringFormatter::capitalize, "");
  }

  private static String formatWords(final String string, final Function<String, String> formatter, final String separator) {
    final String[] words = StringFormatter.splitWords(string);
    final StringBuilder builder = new StringBuilder();

    for (int i = 0; i < words.length; i++) {
      final String formattedWord = formatter.apply(words[i]);
      builder.append(formattedWord);

      if (i < words.length - 1) {
        builder.append(separator);
      }
    }

    return builder.toString();
  }
}
