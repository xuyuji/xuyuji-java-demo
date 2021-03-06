# 祖先问题

## 问题描述

小明和小红是亲兄妹，他俩一起翻了翻他们家的族谱，发现他们家非常庞大，有非常多的名字在族谱里面。族谱中会写清楚每一个人的父亲是谁，当然每个人都只会有一个父亲。对于祖先的定义，我们再这儿举个例子：族谱里面会写小王的父亲是小丁，小丁的父亲是小东，那么实际上小东就是小王的爷爷，也是小王的祖先。

小明很聪明，小明理了理他们的家庭关系，很快就弄清楚了，知道了族谱中每一个人的祖先关系。

但是小红却依旧困惑，于是问了很多问题，希望你能够解答。

小红的问题是，请问A是B的祖先关系是什么？究竟A是不是B的祖先，或者说B是A的祖先，亦或者B和A不存在祖先关系呢。

## 示例

### 输入

```
输入第一行包括一个整数n表示家族成员个数。
接下来n行每行一对整数对a和b标识a是b的父亲，或者b是a的父亲，这需要你来判断。
如果b是-1，那么a就是整个家族的根，也就是辈分最大的人，保证只有一个。
第n+2行是一个整数m表示小红的询问个数。
接下来m行，每行两个正整数A和B。
表示小红想知道A是B的祖先关系。
n,m<=40000,每个节点的编号都不超过40000.
```

### 输出

```
对于每一个询问。
输出1表示A是B的祖先，输出2表示B是A的祖先，都不是输出0
```

### 样例输入

```
10
1 -1
3 1
4 1
5 1
6 1
7 1
8 1
9 1
10 1
2 10
5
1 2
2 3
2 4
2 5
2 10
```

### 样例输出

```
1
0
0
0
2
```

## 解题思路

构建一颗家族树，记录每个人的辈分，小红提问时，找出小辈，向上递归到长辈的辈分为止，判断其中是否有直系父辈。

因为输入的参数(a,b)无法判断谁是长辈，故同时记录a是b的儿子、b是a的儿子，记录辈分最大的人，等输入完成后，从辈分最大的人开始递归处理子辈，去除掉儿子列表里自己的父亲即可。