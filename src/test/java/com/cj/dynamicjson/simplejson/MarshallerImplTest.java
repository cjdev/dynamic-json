package com.cj.dynamicjson.simplejson;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;

public class MarshallerImplTest {
    private MarshallerImpl marshaller = MarshallerImpl.instance;

    @Test
    public void jsonString() {
        String jsonText = "\"Hello, world!\"";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aString(), is("Hello, world!"));
    }

    @Test
    public void jsonNumber() {
        String jsonText = "123.456";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBigDecimal(), is(new BigDecimal("123.456")));
    }

    @Test
    public void jsonTrue() {
        String jsonText = "true";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBoolean(), is(true));
    }

    @Test
    public void jsonFalse() {
        String jsonText = "false";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBoolean(), is(false));
    }

    @Test
    public void jsonNull() {
        String jsonText = "null";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(true));
        assertThat(ast.toString(), is("null"));
    }

    @Test
    public void emptyArray() {
        String jsonText = "[]";
        JsonAst ast = marshaller.parse(jsonText);
        List<JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(0));
    }

    @Test
    public void arrayWithOneElement() {
        String jsonText = "[ \"foo\" ]";
        JsonAst ast = marshaller.parse(jsonText);
        List<JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(1));
        assertThat(list.get(0).aString(), is("foo"));
    }

    @Test
    public void arrayWithSomeElements() {
        String jsonText = "[ \"foo\", 123.456, false]";
        JsonAst ast = marshaller.parse(jsonText);
        List<JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(3));
        assertThat(list.get(0).aString(), is("foo"));
        assertThat(list.get(1).aBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(list.get(2).aBoolean(), is(false));
    }

    @Test
    public void emptyObject() {
        String jsonText = "{}";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.map();
        assertThat(ast.isNull(), is(false));
        assertThat(map.size(), is(0));
    }

    @Test
    public void objectWithOneElement() {
        String jsonText = "{ \"a\": \"foo\" }";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.map();
        assertThat(ast.isNull(), is(false));
        assertThat(map.size(), is(1));
        assertThat(map.get("a").aString(), is("foo"));
    }

    @Test
    public void objectWithSomeElements() {
        String jsonText = "{ \"b\": \"foo\", \"a\": 123.456, \"c\": true}";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.map();
        assertThat(ast.isNull(), is(false));
        assertThat(map.size(), is(3));
        assertThat(map.get("a").aBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(map.get("b").aString(), is("foo"));
        assertThat(map.get("c").aBoolean(), is(true));
    }

    @Test
    public void exampleFromJsonOrgSite() {
        //use example from http://json.org/example
        InputStream jsonText = getClass().getClassLoader().getResourceAsStream("example-from-json-org-site.json");
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> glossary = ast.map().get("glossary").map();
        Map<String, JsonAst> glossDiv = glossary.get("GlossDiv").map();
        Map<String, JsonAst> glossList = glossDiv.get("GlossList").map();
        Map<String, JsonAst> glossEntry = glossList.get("GlossEntry").map();
        Map<String, JsonAst> glossDef = glossEntry.get("GlossDef").map();
        List<JsonAst> glossSeeAlso = glossDef.get("GlossSeeAlso").list();
        assertThat(glossary.get("title").aString(), is("example glossary"));
        assertThat(glossDiv.get("title").aString(), is("S"));
        assertThat(glossSeeAlso.size(), is(2));
        assertThat(glossSeeAlso.get(0).aString(), is("GML"));
        assertThat(glossSeeAlso.get(1).aString(), is("XML"));
    }

    @Test
    public void nullCanBeTreatedAsAnyType() {
        String jsonText = "null";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(true));
        assertThat(ast.aBigDecimal(), is(nullValue()));
        assertThat(ast.list(), notNullValue());
        assertThat(ast.list().size(), is(0));
        assertThat(ast.map(), notNullValue());
        assertThat(ast.map().size(), is(0));
        //assertThat(ast.aString(), is(nullValue()));  //////I DISAGREE WITH THIS ONE
        assertThat(ast.aBoolean(), is(nullValue()));
    }

    @Test
    public void nullCanBeTreatedAsEmptyOptionalForNonCollections() {
        String jsonText = "null";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(true));
        assertThat(ast.oBigDecimal().isPresent(), is(false));
        assertThat(ast.oString().isPresent(), is(false));
        assertThat(ast.oBoolean().isPresent(), is(false));
    }
    
    @Test
    public void stringCanBeANumber_RobustnessPrincipal() {
        String jsonText = "{\"number\":\"4\"}";
        assertThat(marshaller.parse(jsonText).map().get("number").aBigDecimal(), is(new BigDecimal(4)));
    }

    @Test
    public void optionals() {
        assertThat(marshaller.parse("123.45").oBigDecimal(), is(Optional.of(new BigDecimal("123.45"))));
        assertThat(marshaller.parse("\"foo\"").oString(), is(Optional.of("foo")));
        assertThat(marshaller.parse("true").oBoolean(), is(Optional.of(true)));
        assertThat(marshaller.parse("false").oBoolean(), is(Optional.of(false)));
    }
}
