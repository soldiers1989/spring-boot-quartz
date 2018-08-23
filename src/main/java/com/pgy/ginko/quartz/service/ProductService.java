package com.pgy.ginko.quartz.service;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.DataSourceKey;
import com.pgy.ginko.quartz.dao.ProductDao;
import com.pgy.ginko.quartz.model.Product;
import com.pgy.ginko.quartz.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 * @description ProductService
 * @date 2018/8/23 10:45
 */
@Service
@Slf4j
public class ProductService {

    @Resource
    private ProductDao productDao;

    @DataSource(DataSourceKey.master)
    public Product select(long productId) throws ServiceException {
        Product product = productDao.selectByPrimaryKey(productId);
        if (product == null) {
            throw new ServiceException("Product:" + productId + " not found");
        }
        return product;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public Product update(long productId, Product newProduct) throws ServiceException {

        if (productDao.updateByPrimaryKeySelective(newProduct) <= 0) {
            throw new ServiceException("Update product:" + productId + "failed");
        }
        return newProduct;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public boolean add(Product newProduct) throws ServiceException {
        Integer num = productDao.insert(newProduct);
        if (num <= 0) {
            throw new ServiceException("Add product failed");
        }
        return true;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public boolean delete(long productId) throws ServiceException {
        Integer num = productDao.deleteByPrimaryKey(productId);
        if (num <= 0) {
            throw new ServiceException("Delete product:" + productId + "failed");
        }
        return true;
    }

    @DataSource(DataSourceKey.master)
    public List<Product> getAllProduct() {
        return productDao.selectAll();
    }
}