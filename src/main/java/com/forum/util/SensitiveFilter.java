package com.forum.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    /**
     * 替换符
     */
    private static final String REPLACEMENT = "***";

    /**
     * 根结点
     */
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){

       try(
               InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");

               BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

       ){

           String keyword;

           while ((keyword = reader.readLine()) != null){

               this.addkeyword(keyword);

           }

       } catch (IOException e) {

           logger.error("加载敏感词文件失败 : ", e);

       }

    }

    /**
     * 将敏感词加到前缀树中
     */
    private void addkeyword(String keyword){

        TrieNode tempNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {

            char c = keyword.charAt(i);

            TrieNode subNode = tempNode.getSubNode(c);

            if(subNode == null){

                subNode = new TrieNode();

                tempNode.addSubNode(c, subNode);

            }

            /**
             * 指向子结点
             */
            tempNode = subNode;

            /**
             * 设置结束标识
             */
            if(i == keyword.length() - 1){

                tempNode.setKeywordEnd(true);

            }

        }

    }

    /**
     * 过滤敏感词
     * @param text : 待过滤文本
     * @return
     */
    public String filter(String text){

        if(StringUtils.isBlank(text)){

            return null;

        }

        /**
         * 指针一
         */
        TrieNode tempNode = rootNode;

        /**
         * 指针二
         */
        Integer begin = 0;

        /**
         * 指针三
         */
        Integer position = 0;

        /**
         * 结果
         */
        StringBuilder result = new StringBuilder();

        while (position < text.length()){

            char c = text.charAt(position);

            /**
             * 跳过符号
             */
            if(isSymbol(c)){

                /**
                 * 若 指针一 处于根结点 , 将符号记入 result , 让 指针二 向下走一步
                 */
                if(tempNode == rootNode){

                    result.append(c);

                    begin++;

                }

                /**
                 * 符号在开头或中间 , 指针三 都向下走一步
                 */
                position++;

                /**
                 * 直接进入下一次循环
                 */
                continue;

            }

            /**
             * 检查下级结点
             */
            tempNode = tempNode.getSubNode(c);

            /**
             * 下级没有结点
             */
            if(tempNode == null){

                /**
                 * 以 begin 开始的字符串不是敏感词
                 */
                result.append(text.charAt(begin));

                /**
                 * 进入下一个位置
                 */
                position = ++begin;

                /**
                 * 重新指向敏感词前缀树根结点
                 */
                tempNode = rootNode;

            }else if(tempNode.isKeywordEnd()){

                /**
                 * 发现敏感词 , begin ~ position 字符串替换掉
                 */
                result.append(REPLACEMENT);

                /**
                 * 进入下一个位置
                 */
                begin = ++position;

                /**
                 * 重新指向敏感词前缀树根结点
                 */
                tempNode = rootNode;

            }else{
                /**
                 * 没有检查完 , 也不是敏感词 , 检查下一个字符
                 */
                position++;
            }

        }

        /**
         * 检查完最后一组不是敏感词 , 将最后一批字符记入result中
         */
        result.append(text.substring(begin));

        return result.toString();

    }

    /**
     * 判断是否为符号
     */
    private boolean isSymbol(Character character){

        return !CharUtils.isAsciiAlphanumeric(character) && (character < 0x2E80 || character > 0x9FFF);

    }


    /**
     * 前缀树
     */
    @Getter
    @Setter
    private class TrieNode {

        /**
         * 关键词结束标识
         */
        private boolean isKeywordEnd = false;

        /**
         * 子结点
         *
         * key : 下级字符 ;
         *
         * value : 下级结点
         *
         */
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 添加子结点
         */
        public void addSubNode(Character c, TrieNode node){

            subNodes.put(c, node);

        }

        /**
         * 获取子结点
         */
        public TrieNode getSubNode(Character c){

            return subNodes.get(c);

        }

    }

}
