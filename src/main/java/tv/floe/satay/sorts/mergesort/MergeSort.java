package tv.floe.satay.sorts.mergesort;

/**
 * 
 * Articles
 * - http://www.vogella.com/articles/JavaAlgorithmsMergesort/article.html
 * - https://github.com/cowtowncoder/java-merge-sort/tree/master/src/main/java/com/fasterxml/sort
 * - http://www.youtube.com/watch?v=GCae1WNvnZM
 * - http://www.sorting-algorithms.com/merge-sort
 * - http://en.wikipedia.org/wiki/Merge_sort
 * 
 * 
 * Mergesort sorts in worst case in O(n log n) time.
 * 
 *  
 * Notes on Mergesort
 * 
 * - Mergesort is so called divide and conquer algorithm. 
 * - Divide and conquer algorithms divide the original data into smaller sets of data to solve the problem.
 * 
 * Divide and Conquer Algorithms
 *  
 *  Divide
 *    divide the problem into a smaller number of sub problems
 *  
 *  Conquer
 *    conquer the sub problems by solving them recursively
 *    if the sub problem sizes are small enough -> just solve the sub problem
 *    
 *  Combine
 *    combine the solutions  to the sub problems into the solution for the original problem
 *    
 *      
 * High Level Merge Sort
 * 
 *  Divide
 *    
 *    divide the n-th length sequence to be sorted into two sub sequences of n/2 each
 *     
 *  Conquer
 *    
 *    sort the two sub sequences each with merge sort
 *    
 *  Combine
 *  
 *    merge the newly sub sequences together to produce the sorted answer
 * 
 * 
 * 
 * 
 * Due to the required copying of the collection Mergesort is in the average case slower then Quicksort.
 * 
 * @author jpatterson
 *
 */
public class MergeSort {
  
  private int[] numbers;
  private int[] helper;

  private int number;

  /**
   * Sorts an array of integers with MergeSort
   * 
   * - starting point for the merge sort algorithm
   * 
   * 
   * @param values
   */
  public void sort(int[] values) {
    this.numbers = values;
    number = values.length;
    this.helper = new int[number];
    mergesort(0, number - 1);
  }

  /**
   * Algorithm
   * 
   * MERGESORT( A, p, r )
   *    if p < r
   *        then q <- Floor( (p + r) / 2 )
   *            MERGESORT( A, p, q )
   *            MERGESORT( A, q + 1, r )
   *            MERGE( A, q, p, r )
   * 
   * 
   * Input
   * - low and high are indexes into the array to sort
   * 
   * 1. subdivides the array into 2 partitions
   * 
   * - finds the middle by adding the delta of high - low with low, and dividing by 2
   * 
   * 2. merge sorts each half of the array
   * 
   * - mergesort low to middle
   * 
   * - mergesort middle + 1 to high
   * 
   * 3. merges
   * 
   * - merge all parts together
   * 
   * @param low
   * @param high
   */
  private void mergesort(int low, int high) {
    // Check if low is smaller then high, if not then the array is sorted
    if (low < high) {
      // Get the index of the element which is in the middle
      int middle = low + (high - low) / 2;
      // Sort the left side of the array
      mergesort(low, middle);
      // Sort the right side of the array
      mergesort(middle + 1, high);
      // Combine them both
      merge(low, middle, high);
    }
  }

  /**
   * merges the 2 sections together
   * 
   * 1. copy both parts of the partition into the 2nd helper array/buffer
   * 
   * 
   * 2. copy the smallest value from either helper (sub array) back into the main array
   * - sorts the sub-sequence
   * 
   * @param low
   * @param middle
   * @param high
   */
  private void merge(int low, int middle, int high) {

    // Copy both parts into the helper array
    for (int i = low; i <= high; i++) {
      helper[i] = numbers[i];
    }

    int i = low;
    int j = middle + 1;
    int k = low;
    // Copy the smallest values from either the left or the right side back
    // to the original array
    while (i <= middle && j <= high) {
      if (helper[i] <= helper[j]) {
        numbers[k] = helper[i];
        i++;
      } else {
        numbers[k] = helper[j];
        j++;
      }
      k++;
    }
    // Copy the rest of the left side of the array into the target array
    while (i <= middle) {
      numbers[k] = helper[i];
      k++;
      i++;
    }

  }
} 