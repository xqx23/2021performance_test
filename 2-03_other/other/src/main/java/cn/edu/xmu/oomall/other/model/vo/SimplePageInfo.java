package cn.edu.xmu.oomall.other.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplePageInfo<T> {
    Integer page;
    Integer pageSize;
    Integer total;
    Integer pages;
    List<T> list;
}
