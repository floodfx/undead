package com.undead4j.template;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.StringTemplate.RAW;
import static java.lang.StringTemplate.STR;



public class Live {

    public static final StringTemplate.Processor<LiveTemplate, RuntimeException> HTML = template -> {
        return new LiveTemplate(template);
    };

}

