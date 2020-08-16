package com.forum.controller;

import com.forum.entity.Comment;
import com.forum.entity.DiscussPost;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.CommentService;
import com.forum.service.DiscussPostService;
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

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    /**
     * 增加帖子
     *
     * @param title
     *
     * @param content
     *
     * @return
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public String addDiscussPost(String title, String content){

        User user = hostHolder.getUser();

        if(user == null){

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
     *
     * @param model
     *
     * @return
     */
    @GetMapping(value = "/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable(value = "discussPostId")Integer discussPostId, Model model, Page page){

        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);

        model.addAttribute("discussPost", discussPost);

        User user = userService.findUserByUserId(discussPost.getUserId());

        model.addAttribute("user", user);

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

        if(commentList != null){

            for (Comment comment : commentList) {

                Map<String, Object> commentVo = new HashMap<>();

                commentVo.put("comment", comment);

                commentVo.put("user", userService.findUserByUserId(comment.getUserId()));

                /**
                 * 评论的回复
                 */
                List<Comment> replyList = commentService.findCommentsByEntity(ForumConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                /**
                 * 回复的vo list
                 */
                List<Map<String, Object>> replyVoList = new ArrayList<>();

                if(replyList != null){

                    for (Comment reply : replyList) {

                        Map<String, Object> replyVo = new HashMap<>();

                        replyVo.put("reply", reply);

                        replyVo.put("user", userService.findUserByUserId(reply.getUserId()));

                        User target = reply.getTargetId() == 0 ? null : userService.findUserByUserId(reply.getTargetId());

                        replyVo.put("target", target);

                        replyVoList.add(replyVo);

                    }

                }

                commentVo.put("replyList", replyVoList);

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
