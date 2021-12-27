package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AddressDao;
import cn.edu.xmu.oomall.other.microservice.RegionService;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.oomall.other.model.po.AddressPo;
import cn.edu.xmu.oomall.other.model.vo.address.AddressRetVo;
import cn.edu.xmu.oomall.other.model.vo.address.AddressVo;
import cn.edu.xmu.oomall.other.util.PageUtils;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author jxy
 * @create 2021/12/9 9:46 AM
 */

@Service
public class AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private RegionService regionService;

    /**
     * @author jxy
     * @create 2021/12/10 9:23 AM
     */

    public ReturnObject addAddress(Long userId, String loginName, AddressVo addressVo) {
        AddressPo addressPo = cloneVo(addressVo, AddressPo.class);
        ReturnObject retObj = addressDao.getAddressByCustomerId(userId);
        if (!retObj.getCode().equals(ReturnNo.OK))
            return retObj;
        List<AddressPo> addressPos = (List<AddressPo>) retObj.getData();
        if (addressPos.size() >= 20) {
            return new ReturnObject<>(ReturnNo.ADDRESS_OUTLIMIT);
        }
        ReturnObject returnObject = addressDao.addAddress(userId, loginName, addressPo);
        if (!returnObject.getCode().equals(ReturnNo.OK))
            return returnObject;
        addressPo = (AddressPo) returnObject.getData();
        AddressRetVo addressRetVo = cloneVo(addressPo, AddressRetVo.class);
        SimpleObject simpleObject = (SimpleObject) regionService.getSimpleRegionById(addressVo.getRegionId()).getData();

        addressRetVo.setRegion(simpleObject);
        addressRetVo.setBeDefault(addressPo.getBeDefault().equals((byte) 0) ? false : true);
        return new ReturnObject<>(addressRetVo);

    }

    /**
     * @author jxy
     * @create 2021/12/10 9:22 AM
     */

    public ReturnObject getAddresses(Long userId) {
        ReturnObject ret = addressDao.getAddresses(userId);
        List<AddressPo> addressPos = (List<AddressPo>) ret.getData();
        List<Long> regionIds = addressPos.stream().map(AddressPo::getRegionId).collect(Collectors.toList());
        List<AddressRetVo> addressRetVos = new ArrayList<>(addressPos.size());

        for (int i = 0; i < addressPos.size(); i++) {
            Long id = regionIds.get(i);
            AddressRetVo addressRetVo = cloneVo(addressPos.get(i), AddressRetVo.class);
            InternalReturnObject internalReturnObject = regionService.getSimpleRegionById(id);
            SimpleObject simpleObject = regionService.getSimpleRegionById(id).getData();
            addressRetVo.setRegion(simpleObject);
            addressRetVo.setBeDefault(addressPos.get(i).getBeDefault().equals((byte) 0) ? false : true);
            addressRetVos.add(addressRetVo);
        }

        return new ReturnObject<>(addressRetVos);
    }

    /**
     * @author jxy
     * @create 2021/12/10 9:43 AM
     */
    public ReturnObject updateDefaultAddress(Long userId, String loginName, Long id) {
        return addressDao.updateDefaultAddress(userId, loginName, id);
    }

    public ReturnObject updateAddress(Long userId, String loginName, Long id, AddressVo addressVo) {
        InternalReturnObject ret = regionService.getSimpleRegionById(addressVo.getRegionId());
        if (!ret.getErrno().equals(ReturnNo.OK.getCode()))
            return new ReturnObject(ReturnNo.getByCode(ret.getErrno()), ret.getErrmsg());
        return addressDao.updateAddress(userId, loginName, id, addressVo);
    }

    /**
     * @author jxy
     * @create 2021/12/10 11:38 AM
     */
    public ReturnObject deleteAddress(Long userId, Long id) {
        return addressDao.deleteAddress(userId, id);
    }

}
