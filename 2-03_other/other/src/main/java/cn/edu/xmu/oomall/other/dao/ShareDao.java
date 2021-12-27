package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.controller.ShareController;
import cn.edu.xmu.oomall.other.mapper.SharePoMapper;
import cn.edu.xmu.oomall.other.mapper.SuccessfulSharePoMapper;
import cn.edu.xmu.oomall.other.model.bo.SuccessfulShare;
import cn.edu.xmu.oomall.other.model.po.SharePo;
import cn.edu.xmu.oomall.other.model.po.SharePoExample;
import cn.edu.xmu.oomall.other.model.po.SuccessfulSharePo;
import cn.edu.xmu.oomall.other.model.po.SuccessfulSharePoExample;
import cn.edu.xmu.oomall.other.util.DateFormatter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author Lu Zhang
 * @create 2021/12/8
 */
@Repository
public class ShareDao {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private SuccessfulSharePoMapper successfulSharePoMapper;

    @Autowired
    private SharePoMapper sharePoMapper;


    /**
     * @author jxy
     * @create 2021/12/13 7:57 PM
     */

    public ReturnObject getEarliestSuccessfulShare(Long onSaleId, Long customerId) {
        SuccessfulSharePoExample successfulSharePoExample = new SuccessfulSharePoExample();
        SuccessfulSharePoExample.Criteria criteria = successfulSharePoExample.createCriteria();
        criteria.andOnsaleIdEqualTo(onSaleId);
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andStateEqualTo((byte) 0);
        successfulSharePoExample.setOrderByClause("gmt_create asc");

        try {

            List<SuccessfulSharePo> successfulSharePos = successfulSharePoMapper.selectByExample(successfulSharePoExample);
            if (successfulSharePos.size() != 0) {
                SuccessfulSharePo first = successfulSharePos.get(0);
                return new ReturnObject(first);
            } else {
                return new ReturnObject<>(ReturnNo.ORDERITEM_NOTSHARED);
            }
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    /**
     * @author jxy
     * @create 2021/12/13 7:58 PM
     */

    public ReturnObject getShareByShareId(Long shareId) {

        try {
            SharePo sharePo = sharePoMapper.selectByPrimaryKey(shareId);
            if (sharePo != null) {
                return new ReturnObject(sharePo);
            } else {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            }
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    /**
     * @param productId
     * @param beginTime
     * @param endTime
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public ReturnObject listShares(Long userId, Long productId, ZonedDateTime beginTime, ZonedDateTime endTime,
                                   Integer pageNumber, Integer pageSize) {
        try {
            PageHelper.startPage(pageNumber, pageSize);
            SharePoExample example = new SharePoExample();
            SharePoExample.Criteria criteria = example.createCriteria();
            if (userId != null) {
                criteria.andSharerIdEqualTo(userId);
            }
            if (beginTime != null) {
                criteria.andGmtCreateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginTime));
            }
            if (endTime != null) {
                criteria.andGmtCreateLessThanOrEqualTo(DateFormatter.zoned2Local(endTime));
            }
            if (productId != null) {
                criteria.andProductIdEqualTo(productId);
            }

            List<SharePo> sharePos = sharePoMapper.selectByExample(example);

            ReturnObject ret = new ReturnObject<>(new PageInfo<>(sharePos));
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    /**
     * @param productId
     * @param beginTime
     * @param endTime
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public ReturnObject listBeShared(Long userId, Long productId, ZonedDateTime beginTime,
                                     ZonedDateTime endTime, Integer pageNumber, Integer pageSize) {
        try {
            PageHelper.startPage(pageNumber, pageSize);
            SuccessfulSharePoExample example = new SuccessfulSharePoExample();
            SuccessfulSharePoExample.Criteria criteria = example.createCriteria();
            if (userId != null) {
                criteria.andSharerIdEqualTo(userId);
            }
            if (productId != null) {
                criteria.andProductIdEqualTo(productId);
            }
            if (beginTime != null) {
                criteria.andGmtCreateGreaterThanOrEqualTo(DateFormatter.zoned2Local(beginTime));
            }
            if (endTime != null) {
                criteria.andGmtCreateLessThanOrEqualTo(DateFormatter.zoned2Local(endTime));
            }

            List<SuccessfulSharePo> successfulSharePos = successfulSharePoMapper.selectByExample(example);
            return new ReturnObject<>(new PageInfo<>(successfulSharePos));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }


    public ReturnObject getShareById(Long id) {
        try {
            SharePo sharePo = sharePoMapper.selectByPrimaryKey(id);

            if (sharePo == null) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
            }

            return new ReturnObject(sharePo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertSuccessfulShare(SuccessfulShare bo) {
        try {
            SuccessfulSharePo po = cloneVo(bo, SuccessfulSharePo.class);
            successfulSharePoMapper.insert(po);
            SuccessfulShare successfulShare = cloneVo(po, SuccessfulShare.class);
            return new ReturnObject(successfulShare);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertShare(SharePo po) {
        try {
            sharePoMapper.insertSelective(po);
            return new ReturnObject(po);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getShareBySharerIdAndOnsaleId(Long sharerId, Long onsaleId) {
        try {
            SharePoExample example = new SharePoExample();
            SharePoExample.Criteria criteria = example.createCriteria();
            if (sharerId != null) {
                criteria.andSharerIdEqualTo(sharerId);
            }
            if (onsaleId != null) {
                criteria.andOnsaleIdEqualTo(onsaleId);
            }

            List<SharePo> sharePos = sharePoMapper.selectByExample(example);

            return new ReturnObject(sharePos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getSuccessfulShareByShareIdAndProductId(Long sid, Long productId) {
        try {
            var example = new SuccessfulSharePoExample();
            SuccessfulSharePoExample.Criteria criteria = example.createCriteria();
            if (sid != null) {
                criteria.andShareIdEqualTo(sid);
            }
            if (productId != null) {
                criteria.andOnsaleIdEqualTo(productId);
            }

            List<SuccessfulSharePo> pos = successfulSharePoMapper.selectByExample(example);

            return new ReturnObject(pos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }
}