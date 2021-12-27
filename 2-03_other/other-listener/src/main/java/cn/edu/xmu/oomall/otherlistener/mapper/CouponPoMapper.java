package cn.edu.xmu.oomall.otherlistener.mapper;

import cn.edu.xmu.oomall.otherlistener.model.CouponPo;
import cn.edu.xmu.oomall.otherlistener.model.CouponPoExample;

import java.util.List;

public interface CouponPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    int insert(CouponPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    int insertSelective(CouponPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    List<CouponPo> selectByExample(CouponPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    CouponPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CouponPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_coupon
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CouponPo record);
}