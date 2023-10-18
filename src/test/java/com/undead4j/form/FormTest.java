package com.undead4j.form;

import jakarta.validation.Validation;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FormTest {
  @Test
  public void ParseValidation() throws IOException {
    var form = new MyForm();
    var validator = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory()
        .getValidator();
    var violations = validator.validate(form);
    assertEquals(2, violations.size());
    System.out.println(violations);
  }

}
