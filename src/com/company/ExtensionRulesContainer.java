package com.company;

/**
 * Created by Andrzej on 10.02.2019.
 */
public class ExtensionRulesContainer {

    int ruleByteIndex[];
    int ruleByteValue[];

    public ExtensionRulesContainer(int[] ruleByteIndex, int[] ruleByteValue) throws Exception {
        this.ruleByteIndex = ruleByteIndex;
        this.ruleByteValue = ruleByteValue;
        if (ruleByteIndex.length != ruleByteValue.length){
            throw new Exception("Lengths of arrays are not equal");
        }
    }

    public int[] getRuleByteIndex() {
        return ruleByteIndex;
    }

    public int[] getRuleByteValue() {
        return ruleByteValue;
    }
}
