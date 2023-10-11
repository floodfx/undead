package com.undead4j.protocol;

import com.squareup.moshi.JsonDataException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MsgTest {
    String msgStr = """
["join", "msg", "topic", "event", {"foo":"bar"}]
                """;

    String msgStr2 = """
[1, "msg", "topic", "event", {"foo":"bar"}]
                """;
    @Test
    public void ParseRaw() throws IOException
    {
        var msgParser = new MsgParser();
        var msg = msgParser.parseJSON(msgStr);
        System.out.println("msg:" + msg);
        assertEquals( msg.joinRef(), "join");
    }

    @Test
    public void ParseBadRaw() throws IOException
    {
        var msgParser = new MsgParser();
        assertThrows(JsonDataException.class,() -> { msgParser.parseJSON(msgStr2);});
    }

}
