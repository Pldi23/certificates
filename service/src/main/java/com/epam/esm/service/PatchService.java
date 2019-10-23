package com.epam.esm.service;


public interface PatchService<T, S> {

    T patch(S s, Long id);
}
