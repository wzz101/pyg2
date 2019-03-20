package cn.itcast.core.service.seller;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SellerServiceImpl implements SellerService {
    @Resource
    private SellerDao sellerDao;

    /**
     * 商家入住申请
     */

    @Transactional
    @Override
    public void add(Seller seller) {
        //设置商家审核状态
        seller.setStatus("0");//未审核
        seller.setCreateTime(new Date());//提交日期
        // 需要对密码进行加密：md5、加盐（盐值）spring
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        sellerDao.insertSelective(seller);
    }

    /**
     * 待审核商家的列表查询
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
        SellerQuery query = new SellerQuery();
        if (seller.getStatus()!=null && !"".equals(seller.getStatus().trim())){
            query.createCriteria().andStatusEqualTo(seller.getStatus());
        }
        Page<Seller> p = (Page<Seller>) sellerDao.selectByExample(query);
        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 回显商家
     * @param sellerId
     * @return
     */
    @Override
    public Seller findOne(String sellerId) {

        return sellerDao.selectByPrimaryKey(sellerId);
    }

    /**
     * 审核商家
     * @param sellerId
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
