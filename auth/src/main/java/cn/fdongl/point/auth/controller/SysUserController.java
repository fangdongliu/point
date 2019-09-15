package cn.fdongl.point.auth.controller;

import cn.fdongl.point.auth.repository.UserRepository;
import cn.fdongl.point.auth.util.AjaxMessage;
import cn.fdongl.point.auth.util.MsgType;
import cn.fdongl.point.auth.util.MyPage;
import cn.fdongl.point.auth.vo.JwtUser;
import cn.fdongl.point.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @ClassName SysUserController
 * @Description 用户接口控制器
 * @Author zm
 * @Date 2019/9/6 12:12
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/user")
public class SysUserController {
    private AjaxMessage retMsg = new AjaxMessage();

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "getInfo")
    public Object getInfo(@RequestParam(value = "userId") Long userId){
        Optional<User> theUser = userRepository.findById(userId);
        if (theUser.isPresent()){
            return retMsg.Set(MsgType.SUCCESS,theUser,"获取用户信息成功");
        }else{
            return retMsg.Set(MsgType.ERROR,null, "更新用户信息失败");
        }
    }

    @PostMapping(value = "updateInfo")
    public Object updateInfo(
            @RequestParam("userId") Long userId,
            @RequestParam("newPassword") String newPassword,
            @RequestParam(value = "newRealName",required = false) String newRealName
    ){
        Date dateNow = new Date();

        Optional<User> theUser = userRepository.findById(userId);

        if(theUser.isPresent()){
            User user = theUser.get();
            if(null != newRealName) {
                user.setRealName(newRealName);
            }
            user.setUserPwd(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return retMsg.Set(MsgType.SUCCESS, theUser,
                    "更新用户信息成功");
        }
        return retMsg.Set(MsgType.ERROR,null, "更新用户信息失败");
    }

    @PostMapping(value = "addNew")
    public Object addNew(
            @RequestParam("userName") String userName,
            @RequestParam("userType") String userType,
            @RequestParam("departmentName") String departmentName
    ){
        User newUser = new User();
        newUser.setWorkId(userName);
        newUser.setUserType(userType);
        newUser.setUserDepartment(departmentName);
        newUser.setUserPwd(passwordEncoder.encode("123456"));
        userRepository.save(newUser);
        return retMsg.Set(MsgType.SUCCESS, newUser, "新增用户成功");
    }

    @PostMapping(value = "getAll")
    public Object getAll(
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("searchKey") String searchKey
    ){
        PageRequest pageRequest = PageRequest.of(pageIndex,pageSize);
        Page<User> page = null;
        if (searchKey!=null){
            searchKey = '%' + searchKey + '%';
            page = userRepository.findByWorkIdLikeOrRealNameLike(searchKey,searchKey,pageRequest);
        } else {
            page = userRepository.findAll(pageRequest);
        }

        MyPage<User> userPage = new MyPage<>();
        userPage.setResultList(page.getContent());
        userPage.setTotal(page.getTotalElements());

        return retMsg.Set(MsgType.SUCCESS,userPage,"获取用户分页成功");
    }

    @PostMapping(value = "deleteBatch")
    public Object deleteBatch(@RequestBody List<User> userList){
        userRepository.deleteAll(userList);
        return retMsg.Set(MsgType.SUCCESS,null,"删除用户成功");
    }
}
