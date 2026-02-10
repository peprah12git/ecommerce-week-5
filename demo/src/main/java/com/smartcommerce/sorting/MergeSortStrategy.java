package com.smartcommerce.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.smartcommerce.utils.PerformanceMonitor;

/**
 * Merge Sort implementation of the SortStrategy interface.
 * 
 * Time Complexity: O(n log n) in all cases
 * Space Complexity: O(n) for auxiliary arrays
 * 
 * Merge sort is a stable, divide-and-conquer sorting algorithm that:
 * - Divides the input array into two halves
 * - Recursively sorts each half
 * - Merges the sorted halves to produce the final result
 * 
 * @param <T> the type of elements to be sorted
 */
@Component
public class MergeSortStrategy<T> implements SortStrategy<T> {

    private final PerformanceMonitor performanceMonitor = PerformanceMonitor.getInstance();

    @Override
    public List<T> sort(List<T> items, Comparator<T> comparator) {
        long startTime = performanceMonitor.startTimer();
        
        if (items == null || items.size() <= 1) {
            List<T> result = items == null ? new ArrayList<>() : new ArrayList<>(items);
            performanceMonitor.recordQueryTime("MergeSort", startTime);
            return result;
        }

        List<T> result = new ArrayList<>(items);
        mergeSort(result, 0, result.size() - 1, comparator);
        
        performanceMonitor.recordQueryTime("MergeSort", startTime);
        return result;
    }

    /**
     * Recursive merge sort implementation.
     *
     * @param list       the list to sort
     * @param left       the left index (inclusive)
     * @param right      the right index (inclusive)
     * @param comparator the comparator to determine order
     */
    private void mergeSort(List<T> list, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            // Sort first and second halves
            mergeSort(list, left, middle, comparator);
            mergeSort(list, middle + 1, right, comparator);

            // Merge the sorted halves
            merge(list, left, middle, right, comparator);
        }
    }

    /**
     * Merges two sorted subarrays of the list.
     * First subarray is list[left..middle]
     * Second subarray is list[middle+1..right]
     *
     * @param list       the list containing the subarrays
     * @param left       the starting index of the first subarray
     * @param middle     the ending index of the first subarray
     * @param right      the ending index of the second subarray
     * @param comparator the comparator to determine order
     */
    private void merge(List<T> list, int left, int middle, int right, Comparator<T> comparator) {
        // Calculate sizes of two subarrays to be merged
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        // Create temporary lists
        List<T> leftArray = new ArrayList<>(leftSize);
        List<T> rightArray = new ArrayList<>(rightSize);

        // Copy data to temporary lists
        for (int i = 0; i < leftSize; i++) {
            leftArray.add(list.get(left + i));
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray.add(list.get(middle + 1 + j));
        }

        // Merge the temporary lists back into the original list
        int i = 0, j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {
            if (comparator.compare(leftArray.get(i), rightArray.get(j)) <= 0) {
                list.set(k, leftArray.get(i));
                i++;
            } else {
                list.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArray, if any
        while (i < leftSize) {
            list.set(k, leftArray.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of rightArray, if any
        while (j < rightSize) {
            list.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Merge Sort";
    }
}