package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.ExpenditureItemPoMapper;
import cn.edu.xmu.oomall.other.mapper.LiquidationPoMapper;
import cn.edu.xmu.oomall.other.mapper.RevenueItemPoMapper;
import cn.edu.xmu.oomall.other.model.po.*;
import cn.edu.xmu.oomall.other.model.vo.liquidation.LiquidationInfoVo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.SimpleLiquidationInfoVo;
import cn.edu.xmu.oomall.other.util.DateFormatter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author Gao Yanfeng
 * @date 2021/12/10
 */
@Repository
public class LiquidationDao {

    @Autowired
    private LiquidationPoMapper liquidationPoMapper;

    @Autowired
    private RevenueItemPoMapper revenueItemPoMapper;
    @Autowired
    private ExpenditureItemPoMapper expenditureItemPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(LiquidationDao.class);

    public ReturnObject getLiquidations(Long shopId, ZonedDateTime beginDate, ZonedDateTime endDate, Boolean state, Integer page, Integer pageSize) {
        try {
            var example = new LiquidationPoExample();
            var criteria = example.createCriteria();
            if (!Objects.isNull(shopId)) {
                criteria.andShopIdEqualTo(shopId);
            }
            if (!Objects.isNull(beginDate)) {
                criteria.andLiquidDateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginDate));
            }
            if (!Objects.isNull(endDate)) {
                criteria.andLiquidDateLessThanOrEqualTo(DateFormatter.zoned2Local(endDate));
            }
            if (!Objects.isNull(state)) {
                criteria.andStateEqualTo((byte) (state ? 1 : 0));
            }
            PageHelper.startPage(page, pageSize);
            PageInfo<Object> pageInfo = new PageInfo(liquidationPoMapper.selectByExample(example));
            ReturnObject returnObject = new ReturnObject<>(pageInfo);
            return Common.getPageRetVo(returnObject, SimpleLiquidationInfoVo.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getLiquidationById(Long shopId, Long id) {
        try {
            var liquidation = liquidationPoMapper.selectByPrimaryKey(id);
            if (Objects.isNull(liquidation)) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "在指定店铺中未找到指定的清算单");
            }
            if (!liquidation.getShopId().equals(shopId) && shopId != 0) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            return new ReturnObject(cloneVo(liquidation, LiquidationInfoVo.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getRevenueById(Long shopId, Long id) {
        try {
            var revenuePo = revenueItemPoMapper.selectByPrimaryKey(id);
            if (Objects.isNull(revenuePo)) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "在指定店铺中未找到指定的进账明细");
            }
            if (!revenuePo.getShopId().equals(shopId) && shopId != 0) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            return new ReturnObject(revenuePo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getExpenditureById(Long shopId, Long id) {
        try {
            var expenditurePo = expenditureItemPoMapper.selectByPrimaryKey(id);
            if (Objects.isNull(expenditurePo)) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "在指定店铺中未找到指定的出账明细");
            }
            if (!expenditurePo.getShopId().equals(shopId) && shopId != 0) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            return new ReturnObject(expenditurePo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getRevenueItem(Long liquidationId, Long shopId, Long orderId, Long productId,
                                       Long sharerId, ZonedDateTime beginDate,
                                       ZonedDateTime endDate, Integer page,
                                       Integer pageSize, Boolean desc) {
        try {
            var example = new RevenueItemPoExample();
            var criteria = example.createCriteria();

            if (!Objects.isNull(liquidationId)) {
                criteria.andLiquidIdEqualTo(liquidationId);
            }
            if (!Objects.isNull(shopId)) {
                criteria.andShopIdEqualTo(shopId);
            }
            if (!Objects.isNull(orderId)) {
                criteria.andOrderIdEqualTo(orderId);
            }
            if (!Objects.isNull(productId)) {
                criteria.andProductIdEqualTo(productId);
            }
            if (!Objects.isNull(sharerId)) {
                criteria.andSharerIdEqualTo(sharerId);
            }
            if (!Objects.isNull(beginDate)) {
                criteria.andGmtCreateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginDate));
            }
            if (!Objects.isNull(endDate)) {
                criteria.andGmtCreateLessThanOrEqualTo(DateFormatter.zoned2Local(endDate));
            }
            if (!Objects.isNull(desc) && desc.equals(true)) {
                example.setOrderByClause("gmt_create desc");
            }

            PageHelper.startPage(page, pageSize);
            PageInfo<Object> pageInfo = new PageInfo(revenueItemPoMapper.selectByExample(example));
            return new ReturnObject(pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getExpenditureItem(Long liquidationId, Long shopId, Long orderId, Long productId,
                                           Long sharerId, ZonedDateTime beginDate,
                                           ZonedDateTime endDate, Integer page,
                                           Integer pageSize, Boolean desc) {
        try {
            var example = new ExpenditureItemPoExample();
            var criteria = example.createCriteria();

            if (!Objects.isNull(liquidationId)) {
                criteria.andLiquidIdEqualTo(liquidationId);
            }
            if (!Objects.isNull(shopId)) {
                criteria.andShopIdEqualTo(shopId);
            }
            if (!Objects.isNull(orderId)) {
                criteria.andOrderIdEqualTo(orderId);
            }
            if (!Objects.isNull(productId)) {
                criteria.andProductIdEqualTo(productId);
            }
            if (!Objects.isNull(sharerId)) {
                criteria.andSharerIdEqualTo(sharerId);
            }
            if (!Objects.isNull(beginDate)) {
                criteria.andGmtCreateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginDate));
            }
            if (!Objects.isNull(endDate)) {
                criteria.andGmtCreateLessThanOrEqualTo(DateFormatter.zoned2Local(endDate));
            }
            if (!Objects.isNull(desc) && desc.equals(true)) {
                example.setOrderByClause("gmt_create desc");
            }

            PageHelper.startPage(page, pageSize);
            PageInfo<Object> pageInfo = new PageInfo(expenditureItemPoMapper.selectByExample(example));
            return new ReturnObject(pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getRevenueByPaymentIdAndOrderItemId(Long paymentId, Long orderItemId) {
        try {
            var example = new RevenueItemPoExample();
            var criteria = example.createCriteria();
            criteria.andPaymentIdEqualTo(paymentId);
            criteria.andOrderitemIdEqualTo(orderItemId);
            var poList = revenueItemPoMapper.selectByExample(example);
            return new ReturnObject(poList.get(0));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertLiquidation(LiquidationPo po) {
        try {
            liquidationPoMapper.insert(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertRevenue(RevenueItemPo po) {
        try {
            revenueItemPoMapper.insert(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertExpenditure(ExpenditureItemPo po) {
        try {
            expenditureItemPoMapper.insert(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject updateLiquidation(LiquidationPo po) {
        try {
            liquidationPoMapper.updateByPrimaryKeySelective(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }
}
