package com.edexer.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Comparator;

//import org.primefaces.examples.domain.Car;
import org.primefaces.model.SortOrder;

import com.edexer.model.BusinessCard;

public class LazySorter implements Comparator<BusinessCard> {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(BusinessCard bc1, BusinessCard bc2) {
        try {
            Object value1 = BusinessCard.class.getField(this.sortField).get(bc1);
            Object value2 = BusinessCard.class.getField(this.sortField).get(bc2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
