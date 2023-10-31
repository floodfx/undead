package run.undead.template;

/**
 * Undead is a string template processor that escapes all HTML entities in the template.
 */
public class Undead {
  /**
   * HTML is a string template processor that escapes all HTML entities in the template.
   */
  public static final StringTemplate.Processor<UndeadTemplate, RuntimeException> HTML =
      template ->  new UndeadTemplate(template);

  /**
   * EMPTY is an empty template.
   */
  public static final UndeadTemplate EMPTY = HTML."";
}
