package com.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    /**
     * 当前页码
     */
    private Integer currentPage = 1;

    /**
     * 页面显示总数
     */
    private Integer limit = 10;

    /**
     * 数据总数
     */
    private Integer rows;

    /**
     * 查询路径 , 复用分页链接
     */
    private String path;

    /**
     * 限制页数不可为负
     * @param currentPage
     */
    public void setCurrentPage(Integer currentPage){
        if(currentPage >= 1){
            this.currentPage = currentPage;
        }
    }

    /**
     * 限定每页显示数据量为 1 - 100 条
     * @param limit
     */
    public void setLimit(Integer limit){
        if(limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public void setRows(Integer rows){
        if(rows >= 0){
            this.rows = rows;
        }
    }

    /**
     * 根据当前页页码计算当前行起始行数
     * @return
     */
    public Integer getOffset(){
        return(currentPage - 1) * limit;
    }

    /**
     * 获取总页数 = 总行数 / 每页数据
     * @return
     */
    public Integer getTotal(){
        if(rows % limit == 0){
            return rows / limit;
        }else{
            return (rows / limit) + 1;
        }
    }

    /**
     * 获取页面显示起始页码
     *
     * @return
     */
    public Integer getFrom(){
        Integer from = currentPage - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取页面显示结束页码
     *
     * @return
     */
    public Integer getTo(){
        Integer to = currentPage + 2;
        Integer total = getTotal();
        return to > total ? total : to;
    }
}
