package com.cj.dynamicjson;

import org.junit.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.cj.dynamicjson.AbstractSyntaxTree.JsonAst;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicJsonMarshallerTest {
    private DynamicJsonMarshaller marshaller = DynamicJsonMarshaller.instance;

    @Test
    public void jsonString() {
        String jsonText = "\"Hello, world!\"";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.asString(), is("Hello, world!"));
        assertThat(ast.toString(), is("String(Hello, world!)"));
    }

    @Test
    public void jsonNumber() {
        String jsonText = "123.456";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.asBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(ast.toString(), is("Number(123.456)"));
    }

    @Test
    public void jsonTrue() {
        String jsonText = "true";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.asBoolean(), is(true));
        assertThat(ast.toString(), is("Boolean(true)"));
    }

    @Test
    public void jsonFalse() {
        String jsonText = "false";
        JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.asBoolean(), is(false));
        assertThat(ast.toString(), is("Boolean(false)"));
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
        List<JsonAst> list = ast.asList();
        assertThat(list.size(), is(0));
        assertThat(ast.toString(), is("Array()"));
    }

    @Test
    public void arrayWithOneElement() {
        String jsonText = "[ \"foo\" ]";
        JsonAst ast = marshaller.parse(jsonText);
        List<JsonAst> list = ast.asList();
        assertThat(list.size(), is(1));
        assertThat(list.get(0).asString(), is("foo"));
        assertThat(ast.toString(), is("Array(String(foo))"));
    }

    @Test
    public void arrayWithSomeElements() {
        String jsonText = "[ \"foo\", 123.456, false]";
        JsonAst ast = marshaller.parse(jsonText);
        List<JsonAst> list = ast.asList();
        assertThat(list.size(), is(3));
        assertThat(list.get(0).asString(), is("foo"));
        assertThat(list.get(1).asBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(list.get(2).asBoolean(), is(false));
        assertThat(ast.toString(), is("Array(String(foo), Number(123.456), Boolean(false))"));
    }

    @Test
    public void emptyObject() {
        String jsonText = "{}";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.asMap();
        assertThat(map.size(), is(0));
        assertThat(ast.toString(), is("Object()"));
    }

    @Test
    public void objectWithOneElement() {
        String jsonText = "{ \"a\": \"foo\" }";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.asMap();
        assertThat(map.size(), is(1));
        assertThat(map.get("a").asString(), is("foo"));
        assertThat(ast.toString(), is("Object(a -> String(foo))"));
    }

    @Test
    public void objectWithSomeElements() {
        String jsonText = "{ \"b\": \"foo\", \"a\": 123.456, \"c\": true}";
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> map = ast.asMap();
        assertThat(map.size(), is(3));
        assertThat(map.get("a").asBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(map.get("b").asString(), is("foo"));
        assertThat(map.get("c").asBoolean(), is(true));
        assertThat(ast.toString(), is("Object(a -> Number(123.456), b -> String(foo), c -> Boolean(true))"));
    }

    @Test
    public void exampleFromJsonOrgSite() {
        //use example from http://json.org/example
        InputStream jsonText = getClass().getClassLoader().getResourceAsStream("example-from-json-org-site.json");
        JsonAst ast = marshaller.parse(jsonText);
        Map<String, JsonAst> glossary = ast.asMap().get("glossary").asMap();
        Map<String, JsonAst> glossDiv = glossary.get("GlossDiv").asMap();
        Map<String, JsonAst> glossList = glossDiv.get("GlossList").asMap();
        Map<String, JsonAst> glossEntry = glossList.get("GlossEntry").asMap();
        Map<String, JsonAst> glossDef = glossEntry.get("GlossDef").asMap();
        List<JsonAst> glossSeeAlso = glossDef.get("GlossSeeAlso").asList();
        assertThat(glossary.get("title").asString(), is("example glossary"));
        assertThat(glossDiv.get("title").asString(), is("S"));
        assertThat(glossSeeAlso.size(), is(2));
        assertThat(glossSeeAlso.get(0).asString(), is("GML"));
        assertThat(glossSeeAlso.get(1).asString(), is("XML"));
    }
}
