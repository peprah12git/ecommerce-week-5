package com.smartcommerce.sorting;

import java.util.Comparator;
import java.util.List;

/**
 * Strategy interface for sorting algorithms.
 * Allows different sorting implementations to be plugged in.
 * 
 * @param <T> the type of elements to be sorted
 */
public interface SortStrategy<T> {

    /**
     * Sorts the given list using the provided comparator.
     *
     * @param items      the list of items to sort
     * @param comparator the comparator to determine the order
     * @return a new sorted list
     */
    List<T> sort(List<T> items, Comparator<T> comparator);

    /**
     * Returns the name of the sorting algorithm.
     *
     * @return the algorithm name
     */
    String getAlgorithmName();
}