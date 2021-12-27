package cn.edu.xmu.oomall.other.model.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddressPoExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public AddressPoExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIsNull() {
            addCriterion("`customer_id` is null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIsNotNull() {
            addCriterion("`customer_id` is not null");
            return (Criteria) this;
        }

        public Criteria andCustomerIdEqualTo(Long value) {
            addCriterion("`customer_id` =", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotEqualTo(Long value) {
            addCriterion("`customer_id` <>", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThan(Long value) {
            addCriterion("`customer_id` >", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`customer_id` >=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThan(Long value) {
            addCriterion("`customer_id` <", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdLessThanOrEqualTo(Long value) {
            addCriterion("`customer_id` <=", value, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdIn(List<Long> values) {
            addCriterion("`customer_id` in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotIn(List<Long> values) {
            addCriterion("`customer_id` not in", values, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdBetween(Long value1, Long value2) {
            addCriterion("`customer_id` between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andCustomerIdNotBetween(Long value1, Long value2) {
            addCriterion("`customer_id` not between", value1, value2, "customerId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNull() {
            addCriterion("`region_id` is null");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNotNull() {
            addCriterion("`region_id` is not null");
            return (Criteria) this;
        }

        public Criteria andRegionIdEqualTo(Long value) {
            addCriterion("`region_id` =", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotEqualTo(Long value) {
            addCriterion("`region_id` <>", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThan(Long value) {
            addCriterion("`region_id` >", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`region_id` >=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThan(Long value) {
            addCriterion("`region_id` <", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThanOrEqualTo(Long value) {
            addCriterion("`region_id` <=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIn(List<Long> values) {
            addCriterion("`region_id` in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotIn(List<Long> values) {
            addCriterion("`region_id` not in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdBetween(Long value1, Long value2) {
            addCriterion("`region_id` between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotBetween(Long value1, Long value2) {
            addCriterion("`region_id` not between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andDetailIsNull() {
            addCriterion("`detail` is null");
            return (Criteria) this;
        }

        public Criteria andDetailIsNotNull() {
            addCriterion("`detail` is not null");
            return (Criteria) this;
        }

        public Criteria andDetailEqualTo(String value) {
            addCriterion("`detail` =", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotEqualTo(String value) {
            addCriterion("`detail` <>", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThan(String value) {
            addCriterion("`detail` >", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailGreaterThanOrEqualTo(String value) {
            addCriterion("`detail` >=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThan(String value) {
            addCriterion("`detail` <", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLessThanOrEqualTo(String value) {
            addCriterion("`detail` <=", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailLike(String value) {
            addCriterion("`detail` like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotLike(String value) {
            addCriterion("`detail` not like", value, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailIn(List<String> values) {
            addCriterion("`detail` in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotIn(List<String> values) {
            addCriterion("`detail` not in", values, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailBetween(String value1, String value2) {
            addCriterion("`detail` between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andDetailNotBetween(String value1, String value2) {
            addCriterion("`detail` not between", value1, value2, "detail");
            return (Criteria) this;
        }

        public Criteria andConsigneeIsNull() {
            addCriterion("`consignee` is null");
            return (Criteria) this;
        }

        public Criteria andConsigneeIsNotNull() {
            addCriterion("`consignee` is not null");
            return (Criteria) this;
        }

        public Criteria andConsigneeEqualTo(String value) {
            addCriterion("`consignee` =", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeNotEqualTo(String value) {
            addCriterion("`consignee` <>", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeGreaterThan(String value) {
            addCriterion("`consignee` >", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeGreaterThanOrEqualTo(String value) {
            addCriterion("`consignee` >=", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeLessThan(String value) {
            addCriterion("`consignee` <", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeLessThanOrEqualTo(String value) {
            addCriterion("`consignee` <=", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeLike(String value) {
            addCriterion("`consignee` like", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeNotLike(String value) {
            addCriterion("`consignee` not like", value, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeIn(List<String> values) {
            addCriterion("`consignee` in", values, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeNotIn(List<String> values) {
            addCriterion("`consignee` not in", values, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeBetween(String value1, String value2) {
            addCriterion("`consignee` between", value1, value2, "consignee");
            return (Criteria) this;
        }

        public Criteria andConsigneeNotBetween(String value1, String value2) {
            addCriterion("`consignee` not between", value1, value2, "consignee");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("`mobile` is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("`mobile` is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("`mobile` =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("`mobile` <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("`mobile` >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("`mobile` >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("`mobile` <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("`mobile` <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("`mobile` like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("`mobile` not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("`mobile` in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("`mobile` not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("`mobile` between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("`mobile` not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andBeDefaultIsNull() {
            addCriterion("`be_default` is null");
            return (Criteria) this;
        }

        public Criteria andBeDefaultIsNotNull() {
            addCriterion("`be_default` is not null");
            return (Criteria) this;
        }

        public Criteria andBeDefaultEqualTo(Byte value) {
            addCriterion("`be_default` =", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultNotEqualTo(Byte value) {
            addCriterion("`be_default` <>", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultGreaterThan(Byte value) {
            addCriterion("`be_default` >", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultGreaterThanOrEqualTo(Byte value) {
            addCriterion("`be_default` >=", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultLessThan(Byte value) {
            addCriterion("`be_default` <", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultLessThanOrEqualTo(Byte value) {
            addCriterion("`be_default` <=", value, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultIn(List<Byte> values) {
            addCriterion("`be_default` in", values, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultNotIn(List<Byte> values) {
            addCriterion("`be_default` not in", values, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultBetween(Byte value1, Byte value2) {
            addCriterion("`be_default` between", value1, value2, "beDefault");
            return (Criteria) this;
        }

        public Criteria andBeDefaultNotBetween(Byte value1, Byte value2) {
            addCriterion("`be_default` not between", value1, value2, "beDefault");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNull() {
            addCriterion("`creator_id` is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNotNull() {
            addCriterion("`creator_id` is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdEqualTo(Long value) {
            addCriterion("`creator_id` =", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotEqualTo(Long value) {
            addCriterion("`creator_id` <>", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThan(Long value) {
            addCriterion("`creator_id` >", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`creator_id` >=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThan(Long value) {
            addCriterion("`creator_id` <", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThanOrEqualTo(Long value) {
            addCriterion("`creator_id` <=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIn(List<Long> values) {
            addCriterion("`creator_id` in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotIn(List<Long> values) {
            addCriterion("`creator_id` not in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdBetween(Long value1, Long value2) {
            addCriterion("`creator_id` between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotBetween(Long value1, Long value2) {
            addCriterion("`creator_id` not between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorNameIsNull() {
            addCriterion("`creator_name` is null");
            return (Criteria) this;
        }

        public Criteria andCreatorNameIsNotNull() {
            addCriterion("`creator_name` is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorNameEqualTo(String value) {
            addCriterion("`creator_name` =", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameNotEqualTo(String value) {
            addCriterion("`creator_name` <>", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameGreaterThan(String value) {
            addCriterion("`creator_name` >", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameGreaterThanOrEqualTo(String value) {
            addCriterion("`creator_name` >=", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameLessThan(String value) {
            addCriterion("`creator_name` <", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameLessThanOrEqualTo(String value) {
            addCriterion("`creator_name` <=", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameLike(String value) {
            addCriterion("`creator_name` like", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameNotLike(String value) {
            addCriterion("`creator_name` not like", value, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameIn(List<String> values) {
            addCriterion("`creator_name` in", values, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameNotIn(List<String> values) {
            addCriterion("`creator_name` not in", values, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameBetween(String value1, String value2) {
            addCriterion("`creator_name` between", value1, value2, "creatorName");
            return (Criteria) this;
        }

        public Criteria andCreatorNameNotBetween(String value1, String value2) {
            addCriterion("`creator_name` not between", value1, value2, "creatorName");
            return (Criteria) this;
        }

        public Criteria andModifierIdIsNull() {
            addCriterion("`modifier_id` is null");
            return (Criteria) this;
        }

        public Criteria andModifierIdIsNotNull() {
            addCriterion("`modifier_id` is not null");
            return (Criteria) this;
        }

        public Criteria andModifierIdEqualTo(Long value) {
            addCriterion("`modifier_id` =", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotEqualTo(Long value) {
            addCriterion("`modifier_id` <>", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdGreaterThan(Long value) {
            addCriterion("`modifier_id` >", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`modifier_id` >=", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdLessThan(Long value) {
            addCriterion("`modifier_id` <", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdLessThanOrEqualTo(Long value) {
            addCriterion("`modifier_id` <=", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdIn(List<Long> values) {
            addCriterion("`modifier_id` in", values, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotIn(List<Long> values) {
            addCriterion("`modifier_id` not in", values, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdBetween(Long value1, Long value2) {
            addCriterion("`modifier_id` between", value1, value2, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotBetween(Long value1, Long value2) {
            addCriterion("`modifier_id` not between", value1, value2, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierNameIsNull() {
            addCriterion("`modifier_name` is null");
            return (Criteria) this;
        }

        public Criteria andModifierNameIsNotNull() {
            addCriterion("`modifier_name` is not null");
            return (Criteria) this;
        }

        public Criteria andModifierNameEqualTo(String value) {
            addCriterion("`modifier_name` =", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameNotEqualTo(String value) {
            addCriterion("`modifier_name` <>", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameGreaterThan(String value) {
            addCriterion("`modifier_name` >", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameGreaterThanOrEqualTo(String value) {
            addCriterion("`modifier_name` >=", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameLessThan(String value) {
            addCriterion("`modifier_name` <", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameLessThanOrEqualTo(String value) {
            addCriterion("`modifier_name` <=", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameLike(String value) {
            addCriterion("`modifier_name` like", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameNotLike(String value) {
            addCriterion("`modifier_name` not like", value, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameIn(List<String> values) {
            addCriterion("`modifier_name` in", values, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameNotIn(List<String> values) {
            addCriterion("`modifier_name` not in", values, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameBetween(String value1, String value2) {
            addCriterion("`modifier_name` between", value1, value2, "modifierName");
            return (Criteria) this;
        }

        public Criteria andModifierNameNotBetween(String value1, String value2) {
            addCriterion("`modifier_name` not between", value1, value2, "modifierName");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("`gmt_create` is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("`gmt_create` is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(LocalDateTime value) {
            addCriterion("`gmt_create` =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(LocalDateTime value) {
            addCriterion("`gmt_create` <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(LocalDateTime value) {
            addCriterion("`gmt_create` >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_create` >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(LocalDateTime value) {
            addCriterion("`gmt_create` <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_create` <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<LocalDateTime> values) {
            addCriterion("`gmt_create` in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<LocalDateTime> values) {
            addCriterion("`gmt_create` not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_create` between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_create` not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("`gmt_modified` is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("`gmt_modified` is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(LocalDateTime value) {
            addCriterion("`gmt_modified` =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(LocalDateTime value) {
            addCriterion("`gmt_modified` <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(LocalDateTime value) {
            addCriterion("`gmt_modified` >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_modified` >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(LocalDateTime value) {
            addCriterion("`gmt_modified` <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_modified` <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<LocalDateTime> values) {
            addCriterion("`gmt_modified` in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<LocalDateTime> values) {
            addCriterion("`gmt_modified` not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_modified` between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_modified` not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table oomall_address
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table oomall_address
     *
     * @mbg.generated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}