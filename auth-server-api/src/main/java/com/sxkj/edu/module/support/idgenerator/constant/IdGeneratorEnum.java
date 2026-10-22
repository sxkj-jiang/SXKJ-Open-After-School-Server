package com.sxkj.edu.module.support.idgenerator.constant;


public enum IdGeneratorEnum {


    /**
     * 测试generate
     */
    TEST_ID_GENERATOR(2, "testIdGenerator"),


    ORDER(1, "order");

    private long id;
    private String keyName;

    IdGeneratorEnum(int id, String keyName) {
        this.id = id;
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "IdGeneratorEnum{" + "id=" + id + ", keyName='" + keyName + '\'' + '}';
    }

    public long getId() {
        return id;
    }

    public String getKeyName() {
        return keyName;
    }

}
