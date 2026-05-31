package com.mingliu.trigger.http.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页响应结果
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-01-XX XX:XX
 */
@Data
@Schema(description = "分页响应结果")
public class PageResponse<T> {

    @Schema(description = "当前页码")
    private Integer pageNum;

    @Schema(description = "每页大小")
    private Integer pageSize;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Integer pages;

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "是否有下一页")
    private Boolean hasNextPage;

    @Schema(description = "是否有上一页")
    private Boolean hasPreviousPage;

    public PageResponse() {
    }

    public PageResponse(PageInfo<T> pageInfo) {
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
        this.pages = pageInfo.getPages();
        this.list = pageInfo.getList();
        this.hasNextPage = pageInfo.isHasNextPage();
        this.hasPreviousPage = pageInfo.isHasPreviousPage();
    }

    public static <T> PageResponse<T> of(PageInfo<T> pageInfo) {
        return new PageResponse<>(pageInfo);
    }

    public static <T> PageResponse<T> of(IPage<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setPageNum((int) page.getCurrent());
        response.setPageSize((int) page.getSize());
        response.setTotal(page.getTotal());
        response.setPages((int) page.getPages());
        response.setList(page.getRecords());
        response.setHasNextPage(page.getCurrent() < page.getPages());
        response.setHasPreviousPage(page.getCurrent() > 1);
        return response;
    }
}