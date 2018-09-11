package com.wmall;

import com.wmall.commons.Page;
import com.wmall.dto.OrderDto;
import com.wmall.entity.OrderMaster;
import com.wmall.entity.ProductCategory;
import com.wmall.service.OrderMasterService;
import com.wmall.service.ProductCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WmallApplication.class)
public class WmallApplicationTests {

    Logger logger = LoggerFactory.getLogger(WmallApplicationTests.class);

    @Autowired(required = true)
    private ProductCategoryService productCategoryService;

    @Autowired
    private OrderMasterService orderMasterService;

    //@Test
    public void contextLoads() {
    }

    //@Test
    public void test1() {
        logger.info("info......");
        logger.debug("debug......");
        logger.error("error......");
    }

    //@Test
    //@Transactional // 测试中使用事务,完全回滚
    public void findCategory() {
        logger.info("-------------------------------------------------");
        List<Integer> typeList = Arrays.asList(1002);
        List<ProductCategory> productCategoryList = productCategoryService.findByCategoryType(typeList);
        logger.info("--------------------{}", productCategoryList);
    }

    //@Test
    public void orderMasterService() {
        logger.info("----------------------orderMasterServiceTest---------------------------");
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerAddress("福泉路111号");
        orderMaster.setBuyerName("聂晓旭");
        orderMaster.setBuyerPhone("15021733369");
        orderMaster.setOrderId(UUID.randomUUID().toString());
        orderMaster.setBuyerOpenid("amanxu");
        orderMasterService.save(orderMaster);

        String openId = "amanxu";
        Page<OrderMaster> page = new Page(10);
        Page<OrderDto> result = orderMasterService.findByOpenid(openId, page);
        List<OrderDto> resultList = result.getResult();
        logger.info("-------------------------{};{}", result.getTotalPages(), result.getResult().toString());
    }

}
