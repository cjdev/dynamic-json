package com.cj.jsonbuilder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JsonArrayBuilderTest {

    @Test
    public void testListOfLongs() {
        //given
        List<Long> longs = new ArrayList<>();
        longs.add(1l);
        longs.add(2l);
        longs.add(3l);

        //when
        String jsonString = JsonArrayBuilder.ofLongs(longs).toJSONString();

        //then
        assertThat("list of longs should not be a list of objects", jsonString, is("[1,2,3]"));
    }
}
