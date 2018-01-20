package com.company.piglatin;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PigConverterImplTest {

    private static final PigConverterImpl converter = new PigConverterImpl();

    @Test
    public void testWay() {
        Assert.assertEquals("testway", converter.convert("testway"));
        Assert.assertEquals("testwayway", converter.convert("testwayway"));
        Assert.assertEquals("TeSt'way", converter.convert("TeSt'way"));
    }

    @Test
    public void testFindOutPunctuations() {
        List<PigConverterImpl.Punctuation> punList = converter.findOutPunctuations("ah'j'v.");
        List<PigConverterImpl.Punctuation> expected = new ArrayList<>();
        expected.add(new PigConverterImpl.Punctuation(0, '.'));
        expected.add(new PigConverterImpl.Punctuation(2, '\''));
        expected.add(new PigConverterImpl.Punctuation(4, '\''));
        Assert.assertEquals(expected, punList);
    }

    @Test
    public void testInsertPunctuations() {
        List<PigConverterImpl.Punctuation> pList = new ArrayList<>();
        pList.add(new PigConverterImpl.Punctuation(0, '.'));
        pList.add(new PigConverterImpl.Punctuation(2, '\''));
        pList.add(new PigConverterImpl.Punctuation(4, '\''));

        String transformed = converter.insertPunctuations("ahjv", pList);
        Assert.assertEquals("ah'j'v.", transformed);
    }

    @Test
    public void testRemovePunctuations() {
        Assert.assertEquals("ahoj", converter.removePunctuations("ah..o''j"));
    }

    @Test
    public void testFindOutCapitalIndices() {
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(5);
        expected.add(7);
        Assert.assertEquals(expected, converter.findOutCapitalIndices("aH'j'V.Bn"));
    }

    @Test
    public void testCapitalize() {
        List<Integer> capList = new ArrayList<>();
        capList.add(1);
        capList.add(5);
        capList.add(7);
        Assert.assertEquals("aH'j'V.Bn", converter.capitalize("ah'j'v.bn", capList));
    }

    @Test
    public void testVowels() {
        Assert.assertEquals("ahojway", converter.convert("ahoj"));
        Assert.assertEquals("ahojway.", converter.convert("ahoj."));
        Assert.assertEquals("Ahojway.", converter.convert("Ahoj."));
        Assert.assertEquals("Ahojw'ay.", converter.convert("Ah'oj."));
    }

    @Test
    public void testConsonants() {
        Assert.assertEquals("ellohay", converter.convert("hello"));
        Assert.assertEquals("ellohay.", converter.convert("hello."));
        Assert.assertEquals("ElLohay.", converter.convert("HeLlo."));
        Assert.assertEquals("ElLoh'ay.", converter.convert("HeL'lo."));
    }

    @Test
    public void testHyphens() {
        Assert.assertEquals("ElLoh'ay.-Ahojw'ay.", converter.convert("HeL'lo.-Ah'oj."));
        Assert.assertEquals("away-away-away", converter.convert("a-a-a-"));
        Assert.assertEquals("away", converter.convert("a"));
    }

    @Test
    public void testParagraph() {
        Assert.assertEquals("", converter.convert(""));
        Assert.assertEquals("away away away away away away away",
                converter.convert("a a a a a\na\ta"));
    }
}
