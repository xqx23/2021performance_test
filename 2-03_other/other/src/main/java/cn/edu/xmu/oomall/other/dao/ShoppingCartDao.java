package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.ShoppingCartPoMapper;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPoExample;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.ShoppingCartVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoCreatedFields;
import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoModifiedFields;

/**
 * @author Jx
 * @version 创建时间：2020/11/29
 */

@Repository
public class ShoppingCartDao {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartDao.class);

    @Autowired
    private ShoppingCartPoMapper shoppingCartPoMapper;

    public ReturnObject clearCart(Long userId) {
        ShoppingCartPoExample shoppingCartPoExample = new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria = shoppingCartPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        try {
            shoppingCartPoMapper.deleteByExample(shoppingCartPoExample);
            return new ReturnObject();
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject deleteCart(Long cartId) {
        try {
            shoppingCartPoMapper.deleteByPrimaryKey(cartId);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
        return new ReturnObject<>(ReturnNo.OK);
    }

    public ReturnObject getCartByUserId(Long userId, Integer page, Integer pageSize) {
        ShoppingCartPoExample shoppingCartPoExample = new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria = shoppingCartPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        List<ShoppingCartPo> cartPos = null;
        PageHelper.startPage(page, pageSize, true, true, null);
        try {
            cartPos = shoppingCartPoMapper.selectByExample(shoppingCartPoExample);
            PageInfo<ShoppingCartPo> pageInfo = new PageInfo<>(cartPos);
            return new ReturnObject(pageInfo);
        } catch (DataAccessException e) {
            logger.error("findCarts: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject addToCart(Long userId, String loginName, Long productId, Long quantity, Long price) {
        if (quantity == null) {
            quantity = 1L;
        }

        ShoppingCartPoExample example = new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andProductIdEqualTo(productId);
        try {
            List<ShoppingCartPo> oldPos = shoppingCartPoMapper.selectByExample(example);
            if (oldPos != null && oldPos.size() > 0) {
                ShoppingCartPo po = oldPos.get(0);
                Long originalQuantity = po.getQuantity() == null ? 0L : po.getQuantity();
                if (originalQuantity + quantity > 0) {
                    po.setQuantity(originalQuantity + quantity);
                } else {
                    return deleteCart(po.getId());
                }

                po.setPrice(price);
                setPoModifiedFields(po, userId, loginName);
                shoppingCartPoMapper.updateByPrimaryKeySelective(po);

                return new ReturnObject(po);
            } else {
                ShoppingCartPo po = new ShoppingCartPo();
                po.setCustomerId(userId);
                po.setProductId(productId);
                po.setQuantity(quantity);
                po.setPrice(price);
                setPoCreatedFields(po, userId, loginName);
                shoppingCartPoMapper.insertSelective(po);
                return new ReturnObject(po);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject judgeCart(Long userId, Long cartId) {
        try {
            ShoppingCartPo po = shoppingCartPoMapper.selectByPrimaryKey(cartId);
            /*资源不存在*/
            if (po == null) {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            }
            /*资源id非自己对象*/
            if (!po.getCustomerId().equals(userId)) {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }

            return new ReturnObject<>(po);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }

    }


    public ReturnObject modifyCart(ShoppingCartPo shoppingCartPo, ShoppingCartVo vo, Long loginUser, String loginName) {
        try {
            shoppingCartPo = (ShoppingCartPo) copyVo(vo, shoppingCartPo);
            setPoModifiedFields(shoppingCartPo, loginUser, loginName);
            shoppingCartPoMapper.updateByPrimaryKeySelective(shoppingCartPo);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
        return new ReturnObject<>(ReturnNo.OK);
    }

    /**
     * createdBy 蒋欣雨 2021/12/1
     * 将source中对应字段copy至target对象其他字段不变，若source中字段为null说明不修改
     */
    public Object copyVo(Object source, Object target) {
        Class sourceClass = source.getClass();
        Class targetClass = target.getClass();
        try {
            Field[] sourceFields = sourceClass.getDeclaredFields();
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                //若修改的字段为空，则说明不修改该字段
                if (sourceField.get(source) == null)
                    continue;
                Field targetField = null;
                try {
                    targetField = targetClass.getDeclaredField(sourceField.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }

                //静态和Final不能拷贝
                int mod = targetField.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                targetField.setAccessible(true);

                Class<?> sourceFieldType = sourceField.getType();
                Class<?> targetFieldType = targetField.getType();
                //属性名相同，类型相同，直接克隆
                if (targetFieldType.equals(sourceFieldType)) {
                    Object newObject = sourceField.get(source);
                    targetField.set(target, newObject);
                }
                //属性名相同，类型不同
                else {
                    boolean sourceFieldIsIntegerOrByteAndTargetFieldIsEnum = ("Integer".equals(sourceFieldType.getSimpleName()) || "Byte".equals(sourceFieldType.getSimpleName())) && targetFieldType.isEnum();
                    boolean targetFieldIsIntegerOrByteAndSourceFieldIsEnum = ("Integer".equals(targetFieldType.getSimpleName()) || "Byte".equals(targetFieldType.getSimpleName())) && sourceFieldType.isEnum();
                    //整形或Byte转枚举
                    if (sourceFieldIsIntegerOrByteAndTargetFieldIsEnum) {
                        Object newObj = sourceField.get(source);
                        if ("Byte".equals(sourceFieldType.getSimpleName())) {
                            newObj = ((Byte) newObj).intValue();
                        }
                        Object[] enumer = targetFieldType.getEnumConstants();
                        targetField.set(target, enumer[(int) newObj]);
                    }
                    //枚举转整形或Byte
                    else if (targetFieldIsIntegerOrByteAndSourceFieldIsEnum) {
                        Object value = ((Enum) sourceField.get(source)).ordinal();
                        if ("Byte".equals(targetFieldType.getSimpleName())) {
                            value = ((Integer) value).byteValue();
                        }
                        targetField.set(target, value);
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return target;
    }

}


