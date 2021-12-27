package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AfterSalePoMapper;
import cn.edu.xmu.oomall.other.model.bo.AfterSale;
import cn.edu.xmu.oomall.other.model.po.AfterSalePo;
import cn.edu.xmu.oomall.other.model.po.AfterSalePoExample;
import cn.edu.xmu.oomall.other.model.vo.aftersale.AfterSaleSimpleRetVo;
import cn.edu.xmu.oomall.other.util.DateFormatter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoCreatedFields;
import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoModifiedFields;

/***
 *@author 李智樑
 *@create 2021/12/5 9:54
 */
@Repository
public class AfterSaleDao {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleDao.class);

    @Autowired
    private AfterSalePoMapper afterSalePoMapper;

    public ReturnObject getAfterSaleStates() {
        List<Map<String, Object>> stateList = new ArrayList<>();
        for (AfterSale.State state : AfterSale.State.values()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("code", state.getCode());
            temp.put("name", state.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    public ReturnObject insertAftersale(AfterSalePo po, Long userId, String loginName) {
        try {
            setPoCreatedFields(po, userId, loginName);
            afterSalePoMapper.insert(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }


    public ReturnObject getAllAftersales(Long userId, Long shopId, ZonedDateTime beginTime, ZonedDateTime endTime,
                                         Integer page, Integer pageSize, Integer type, Integer state, Boolean isTimeDesc) {
        AfterSalePoExample afterSalePoExample = new AfterSalePoExample();
        AfterSalePoExample.Criteria criteria = afterSalePoExample.createCriteria();
        if (userId != null) {
            criteria.andCustomerIdEqualTo(userId);
        }
        if (shopId != null) {
            criteria.andShopIdEqualTo(shopId);
        }
        if (beginTime != null) {
            criteria.andGmtCreateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginTime));
        }
        if (endTime != null) {
            criteria.andGmtCreateLessThanOrEqualTo(DateFormatter.zoned2Local(endTime));
        }
        if (type != null) {
            criteria.andTypeEqualTo(type.byteValue());
        }
        if (state != null) {
            criteria.andStateEqualTo(state.byteValue());
        }
        if (isTimeDesc) {
            afterSalePoExample.setOrderByClause("gmt_create desc");
        }

        try {
            PageHelper.startPage(page, pageSize);
            List<AfterSalePo> afterSalePos = afterSalePoMapper.selectByExample(afterSalePoExample);
            PageInfo<AfterSalePo> pageInfo = new PageInfo<>(afterSalePos);
            return Common.getPageRetVo(new ReturnObject(pageInfo), AfterSaleSimpleRetVo.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getAfterSaleById(Long id) {
        try {
            AfterSalePo afterSalePo = afterSalePoMapper.selectByPrimaryKey(id);

            if (afterSalePo == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
            }

            return new ReturnObject(afterSalePo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject updateAfterSale(AfterSalePo po, Long userId, String loginName) {
        try {
            setPoModifiedFields(po, userId, loginName);
            afterSalePoMapper.updateByPrimaryKeySelective(po);
            return new ReturnObject();
        } catch (Exception e) {
            logger.error(e.toString());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getAfterSaleBySn(String documentId) {
        try {
            var example = new AfterSalePoExample();
            var criteria = example.createCriteria();
            criteria.andServiceSnEqualTo(documentId);
            var poList = afterSalePoMapper.selectByExample(example);
            return new ReturnObject(poList.get(0));
        } catch (Exception e) {
            logger.error(e.toString());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }
}
