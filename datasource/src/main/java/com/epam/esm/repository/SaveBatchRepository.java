package com.epam.esm.repository;

import java.util.List;


public interface SaveBatchRepository<T> {

    List<T> save(List<T> orderCertificates);
}
