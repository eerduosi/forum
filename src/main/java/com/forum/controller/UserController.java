package com.forum.controller;

import com.forum.annotation.LoginRequired;
import com.forum.entity.User;
import com.forum.service.FollowService;
import com.forum.service.LikeService;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 上传文件目录
     */
    @Value("${forum.path.upload}")
    private String uploadPath;

    /**
     * 项目域名
     */
    @Value("${forum.path.domain}")
    private String domain;

    /**
     * 项目名
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private HostHolder hostHolder;

    @Autowired
    public void setHostHolder(HostHolder hostHolder) {
        this.hostHolder = hostHolder;
    }

    private LikeService likeService;

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    private FollowService followService;

    @Autowired
    public void setFollowService(FollowService followService) {
        this.followService = followService;
    }

    /**
     * 跳转到账号设置页面
     *
     * @return
     *
     */
    @LoginRequired
    @GetMapping(value = "/setting")
    public String getSettingPage(){

        return "site/setting";

    }

    /**
     *
     * 上传用户头像
     *
     * @param headerImage
     *
     * @param model
     *
     * @return
     *
     */
    @LoginRequired
    @PostMapping(value = "/upload")
    public String upload(MultipartFile headerImage, Model model){

        /**
         * 未选择图片
         */
        if(headerImage == null){

            model.addAttribute("error", "您还未选择图片");

            return "site/setting";

        }

        /**
         * 获取图片名
         */
        String fileName = headerImage.getOriginalFilename();

        /**
         * 获取图片扩展名
         */
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        /**
         * 图片格式不正确
         */
        if(StringUtils.isBlank(suffix)){

            model.addAttribute("error", "文件的格式不正确");

            return "site/setting";

        }

        /**
         * 生成随机文件名
         */
        fileName = ForumUtil.generateUUID() + suffix;

        /**
         * 定义文件存放位置
         */
        File destination = new File(uploadPath + "/" + fileName);
        logger.info(destination.getAbsolutePath());

        /**
         * 将图片保存到服务器
         */
        try {

            headerImage.transferTo(destination);

        } catch (IOException e) {

            logger.error("上传文件失败 : " + e.getMessage());

            throw new RuntimeException("上传文件失败 , 服务器发生异常 : ", e);

        }

        /**
         * 更新用户头像路径
         */
        User user = hostHolder.getUser();

        String headerUrl = domain + contextPath + "/user/header/" + fileName;

        userService.updateHeaderUrl(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     *
     * 获取图片
     *
     * @param fileName
     *
     * @param response
     *
     */
    @GetMapping(value = "/header/{fileName}")
    public void getHeader(@PathVariable(value = "fileName")String fileName, HttpServletResponse response){

        /**
         * 服务器存放路径
         */
        fileName = uploadPath + "/" + fileName;

        /**
         * 文件的扩展名
         */
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        /**
         * 设定传输格式
         */
        response.setContentType("image/" + suffix);

        try( ServletOutputStream outputStream = response.getOutputStream();
             FileInputStream fis = new FileInputStream(fileName) ) {

            byte[]  buffer = new byte[1024];

            int b = 0;

            while ((b=fis.read(buffer)) != -1){

                outputStream.write(buffer, 0, b);;

            }

        } catch (IOException e) {

            logger.error("读取头像失败 : ", e.getMessage());

        }

    }

    /**
     *跳转到用户个人中心页面
     */
    @GetMapping(value = "/profile/{userId}")
    public String getProfilePage(@PathVariable(value = "userId")Integer userId, Model model){
        User user = userService.findUserByUserId(userId);

        if(user == null){
            throw new RuntimeException("该用户不存在");
        }

        //用户
        model.addAttribute("user", user);

        //点赞数量
        Integer userLikeCount = likeService.findUserLikeCount(userId);

        model.addAttribute("likeCount", userLikeCount);

        //关注数量
        Long followeeCount = followService.findFolloweeCount(userId, ForumConstant.ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        //粉丝数量
        Long followerCount = followService.findFollowerCount(ForumConstant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        //是否被关注
        Boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "site/profile";

    }

}
