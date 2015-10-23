package com.commnetworks.unositetester.helpers;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by PGayee@werner.com on 10/23/15
 */
public class NicePrintingMap<K, V>{
    private Map<K, V> map;

    public NicePrintingMap(Map<K, V> map){
        this.map = map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<K, V> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();

    }
}
