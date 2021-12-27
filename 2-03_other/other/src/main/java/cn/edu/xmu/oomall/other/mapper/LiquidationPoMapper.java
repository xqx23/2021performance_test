package cn.edu.xmu.oomall.other.mapper;

import cn.edu.xmu.oomall.other.model.po.LiquidationPo;
import cn.edu.xmu.oomall.other.model.po.LiquidationPoExample;
import java.util.List;

public interface LiquidationPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    int insert(LiquidationPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    int insertSelective(LiquidationPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    List<LiquidationPo> selectByExample(LiquidationPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    LiquidationPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LiquidationPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_liquidation
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LiquidationPo record);
}