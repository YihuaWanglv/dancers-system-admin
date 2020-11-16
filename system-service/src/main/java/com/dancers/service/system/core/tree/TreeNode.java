package com.dancers.service.system.core.tree;

/**
 * 树类型接口，实现此接口的类，会被标记为树节点的类型，从而可以使用TreeBuilder来构建树数据结构
 *
 * @param <S>
 * @param <T>
 */
public interface TreeNode<S, T> {

    S getPrimaryValue();
    S getParentValue();
    void addChild(T t);
    boolean isRoot();
}
