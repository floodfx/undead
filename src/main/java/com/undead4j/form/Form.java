package com.undead4j.form;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.undead4j.url.Values;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.*;

public class Form<T> {

  private ObjectMapper mapper;
  private Validator validator;
  private Values values;
  private Values changes;
  private Class clazz;
  private String action;
  private Set<String> touched;
  private T model;
  private Map<String, String> errors;

  public Form(Class clazz, Values initial) {
    this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.validator = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory()
        .getValidator();
    this.touched = new HashSet<>();
    this.values = initial;
    this.changes = new Values();
    this.clazz = clazz;
    this.errors = new HashMap<>();
    this.model = (T)mapper.convertValue(values.asMap(), clazz);
  }

  public void update(Values newValues, String action) {
    this.action = action;

    // merge old and new data and calculate changes
    newValues.asMap().forEach((k, v) -> {
      if (values.get(k) == null || !values.get(k).equals(v)) {
        switch(v) {
          case String s -> changes.add(k, s);
          case List l -> {
            for (Object o : l) {
              changes.add(k, String.valueOf(o));
            }
          }
          // throw error?
          default -> changes.add(k, String.valueOf(v));
        }
      }
      values.set(k, v);
    });
    // handle case where _target is set but the newData does not contain the _target field
    // this happens in the case of a checkbox that is unchecked
    var target = newValues.get("_target");
    if (target != null && newValues.get(target) == null) {
      values.delete(target);
      changes.add(target, "");
    }
    // validate if action is not empty
    if(!Strings.isNullOrEmpty(action)) {
      // map values to model
      model = (T)mapper.convertValue(values.asMap(), clazz);
      // validate model
      var violations = validator.validate(model);
      // convert violations to errors
      errors = new HashMap<>();
      for (var v : violations) {
        errors.put(v.getPropertyPath().toString(), v.getMessage());
      }

      // if we get a _target field in the form, use it to indicate which fields were touched
      // if not, assume all fields in input were touched and all fields
      // with errors were touched
      if(target != null) {
        this.touched.add(target);
      }
      else {
        for(var k : values.asMap().keySet()) {
          this.touched.add(k);
        }
        for (var e : errors.keySet()) {
          this.touched.add(e);
        }
      }
    }
  }

  public Map<String, String> errors() {
    return this.errors;
  }

  public String errorFor(String key) {
    if(errors == null || errors.get(key) == null || touched == null || !touched.contains(key) || valid()) {
      return null;
    }
    return this.errors.get(key);
  }

  public String action() {
    return this.action;
  }

  public T model() {
    return this.model;
  }

  public Map<String, Object> changes() {
    return this.changes.asMap();
  }

  public Values data() {
    return this.values;
  }

  public boolean valid() {
    // no action or empty errors means is always valid
    if(Strings.isNullOrEmpty(action) || errors().isEmpty() || touched.isEmpty()) {
      return true;
    }
    // if nothing was touched the changeset is valid
    // regardless of whether or not there are errors
    // otherwise, only check for errors on touched fields
    // and return false if there are any errors
    for(String k : touched) {
      if(errors().containsKey(k)) {
        return false;
      }
    }
    return true;
  }
}
