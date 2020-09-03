package org.thinkingingis.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thinkingingis.dao.TaxMapper;
import org.thinkingingis.dto.TaxDTO;
import org.thinkingingis.enums.YseOrNo;
import org.thinkingingis.model.Tax;
import org.thinkingingis.service.TaxService;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaxServiceImpl implements TaxService {
    private final Logger logger = LoggerFactory.getLogger(TaxServiceImpl.class);
    @Autowired
    private TaxMapper taxMapper;

    @Override
    public List<TaxDTO> getList(TaxDTO taxDTO) {
        return taxMapper.getList(taxDTO);
    }

    @Override
    public TaxDTO getByPk(Long taxPk) throws InvocationTargetException, IllegalAccessException {
        Tax tax = taxMapper.selectById(taxPk);
        TaxDTO taxDTO =  new TaxDTO();
        BeanUtils.copyProperties(taxDTO,tax);
        return taxDTO;
    }

    /**
     * {0},{1},{2},{3},{4},{5},{6},{7}
     * 发票生成的pattern说明
     * 0.表示是个人(01)或者企业
     * 1.表示发票种类类型（电子发票或者普通发票）
     * 2.发票代码
     * 3.发票号码
     * 4.开票金额
     * 5.开票日期
     * 6.发票检验码
     * 7.暂时未知，随意填写四个位就行
     * @param taxDTO
     * @return
     */
    @Override
    public String genContent(TaxDTO taxDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String content = MessageFormat.format("{0},{1},{2},{3},{4},{5},{6},{7}",taxDTO.getSid().trim(),
                taxDTO.getType().trim(),taxDTO.getTaxCode().toString().trim(),taxDTO.getTaxNo().trim(), taxDTO.getAmountStr().trim(),sdf.format(taxDTO.getTaxDate()),"50379805223291340192","1135");
        logger.info("Content:{}",content);
        return content;
    }

    @Override
    public TaxDTO getFirstTax() {
        TaxDTO taxDTO = new TaxDTO();
        taxDTO.setUsed(YseOrNo.NO.getCode());
        List<TaxDTO> list = taxMapper.getList(taxDTO);
        Optional<TaxDTO>  optional = list.stream().sorted(Comparator.comparing(TaxDTO::getTaxPk)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }else {
            throw new RuntimeException("没有记录了");
        }
    }

    @Override
    public TaxDTO markUsedAndGetNext(Long taxPk) {
        Tax tax = taxMapper.selectById(taxPk);
        tax.setUsed(YseOrNo.YES.getCode());
        tax.setRecDate(new Date());
        taxMapper.updateById(tax);
        return getFirstTax();
    }
}
