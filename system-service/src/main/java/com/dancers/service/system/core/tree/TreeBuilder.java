package com.dancers.service.system.core.tree;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现TreeNode的类可以利用此Builder类把list转为tree
 *
 * @param <S>
 * @param <T>
 */
@Slf4j
public class TreeBuilder<S, T extends TreeNode> {

    public List<T> toTree(List<T> list) {
        Map<S, T> map = new HashMap<>();
        for (T t : list) {
            if (map.put((S) t.getPrimaryValue(), t) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        list.forEach(obj -> {
            S parentId = (S) obj.getParentValue();
            if (parentId != null && map.containsKey(parentId)) {
                map.get(parentId).addChild(obj);
            }
        });
        List<T> tree = new ArrayList<>();
        map.values().forEach(obj -> {
            if (obj.isRoot()) {
                tree.add(obj);
            }
        });
        return tree;
    }
}
