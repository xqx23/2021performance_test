package cn.edu.xmu.oomall.other.dao;


import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AddressPoMapper;
import cn.edu.xmu.oomall.other.model.po.AddressPo;
import cn.edu.xmu.oomall.other.model.po.AddressPoExample;
import cn.edu.xmu.oomall.other.model.vo.address.AddressVo;
import cn.edu.xmu.privilegegateway.annotation.util.Common;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoCreatedFields;
import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoModifiedFields;

/**
 * @author jxy
 * @create 2021/12/9 9:46 AM
 */

@Repository
public class AddressDao {
    private static final Logger logger = LoggerFactory.getLogger(AddressDao.class);

    @Autowired
    private AddressPoMapper addressPoMapper;


    /**
     * 新增地址
     *
     * @author jxy
     * @create 2021/12/9 9:42 PM
     */

    public ReturnObject addAddress(Long userId, String loginName, AddressPo addressPo) {
        try {

            addressPo.setBeDefault((byte) 0);
            addressPo.setCustomerId(userId);
            setPoCreatedFields(addressPo, userId, loginName);
            addressPoMapper.insert(addressPo);
            return new ReturnObject<>(addressPo);
        } catch (DataAccessException e) {
            logger.error("addAddress: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject getAddressByCustomerId(Long customerId) {
        try {
            AddressPoExample addressPoExample = new AddressPoExample();
            AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
            criteria.andCustomerIdEqualTo(customerId);
            List<AddressPo> addressPos = addressPoMapper.selectByExample(addressPoExample);
            return new ReturnObject<>(addressPos);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    /**
     * 用户查询已有地址
     *
     * @author jxy
     * @create 2021/12/10 9:18 AM
     */
    public ReturnObject getAddresses(Long userId) {
        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        List<AddressPo> addressPos;
        try {
            addressPos = addressPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getAddresses: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR);
        }
        return new ReturnObject(addressPos);
    }

    /**
     * 买家修改自己的地址信息
     *
     * @author jxy
     * @create 2021/12/10 10:11 AM
     */

    public ReturnObject updateAddress(Long userId, String loginName, Long id, AddressVo addressVo) {
        try {
            AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
            if (addressPo == null) return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            if (addressPo.getCustomerId() != userId) {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            Common.copyAttribute(addressVo, addressPo);
            setPoModifiedFields(addressPo, userId, loginName);
            addressPoMapper.updateByPrimaryKey(addressPo);
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ReturnNo.OK);
    }

    /**
     * 设置默认地址
     *
     * @author jxy
     * @create 2021/12/10 9:44 AM
     */

    public ReturnObject updateDefaultAddress(Long userId, String loginName, Long id) {
        AddressPoExample addressPoExample = new AddressPoExample();
        AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andBeDefaultEqualTo((byte) 1);
        List<AddressPo> addressPos;
        try {
            addressPos = addressPoMapper.selectByExample(addressPoExample);
            if (addressPos.size() > 0) {
                for (AddressPo po : addressPos) {
                    po.setBeDefault((byte) 0);
                    setPoModifiedFields(po, userId, loginName);
                    addressPoMapper.updateByPrimaryKey(po);
                }
            }
            AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
            if (addressPo == null) {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST, "address: Address Not Found");
            } else if (addressPo.getCustomerId() != userId) return new ReturnObject<>(ReturnNo.RESOURCE_ID_OUTSCOPE);
            else {
                addressPo.setBeDefault((byte) 1);
                setPoModifiedFields(addressPo, userId, loginName);
                addressPoMapper.updateByPrimaryKey(addressPo);
            }
        } catch (DataAccessException e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("addAddress: DataAccessException:%s", e.getMessage()));
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ReturnNo.OK);
    }

    /**
     * 买家删除地址
     *
     * @author jxy
     * @create 2021/12/10 11:39 AM
     */

    public ReturnObject deleteAddress(Long userId, Long id) {
        try {
            AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
            if (addressPo == null) return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            if (addressPo.getCustomerId() != userId) return new ReturnObject<>(ReturnNo.RESOURCE_ID_OUTSCOPE);
            addressPoMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ReturnNo.OK);
    }

}
