package run.undead.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static run.undead.template.Undead.EMPTY;

public class Directive {



  /**
   * NoEscape ensures no additional HTML escaping occurs for this template which is
   * useful when you want to embed HTML inside a template without it being escaped.
   * THIS IS UNSAFE AND SHOULD BE USED WITH CAUTION.
   * @param obj object (or template) to not escape
   */
  public static UndeadTemplate NoEscape(Object obj) {
    switch (obj) {
      case UndeadTemplate lt -> {
        return lt;
      }
      default -> {
        var fragment = StringTemplate.of(List.of(String.valueOf(obj)), List.of());
        return new UndeadTemplate(fragment);
      }
    }
  }

  /**
   * If models an if statement in a template.  If the condition is true the trueCase is returned
   * otherwise an empty template is returned.
   * @param object object to test
   * @param cond case to test
   * @return trueCase if condition is true otherwise an empty template
   * @param <T> type of object to test
   */
  public static <T> UndeadTemplate If(T object, Case<T> cond) {
    if (cond.test(object)) {
      return cond.apply(object);
    }
    return EMPTY;
  }

  /**
   * If models an if statement in a template.  If the condition is true the trueCase is returned
   * otherwise an empty template is returned.
   * @param cond condition to test
   * @param trueCase template to return if condition is true
   * @return trueCase if condition is true otherwise an empty template
   */
  public static UndeadTemplate If(Boolean cond, UndeadTemplate trueCase) {
    if (cond) {
      return trueCase;
    }
    return EMPTY;
  }


  /**
   * If models a ternary operator in a template.  If the condition is true the trueCase is returned
   * otherwise the falseCase is returned.
   * @param cond condition to test
   * @param trueCase template to return if condition is true
   * @param falseCase template to return if condition is false
   * @return trueCase if condition is true otherwise falseCase
   */
  public static UndeadTemplate If(Boolean cond, UndeadTemplate trueCase, UndeadTemplate falseCase) {
    if (cond) {
      return trueCase;
    }
    return falseCase;
  }

  /**
   * If models a ternary operator in a template.  If the condition is true the trueCase is returned
   * otherwise the falseCase is returned.
   * @param object object to test
   * @param p predicate to test
   * @param trueFunc function to apply if predicate is true
   * @param falseFunc function to apply if predicate is false
   * @return trueCase if condition is true otherwise falseCase
   * @param <T> type of object to test
   */
  public static <T> UndeadTemplate If(T object, Predicate<T> p, Function<T, UndeadTemplate> trueFunc, Function<T, UndeadTemplate> falseFunc) {
    if (p.test(object)) {
      return trueFunc.apply(object);
    }
    return falseFunc.apply(object);
  }

  /**
   * Switch is a switch statement for templates.  It takes a list of cases and returns the first
   * case that matches.  If no case matches it returns an empty template.
   * @param object object to switch on
   * @param cases list of cases to match
   * @return the first case that matches or an empty template
   * @param <T> type of object to switch on
   */
  public static <T> UndeadTemplate Switch(T object, Case<T>... cases) {
    for (var c : cases) {
      if (c.predicate().test(object)) {
        return c.function().apply(object);
      }
    }
    return EMPTY;
  }

  /**
   * Map applies a function to each element of a collection and returns a list of the results
   * @param collection collection of data to map over
   * @param func function to apply to each element of the collection
   * @return a list of the results of applying the function to each element of the collection
   * @param <T>
   */
  public static <T> List<UndeadTemplate> Map(Collection<T> collection, Function<T, UndeadTemplate> func) {
    return collection.stream().map(func).collect(Collectors.toList());
  }

  /**
   * Join joins a list of templates with a separator template.
   * @param tmpls list of templates to join
   * @param sep separator template
   * @return a new template that is the concatenation of all templates with the separator template between each
   */
  public static UndeadTemplate Join(List<UndeadTemplate> tmpls, UndeadTemplate sep) {
    return UndeadTemplate.concat(
        IntStream.range(0, tmpls.size())
            .mapToObj(
                i -> {
                  var tmpl = tmpls.get(i);
                  if (i == tmpls.size() - 1) {
                    return tmpl;
                  }
                  return UndeadTemplate.concat(tmpl, sep);
                })
            .collect(Collectors.toList()));
  }

  /**
   * Range returns a list of integers from start to end by step
   * @param start start of range
   * @param end end of range
   * @param step step of range
   * @return list of integers from start to end
   */
  public static List<Integer> Range(int start, int end, int step) {
    var list = new ArrayList<Integer>();
    for(var i = start; step > 0 ? i < end : end < i; i += step) {
      list.add(i);
    }
    return list;
  }

  /**
   * Range returns a list of integers from zero to end by step of 1
   * @param end end of range
   * @return list of integers from zero to end by step of 1
   */
  public static List<Integer> Range(int end) {
    return Range(0, end, 1);
  }

  /**
   * Case is a predicate and function pair used to model a case statement in a template.
   * The predicate is used to determine if the case matches and the function is used to
   * generate the template if the case matches.  In both the predicate and function the
   * input is the object being switched on.
   * @param <T>
   */
  public interface Case<T> {
    Predicate<T> predicate();
    Function<T, UndeadTemplate> function();

    default boolean test(T t) {
      return predicate().test(t);
    }

    default UndeadTemplate apply(T t) {
      return function().apply(t);
    }

    /**
     * of creates a new case with the given predicate and function
     * @param predicate predicate that determines if the case matches
     * @param function function that generates the template if the case matches
     * @return a new case made with the given predicate and function
     * @param <T> type of object passed to predicate and function
     */
    static <T> Case<T> of(Predicate<T> predicate, Function<T, UndeadTemplate> function) {
      return new Case<T>() {
        @Override
        public Predicate<T> predicate() {
          return predicate;
        }

        @Override
        public Function<T, UndeadTemplate> function() {
          return function;
        }
      };
    }

    static Case of(Boolean cond, UndeadTemplate tmpl) {
      return Case.of(t -> cond, t -> tmpl);
    }

    /**
     * defaultOf creates a new case with the given function and a predicate that always returns true
     * @param func function that generates the template if this case is matched
     * @return a new case made with the given function and a predicate that always returns true
     * @param <T> type of object passed to predicate and function
     */
    static <T> Case<T> defaultOf(Function<T, UndeadTemplate> func) {
      return Case.of(t -> true, func);
    }

    static Case defaultOf(UndeadTemplate tmpl) {
      return Case.of(t -> true, t -> tmpl);
    }

  }

}
