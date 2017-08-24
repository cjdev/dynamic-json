package com.cj.dynamicjson;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.cj.dynamicjson.jackson.MarshallerImpl;

@RunWith(Parameterized.class)
public class MarshallerImplTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { MarshallerImpl.instance }, { com.cj.dynamicjson.simplejson.Marshaller.instance }
        });
    }

    private Marshaller marshaller;

    public MarshallerImplTest(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    @Test
    public void jsonString() {
        String jsonText = "\"Hello, world!\"";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aString(), is("Hello, world!"));
    }

    @Test
    public void jsonNumber() {
        String jsonText = "123.456";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBigDecimal(), is(new BigDecimal("123.456")));
    }

    @Test
    public void jsonTrue() {
        String jsonText = "true";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBoolean(), is(true));
    }

    @Test
    public void jsonFalse() {
        String jsonText = "false";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(false));
        assertThat(ast.aBoolean(), is(false));
    }

    @Test
    public void jsonNull() {
        String jsonText = "null";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(true));
    }

    @Test
    public void emptyArray() {
        String jsonText = "[]";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        List<AbstractSyntaxTree.JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(0));
    }
    
    @Test
    public void emptyArrayUsingParseArray() {
        String jsonText = "[]";
        Stream<AbstractSyntaxTree.JsonAst> asts = marshaller.parseList(asInputStream(jsonText));
        assertThat(asts.count(), is(0L));
    }

    private InputStream asInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void arrayWithOneElement() {
        String jsonText = "[ \"foo\" ]";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        List<AbstractSyntaxTree.JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(1));
        assertThat(list.get(0).aString(), is("foo"));
    }
    
    @Test
    public void arrayWithOneElementUsingParseArray() {
        String jsonText = "[ \"foo\" ]";
        Stream<AbstractSyntaxTree.JsonAst> ast = marshaller.parseList(asInputStream(jsonText));
        List<AbstractSyntaxTree.JsonAst> list = ast.collect(Collectors.toList());
        assertThat(list.size(), is(1));
        assertThat(list.get(0).aString(), is("foo"));
    }

    @Test
    public void arrayWithSomeElements() {
        String jsonText = "[ \"foo\", 123.456, false]";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        List<AbstractSyntaxTree.JsonAst> list = ast.list();
        assertThat(ast.isNull(), is(false));
        assertThat(list.size(), is(3));
        assertThat(list.get(0).aString(), is("foo"));
        assertThat(list.get(1).aBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(list.get(2).aBoolean(), is(false));
    }
    
    @Test
    public void arrayWithSomeElementsUsingParseList() {
        String jsonText = "[ \"foo\", {\"o\":123.456}, 123.456, false]";
        List<AbstractSyntaxTree.JsonAst> list = marshaller.parseList(asInputStream(jsonText)).collect(Collectors.toList());
        assertThat(list.size(), is(4));
        assertThat(list.get(0).aString(), is("foo"));
        assertThat(list.get(1).object().get("o").aBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(list.get(2).aBigDecimal(), is(new BigDecimal("123.456")));
        assertThat(list.get(3).aBoolean(), is(false));
    }

    @Test
    public void emptyObject() {
        String jsonText = "{}";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        Map<String, AbstractSyntaxTree.JsonAst> map = ast.map();
        assertThat(ast.isNull(), is(false));
        assertThat(map.size(), is(0));
    }

    @Test
    public void objectWithOneElement() {
        String jsonText = "{ \"a\": \"foo\" }";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        Map<String, AbstractSyntaxTree.JsonAst> map = ast.map();
        assertThat(ast.isNull(), is(false));
        assertThat(map.size(), is(1));
        assertThat(map.get("a").aString(), is("foo"));
    }

    @Test
    public void objectWithSomeElements() {
        String jsonText = "{ \"b\": \"foo\", \"a\": 123.456, \"c\": true}";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        Map<String, AbstractSyntaxTree.JsonAst> map = ast.map();
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
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        Map<String, AbstractSyntaxTree.JsonAst> glossary = ast.map().get("glossary").map();
        Map<String, AbstractSyntaxTree.JsonAst> glossDiv = glossary.get("GlossDiv").map();
        Map<String, AbstractSyntaxTree.JsonAst> glossList = glossDiv.get("GlossList").map();
        Map<String, AbstractSyntaxTree.JsonAst> glossEntry = glossList.get("GlossEntry").map();
        Map<String, AbstractSyntaxTree.JsonAst> glossDef = glossEntry.get("GlossDef").map();
        List<AbstractSyntaxTree.JsonAst> glossSeeAlso = glossDef.get("GlossSeeAlso").list();
        assertThat(glossary.get("title").aString(), is("example glossary"));
        assertThat(glossDiv.get("title").aString(), is("S"));
        assertThat(glossSeeAlso.size(), is(2));
        assertThat(glossSeeAlso.get(0).aString(), is("GML"));
        assertThat(glossSeeAlso.get(1).aString(), is("XML"));
    }

    @Test
    public void nullCanBeTreatedAsAnyType() {
        String jsonText = "null";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
        assertThat(ast.isNull(), is(true));
        assertThat(ast.aBigDecimal(), is(nullValue()));
        assertThat(ast.list(), notNullValue());
        assertThat(ast.list().size(), is(0));
        assertThat(ast.map(), notNullValue());
        assertThat(ast.map().size(), is(0));
//        assertThat(ast.aString(), is(nullValue()));
        assertThat(ast.aBoolean(), is(nullValue()));
    }

    @Test
    public void nullCanBeTreatedAsEmptyOptionalForNonCollections() {
        String jsonText = "null";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(jsonText);
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

    @Test
    public void typeCoercionBehavior(){
        assertThat(marshaller.parse("\"123.45\"").aBigDecimal(), is(new BigDecimal("123.45")));
        assertThat(marshaller.parse("\"false\"").aBoolean(), is(false));
        assertThat(marshaller.parse("\"null\"").aString(), is("null"));
        assertThat(marshaller.parse("123.45").aString(), is("123.45"));
        assertThat(marshaller.parse("true").aString(), is("true"));
        assertThat(marshaller.parse("null").aString(), is((String)null));
        assertThat(marshaller.parse("null").aBigDecimal(), is((BigDecimal)null));
        assertThat(marshaller.parse("null").aBoolean(), is((Boolean)null));
        assertThat(marshaller.parse("123.45").aBoolean(), is(true));
        assertThat(marshaller.parse("0").aBoolean(), is(false));
        assertThat(marshaller.parse("true").aBigDecimal(), is(BigDecimal.ONE));
        assertThat(marshaller.parse("false").aBigDecimal(), is(BigDecimal.ZERO));
    }

    @Test
    public void typeCoercionBehaviorForOptionals(){
        assertThat(marshaller.parse("{\"number\":\"\"}").object().get("number").oBigDecimal(), is(Optional.empty()));
    }

    @Test
    public void otherNumericTypes(){
        assertThat(marshaller.parse("123").oLong().get(), is(123L));
        assertThat(marshaller.parse("123").oInteger().get(), is(123));
        assertThat(marshaller.parse("123.45").oFloat().get(), is(123.45F));
        assertThat(marshaller.parse("123.45").oDouble().get(), is(123.45));
    }
    
    @Test
    public void aStringOnAMapConvertsBackToAString() {
    		String json = "{\"a\":\"foo\"}";
    		AbstractSyntaxTree.JsonAst ast = marshaller.parse(json);
    		assertThat(ast.aString(), is(json));
    }

    @Test
    public void aStringOnAMapOfMapsConvertsBackToAString() {
        String json = "{\"a\":{\"b\":\"foo\"}}";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(json);
        assertThat(ast.aString(), is(json));
    }

    @Test
    public void aStringOnAListBackToAString() {
        String json = "[1,2,3,[]]";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(json);
        assertThat(ast.aString(), is(json));
    }

    @Test
    public void aStringOnAMapOfArraysConvertsBackToAString() {
        String json = "{\"a\":[{},true,{\"b\":\"good\"}]}";
        AbstractSyntaxTree.JsonAst ast = marshaller.parse(json);
        assertThat(ast.aString(), is(json));
    }

}
