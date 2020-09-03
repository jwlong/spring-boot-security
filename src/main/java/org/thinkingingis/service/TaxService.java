package org.thinkingingis.service;

import org.thinkingingis.dto.TaxDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TaxService {
    List<TaxDTO> getList(TaxDTO taxDTO);

    TaxDTO getByPk(Long taxPk) throws InvocationTargetException, IllegalAccessException;

    String genContent(TaxDTO taxDTO);

    TaxDTO getFirstTax();

    TaxDTO markUsedAndGetNext(Long taxPk);
}
