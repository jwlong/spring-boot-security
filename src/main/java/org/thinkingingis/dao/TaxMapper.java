package org.thinkingingis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.thinkingingis.dto.TaxDTO;
import org.thinkingingis.model.Tax;

import java.util.List;

@Mapper
public interface TaxMapper extends BaseMapper<Tax> {
    List<TaxDTO> getList(TaxDTO taxDTO);
}
