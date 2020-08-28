package com.forum.controller;

import com.forum.entity.Comment;
import com.forum.entity.DiscussPost;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.CommentService;
import com.forum.service.DiscussPostService;
import com.forum.service.LikeService;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/discuss")
public class DiscussPostController {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostController.class);

    private DiscussPostService discussPostService;

    private HostHolder hostHolder;

    private UserService userService;

    private CommentService commentService;

    private LikeService likeService;

    @Autowired
    public void setDiscussPostService(DiscussPostService discussPostService) {
        this.discussPostService = discussPostService;
    }

    @Autowired
    public void setHostHolder(HostHolder hostHolder) {
        this.hostHolder = hostHolder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 增加帖子
     *
     * @param title
     * @param content
     * @return
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {

        User user = hostHolder.getUser();

        if (user == null) {

            return ForumUtil.getJSONString(403, "您还未登录");

        }

        DiscussPost discussPost = new DiscussPost();

        discussPost.setUserId(hostHolder.getUser().getId());

        discussPost.setTitle(title);

        discussPost.setContent(content);

        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        return ForumUtil.getJSONString(0, "发布成功");

    }

    /**
     * 查询帖子
     *
     * @param discussPostId
     * @param model
     * @return
     */
    @GetMapping(value = "/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable(value = "discussPostId") Integer discussPostId, Model model, Page page) {

        //帖子信息
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("discussPost", discussPost);

        //作者信息
        User user = userService.findUserByUserId(discussPost.getUserId());
        model.addAttribute("user", user);

        //帖子点赞信息
        Long likeCount = likeService.findEntityLikeCount(ForumConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);

        //帖子点赞状态
        Integer likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        page.setLimit(5);

        page.setPath("/discuss/detail/" + discussPostId);

        page.setRows(discussPost.getCommentCount());

        /**
         * 帖子的评论
         */
        List<Comment> commentList = commentService.findCommentsByEntity(ForumConstant.ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());

        /**
         * 评论的vo list
         */
        List<Map<String, Object>> commentVoList = new ArrayList<>();

        if (commentList != null) {

            for (Comment comment : commentList) {

                //评论VO
                Map<String, Object> commentVo = new HashMap<>();

                //评论
                commentVo.put("comment", comment);

                //作者
                commentVo.put("user", userService.findUserByUserId(comment.getUserId()));

                //评论点赞信息
                likeCount = likeService.findEntityLikeCount(ForumConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);

                //评论点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                //评论的回复
                List<Comment> replyList = commentService.findCommentsByEntity(ForumConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                /**
                 * 回复的vo list
                 */
                List<Map<String, Object>> replyVoList = new ArrayList<>();

                if (replyList != null) {

                    for (Comment reply : replyList) {

                        //回复VO
                        Map<String, Object> replyVo = new HashMap<>();

                        //回复
                        replyVo.put("reply", reply);

                        //作者
                        replyVo.put("user", userService.findUserByUserId(reply.getUserId()));

                        //回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserByUserId(reply.getTargetId());
                        replyVo.put("target", target);

                        //回复点赞信息
                        likeCount = likeService.findEntityLikeCount(ForumConstant.ENTITY_TYPE_POST, reply.getId());
                        replyVo.put("likeCount", likeCount);

                        //回复点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_POST, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);

                    }

                }

                commentVo.put("replys", replyVoList);

                /**
                 * 回复的数量
                 */
                Integer replyCount = commentService.findCommentCount(ForumConstant.ENTITY_TYPE_COMMENT, comment.getId());

                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);

            }

        }

        model.addAttribute("comments", commentVoList);

        return "site/discuss-detail";

    }

}
