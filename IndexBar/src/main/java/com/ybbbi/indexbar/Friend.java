package com.ybbbi.indexbar;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * ybbbi
 * 2019-07-10 15:33
 */
public class Friend implements Comparable<Friend>{
    public Friend(String name) {
        this.name = name;
        this.pinyin=Pinyin.toPinyin(name,"").substring(0,1);
    }

    public String name;
    public String pinyin;

    @Override
    public int compareTo(Friend friend) {
        return this.pinyin.compareTo(friend.pinyin);
    }
}
