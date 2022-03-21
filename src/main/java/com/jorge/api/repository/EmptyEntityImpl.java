package com.jorge.api.repository;

public class EmptyEntityImpl implements IEmptyEntity{

    private final Long id;
    private final String name;


    public EmptyEntityImpl(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
