package cn.itcast.core.controller.order;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.order.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    /**
     * 提交订单
     */


    //@RequestMapping("/findAll.do")
    //public List<Order> findAll() {
//
//
//        List<Order> orders = orderService.findAll();
//        System.out.println(orders);
//        return orders;
////
//   }
//    @RequestMapping("/findpage.do")
//    public PageResult findPage(Integer pageNum, Integer pageSize){
//        return orderService.findPage(pageNum, pageSize);
//    }
//    @RequestMapping("/search.do")
//    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
//                String name = SecurityContextHolder.getContext().getAuthentication().getName();
//                return orderService.searchForShop(page, rows, order,name);
//           }

//    @RequestMapping("/findPage.do")
//    public PageResult findPage(Integer pageNum, Integer pageSize){
//        return orderService.findPage(pageNum, pageSize);
//    }
//    }
        @RequestMapping("/search.do")
            public PageResult search(Integer page, Integer rows, @RequestBody Order order){
                    String name = SecurityContextHolder.getContext().getAuthentication().getName();
                    return orderService.search(page, rows, order);
                }




        }




