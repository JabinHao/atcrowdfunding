package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.po.ReturnPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReturnPOMapper {
    long countByExample(ReturnPOExample example);

    int deleteByExample(ReturnPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ReturnPO record);

    int insertSelective(ReturnPO record);

    List<ReturnPO> selectByExample(ReturnPOExample example);

    ReturnPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ReturnPO record, @Param("example") ReturnPOExample example);

    int updateByExample(@Param("record") ReturnPO record, @Param("example") ReturnPOExample example);

    int updateByPrimaryKeySelective(ReturnPO record);

    int updateByPrimaryKey(ReturnPO record);

    void insertReturnPOBatch(@Param("returnPOList") List<ReturnPO> returnPOList, @Param("projectId") Integer projectId);
}