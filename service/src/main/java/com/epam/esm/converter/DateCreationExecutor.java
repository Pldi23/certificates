package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ParameterSearchType;

import java.time.LocalDate;
import java.util.List;

/**
 * to execute criteria creation process with LocaleDate fields
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public interface DateCreationExecutor<T> {

    T execute(ParameterSearchType parameterSearchType, List<LocalDate> parameters);
}
