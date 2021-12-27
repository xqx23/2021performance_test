package cn.edu.xmu.oomall.other.model.po;

import java.time.LocalDateTime;

public class CouponPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.coupon_sn
     *
     * @mbg.generated
     */
    private String couponSn;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.name
     *
     * @mbg.generated
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.customer_id
     *
     * @mbg.generated
     */
    private Long customerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.activity_id
     *
     * @mbg.generated
     */
    private Long activityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.begin_time
     *
     * @mbg.generated
     */
    private LocalDateTime beginTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.end_time
     *
     * @mbg.generated
     */
    private LocalDateTime endTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.state
     *
     * @mbg.generated
     */
    private Byte state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.creator_name
     *
     * @mbg.generated
     */
    private String creatorName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.modifier_id
     *
     * @mbg.generated
     */
    private Long modifierId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.modifier_name
     *
     * @mbg.generated
     */
    private String modifierName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.gmt_create
     *
     * @mbg.generated
     */
    private LocalDateTime gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column oomall_coupon.gmt_modified
     *
     * @mbg.generated
     */
    private LocalDateTime gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.id
     *
     * @return the value of oomall_coupon.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.id
     *
     * @param id the value for oomall_coupon.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.coupon_sn
     *
     * @return the value of oomall_coupon.coupon_sn
     *
     * @mbg.generated
     */
    public String getCouponSn() {
        return couponSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.coupon_sn
     *
     * @param couponSn the value for oomall_coupon.coupon_sn
     *
     * @mbg.generated
     */
    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn == null ? null : couponSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.name
     *
     * @return the value of oomall_coupon.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.name
     *
     * @param name the value for oomall_coupon.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.customer_id
     *
     * @return the value of oomall_coupon.customer_id
     *
     * @mbg.generated
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.customer_id
     *
     * @param customerId the value for oomall_coupon.customer_id
     *
     * @mbg.generated
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.activity_id
     *
     * @return the value of oomall_coupon.activity_id
     *
     * @mbg.generated
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.activity_id
     *
     * @param activityId the value for oomall_coupon.activity_id
     *
     * @mbg.generated
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.begin_time
     *
     * @return the value of oomall_coupon.begin_time
     *
     * @mbg.generated
     */
    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.begin_time
     *
     * @param beginTime the value for oomall_coupon.begin_time
     *
     * @mbg.generated
     */
    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.end_time
     *
     * @return the value of oomall_coupon.end_time
     *
     * @mbg.generated
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.end_time
     *
     * @param endTime the value for oomall_coupon.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.state
     *
     * @return the value of oomall_coupon.state
     *
     * @mbg.generated
     */
    public Byte getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.state
     *
     * @param state the value for oomall_coupon.state
     *
     * @mbg.generated
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.creator_id
     *
     * @return the value of oomall_coupon.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.creator_id
     *
     * @param creatorId the value for oomall_coupon.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.creator_name
     *
     * @return the value of oomall_coupon.creator_name
     *
     * @mbg.generated
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.creator_name
     *
     * @param creatorName the value for oomall_coupon.creator_name
     *
     * @mbg.generated
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.modifier_id
     *
     * @return the value of oomall_coupon.modifier_id
     *
     * @mbg.generated
     */
    public Long getModifierId() {
        return modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.modifier_id
     *
     * @param modifierId the value for oomall_coupon.modifier_id
     *
     * @mbg.generated
     */
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.modifier_name
     *
     * @return the value of oomall_coupon.modifier_name
     *
     * @mbg.generated
     */
    public String getModifierName() {
        return modifierName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.modifier_name
     *
     * @param modifierName the value for oomall_coupon.modifier_name
     *
     * @mbg.generated
     */
    public void setModifierName(String modifierName) {
        this.modifierName = modifierName == null ? null : modifierName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.gmt_create
     *
     * @return the value of oomall_coupon.gmt_create
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.gmt_create
     *
     * @param gmtCreate the value for oomall_coupon.gmt_create
     *
     * @mbg.generated
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column oomall_coupon.gmt_modified
     *
     * @return the value of oomall_coupon.gmt_modified
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column oomall_coupon.gmt_modified
     *
     * @param gmtModified the value for oomall_coupon.gmt_modified
     *
     * @mbg.generated
     */
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}